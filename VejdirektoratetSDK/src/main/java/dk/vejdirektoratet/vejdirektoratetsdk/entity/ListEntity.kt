/**
 *  ListEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.InvalidEntityException
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject
import java.util.*


open class ListEntity(data: JSONObject): BaseEntity(data) {
    val timestamp: Date = Utils.epocMilliFromIso8601String(data.getString(Constants.TIMESTAMP))
    val heading: String = data.getString(Constants.HEADING)
    val description: String = data.getString(Constants.DESCRIPTION)
    val bounds: Bounds = JSONUtils.boundsFromJson(data.getJSONObject(Constants.BOUNDS))


    class Traffic(data: JSONObject): ListEntity(data) {
        companion object {
            @Throws(VDException::class)
            fun fromEntity(data: JSONObject): Traffic {
                throw InvalidEntityException("Invalid entity! $data")
                //return Traffic(data)
            }
        }
    }

    class Roadwork(data: JSONObject): ListEntity(data) {
        companion object {
            @Throws(VDException::class)
            fun fromEntity(data: JSONObject): Roadwork {
                throw InvalidEntityException("Invalid entity! $data")
                //return Roadwork(data)
            }
        }
    }
}
