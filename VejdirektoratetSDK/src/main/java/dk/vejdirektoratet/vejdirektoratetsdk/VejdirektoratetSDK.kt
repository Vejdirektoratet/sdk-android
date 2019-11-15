/**
 *  VejdirektoratetSDK.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import com.google.android.gms.maps.model.LatLng as GoogleLatLng
import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils

enum class EntityType(val value: String) {
    TRAFFIC("traffic"),
    ROADWORK("roadworks"),
    TRAFFIC_DENSITY("trafficdensity"),
    WINTER_DEICING("winterdeicing"),
    WINTER_CONDITION("wintercondition"),
    UNKNOWN("unknown")
}

enum class ViewType {
    LIST,
    MAP,
    GEO,
    UNKNOWN
}

enum class MapType(val value: String) {
    MARKER("MARKER"),
    POLYLINE("POLYLINE"),
    POLYGON("POLYGON"),
    UNKNOWN("UNKNOWN")
}

class VejdirektoratetSDK {

    companion object {
        fun request(entityTypes: List<EntityType>, region: Bounds?, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit) {
            Feed().request(entityTypes, region, zoom, viewType, apiKey, onCompletion)
        }

        fun request(entityTypes: List<EntityType>, region: LatLngBounds, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit) {
            val southWest = LatLng(region.southwest.latitude, region.southwest.longitude)
            val northEast = LatLng(region.northeast.latitude, region.northeast.longitude)
            request(entityTypes, Bounds(southWest, northEast), zoom, viewType, apiKey, onCompletion)
        }
    }
}

data class LatLng(val lat: Double, val lng: Double)

fun LatLng.asGoogleLatLng(): GoogleLatLng {
    return Utils.latLngToGoogleLatLng(this)
}

fun MutableList<LatLng>.asGoogleLatLng(): MutableList<GoogleLatLng> {
    val googleLatLngs: MutableList<GoogleLatLng> = mutableListOf()

    for (i in 0 until this.size) {
        googleLatLngs.add(Utils.latLngToGoogleLatLng(this[i]))
    }

    return googleLatLngs
}

data class Bounds(val southWest: LatLng, val northEast: LatLng)

fun Bounds.asLatLngBounds(): LatLngBounds {
    return Utils.boundsToLatLngBounds(this)
}

fun MutableList<Bounds>.asLatLngBounds(): MutableList<LatLngBounds> {
    val latlngBounds: MutableList<LatLngBounds> = mutableListOf()

    for (i in 0 until this.size) {
        latlngBounds.add(Utils.boundsToLatLngBounds(this[i]))
    }

    return latlngBounds
}
