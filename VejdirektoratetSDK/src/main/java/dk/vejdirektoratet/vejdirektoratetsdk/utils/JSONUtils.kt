/**
 *  JSONUtils.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-13.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.utils

import dk.vejdirektoratet.vejdirektoratetsdk.Bounds
import dk.vejdirektoratet.vejdirektoratetsdk.LatLng
import org.json.JSONArray
import org.json.JSONObject

internal object JSONUtils {

    fun latLngFromJson(latLngObject: JSONObject): LatLng {
       return LatLng(latLngObject.getDouble("lat"), latLngObject.getDouble("lng"))
    }

    fun latLongListFromJson(listJsonArray: JSONArray): MutableList<LatLng> {
        val latLngList: MutableList<LatLng> = mutableListOf()

        for (i in 0 until listJsonArray.length()) {
            val latLng = latLngFromJson(listJsonArray.getJSONObject(i))
            latLngList.add(latLng)
        }

        return latLngList
    }

    fun boundsFromJson(boundsObject: JSONObject): Bounds {
        val jsonSouthWest = boundsObject.getJSONObject("southWest")
        val jsonNorthEast = boundsObject.getJSONObject("northEast")
        val southWest = latLngFromJson(jsonSouthWest)
        val northEast = latLngFromJson(jsonNorthEast)

        return Bounds(southWest, northEast)
    }

}