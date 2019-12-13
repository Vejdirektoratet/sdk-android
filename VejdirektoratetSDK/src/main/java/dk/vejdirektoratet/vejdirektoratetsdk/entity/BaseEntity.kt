/**
 *  BaseEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import android.os.Parcelable
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.DateParceler
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.json.JSONObject
import java.util.Date

internal val validEntityTypes = mapOf(
    Constants.LATEX_TRAFFIC to BaseEntity.BaseEntityType.Traffic,
    Constants.LATEX_ROADWORK to BaseEntity.BaseEntityType.RoadWork,
    Constants.VD_GEO_INRIX_SEGMENT to BaseEntity.BaseEntityType.RoadSegment
)

@Parcelize
@TypeParceler<Date, DateParceler>
open class BaseEntity(val entityType: BaseEntityType, val tag: String) : Parcelable {
    enum class BaseEntityType {
        Traffic,
        RoadWork,
        RoadSegment
    }
}

internal open class EntityValidator {
    @Throws(VDException::class)
    open fun validate(data: JSONObject) {
        DictionaryValidator(fields = mapOf(
            Constants.ENTITY_TYPE to StringValidator(validValues = validEntityTypes.keys.toList()),
            Constants.TAG to StringValidator()
        )).validate(data)
    }
}
