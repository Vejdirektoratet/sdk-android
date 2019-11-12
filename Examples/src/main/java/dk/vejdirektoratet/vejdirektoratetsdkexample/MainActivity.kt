/**
 *  MainActivity.kt
 *  Examples
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdkexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.vejdirektoratet.vejdirektoratetsdk.*

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private val apiKey: String = "" // Add VD apiKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bounds = Bounds(LatLng(56.1417197,10.1479164), LatLng(56.1954316,10.2435318))

        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = bounds, viewType = ViewType.LIST, apiKey = apiKey) { result: JSONArray ->
            this@MainActivity.runOnUiThread {
                center_text.text = result.getJSONObject(0).getString("heading")
                below_text.text = result.getJSONObject(0).getString("description")
            }
        }

        /*VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = bounds, viewType = ViewType.LIST, apiKey = "") { result: String ->
            center_text.text = result
        }*/
    }
}
