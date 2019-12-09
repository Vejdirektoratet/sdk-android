/**
 *  Feed.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.feed

import dk.vejdirektoratet.vejdirektoratetsdk.*
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.entity.*
import dk.vejdirektoratet.vejdirektoratetsdk.http.HTTP
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity.MapType
import dk.vejdirektoratet.vejdirektoratetsdk.http.VDRequest
import org.json.JSONArray
import org.json.JSONObject

class Feed {

    /**
     * Defines all possible results of a request
     */
    sealed class Result {
        /**
         * The response of a successful request
         *
         * Extends [Result] with a MutableList of entities
         */
        class Success(val entities: MutableList<BaseEntity>): Result()

        /**
         * The response of a failed request
         *
         * Extends [Result] with an Exception
         */
        open class Error(open val exception: Exception): Result()

        /**
         * The response of a request with a http status code different from 200
         *
         * Extends [Result.Error] with the status code of the http response
         */
        class HttpError(override val exception: Exception, val statusCode: Int): Error(exception)
    }

    internal fun request(entityTypes: List<EntityType>, region: VDBounds?, zoom: Int?, viewType: ViewType, apiKey: String, onCompletion: (result: Result) -> Unit): VDRequest {
        return HTTP().request(entityTypes, region, zoom, viewType, apiKey) { result: HTTP.Result ->
            onCompletion(mapHttpResult(result, viewType))
        }
    }

    private fun mapHttpResult(httpResult: HTTP.Result, viewType: ViewType): Result = when (httpResult) {
        is HTTP.Result.Success -> Result.Success(mapEntities(httpResult.data, viewType))
        is HTTP.Result.HttpError -> Result.HttpError(httpResult.exception, httpResult.statusCode)
        is HTTP.Result.Error -> Result.Error(httpResult.exception)
    }

    private fun mapEntities(entities: JSONArray, viewType: ViewType): MutableList<BaseEntity> {
        val mappedEntities: MutableList<BaseEntity> = mutableListOf()

        for (i in 0 until entities.length()) {
            try {
                val entity = mapEntity(entities.getJSONObject(i), viewType)
                mappedEntities.add(entity)
            } catch (e: VDException) {
                continue
            }
        }

        return mappedEntities
    }

    private fun mapEntity(entity: JSONObject, viewType: ViewType): BaseEntity = when (viewType) {
        ViewType.LIST -> mapListEntity(entity)
        ViewType.MAP -> mapMapEntity(entity)
    }

    private fun mapListEntity(entity: JSONObject): BaseEntity {
        if (entity.has(Constants.ENTITY_TYPE)) {
            val entityType = entity.get(Constants.ENTITY_TYPE)

            if (entityType is String) {
                return when(entityType) {
                    Constants.LATEX_TRAFFIC -> Traffic.fromEntity(entity)
                    Constants.LATEX_ROADWORK -> Roadwork.fromEntity(entity)
                    else -> throw UnknownEntityTypeException("Unknown EntityType! $entity")
                }
            }
        }
        throw UnknownEntityTypeException("Unknown EntityType! $entity")
    }

    private fun mapMapEntity(entity: JSONObject): BaseEntity {
        if (entity.has(Constants.TYPE)) {
            val type = entity.get(Constants.TYPE)

            if (type is String) {
                return when(type) {
                    MapType.MARKER.value -> MapMarker.fromEntity(entity)
                    MapType.POLYLINE.value -> MapPolyline.fromEntity(entity)
                    MapType.POLYGON.value -> MapPolygon.fromEntity(entity)
                    else -> throw UnknownMapTypeException("Unknown MapType! $entity")
                }
            }
        }
        throw UnknownMapTypeException("Unknown MapType! $entity")
    }
}
