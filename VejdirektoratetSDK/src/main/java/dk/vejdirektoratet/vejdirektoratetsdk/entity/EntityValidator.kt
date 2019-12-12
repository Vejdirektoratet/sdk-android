/**
 *  EntityValidator.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-18.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk.entity

import dk.vejdirektoratet.vejdirektoratetsdk.IncorrectTypeException
import dk.vejdirektoratet.vejdirektoratetsdk.IncorrectValueException
import dk.vejdirektoratet.vejdirektoratetsdk.MissingRequiredFieldException
import dk.vejdirektoratet.vejdirektoratetsdk.MissingRequiredValueException
import dk.vejdirektoratet.vejdirektoratetsdk.VDException
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

internal inline fun <reified T> checkType(data: Any?) {
    if (data !is T) {
        if (data == null) {
            throw IncorrectTypeException(T::class, null, data)
        }
        throw IncorrectTypeException(T::class, data::class, data)
    }
}

internal open class Validator {
    @Throws(VDException::class)
    open fun validate(data: Any?) {}
}

internal open class ValueValidator(private val optional: Boolean = false, private val validValues: List<Any>? = null) : Validator() {

    override fun validate(data: Any?) {
        if (data == null || data.equals(null)) {
            if (optional) {
                return
            }
            throw MissingRequiredValueException(data)
        }

        if (validValues != null) {
            var found = false
            for (value in validValues) {
                if (value == data) {
                    found = true
                }
            }
            if (!found) {
                throw IncorrectValueException(validValues, data)
            }
        }
    }
}

internal class StringValidator(optional: Boolean = false, validValues: List<Any>? = null) : ValueValidator(optional, validValues) {
    override fun validate(data: Any?) {
        super.validate(data)
        if (data != null && !data.equals(null)) {
            checkType<String>(data)
        }
    }
}

internal class NumberValidator : ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<Number>(data)
    }
}

internal class BooleanValidator : ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<Boolean>(data)
    }
}

internal class TimestampValidator : ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<String>(data)

        if (data != null && !data.equals(null)) {
            Utils.dateFromIso8601String(data as String)
        }
    }
}

internal class DictionaryValidator(private val optional: Boolean = false, private val fields: Map<String, Validator>) : ValueValidator(optional) {
    override fun validate(data: Any?) {
        super.validate(data)

        if (data != null && !data.equals(null)) {
            val jsonData = data as JSONObject
            for ((fieldName, validator) in fields) {
                try {
                    var fieldNameData: Any? = null
                    if (jsonData.has(fieldName)) {
                        fieldNameData = jsonData.get(fieldName)
                    } else if (validator !is DictionaryValidator || !validator.optional) {
                        throw MissingRequiredFieldException(fieldName, data)
                    }
                    validator.validate(fieldNameData)
                } catch (e: JSONException) {
                    throw MissingRequiredFieldException(fieldName, data)
                }
            }
        }
    }
}

internal class ArrayValidator(optional: Boolean = false, private val ignoreInvalidEntries: Boolean = false, private val validator: Validator) : ValueValidator(optional) {
    override fun validate(data: Any?) {
        super.validate(data)

        if (ignoreInvalidEntries) {
            return
        }

        if (data != null) {
            val jsonData = data as JSONArray
            for (i in 0 until jsonData.length()) {
                validator.validate(jsonData.getJSONObject(i))
            }
        }
    }
}
