/**
 *  VejdirektoratetSDKTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-20.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import com.google.android.gms.maps.model.LatLngBounds
import kotlin.test.asserter
import kotlin.test.Test
import com.google.android.gms.maps.model.LatLng

class VejdirektoratetSDKTest {

    @Test
    fun `asLatLng() - Should return a LatLng`() {
        val expectedLat = 55.1231
        val expectedLng = 10.12314
        val vdLatLng = VDLatLng(expectedLat, expectedLng)

        val latLng = vdLatLng.asLatLng()

        asserter.assertTrue("Should be of type LatLng", latLng is LatLng)
        asserter.assertEquals("Should have the expected Latitude", expectedLat, latLng.latitude)
        asserter.assertEquals("Should have the expected Longitude", expectedLng, latLng.longitude)
    }

    @Test
    fun `asLatLng() - Should return a MutableList of LatLng`() {
        val expectedLat = 55.1231
        val expectedLng = 10.12314
        val vdLatLngList = mutableListOf(
            VDLatLng(expectedLat, expectedLng),
            VDLatLng(expectedLat, expectedLng)
        )

        val latLngList = vdLatLngList.asLatLng()

        asserter.assertEquals("Should contain 2 items", 2, latLngList.size)
        asserter.assertTrue("Should be of type MutableList containing LatLng", latLngList is MutableList<LatLng>)
        asserter.assertTrue("Should containing LatLng ", latLngList[0] is LatLng)
        asserter.assertEquals("Should have the expected Latitude", expectedLat, latLngList[0].latitude)
        asserter.assertEquals("Should have the expected Longitude", expectedLng, latLngList[0].longitude)
    }

    @Test
    fun `asLatLngBounds() - Should return a LatLngBounds`() {
        val expectedSouthWestLat = 55.1231
        val expectedSouthWestLng = 10.12314
        val vdSouthWest = VDLatLng(expectedSouthWestLat, expectedSouthWestLng)

        val expectedNorthEastLat = 57.6532
        val expectedNorthEastLng = 12.54321
        val vdNorthEast = VDLatLng(expectedNorthEastLat, expectedNorthEastLng)

        val vDBounds = VDBounds(vdSouthWest, vdNorthEast)
        val latLngBounds = vDBounds.asLatLngBounds()

        asserter.assertTrue("Should be of type LatLngBounds", latLngBounds is LatLngBounds)
        asserter.assertEquals("latLngBounds should have the expected southwest.latitude", expectedSouthWestLat, latLngBounds.southwest.latitude)
        asserter.assertEquals("latLngBounds should have the expected southwest.longitude", expectedSouthWestLng, latLngBounds.southwest.longitude)
        asserter.assertEquals("latLngBounds should have the expected northeast.latitude", expectedNorthEastLat, latLngBounds.northeast.latitude)
        asserter.assertEquals("latLngBounds should have the expected northeast.longitude", expectedNorthEastLng, latLngBounds.northeast.longitude)
    }

    @Test
    fun `asLatLngBounds() - Should return a MutableList of LatLngBounds`() {
        val expectedSouthWestLat = 55.1231
        val expectedSouthWestLng = 10.12314
        val vdSouthWest = VDLatLng(expectedSouthWestLat, expectedSouthWestLng)

        val expectedNorthEastLat = 57.6532
        val expectedNorthEastLng = 12.54321
        val vdNorthEast = VDLatLng(expectedNorthEastLat, expectedNorthEastLng)

        val vDBounds = mutableListOf(
            VDBounds(vdSouthWest, vdNorthEast),
            VDBounds(vdSouthWest, vdNorthEast)
        )

        val latLngBounds = vDBounds.asLatLngBounds()

        asserter.assertEquals("Should contain 2 items", 2, latLngBounds.size)
        asserter.assertTrue("Should be of type MutableList containing LatLngBounds", latLngBounds is MutableList<LatLngBounds>)
        asserter.assertTrue("Should containing LatLngBounds", latLngBounds[0] is LatLngBounds)
        asserter.assertEquals("latLngBounds should have the expected southwest.latitude", expectedSouthWestLat, latLngBounds[0].southwest.latitude)
        asserter.assertEquals("latLngBounds should have the expected southwest.longitude", expectedSouthWestLng, latLngBounds[0].southwest.longitude)
        asserter.assertEquals("latLngBounds should have the expected northeast.latitude", expectedNorthEastLat, latLngBounds[0].northeast.latitude)
        asserter.assertEquals("latLngBounds should have the expected northeast.longitude", expectedNorthEastLng, latLngBounds[0].northeast.longitude)
    }
}
