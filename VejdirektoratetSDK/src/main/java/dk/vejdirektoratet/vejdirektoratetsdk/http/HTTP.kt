/**
 *  HTTP.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.http

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result as FuelResult
import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.EntityType
import dk.vejdirektoratet.vejdirektoratetsdk.ViewType
import org.json.JSONArray
import org.json.JSONObject

internal class HTTP {

    sealed class Result {
        class Success(val data: JSONArray): Result()
        open class Error(open val exception: Exception): Result()
        class HttpError(override val exception: Exception, val statusCode: Int): Error(exception)
    }

    private val baseUrl = mapOf(
        ViewType.LIST to "https://test-vdapp.dannap.dk/api/v2/list/snapshot",
        ViewType.MAP to "https://test-vdapp.dannap.dk/api/v2/map/snapshot",
        ViewType.GEO to ""
    )

    internal fun request(entityTypes: List<EntityType>, region: Bounds?, zoom: Int?, viewType: ViewType, apiKey: String, onCompletion: (result: Result) -> Unit) {
        val url = buildUrl(entityTypes, region, zoom, viewType, apiKey)

        if (url.isNotBlank()) {
            request(url, onCompletion)
        } else {
            onCompletion(Result.Error(EmptyURLException("Empty Url!")))
        }
    }

    private fun request(url: String, onCompletion: (result: Result) -> Unit) {
        url.httpGet().response { _, response, result ->
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

    private fun buildUrl(entityTypes: List<EntityType>, region: Bounds?, zoom: Int?, viewType: ViewType, apiKey: String): String {
        var url: String = baseUrl[viewType] ?: ""
        if (url == "") {
            return url
        }

        url = "$url?types=${entityTypes.joinToString(separator = ",") { type -> type.value }}"

        if (region != null) {
            url = "$url&sw=${region.southWest.lat},${region.southWest.lng}&ne=${region.northEast.lat},${region.northEast.lng}"
        }

        if (zoom != Int.MAX_VALUE && viewType == ViewType.MAP) {
            url = "$url&zoom=$zoom"
        }

        url = "$url&api_key=$apiKey"

        return url
    }

    private fun buildError(header: String, message: String): JSONArray {
        val errorArray = JSONArray()
        val errorObject = JSONObject()
        errorObject.put("heading", header)
        errorObject.put("description", message)
        errorArray.put(errorObject)
        return errorArray
    }

    private class EmptyURLException(message: String): Exception(message)
}
