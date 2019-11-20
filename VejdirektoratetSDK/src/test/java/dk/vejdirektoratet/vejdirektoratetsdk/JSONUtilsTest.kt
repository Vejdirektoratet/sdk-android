/**
 *  JSONUtilsTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-20.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import org.json.JSONArray
import org.json.JSONObject
import kotlin.test.Test
import kotlin.test.asserter

class JSONUtilsTest {

    @Test
    fun `latLngFromJson() - Should create a VDLatLng object from a JSONObject`() {
        val expectedLat = 55.1231
        val expectedLng = 10.12314
        val jsonObject = JSONObject("{\"lat\": $expectedLat, \"lng\": $expectedLng}")

        val vdLatLng = JSONUtils.latLngFromJson(jsonObject)

        asserter.assertTrue("Should be of type VDLatLng", vdLatLng is VDLatLng)
        asserter.assertEquals("Should have the expected Latitude", expectedLat, vdLatLng.lat)
        asserter.assertEquals("Should have the expected Longitude", expectedLng, vdLatLng.lng)
    }

    @Test
    fun `latLongListFromJson() - Should create a MutableList containing VDLatLng from a JSONArray`() {
        val expectedLat = 55.1231
        val expectedLng = 10.12314
        val jsonArray = JSONArray("[{\"lat\": $expectedLat, \"lng\": $expectedLng}, {\"lat\": $expectedLat, \"lng\": $expectedLng}]")

        val vdLatLngList = JSONUtils.latLongListFromJson(jsonArray)

        asserter.assertEquals("Should contain 2 items", 2, vdLatLngList.size)
        asserter.assertTrue("Should be of type MutableList containing VDLatLng", vdLatLngList is MutableList<VDLatLng>)
        asserter.assertEquals("Should have the expected Latitude", expectedLat, vdLatLngList[0].lat)
        asserter.assertEquals("Should have the expected Longitude", expectedLng, vdLatLngList[0].lng)
    }

    @Test
    fun `boundsFromJson() - Should create a VDBounds object from a JSONObject`() {
        val expectedSouthWestLat = 55.1231
        val expectedSouthWestLng = 10.12314
        val expectedNorthEastLat = 57.6532
        val expectedNorthEastLng = 12.54321

        val jsonObject = JSONObject("{\"southWest\": {\"lat\": $expectedSouthWestLat, \"lng\": $expectedSouthWestLng}, \"northEast\": {\"lat\": $expectedNorthEastLat, \"lng\": $expectedNorthEastLng}}")

        val vdBounds = JSONUtils.boundsFromJson(jsonObject)

        asserter.assertTrue("Should be of type VDBounds", vdBounds is VDBounds)
        asserter.assertEquals("latLngBounds should have the expected southwest.latitude", expectedSouthWestLat, vdBounds.southWest.lat)
        asserter.assertEquals("latLngBounds should have the expected southwest.longitude", expectedSouthWestLng, vdBounds.southWest.lng)
        asserter.assertEquals("latLngBounds should have the expected northeast.latitude", expectedNorthEastLat, vdBounds.northEast.lat)
        asserter.assertEquals("latLngBounds should have the expected northeast.longitude", expectedNorthEastLng, vdBounds.northEast.lng)
    }
}
