/**
 *  VejdirektoratetSDK.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed
import dk.vejdirektoratet.vejdirektoratetsdk.http.VDRequest
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import kotlinx.android.parcel.Parcelize

/**
 * An enum describing the available entity types
 *
 * [TRAFFIC] traffic events
 * [ROADWORK] roadwork events
 * [TRAFFIC_DENSITY] congestion level of road segments
 * [WINTER_DEICING] deicing status of road segments
 * [WINTER_CONDITION] condition of road segments with respect to slipperiness
 */
@Parcelize
enum class EntityType(val value: String) : Parcelable {
    TRAFFIC("traffic"),
    ROADWORK("roadworks"),
    TRAFFIC_DENSITY("trafficdensity"),
    WINTER_DEICING("winterdeicing"),
    WINTER_CONDITION("wintercondition"),
}

/**
 * An enum describing the available view types
 *
 * [LIST] returns entities suitable for non-map representation
 * [MAP] returns entities suitable for map representation
 */
@Parcelize
enum class ViewType : Parcelable {
    LIST,
    MAP
}

class VejdirektoratetSDK {

    companion object {
        /**
         * A method for requesting entities
         *
         * @param entityTypes the desired [EntityType]'s to be returned in the callback
         * @param region the region from which to get entities
         * @param zoom Google-like zoom level to determine the detail level
         * @param viewType the desired [ViewType] for which to display the entities
         * @param apiKey the API key should be acquired from https://nap.vd.dk/
         * @param onCompletion the callback method which will receive the entities in form of a [Feed.Result]
         * @return [VDRequest] returns a cancellable request
         */
        fun request(entityTypes: List<EntityType>, region: VDBounds?, zoom: Int? = null, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit): VDRequest {
            return Feed().request(entityTypes, region, zoom, viewType, apiKey, onCompletion)
        }

        /**
         * A method for requesting entities
         *
         * @param entityTypes the desired [EntityType]'s to be returned in the callback
         * @param region the region from which to get entities
         * @param zoom Google-like zoom level to determine the detail level
         * @param viewType the desired [ViewType] for which to display the entities
         * @param apiKey the API key should be acquired from https://nap.vd.dk/
         * @param onCompletion the callback method which will receive the entities in form of a [Feed.Result]
         * @return [VDRequest] returns a cancellable request
         */
        fun request(entityTypes: List<EntityType>, region: LatLngBounds, zoom: Int? = null, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit): VDRequest {
            return request(entityTypes, Utils.latLngBoundsToVDBounds(region), zoom, viewType, apiKey, onCompletion)
        }

        /**
         * A method for requesting a specific entity from its identifier
         *
         * @param id the identifier of the entity
         * @param apiKey the API key should be acquired from https://nap.vd.dk/
         * @param onCompletion the callback method which will receive the result of the request in from of a [Feed.Result]
         * @return [VDRequest] returns a cancellable request
         */
        fun requestEntity(tag: String, viewType: ViewType, apiKey: String, onCompletion: (result: Feed.Result) -> Unit): VDRequest {
            return Feed().requestEntity(tag, viewType, apiKey, onCompletion)
        }
    }
}

/**
 * A geographical point represented by a latitude and a longitude
 *
 * A data class wrapping a latitude and a longitude into an object
 *
 * @property lat the latitude as a Double
 * @property lng the longitude as a Double
 */
@Parcelize
data class VDLatLng(val lat: Double, val lng: Double) : Parcelable

/**
 * Converting a [VDLatLng] into a Google Maps [LatLng] object
 *
 * An extension function extending the [VDLatLng] data class,
 * enabling easy conversion from [VDLatLng] to Google Maps [LatLng]
 */
fun VDLatLng.asLatLng(): LatLng {
    return Utils.vdLatLngToLatLng(this)
}

/**
 * Converting a MutableList of [VDLatLng] into a MutableList of Google Maps [LatLng] object
 *
 * An extension function extending the [VDLatLng] data class,
 * enabling easily converting MutableLists of [VDLatLng] into MutableList of Google Maps [LatLng]
 */
fun MutableList<VDLatLng>.asLatLng(): MutableList<LatLng> {
    val latLngs: MutableList<LatLng> = mutableListOf()

    for (i in 0 until this.size) {
        latLngs.add(Utils.vdLatLngToLatLng(this[i]))
    }

    return latLngs
}

/**
 * A geographical region spanned by two [VDLatLng] objects
 *
 * A data class wrapping two [VDLatLng] objects representing the south west
 * and north east boarders of the geographical region
 *
 * @property southWest the south west boarder of the region
 * @property northEast the north east boarder of the region
 */
@Parcelize
data class VDBounds(val southWest: VDLatLng, val northEast: VDLatLng) : Parcelable

/**
 * Converting a [VDBounds] into a Google Maps [LatLngBounds] object
 *
 * An extension function extending the [VDBounds] data class,
 * enabling easy conversion from [VDBounds] to Google Maps [LatLngBounds]
 */
fun VDBounds.asLatLngBounds(): LatLngBounds {
    return Utils.vdBoundsToLatLngBounds(this)
}

/**
 * Converting a MutableList of [VDBounds] into a MutableList of Google Maps [LatLngBounds] object
 *
 * An extension function extending the [VDBounds] data class,
 * enabling easily converting MutableLists of [VDBounds] into MutableList of Google Maps [LatLngBounds]
 */
fun MutableList<VDBounds>.asLatLngBounds(): MutableList<LatLngBounds> {
    val latlngBounds: MutableList<LatLngBounds> = mutableListOf()

    for (i in 0 until this.size) {
        latlngBounds.add(Utils.vdBoundsToLatLngBounds(this[i]))
    }

    return latlngBounds
}
