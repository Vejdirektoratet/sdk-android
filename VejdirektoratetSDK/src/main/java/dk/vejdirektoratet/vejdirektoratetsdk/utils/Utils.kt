/**
 *  Utils.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-13.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.IllegalDateFormatException
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity.MapType
import dk.vejdirektoratet.vejdirektoratetsdk.LatLng as VDLatLng
import java.text.SimpleDateFormat
import java.util.*

internal object Utils {

    fun dateFromIso8601String(timeString: String): Date {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            return formatter.parse(timeString) as Date
        } catch (e: Exception) {
            throw IllegalDateFormatException(timeString)
        }
    }

    fun boundsToLatLngBounds(vdBounds: Bounds): LatLngBounds {
        val southWest = LatLng(vdBounds.southWest.lat, vdBounds.southWest.lng)
        val northEast = LatLng(vdBounds.northEast.lat, vdBounds.northEast.lng)
        return LatLngBounds(southWest, northEast)
    }

    fun latLngToGoogleLatLng(vdLatLng: VDLatLng): LatLng {
        return LatLng(vdLatLng.lat, vdLatLng.lng)
    }

    internal fun mapTypeFromString(entityTypeString: String): MapType {
        return when (entityTypeString) {
            Constants.MAP_TYPE_MARKER -> MapType.MARKER
            Constants.MAP_TYPE_POLYLINE -> MapType.POLYLINE
            Constants.MAP_TYPE_POLYGON -> MapType.POLYGON
            else -> MapType.UNKNOWN
        }
    }
}

