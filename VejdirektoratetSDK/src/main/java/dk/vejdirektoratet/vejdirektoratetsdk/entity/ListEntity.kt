/**
 *  ListEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.VDBounds
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.DateParceler
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils.baseEntityTypeFromString
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.json.JSONException
import org.json.JSONObject
import java.util.Date

@Parcelize
@TypeParceler<Date, DateParceler>
open class ListEntity(private val _entityType: BaseEntityType, private val _tag: String, val timestamp: Date, val heading: String, val description: String, val bounds: VDBounds?) : BaseEntity(_entityType, _tag)

@Parcelize
@TypeParceler<Date, DateParceler>
data class Traffic(private val _entityType: BaseEntityType, private val _tag: String, private val _timestamp: Date, private val _heading: String, private val _description: String, private val _bounds: VDBounds?) : ListEntity(_entityType, _tag, _timestamp, _heading, _description, _bounds) {
    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): Traffic {
            TrafficValidator().validate(data)
            val entityType: BaseEntityType = baseEntityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
            val tag: String = data.getString(Constants.TAG)
            val timestamp: Date = Utils.dateFromIso8601String(data.getString(Constants.TIMESTAMP))
            val heading: String = data.getString(Constants.HEADING)
            val description: String = data.getString(Constants.DESCRIPTION)
            val bounds: VDBounds? = try { JSONUtils.vdBoundsFromJson(data.getJSONObject(Constants.BOUNDS)) } catch (e: JSONException) { null }
            return Traffic(entityType, tag, timestamp, heading, description, bounds)
        }
    }

    internal class TrafficValidator : ListValidator()
}

@Parcelize
@TypeParceler<Date, DateParceler>
data class Roadwork(private val _entityType: BaseEntityType, private val _tag: String, private val _timestamp: Date, private val _heading: String, private val _description: String, private val _bounds: VDBounds?) : ListEntity(_entityType, _tag, _timestamp, _heading, _description, _bounds) {
    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): Roadwork {
            RoadworkValidator().validate(data)
            val entityType: BaseEntityType = baseEntityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
            val tag: String = data.getString(Constants.TAG)
            val timestamp: Date = Utils.dateFromIso8601String(data.getString(Constants.TIMESTAMP))
            val heading: String = data.getString(Constants.HEADING)
            val description: String = data.getString(Constants.DESCRIPTION)
            val bounds: VDBounds? = try { JSONUtils.vdBoundsFromJson(data.getJSONObject(Constants.BOUNDS)) } catch (e: JSONException) { null }
            return Roadwork(entityType, tag, timestamp, heading, description, bounds)
        }
    }

    internal class RoadworkValidator : ListValidator()
}

internal open class ListValidator : EntityValidator() {
    override fun validate(data: JSONObject) {
        super.validate(data)

        DictionaryValidator(fields = mapOf(
            Constants.TIMESTAMP to TimestampValidator(),
            Constants.HEADING to StringValidator(),
            Constants.DESCRIPTION to StringValidator(),
            Constants.BOUNDS to DictionaryValidator(true, mapOf(
                Constants.SOUTH_WEST to DictionaryValidator(fields = mapOf(
                    Constants.LATITUDE to NumberValidator(),
                    Constants.LONGITUDE to NumberValidator()
                )),
                Constants.NORTH_EAST to DictionaryValidator(fields = mapOf(
                    Constants.LATITUDE to NumberValidator(),
                    Constants.LONGITUDE to NumberValidator()
                ))
            ))
        )).validate(data)
    }
}
