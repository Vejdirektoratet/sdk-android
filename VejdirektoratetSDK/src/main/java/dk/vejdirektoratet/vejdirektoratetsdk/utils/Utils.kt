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
import java.text.SimpleDateFormat
import java.util.*

internal object Utils {

    fun epocMilliFromIso8601String(timeString: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return formatter.parse(timeString) as Date
    }

    fun boundsToLatLngBounds(vdBounds: Bounds): LatLngBounds {
        val southWest = LatLng(vdBounds.southWest.lat, vdBounds.southWest.lng)
        val northEast = LatLng(vdBounds.northEast.lat, vdBounds.northEast.lng)
        return LatLngBounds(southWest, northEast)
    }

}

