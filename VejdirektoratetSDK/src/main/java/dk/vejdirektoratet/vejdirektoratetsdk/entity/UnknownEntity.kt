/**
 *  UnknownEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-12.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import org.json.JSONObject

internal class UnknownEntity : BaseEntity(JSONObject("{\"${Constants.ENTITY_TYPE}\": \"\",\"${Constants.TAG}\": \"\"}"))