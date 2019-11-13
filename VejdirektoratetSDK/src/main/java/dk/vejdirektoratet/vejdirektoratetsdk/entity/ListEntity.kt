/**
 *  ListEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject
import java.util.*


open class ListEntity(data: JSONObject): BaseEntity(data) {
    val timestamp: Date = Utils.epocMilliFromIso8601String(data.getString("timestamp"))
    val heading: String = data.getString("heading")
    val description: String = data.getString("description")
    val bounds: Bounds = JSONUtils.boundsFromJson(data.getJSONObject("bounds"))


    class Traffic(data: JSONObject): ListEntity(data) {
        companion object {
            fun fromEntity(data: JSONObject): Traffic {
                return Traffic(data)
            }
        }
    }

    class Roadwork(data: JSONObject): ListEntity(data) {
        companion object {
            fun fromEntity(data: JSONObject): Roadwork {
                return Roadwork(data)
            }
        }
    }
}
