/**
 *  BaseEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.EntityType
import dk.vejdirektoratet.vejdirektoratetsdk.feed.entityTypeFromString
import org.json.JSONObject

open class BaseEntity(data: JSONObject) {
    val entityType: EntityType = entityTypeFromString(data.getString(Constants.ENTITY_TYPE))
    val tag: String = data.getString(Constants.TAG)
}
