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
import org.json.JSONArray

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
        class Success(val data: JSONArray) : Result()
        open class Error(open val exception: Exception) : Result()
        class HttpError(override val exception: Exception, val statusCode: Int) : Error(exception)
    }

    private val baseUrl = mapOf(
        ViewType.LIST to Constants.BASE_URL_LIST,
        ViewType.MAP to Constants.BASE_URL_MAP
    )

    internal fun request(entityTypes: List<EntityType>, region: VDBounds?, zoom: Int?, viewType: ViewType, apiKey: String, onCompletion: (result: Result) -> Unit): VDRequest {
        val url = buildUrl(entityTypes, region, zoom, viewType, apiKey)
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
                            onCompletion(Result.Success(JSONArray(String(result.value))))
                        }
                    }
                }
        }

        return VDRequest(httpRequest)
    }

    private fun buildUrl(entityTypes: List<EntityType>, region: VDBounds?, zoom: Int?, viewType: ViewType, apiKey: String): String {
        var url: String = baseUrl[viewType] ?: ""
        if (url == "") {
            return url
        }

        url = "$url?types=${entityTypes.joinToString(separator = ",") { type -> type.value }}"

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
