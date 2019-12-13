/**
 *  MapEntity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-08.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import android.os.Parcelable
import dk.vejdirektoratet.vejdirektoratetsdk.Constants
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.VDLatLng
import dk.vejdirektoratet.vejdirektoratetsdk.utils.JSONUtils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils.baseEntityTypeFromString
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
open class MapEntity(private val _entityType: BaseEntityType, private val _tag: String, val type: MapType, val style: MapStyle) : BaseEntity(_entityType, _tag) {
    enum class MapType(val value: String) {
        MARKER("MARKER"),
        POLYLINE("POLYLINE"),
        POLYGON("POLYGON"),
        UNKNOWN("UNKNOWN")
    }
}

@Parcelize
class MapMarker(private val _entityType: BaseEntityType, private val _tag: String, private val _type: MapType, private val _style: MapStyle, val center: VDLatLng) : MapEntity(_entityType, _tag, _type, _style) {

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapEntity {
            MapMarkerValidator().validate(data)
            val entityType: BaseEntityType = baseEntityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
            val tag: String = data.getString(Constants.TAG)
            val type: MapType = Utils.mapTypeFromString(data.getString(Constants.TYPE))
            val style: MapStyle = MapStyle.fromEntity(data.getJSONObject(Constants.STYLE))
            val center: VDLatLng = JSONUtils.latLngFromJson(data.getJSONObject(Constants.CENTER))
            return MapMarker(entityType, tag, type, style, center)
        }
    }

    internal class MapMarkerValidator : MapValidator() {
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

@Parcelize
class MapPolyline(private val _entityType: BaseEntityType, private val _tag: String, private val _type: MapType, private val _style: MapStyle, val points: MutableList<VDLatLng>) : MapEntity(_entityType, _tag, _type, _style) {

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapPolyline {
            MapPolylineValidator().validate(data)
            val entityType: BaseEntityType = baseEntityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
            val tag: String = data.getString(Constants.TAG)
            val type: MapType = Utils.mapTypeFromString(data.getString(Constants.TYPE))
            val style: MapStyle = MapStyle.fromEntity(data.getJSONObject(Constants.STYLE))
            val points: MutableList<VDLatLng> = JSONUtils.latLongListFromJson(data.getJSONArray(Constants.POINTS))
            return MapPolyline(entityType, tag, type, style, points)
        }
    }

    internal class MapPolylineValidator : MapValidator() {
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

@Parcelize
class MapPolygon(private val _entityType: BaseEntityType, private val _tag: String, private val _type: MapType, private val _style: MapStyle, val points: MutableList<VDLatLng>) : MapEntity(_entityType, _tag, _type, _style) {

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapPolygon {
            MapPolygonValidator().validate(data)
            val entityType: BaseEntityType = baseEntityTypeFromString(data.getString(Constants.ENTITY_TYPE))!!
            val tag: String = data.getString(Constants.TAG)
            val type: MapType = Utils.mapTypeFromString(data.getString(Constants.TYPE))
            val style: MapStyle = MapStyle.fromEntity(data.getJSONObject(Constants.STYLE))
            val points: MutableList<VDLatLng> = JSONUtils.latLongListFromJson(data.getJSONArray(Constants.POINTS))
            return MapPolygon(entityType, tag, type, style, points)
        }
    }

    internal class MapPolygonValidator : MapValidator() {
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

@Parcelize
class MapStyle(val id: String, val icon: String?, val dashColor: String, val dashed: Boolean, val fillColor: String, val strokeColor: String, val strokeWidth: Double, val zIndex: Double) : Parcelable {

    companion object {
        @Throws(VDException::class)
        fun fromEntity(data: JSONObject): MapStyle {
            validator.validate(data)
            val id: String = data.getString(Constants.ID)
            val icon: String? = data.getString(Constants.ICON)
            val dashColor: String = data.getString(Constants.DASH_COLOR)
            val dashed: Boolean = data.getBoolean(Constants.DASHED)
            val fillColor: String = data.getString(Constants.FILL_COLOR)
            val strokeColor: String = data.getString(Constants.STROKE_COLOR)
            val strokeWidth: Double = data.getDouble(Constants.STROKE_WIDTH)
            val zIndex: Double = data.getDouble(Constants.Z_INDEX)
            return MapStyle(id, icon, dashColor, dashed, fillColor, strokeColor, strokeWidth, zIndex)
        }

        internal val validator = DictionaryValidator(fields = mapOf(
            Constants.ID to StringValidator(),
            Constants.ICON to StringValidator(true),
            Constants.DASH_COLOR to StringValidator(),
            Constants.DASHED to BooleanValidator(),
            Constants.FILL_COLOR to StringValidator(),
            Constants.STROKE_COLOR to StringValidator(),
            Constants.STROKE_WIDTH to NumberValidator(),
            Constants.Z_INDEX to NumberValidator()
        ))
    }
}

internal open class MapValidator : EntityValidator() {
    @Throws(VDException::class)
    override fun validate(data: JSONObject) {
        super.validate(data)

        DictionaryValidator(fields = mapOf(
            Constants.TYPE to StringValidator()
        )).validate(data)
    }
}
