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
import dk.vejdirektoratet.vejdirektoratetsdk.entity.ListEntity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val apiKey: String = BuildConfig.VD_API_KEY // Add VD apiKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bounds = Bounds(LatLng(56.1417197,10.1479164), LatLng(56.1954316,10.2435318))

        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = bounds, viewType = ViewType.LIST, apiKey = apiKey) { result: Feed.Result ->
            when (result) {
                is Feed.Result.Success -> handleSuccess(result, false)
                is Feed.Result.Error -> handleFailure(result, false)
            }
        }

        VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = bounds, viewType = ViewType.MAP, apiKey = apiKey) { result: Feed.Result ->
            when (result) {
                is Feed.Result.Success -> handleSuccess(result, true)
                is Feed.Result.Error -> handleFailure(result, true)
            }
        }
    }

    private fun handleSuccess(result: Feed.Result.Success, map: Boolean) {
        if (result.entities.isNotEmpty()) {
            this@MainActivity.runOnUiThread {
                val description = result.entities.joinToString(separator = "\n") { entity -> if (entity is ListEntity) entity.description else (entity as MapEntity).type.value}
                updateTexts(description, map)
            }
        }
    }

    private fun handleFailure(error: Feed.Result.Error, map: Boolean) {
        val header: String = when (error) {
            is Feed.Result.HttpError -> error.statusCode.toString()
            else -> "Error!"
        }

        this@MainActivity.runOnUiThread {
            updateTexts(error.exception.localizedMessage, map)
        }
    }

    private fun updateTexts(content: String, map: Boolean) {
        if (map) {
            map_text.text = content
        } else {
            list_text.text = content
        }
    }
}
