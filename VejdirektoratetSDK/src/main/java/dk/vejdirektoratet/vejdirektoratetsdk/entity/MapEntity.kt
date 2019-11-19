/**
 *  MapEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.LatLng
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject

open class MapEntity(data: JSONObject): BaseEntity(data) {
    val type: MapType = Utils.mapTypeFromString(data.getString(Constants.TYPE))
    val style: MapStyle = MapStyle.fromEntity(data.getJSONObject(Constants.STYLE))

    enum class MapType(val value: String) {
        MARKER("MARKER"),
        POLYLINE("POLYLINE"),
        POLYGON("POLYGON"),
        UNKNOWN("UNKNOWN")
    }
}

class MapMarker(data: JSONObject): MapEntity(data) {
    val center: LatLng = JSONUtils.latLngFromJson(data.getJSONObject(Constants.CENTER))

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapEntity {
            MapMarkerValidator().validate(data)
            return MapMarker(data)
        }
    }

    private class MapMarkerValidator: MapValidator() {
        override fun validate(data: JSONObject) {
            super.validate(data)

            DictionaryValidator(fields = mapOf(
                Constants.CENTER to DictionaryValidator(fields = mapOf(
                    Constants.LATITUDE to NumberValidator(),
                    Constants.LONGITUDE to NumberValidator()
                )),
                Constants.STYLE to MapStyle.validator
            )).validate(data)
        }
    }
}

class MapPolyline(data: JSONObject): MapEntity(data) {
    val points: MutableList<LatLng> = JSONUtils.latLongListFromJson(data.getJSONArray(Constants.POINTS))

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapPolyline {
            MapPolylineValidator().validate(data)
            return MapPolyline(data)
        }
    }

    private class MapPolylineValidator: MapValidator() {
        override fun validate(data: JSONObject) {
            super.validate(data)

            DictionaryValidator(fields = mapOf(
                Constants.POINTS to ArrayValidator(validator = DictionaryValidator(fields = mapOf(
                    Constants.LATITUDE to NumberValidator(),
                    Constants.LONGITUDE to NumberValidator()
                ))),
                Constants.STYLE to MapStyle.validator
            )).validate(data)
        }
    }
}

class MapPolygon(data: JSONObject): MapEntity(data) {
    val points: MutableList<LatLng> = JSONUtils.latLongListFromJson(data.getJSONArray(Constants.POINTS))

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapPolygon {
            MapPolygonValidator().validate(data)
            return MapPolygon(data)
        }
    }

    private class MapPolygonValidator: MapValidator() {
        override fun validate(data: JSONObject) {
            super.validate(data)

            DictionaryValidator(fields = mapOf(
                Constants.POINTS to ArrayValidator(validator = DictionaryValidator(fields = mapOf(
                    Constants.LATITUDE to NumberValidator(),
                    Constants.LONGITUDE to NumberValidator()
                ))),
                Constants.STYLE to MapStyle.validator
            )).validate(data)
        }
    }
}

class MapStyle(data: JSONObject) {
    val id: String = data.getString(Constants.ID)
    val icon: String = data.getString(Constants.ICON)
    val dashColor: String = data.getString(Constants.DASH_COLOR)
    val dashed: Boolean = data.getBoolean(Constants.DASHED)
    val fillColor: String = data.getString(Constants.FILL_COLOR)
    val strokeColor: String = data.getString(Constants.STROKE_COLOR)
    val strokeWidth: Double = data.getDouble(Constants.STROKE_WIDTH)
    val zIndex: Double = data.getDouble(Constants.Z_INDEX)

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapStyle {
            validator.validate(data)
            return MapStyle(data)
        }

        internal val validator = DictionaryValidator(fields = mapOf(
            Constants.ID to StringValidator(),
            Constants.ICON to StringValidator(),
            Constants.DASH_COLOR to StringValidator(),
            Constants.DASHED to BooleanValidator(),
            Constants.FILL_COLOR to StringValidator(),
            Constants.STROKE_COLOR to StringValidator(),
            Constants.STROKE_WIDTH to NumberValidator(),
            Constants.Z_INDEX to NumberValidator()
        ))
    }
}

private open class MapValidator: EntityValidator() {
    @Throws(VDException::class)
    override fun validate(data: JSONObject) {
        super.validate(data)

        DictionaryValidator(fields = mapOf(
            Constants.TYPE to StringValidator()
        )).validate(data)
    }
}
