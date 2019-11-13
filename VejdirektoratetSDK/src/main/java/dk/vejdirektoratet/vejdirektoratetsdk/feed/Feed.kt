/**
 *  Feed.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.feed

import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.EntityType
import dk.vejdirektoratet.vejdirektoratetsdk.ViewType
import dk.vejdirektoratet.vejdirektoratetsdk.http.HTTP
import dk.vejdirektoratet.vejdirektoratetsdk.entity.BaseEntity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.ListEntity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.UnknownEntity
import org.json.JSONArray
import org.json.JSONObject

internal fun entityTypeFromString(entityTypeString: String): EntityType {
    return when (entityTypeString) {
        "latextraffic" -> EntityType.TRAFFIC
        "latexroadwork" -> EntityType.ROADWORK
        else -> EntityType.UNKNOWN
    }
}

class Feed {

    sealed class Result {
        class Success(val entities: MutableList<BaseEntity>): Result()
        open class Error(open val exception: Exception): Result()
        class HttpError(override val exception: Exception, val statusCode: Int): Error(exception)
    }

    internal fun request(entityTypes: List<EntityType>, region: Bounds?, zoom: Int?, viewType: ViewType, apiKey: String, onCompletion: (result: Result) -> Unit) {
        HTTP().request(entityTypes, region, zoom, viewType, apiKey) { result: HTTP.Result ->
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
            val entity = mapEntity(entities.getJSONObject(i), viewType)

            if (entity !is UnknownEntity) {
                mappedEntities.add(entity)
            }
        }

        return mappedEntities
    }

    private fun mapEntity(entity: JSONObject, viewType: ViewType): BaseEntity = when (viewType) {
        ViewType.LIST -> mapListEntity(entity)
        ViewType.MAP -> mapMapEntity(entity)
        else -> UnknownEntity()
    }

    private fun mapListEntity(entity: JSONObject): BaseEntity = when(entityTypeFromString(entity.getString("entityType"))) {
        EntityType.TRAFFIC -> ListEntity.Traffic.fromEntity(entity)
        EntityType.ROADWORK -> ListEntity.Roadwork.fromEntity(entity)
        else -> UnknownEntity()
    }

    private fun mapMapEntity(entity: JSONObject): BaseEntity {
        return UnknownEntity()
    }
}
