/**
 *  VejdirektoratetSDK.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.http.HTTP

fun requestTest(): String {
    return "test"
}

class VejdirektoratetSDK {

    companion object {
        fun request(first: String, second: String): String {
            return HTTP().getData(first, second)
        }
    }

}
