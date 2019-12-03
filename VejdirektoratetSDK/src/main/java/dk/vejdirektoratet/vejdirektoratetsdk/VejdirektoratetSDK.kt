/**
 *  VejdirektoratetSDK.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed
import dk.vejdirektoratet.vejdirektoratetsdk.http.VDRequest
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils

enum class EntityType(val value: String) {
    TRAFFIC("traffic"),
    ROADWORK("roadworks"),
    TRAFFIC_DENSITY("trafficdensity"),
    WINTER_DEICING("winterdeicing"),
    WINTER_CONDITION("wintercondition"),
}

enum class ViewType {
    LIST,
    MAP,
    GEO
}

class VejdirektoratetSDK {

    companion object {
        fun request(entityTypes: List<EntityType>, region: VDBounds?, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit): VDRequest {
            return Feed().request(entityTypes, region, zoom, viewType, apiKey, onCompletion)
        }

        fun request(entityTypes: List<EntityType>, region: LatLngBounds, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit): VDRequest {
            return request(entityTypes, Utils.latLngBoundsToVDBounds(region), zoom, viewType, apiKey, onCompletion)
        }
    }
}

data class VDLatLng(val lat: Double, val lng: Double)

fun VDLatLng.asLatLng(): LatLng {
    return Utils.vdLatLngToLatLng(this)
}

fun MutableList<VDLatLng>.asLatLng(): MutableList<LatLng> {
    val latLngs: MutableList<LatLng> = mutableListOf()

    for (i in 0 until this.size) {
        latLngs.add(Utils.vdLatLngToLatLng(this[i]))
    }

    return latLngs
}

data class VDBounds(val southWest: VDLatLng, val northEast: VDLatLng)

fun VDBounds.asLatLngBounds(): LatLngBounds {
    return Utils.VDBoundsToLatLngBounds(this)
}

fun MutableList<VDBounds>.asLatLngBounds(): MutableList<LatLngBounds> {
    val latlngBounds: MutableList<LatLngBounds> = mutableListOf()

    for (i in 0 until this.size) {
        latlngBounds.add(Utils.VDBoundsToLatLngBounds(this[i]))
    }

    return latlngBounds
}
