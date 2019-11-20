/**
 *  JSONUtils.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-13.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.utils

import dk.vejdirektoratet.vejdirektoratetsdk.VDBounds
import dk.vejdirektoratet.vejdirektoratetsdk.VDLatLng
import org.json.JSONArray
import org.json.JSONObject

internal object JSONUtils {

    fun latLngFromJson(latLngObject: JSONObject): VDLatLng {
       return VDLatLng(latLngObject.getDouble("lat"), latLngObject.getDouble("lng"))
    }

    fun latLongListFromJson(listJsonArray: JSONArray): MutableList<VDLatLng> {
        val latLngList: MutableList<VDLatLng> = mutableListOf()

        for (i in 0 until listJsonArray.length()) {
            val latLng = latLngFromJson(listJsonArray.getJSONObject(i))
            latLngList.add(latLng)
        }

        return latLngList
    }

    fun boundsFromJson(boundsObject: JSONObject): VDBounds {
        val jsonSouthWest = boundsObject.getJSONObject("southWest")
        val jsonNorthEast = boundsObject.getJSONObject("northEast")
        val southWest = latLngFromJson(jsonSouthWest)
        val northEast = latLngFromJson(jsonNorthEast)

        return VDBounds(southWest, northEast)
    }

}