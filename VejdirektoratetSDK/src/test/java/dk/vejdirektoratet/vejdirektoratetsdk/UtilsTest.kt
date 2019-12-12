/**
 *
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-21.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.asserter

class UtilsTest {

    @Test
    fun `dateFromIso8601String() - Should throw IllegalDateFormatException if the date string has an invalid format`() {
        val invalidDateString0 = ""
        val invalidDateString1 = "2019-8-15T06:02:44.000"

        assertFailsWith<IllegalDateFormatException> { Utils.dateFromIso8601String(invalidDateString0) }
        assertFailsWith<IllegalDateFormatException> { Utils.dateFromIso8601String(invalidDateString1) }
    }

    @Test
    fun `dateFromIso8601String() - Should return a Date object generated from the date string`() {
        val dateString = "2019-08-15T06:02:44.000+0000"
        val expectedTimestamp = 1565848964000

        val date = Utils.dateFromIso8601String(dateString)

        asserter.assertTrue("Should be of type Date", date is Date)
        asserter.assertEquals("Should have the correct timestamp", expectedTimestamp, date.time)
    }

    @Test
    fun `VDBoundsToLatLngBounds() - Should convert a VDBounds to a LatLngBounds`() {
        val southWestLat = 55.1231
        val southWestLng = 10.12314
        val southWest = VDLatLng(southWestLat, southWestLng)

        val northEastLat = 57.6532
        val northEastLng = 12.54321
        val northEast = VDLatLng(northEastLat, northEastLng)

        val vdBounds = VDBounds(southWest, northEast)
        val latLngBounds = Utils.vdBoundsToLatLngBounds(vdBounds)

        asserter.assertTrue("Should be of type LatLngBounds", latLngBounds is LatLngBounds)
        asserter.assertEquals("Should have the correct latitude for southWest", vdBounds.southWest.lat, latLngBounds.southwest.latitude)
        asserter.assertEquals("Should have the correct longitude for southWest", vdBounds.southWest.lng, latLngBounds.southwest.longitude)
        asserter.assertEquals("Should have the correct latitude for northEast", vdBounds.northEast.lat, latLngBounds.northeast.latitude)
        asserter.assertEquals("Should have the correct longitude for northEast", vdBounds.northEast.lng, latLngBounds.northeast.longitude)
    }

    @Test
    fun `vdLatLngToLatLng() - Should convert a VDLatLng to a LatLng`() {
        val vdLatLng = VDLatLng(55.1231, 10.12314)
        val latLng = Utils.vdLatLngToLatLng(vdLatLng)

        asserter.assertTrue("Should be of type LatLng", latLng is LatLng)
        asserter.assertEquals("Should have the correct latitude", vdLatLng.lat, latLng.latitude)
        asserter.assertEquals("Should have the correct longitude", vdLatLng.lng, latLng.longitude)
    }

    @Test
    fun `mapTypeFromString() - Should correctly map a string to a MapType`() {
        val mapTypeMarker = Utils.mapTypeFromString(Constants.MAP_TYPE_MARKER)
        val mapTypePolyline = Utils.mapTypeFromString(Constants.MAP_TYPE_POLYLINE)
        val mapTypePolygon = Utils.mapTypeFromString(Constants.MAP_TYPE_POLYGON)
        val mapTypeSomethingElse = Utils.mapTypeFromString("somethingElse")

        asserter.assertEquals("Should be the MapType MARKER", MapEntity.MapType.MARKER, mapTypeMarker)
        asserter.assertEquals("Should be the MapType POLYLINE", MapEntity.MapType.POLYLINE, mapTypePolyline)
        asserter.assertEquals("Should be the MapType POLYGON", MapEntity.MapType.POLYGON, mapTypePolygon)
        asserter.assertEquals("Should be the MapType UNKNOWN", MapEntity.MapType.UNKNOWN, mapTypeSomethingElse)
    }

    @Test
    fun `latLngBoundsToVDBounds() - Should convert a LatLngBound to a VDBounds`() {
        val southWestLat = 55.1231
        val southWestLng = 10.12314
        val southWest = LatLng(southWestLat, southWestLng)

        val northEastLat = 57.6532
        val northEastLng = 12.54321
        val northEast = LatLng(northEastLat, northEastLng)

        val latLngBounds = LatLngBounds(southWest, northEast)
        val vdBounds = Utils.latLngBoundsToVDBounds(latLngBounds)

        asserter.assertTrue("Should be of type LatLngBounds", vdBounds is VDBounds)
        asserter.assertEquals("Should have the correct latitude for southWest", latLngBounds.southwest.latitude, vdBounds.southWest.lat)
        asserter.assertEquals("Should have the correct longitude for southWest", latLngBounds.southwest.longitude, vdBounds.southWest.lng)
        asserter.assertEquals("Should have the correct latitude for northEast", latLngBounds.northeast.latitude, vdBounds.northEast.lat)
        asserter.assertEquals("Should have the correct longitude for northEast", latLngBounds.northeast.longitude, vdBounds.northEast.lng)
    }
}
