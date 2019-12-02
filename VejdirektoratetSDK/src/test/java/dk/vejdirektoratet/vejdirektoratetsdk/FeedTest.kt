/**
 *  FeedTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-22.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.entity.*
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import okhttp3.mockwebserver.MockResponse
import kotlin.test.Test
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.asserter

class FeedTest {
    private val mockServer = MockWebServer()

    @BeforeTest
    fun setup() {
        mockServer.start(BuildConfig.SERVER_PORT)
    }


    @AfterTest
    fun teardown() {
        mockServer.shutdown()
    }

    @Test
    fun `LIST - test mixing Traffic and Roadwork`() {

        val response = """[
                    {"heading":"Tabt gods","description":"Rute 21 Motortrafikvej 21, fra Sjællands Odde mod Holbæk mellem Slagelse/Nykøbing Sj. og Asnæs. Tabt gods, vejhjælp er på vej. Der ligger en dunk midt på vejen","tag":"VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000","bounds":{"southWest":{"lat":55.808418,"lng":11.582623},"northEast":{"lat":55.808418,"lng":11.582623}}},
                    {"heading":"Vejarbejde","description":"O2 København-Østerbro, Århusgade til Kalkbrænderihavnsgade mellem Hjørringgade og Trelleborggade. Vejarbejde, spærret. Metroarbejde. Spærret for al trafik fra Århusgade til Kalkbrænderihavnsgade, Følg skiltning på stedet. Cykler og gående skal via Vordingborggade eller Nordre Frihavnsgade","tag":"VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTIxNjU3N19USUMtVHJhZmlrbWFuMi8x","entityType":"latexroadwork","timestamp":"2018-08-17T09:08:25.000+0000","bounds":{"southWest":{"lat":55.707131,"lng":12.589524},"northEast":{"lat":55.70728,"lng":12.59096}}}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 2", 2, entities.size)

        val firstEntity = entities[0] as ListEntity
        asserter.assertTrue("Entity should be of type Traffic", firstEntity is Traffic)
        asserter.assertEquals("Should contain correct tag", "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x", firstEntity.tag)
        asserter.assertEquals("Should contain correct timestamp", Utils.dateFromIso8601String("2019-09-12T09:20:26.000+0000"), firstEntity.timestamp)
        asserter.assertEquals("Should contain correct heading", "Tabt gods", firstEntity.heading)
        asserter.assertEquals("Should contain correct description", "Rute 21 Motortrafikvej 21, fra Sjællands Odde mod Holbæk mellem Slagelse/Nykøbing Sj. og Asnæs. Tabt gods, vejhjælp er på vej. Der ligger en dunk midt på vejen", firstEntity.description)
        asserter.assertNotNull("Bounds should not be null", firstEntity.bounds)
        asserter.assertEquals("Should contain correct SouthWest latitude", 55.808418, firstEntity.bounds?.southWest?.lat)
        asserter.assertEquals("Should contain correct NorthEast longitude", 11.582623, firstEntity.bounds?.southWest?.lng)
        asserter.assertEquals("Should contain correct SouthWest latitude", 55.808418, firstEntity.bounds?.northEast?.lat)
        asserter.assertEquals("Should contain correct NorthEast longitude", 11.582623, firstEntity.bounds?.northEast?.lng)

        val secondEntity = entities[1] as ListEntity
        asserter.assertTrue("Entity should be of type Roadwork", secondEntity is Roadwork)
        asserter.assertEquals("Should contain correct tag", "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTIxNjU3N19USUMtVHJhZmlrbWFuMi8x", secondEntity.tag)
        asserter.assertEquals("Should contain correct timestamp", Utils.dateFromIso8601String("2018-08-17T09:08:25.000+0000"), secondEntity.timestamp)
        asserter.assertEquals("Should contain correct heading", "Vejarbejde", secondEntity.heading)
        asserter.assertEquals("Should contain correct description", "O2 København-Østerbro, Århusgade til Kalkbrænderihavnsgade mellem Hjørringgade og Trelleborggade. Vejarbejde, spærret. Metroarbejde. Spærret for al trafik fra Århusgade til Kalkbrænderihavnsgade, Følg skiltning på stedet. Cykler og gående skal via Vordingborggade eller Nordre Frihavnsgade", secondEntity.description)
        asserter.assertNotNull("Bounds should not be null", secondEntity.bounds)
        asserter.assertEquals("Should contain correct SouthWest latitude", 55.707131, secondEntity.bounds?.southWest?.lat)
        asserter.assertEquals("Should contain correct NorthEast longitude", 12.589524, secondEntity.bounds?.southWest?.lng)
        asserter.assertEquals("Should contain correct SouthWest latitude", 55.70728, secondEntity.bounds?.northEast?.lat)
        asserter.assertEquals("Should contain correct NorthEast longitude", 12.59096, secondEntity.bounds?.northEast?.lng)
    }

    @Test
    fun `LIST - test Traffic without bounds`() {

        val response = """[
                    {"heading":"as","description":"as","tag":"as","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"as","description":"as","tag":"as","entityType":"latextraffic","timestamp":"2019-09-12T09:21:27.000+0000","bounds": null}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 2", 2, entities.size)

        val firstEntity = entities[0] as ListEntity
        asserter.assertTrue("Entity should be of type Traffic", firstEntity is Traffic)
        asserter.assertEquals("Should contain correct timestamp", Utils.dateFromIso8601String("2019-09-12T09:20:26.000+0000"), firstEntity.timestamp)
        asserter.assertNull("Bounds should be null", firstEntity.bounds)

        val secondEntity = entities[1] as ListEntity
        asserter.assertTrue("Entity should be of type Roadwork", secondEntity is Traffic)
        asserter.assertEquals("Should contain correct timestamp", Utils.dateFromIso8601String("2019-09-12T09:21:27.000+0000"), secondEntity.timestamp)
        asserter.assertNull("Bounds shouldbe null", secondEntity.bounds)
    }

    @Test
    fun `LIST - test that invalid Traffic entities are filtered away`() {

        val response = """[
                    {"heading":"Correct","description":"","tag":"","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"EntityType float value","description":"", "tag":"","entityType":42.24,"timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"EntityType unknown","description":"","tag":"","entityType":"unknown","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Description integer value","description":42, "tag":"","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Timestamp doesn't validate","description":"", "tag":"","entityType":"latextraffic","timestamp":"Tue, 17 Sep 2019 11:35:13 GMT"},
                    {"heading":"Timestamp invalid type","description":"", "tag":"","entityType":"latextraffic","timestamp":42.23},
                    {"heading":"Tag <null>","description":"","tag":null,"timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Latitude (southWest) invalid type","description":"Rute 21","tag":"","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000","bounds":{"southWest":{"lat":"invalidLat","lng":11.582623},"northEast":{"lat":55.808418,"lng":11.582623}}}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 1", 1, entities.size)

        val entity = entities[0] as ListEntity
        asserter.assertTrue("Entity should be of type Traffic", entity is Traffic)
        asserter.assertEquals("Should have correct heading", "Correct", entity.heading)
    }

    @Test
    fun `LIST - test that entities with missing fields are filtered away`() {

        val response = """[
                    {"heading":"Correct","description":"asd","tag":"asd","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Missing description","tag":"","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Description <null>","description":null,"tag":"","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Missing entityType","description":"","tag":"","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"EntityType <null>","description":"","tag":"","entityType":null,"timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Missing tag","entityType":"latextraffic","timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Tag <null>","description":"","tag":null,"timestamp":"2019-09-12T09:20:26.000+0000"},
                    {"heading":"Missing timestamp","description":"","tag":"","entityType":"latextraffic"},
                    {"heading":"Timestamp <null>","description":"","tag":"","entityType":"latextraffic","timestamp":null}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 1", 1, entities.size)

        val entity = entities[0] as ListEntity
        asserter.assertTrue("Entity should be of type Traffic", entity is Traffic)
        asserter.assertEquals("Should have correct heading", "Correct", entity.heading)
    }

    @Test
    fun `MAP - test mixing Traffic and Roadwork`() {

        val response = """[
                    {"entityType":"latextraffic","tag":"VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x","points":[{"lat":56.46536,"lng":9.98535},{"lat":56.46492,"lng":9.98551},{"lat":56.46411,"lng":9.98581},{"lat":56.46383,"lng":9.98591},{"lat":56.46351,"lng":9.98603},{"lat":56.46319,"lng":9.98615},{"lat":56.46248,"lng":9.98639},{"lat":56.46204,"lng":9.98655},{"lat":56.46147,"lng":9.98678},{"lat":56.46077,"lng":9.98713},{"lat":56.460111,"lng":9.987521}],"style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"https://s3-eu-west-1.amazonaws.com/static.nap.vd.dk/icons/roadsign_trafikmelding.png","dashed":false,"dashColor":"#000000ff","zIndex":0},"extras":{},"type":"POLYLINE"},
                    {"entityType":"latexroadwork","tag":"VHJhZmlrbWFuMi9yX09UbWFuL3Zlam1hbl84NTFfMTktMDM3MjNfVElDLVRyYWZpa21hbjIvMQ","center":{"lat":57.070131,"lng":9.910451},"style":{"id":"Trafficevent.RoadWork","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"https://s3-eu-west-1.amazonaws.com/static.nap.vd.dk/icons/roadsign_vejarbejde.png","dashed":false,"dashColor":"#000000ff","zIndex":0},"extras":{},"type":"MARKER"}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.MAP, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 2", 2, entities.size)

        val firstEntity = entities[0] as MapEntity
        asserter.assertTrue("Entity should be of type MapPolyline", firstEntity is MapPolyline)
        firstEntity as MapPolyline
        asserter.assertEquals("Entity should be of type EntityType.TRAFFIC", BaseEntity.EntityType.Traffic, firstEntity.entityType)
        asserter.assertEquals("Should contain correct tag", "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x", firstEntity.tag)
        asserter.assertEquals("Should contain 11 points", 11, firstEntity.points.size)

        val secondEntity = entities[1] as MapEntity
        asserter.assertTrue("Entity should be of type MapMarker", secondEntity is MapMarker)
        secondEntity as MapMarker
        asserter.assertEquals("Entity should be of type EntityType.ROADWORK", BaseEntity.EntityType.RoadWork, secondEntity.entityType)
        asserter.assertEquals("Should contain correct tag", "VHJhZmlrbWFuMi9yX09UbWFuL3Zlam1hbl84NTFfMTktMDM3MjNfVElDLVRyYWZpa21hbjIvMQ", secondEntity.tag)
        asserter.assertEquals("Should contain correct center.latitude", secondEntity.center.lat, 57.070131)
        asserter.assertEquals("Should contain correct center.longitude", secondEntity.center.lng, 9.910451)
    }

    @Test
    fun `MAP - test Traffic without style`() {

        val response = """[
                    {"entityType":"latextraffic","tag":"VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x","points":[],"type":"POLYLINE"},
                    {"entityType":"latexroadwork","tag":"VHJhZmlrbWFuMi9yX09UbWFuL3Zlam1hbl84NTFfMTktMDM3MjNfVElDLVRyYWZpa21hbjIvMQ","center":{"lat":57.070131,"lng":9.910451},"style":null,"type":"MARKER"}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.MAP, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 0", 0, entities.size)
    }

    @Test
    fun `MAP - test that invalid Traffic entities are filtered away`() {

        val response = """[
                    {"tag":"Correct","entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"type":"MARKER", "style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"entityType <integer>","entityType":42,"center":{"lat":57.070131,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"center <float>","center":42.24,"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"center.lat <string>","entityType":"latexroadwork","center":{"lat":"I'm a string, but should be a float","lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"center.lng <string>","entityType":"latexroadwork","center":{"lat":57.070131,"lng":"I'm a string, but should be a float"},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"type <float>","entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"type":42.24,"style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":42.24,"entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.MAP, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 1", 1, entities.size)

        val entity = entities[0] as MapEntity
        asserter.assertTrue("Entity should be of type MapMarker", entity is MapMarker)
        asserter.assertEquals("Should have correct tag", "Correct", entity.tag)
    }

    @Test
    fun `MAP - test that entities with missing fields are filtered away`() {

        val response = """[
                    {"tag":"Correct","entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing entityType","center":{"lat":57.070131,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"entityType <null>","entityType":null,"center":{"lat":57.070131,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing center","entityType":"latexroadwork","type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Center <null>","center": null, "entityType":"latexroadwork","type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing center.lat","entityType":"latexroadwork","center":{"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"center.lat <null>","entityType":"latexroadwork","center":{"lat":null,"lng":9.910451},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing center.lng","entityType":"latexroadwork","center":{"lat":57.070131},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"center.lng <null>","entityType":"latexroadwork","center":{"lng":null,"lat":57.070131},"type":"MARKER","style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing type","entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"type <null>","type":null,"entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"style":{"id":"Trafficevent.Activity","strokeColor":"#ff0000ff","strokeWidth":3.0,"fillColor":"#ff000088","icon":"","dashed":false,"dashColor":"#000000ff","zIndex":0}},
                    {"tag":"Missing <style>","entityType":"latexroadwork","center":{"lat":57.070131,"lng":9.910451},"type":"MARKER"}
                ]"""
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.MAP, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)

        val entities = (requestResult as Feed.Result.Success).entities
        asserter.assertEquals("Entities should have size = 1", 1, entities.size)

        val entity = entities[0] as MapEntity
        asserter.assertTrue("Entity should be of type MapMarker", entity is MapMarker)
        asserter.assertEquals("Should have correct tag", "Correct", entity.tag)
    }
}
