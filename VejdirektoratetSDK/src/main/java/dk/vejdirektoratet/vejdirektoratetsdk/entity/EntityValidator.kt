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
import org.json.JSONException
import org.json.JSONObject

internal inline fun <reified T> checkType(data: Any?) {
    if (data !is T) {
        if (data == null) {
            throw IncorrectTypeException(String::class, null, data)
        }
        throw IncorrectTypeException(String::class, data::class, data)
    }
}

internal open class Validator {
    @Throws(VDException::class)
    open fun validate(data: Any?) {}
}

internal open class ValueValidator(private val optional: Boolean = false, private val validValues: List<Any>? = null): Validator() {

    override fun validate(data: Any?) {
        if (data == null) {
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

internal class StringValidator(validValues: List<Any>? = null): ValueValidator(validValues = validValues) {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<String>(data)
    }
}

internal class NumberValidator: ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<Number>(data)
    }
}

internal class DoubleValidator: ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<Double>(data)
    }
}

internal class BooleanValidator: ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<Boolean>(data)
    }
}

internal class TimestampValidator: ValueValidator() {
    override fun validate(data: Any?) {
        super.validate(data)
        checkType<String>(data)

        if (data != null) {
            Utils.dateFromIso8601String(data as String)
        }
    }
}

internal class DictionaryValidator(optional: Boolean = false, private val fields: Map<String, Validator>): ValueValidator(optional) {
    override fun validate(data: Any?) {
        super.validate(data)

        if (data != null) {
            val jsonData = data as JSONObject
            for ((fieldName, validator) in fields) {
                try {
                    validator.validate(jsonData.get(fieldName))
                } catch (e: JSONException) {
                    throw MissingRequiredFieldException(fieldName, data)
                }
            }
        }
    }
}