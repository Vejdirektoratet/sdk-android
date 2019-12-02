/**
 *  Exceptions.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-15.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

internal open class VDException(message: String): Exception(message)
internal class EmptyURLException(message: String): VDException(message)
internal class UnknownEntityTypeException(message: String): VDException(message)
internal class UnknownViewTypeException(message: String): VDException(message)
internal class UnknownMapTypeException(message: String): VDException(message)
internal class InvalidEntityException(message: String): VDException(message)

internal class IncorrectValueException(expectedValues: Any, actualValue: Any?): VDException("expectedValues: $expectedValues, actualValue: $actualValue")
internal class IncorrectTypeException(expectedType: Any, actualType: Any?, data: Any?): VDException("expectedType: $expectedType, actualType: $actualType, data: $data")
internal class MissingRequiredValueException(data: Any?): VDException("data: $data")
internal class MissingRequiredFieldException(fieldName: String, data: Any?): VDException("fieldName: $fieldName, data: $data")
internal class IllegalDateFormatException(data: Any?): VDException("data: $data")

// For testing
internal class MissingExceptionException(expectedException: Any, data: Any?): VDException("\n\nExpected:   $expectedException\nFor data:   $data")
internal class IncorrectExceptionException(expectedException: Any, actualException: Any, data: Any?): VDException("\n\nExpected:   $expectedException\nActual:     $actualException\n\nFor data:   $data")
