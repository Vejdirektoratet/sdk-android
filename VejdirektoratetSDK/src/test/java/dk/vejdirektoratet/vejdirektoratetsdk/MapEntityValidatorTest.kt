/**
 *  MapEntityValidatorTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-12-02.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapMarker
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapPolygon
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapPolyline
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject
import kotlin.test.Test

class MapEntityValidatorTest {

    @Test
    fun `test valid Marker entity`() {
        val entity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {
                    "lat": 55.808418,
                    "lng": 11.582623
                },
                "style": {
                    "id": "Trafficevent.Activity",
                    "strokeColor": "#ff0000ff",
                    "strokeWidth": 3.0,
                    "fillColor": "#ff000088",
                    "icon": "https://s3-eu-west-1.amazonaws.com/static.nap.vd.dk/icons/roadsign_trafikmelding.png",
                    "dashed": false,
                    "dashColor": "#000000ff",
                    "zIndex": 0
                },
                "type":"MARKER"
            }"""

        MapMarker.MapMarkerValidator().validate(JSONObject(entity))
    }

    @Test(expected = MissingRequiredFieldException::class)
    fun `test invalid Marker entity`() {
        val entity = "{}"
        MapMarker.MapMarkerValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing Marker fields`() {
        val validator = MapMarker.MapMarkerValidator()

        val missingEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0}
            }"""

        val missingTagEntity = """{
                "entityType": "latextraffic",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0}
            }"""

        val missingCenterEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0}
            }"""

        val missingTypeEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0}
            }"""

        val missingStyleEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623}
            }"""


        Utils.assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingCenterEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingStyleEntity, validator)
    }

    @Test
    fun `test null Marker fields`() {
        val validator = MapMarker.MapMarkerValidator()

        val nullEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0},
                "entityType": null            
            }"""

        val nullTagEntity = """{
                "entityType": "latextraffic",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0},
                "tag": null
            }"""

        val nullCenterEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "type":"MARKER",
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0},
                "center": null    
            }"""

        val nullTypeEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0},
                "type": null            
            }"""

        val nullStyleEntity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMl9USUMtVHJhZmlrbWFuMi8x",
                "center": {"lat": 55.808418, "lng": 11.582623},
                "style": null,
                "type":"MARKER"
            }"""

        Utils.assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullCenterEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullStyleEntity, validator)
    }

    @Test
    fun `test valid Polyline entity`() {
        val entity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "style": {"id": "Trafficevent.Activity", "strokeColor": "#ff0000ff", "strokeWidth": 3.0, "fillColor": "#ff000088", "icon": "", "dashed": false, "dashColor": "#000000ff", "zIndex": 0},
                "type": "POLYLINE"
            }"""
        MapPolyline.MapPolylineValidator().validate(JSONObject(entity))
    }

    @Test(expected = MissingRequiredFieldException::class)
    fun `test invalid Polyline entity`() {
        val entity = "{}"
        MapPolyline.MapPolylineValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing Polyline fields`() {
        val validator = MapPolyline.MapPolylineValidator()

        val missingEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYLINE"
            }"""

        val missingTagEntity = """{
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYLINE"            
            }"""

        val missingPointsEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "type": "POLYLINE"            
            }"""

        val missingTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ]            
            }"""

        Utils.assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingPointsEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTypeEntity, validator)
    }

    @Test
    fun `test null Polyline fields`() {
        val validator = MapPolyline.MapPolylineValidator()

        val nullEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYLINE",
                "entityType": null            
            }"""

        val nullTagEntity = """{
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYLINE",
                "tag": null            
            }"""

        val nullPointsEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "type": "POLYLINE",
                "points": null            
            }"""

        val nullTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": null            
            }"""

        Utils.assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullPointsEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTypeEntity, validator)
    }

    @Test
    fun `test valid Polygon entity`() {
        val entity = """{
                "entityType": "latextraffic",
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "style": {
                    "id": "Trafficevent.Activity",
                    "strokeColor": "#ff0000ff",
                    "strokeWidth": 3.0,
                    "fillColor": "#ff000088",
                    "icon": "https://s3-eu-west-1.amazonaws.com/static.nap.vd.dk/icons/roadsign_trafikmelding.png",
                    "dashed": false,
                    "dashColor": "#000000ff",
                    "zIndex": 0
                },
                "type": "POLYGON"
            }"""

        MapPolygon.MapPolygonValidator().validate(JSONObject(entity))
    }

    @Test(expected = MissingRequiredFieldException::class)
    fun `test invalid Polygon entity`() {
        val entity = "{}"
        MapPolygon.MapPolygonValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing Polygon fields`() {
        val validator = MapPolygon.MapPolygonValidator()

        val missingEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYGON"            
            }"""

        val missingTagEntity = """{
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYGON"            
            }"""

        val missingPointsEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "type": "POLYGON"
            }"""

        val missingTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ]        
            }"""

        Utils.assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingPointsEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTypeEntity, validator)
    }

    @Test
    fun `test null Polygon fields`() {
        val validator = MapPolygon.MapPolygonValidator()

        val nullEntityTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYGON",
                "entityType": null            
            }"""

        val nullTagEntity = """{
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": "POLYGON",
                "tag": null            
            }"""

        val nullPointsEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "type": "POLYGON",
                "points": null
            }"""

        val nullTypeEntity = """{
                "tag": "VHJhZmlrbWFuMi9yX1RyYWZpa21hbjIvMTMzNDQzMF9USUMtVHJhZmlrbWFuMi8x",
                "entityType": "latextraffic",
                "points": [
                    {"lat": 56.46536, "lng": 9.98535},
                    {"lat": 56.46492, "lng": 9.98551}
                ],
                "type": null            
            }"""

        Utils.assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullPointsEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTypeEntity, validator)
    }
}
