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
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject
import java.util.*


open class ListEntity(data: JSONObject): BaseEntity(data) {
    val timestamp: Date = Utils.dateFromIso8601String(data.getString(Constants.TIMESTAMP))
    val heading: String = data.getString(Constants.HEADING)
    val description: String = data.getString(Constants.DESCRIPTION)
    val bounds: Bounds = JSONUtils.boundsFromJson(data.getJSONObject(Constants.BOUNDS))


    class Traffic(data: JSONObject): ListEntity(data) {
        companion object {
            @Throws(VDException::class)
            fun fromEntity(data: JSONObject): Traffic {
                TrafficValidator().validate(data)
                return Traffic(data)
            }
        }

        private class TrafficValidator: ListValidator()
    }

    class Roadwork(data: JSONObject): ListEntity(data) {
        companion object {
            @Throws(VDException::class)
            fun fromEntity(data: JSONObject): Roadwork {
                RoadworkValidator().validate(data)
                return Roadwork(data)
            }
        }

        private class RoadworkValidator: ListValidator()
    }

    private open class ListValidator: EntityValidator() {
        override fun validate(data: JSONObject) {
            super.validate(data)

            DictionaryValidator(fields = mapOf(
                Constants.TIMESTAMP to TimestampValidator(),
                Constants.HEADING to StringValidator(),
                Constants.DESCRIPTION to StringValidator(),
                Constants.BOUNDS to DictionaryValidator(true, mapOf(
                    Constants.SOUTH_WEST to DictionaryValidator(fields = mapOf(
                        Constants.LATITUDE to DoubleValidator(),
                        Constants.LONGITUDE to DoubleValidator()
                    )),
                    Constants.NORTH_EAST to DictionaryValidator(fields = mapOf(
                        Constants.LATITUDE to DoubleValidator(),
                        Constants.LONGITUDE to DoubleValidator()
                    ))
                ))
            )).validate(data)
        }
    }
}
