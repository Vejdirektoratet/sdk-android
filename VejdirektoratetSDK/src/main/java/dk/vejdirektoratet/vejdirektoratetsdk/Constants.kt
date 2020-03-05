package dk.vejdirektoratet.vejdirektoratetsdk

/**
 *  Constants.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-14.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

internal class Constants {
    companion object {
        const val TIMESTAMP = "timestamp"
        const val HEADING = "heading"
        const val DESCRIPTION = "description"
        const val TAG = "tag"
        const val ENTITY_TYPE = "entityType"
        const val BOUNDS = "bounds"
        const val SOUTH_WEST = "southWest"
        const val NORTH_EAST = "northEast"
        const val LATITUDE = "lat"
        const val LONGITUDE = "lng"
        const val POINTS = "points"
        const val CENTER = "center"
        const val STYLE = "style"
        const val ID = "id"
        const val STROKE_COLOR = "strokeColor"
        const val STROKE_WIDTH = "strokeWidth"
        const val FILL_COLOR = "fillColor"
        const val ICON = "icon"
        const val DASHED = "dashed"
        const val DASH_COLOR = "dashColor"
        const val Z_INDEX = "zIndex"
        const val EXTRAS = "extras"
        const val TYPE = "type"

        const val LATEX_TRAFFIC = "latextraffic"
        const val LATEX_ROADWORK = "latexroadwork"
        const val VD_GEO_INRIX_SEGMENT = "vd.geo.inrix.segment"

        const val MAP_TYPE_MARKER = "MARKER"
        const val MAP_TYPE_POLYLINE = "POLYLINE"
        const val MAP_TYPE_POLYGON = "POLYGON"

        private const val BASE_URL = BuildConfig.SERVER_URL
        const val BASE_URL_LIST = "${BASE_URL}list"
        const val BASE_URL_MAP = "${BASE_URL}map"

        const val BASE_URL_LIST_SNAPSHOT = "$BASE_URL_LIST/snapshot"
        const val BASE_URL_MAP_SNAPSHOT = "$BASE_URL_MAP/snapshot"

        const val BASE_URL_LIST_ENTITY = "$BASE_URL_LIST/entity"
        const val BASE_URL_MAP_ENTITY = "$BASE_URL_MAP/entity"
    }
}
