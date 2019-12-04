/**
 *  BaseEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import org.json.JSONObject

internal val validEntityTypes = mapOf(
    Constants.LATEX_TRAFFIC to BaseEntity.EntityType.Traffic,
    Constants.LATEX_ROADWORK to BaseEntity.EntityType.RoadWork,
    Constants.VD_GEO_INRIX_SEGMENT to BaseEntity.EntityType.RoadSegment
)

open class BaseEntity(data: JSONObject) {
    enum class EntityType {
        Traffic,
        RoadWork,
        RoadSegment
    }

    val entityType: EntityType = entityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
    val tag: String = data.getString(Constants.TAG)

    private fun entityTypeFromString(entityType: String): EntityType? {
        return validEntityTypes[entityType]
    }
}

internal open class EntityValidator {
    @Throws(VDException::class)
    open fun validate(data: JSONObject) {
        DictionaryValidator(fields = mapOf(
            Constants.ENTITY_TYPE to StringValidator(validEntityTypes.keys.toList()),
            Constants.TAG to StringValidator()
        )).validate(data)
    }
}
