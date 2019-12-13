/**
 *  Parcelers.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-12-12.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import android.os.Parcel
import kotlinx.android.parcel.Parceler
import java.util.*

internal object DateParceler : Parceler<Date> {

    override fun create(parcel: Parcel) = Date(parcel.readLong())

    override fun Date.write(parcel: Parcel, flags: Int)
            = parcel.writeLong(time)
}
