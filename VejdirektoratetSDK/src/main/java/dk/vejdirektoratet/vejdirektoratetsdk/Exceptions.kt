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