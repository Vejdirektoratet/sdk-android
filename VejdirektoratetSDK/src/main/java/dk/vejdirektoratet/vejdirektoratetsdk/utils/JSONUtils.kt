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
import org.json.JSONObject

internal object JSONUtils {

    fun boundsFromJson(boundsObject: JSONObject): Bounds {
        val jsonSouthWest = boundsObject.getJSONObject("southWest")
        val jsonNorthEast = boundsObject.getJSONObject("northEast")
        val southWest = LatLng(jsonSouthWest.getDouble("lat"), jsonSouthWest.getDouble("lng"))
        val northEast = LatLng(jsonNorthEast.getDouble("lat"), jsonNorthEast.getDouble("lng"))

        return Bounds(southWest, northEast)
    }

}