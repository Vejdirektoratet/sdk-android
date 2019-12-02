/**
 *  HttpListTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-21.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed
import okhttp3.mockwebserver.MockResponse
import kotlin.test.Test
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.asserter

class HttpTest {
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
    fun `Should return Result Success`() {
        val response = "[]"
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)
    }

    @Test
    fun `Should return a list of entities`() {
        val response = "[]"
        mockServer.enqueue(MockResponse().setBody(response).setResponseCode(200))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.Success", requestResult is Feed.Result.Success)
        asserter.assertNotNull("Entities should NOT be null", ((requestResult as Feed.Result.Success).entities))
    }

    @Test
    fun `Should return Result HttpError with correct status code`() {
        performErrorCodeTest(400)
        performErrorCodeTest(404)
        performErrorCodeTest(500)
    }

    private fun performErrorCodeTest(expectedErrorCode: Int) {
        mockServer.enqueue(MockResponse().setResponseCode(expectedErrorCode))

        var requestResult: Feed.Result? = null
        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = null, viewType = ViewType.LIST, apiKey = "test_key") { result: Feed.Result ->
            requestResult = result
        }
        Thread.sleep(200)

        asserter.assertNotNull("Should NOT be null", requestResult)
        asserter.assertTrue("Should be of type Result.HttpError", requestResult is Feed.Result.HttpError)
        asserter.assertEquals("Should have error code $expectedErrorCode", expectedErrorCode, (requestResult as Feed.Result.HttpError).statusCode)
    }
}
