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
import dk.vejdirektoratet.vejdirektoratetsdk.*
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.IllegalDateFormatException
import dk.vejdirektoratet.vejdirektoratetsdk.entity.EntityValidator
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity.MapType
import dk.vejdirektoratet.vejdirektoratetsdk.entity.Validator
import org.json.JSONObject
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

    fun VDBoundsToLatLngBounds(vdBounds: VDBounds): LatLngBounds {
        val southWest = LatLng(vdBounds.southWest.lat, vdBounds.southWest.lng)
        val northEast = LatLng(vdBounds.northEast.lat, vdBounds.northEast.lng)
        return LatLngBounds(southWest, northEast)
    }

    fun vdLatLngToLatLng(vdLatLng: VDLatLng): LatLng {
        return LatLng(vdLatLng.lat, vdLatLng.lng)
    }

    fun mapTypeFromString(entityTypeString: String): MapType {
        return when (entityTypeString) {
            Constants.MAP_TYPE_MARKER -> MapType.MARKER
            Constants.MAP_TYPE_POLYLINE -> MapType.POLYLINE
            Constants.MAP_TYPE_POLYGON -> MapType.POLYGON
            else -> MapType.UNKNOWN
        }
    }

    fun latLngBoundsToVDBounds(latLngBounds: LatLngBounds): VDBounds {
        val southWest = VDLatLng(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude)
        val northEast = VDLatLng(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude)
        return VDBounds(southWest, northEast)
    }

    internal inline fun <reified T> assertThrowException(data: String, validator: EntityValidator) {
        try {
            validator.validate(JSONObject(data))
            throw MissingExceptionException(T::class, data)
        } catch (e: Exception) {
            handleException<T>(e, data)
        }
    }

    internal inline fun <reified T> assertThrowException(data: String, validator: Validator) {
        try {
            validator.validate(JSONObject(data))
            throw MissingExceptionException(T::class, data)
        } catch (e: Exception) {
            handleException<T>(e, data)
        }
    }

    @Throws(VDException::class)
    private inline fun <reified T> handleException(e: Exception, data: String) {
        if (e is MissingExceptionException) {
            throw MissingExceptionException(T::class, data)
        } else if (e !is T) {
            throw IncorrectExceptionException(T::class, e::class, data)
        }
    }
}

