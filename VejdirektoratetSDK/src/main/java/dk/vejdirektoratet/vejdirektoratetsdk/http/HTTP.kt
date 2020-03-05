/**
 *  HTTP.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.http

import com.github.kittinunf.fuel.core.requests.CancellableRequest
import com.github.kittinunf.fuel.core.requests.isCancelled
import com.github.kittinunf.fuel.httpGet
import dk.vejdirektoratet.vejdirektoratetsdk.ViewType
import dk.vejdirektoratet.vejdirektoratetsdk.EntityType
import dk.vejdirektoratet.vejdirektoratetsdk.VDBounds
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import com.github.kittinunf.result.Result as FuelResult

/**
 *  A request that can be cancelled
 *
 *  @property request the network request
 */
class VDRequest(private val request: CancellableRequest) {
    fun cancel() {
        request.cancel()
    }
}

internal class HTTP {

    sealed class Result {
        class Success(val data: String) : Result()
        open class Error(open val exception: Exception) : Result()
        class HttpError(override val exception: Exception, val statusCode: Int) : Error(exception)
    }

    private val baseUrlSnapshot = mapOf(
        ViewType.LIST to Constants.BASE_URL_LIST_SNAPSHOT,
        ViewType.MAP to Constants.BASE_URL_MAP_SNAPSHOT
    )

    private val baseUrlEntity = mapOf(
        ViewType.LIST to Constants.BASE_URL_LIST_SNAPSHOT,
        ViewType.MAP to Constants.BASE_URL_MAP_SNAPSHOT
    )

    internal fun request(tag: String?, entityTypes: List<EntityType>, region: VDBounds?, zoom: Int?, viewType: ViewType, apiKey: String, onCompletion: (result: Result) -> Unit): VDRequest {
        val url = buildUrl(tag, entityTypes, region, zoom, viewType, apiKey)
        return request(url, onCompletion)
    }

    private fun request(url: String, onCompletion: (result: Result) -> Unit): VDRequest {
        val httpRequest = url.httpGet()
            .interrupt { request -> println("${request.url} was interrupted and cancelled") }
            .response { request, response, result ->
                if (!request.isCancelled) {
                    when (result) {
                        is FuelResult.Failure -> {
                            onCompletion(Result.HttpError(result.error, response.statusCode))
                        }
                        is FuelResult.Success -> {
                            onCompletion(Result.Success(String(result.value)))
                        }
                    }
                }
        }

        return VDRequest(httpRequest)
    }

    private fun buildUrl(tag: String?, entityTypes: List<EntityType>, region: VDBounds?, zoom: Int?, viewType: ViewType, apiKey: String): String {
        var url = baseUrlSnapshot[viewType]

        url = "$url?types=${entityTypes.joinToString(separator = ",") { type -> type.value }}"

        if (tag != null) {
            url = "$url&tag=$tag"
        }

        if (region != null) {
            url = "$url&sw=${region.southWest.lat},${region.southWest.lng}&ne=${region.northEast.lat},${region.northEast.lng}"
        }

        if (zoom != null && viewType == ViewType.MAP) {
            url = "$url&zoom=$zoom"
        }

        url = "$url&api_key=$apiKey"

        return url
    }
}
