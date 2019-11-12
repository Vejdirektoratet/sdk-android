/**
 *  VejdirektoratetSDK.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.http.HTTP
import org.json.JSONArray

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

class Bounds(val southWest: LatLng, val northEast: LatLng)

class LatLng(val lat: Double, val lng: Double)

class VejdirektoratetSDK {

    companion object {
        fun request(entityTypes: List<EntityType>, region: Bounds?, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: JSONArray) -> Unit) {
            HTTP().request(entityTypes, region, zoom, viewType, apiKey, onCompletion)
        }

        fun request(entityTypes: List<EntityType>, region: LatLngBounds, zoom: Int? = Int.MAX_VALUE, viewType: ViewType, apiKey: String, onCompletion: (result: JSONArray) -> Unit) {
            val southWest = LatLng(region.southwest.latitude, region.southwest.longitude)
            val northEast = LatLng(region.northeast.latitude, region.northeast.longitude)
            request(entityTypes, Bounds(southWest, northEast), zoom, viewType, apiKey, onCompletion)
        }
    }
}
