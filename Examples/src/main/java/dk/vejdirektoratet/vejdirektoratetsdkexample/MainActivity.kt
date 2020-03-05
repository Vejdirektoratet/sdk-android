/**
 *  MainActivity.kt
 *  Examples
 *
 *  Created by Søren Krogh Sørensen on 2019-11-07.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdkexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import dk.vejdirektoratet.vejdirektoratetsdk.*
import dk.vejdirektoratet.vejdirektoratetsdk.entity.BaseEntity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.ListEntity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.MapEntity
import dk.vejdirektoratet.vejdirektoratetsdk.feed.Feed

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val apiKey: String = BuildConfig.VD_API_KEY // Add VD apiKey
    private var entity: BaseEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entity_button.isEnabled = buttonEnabled()

        val bounds = VDBounds(VDLatLng(56.004548, 9.739952), VDLatLng(56.377372, 10.388643))

        val listRequest = VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC, EntityType.ROADWORK), region = bounds, viewType = ViewType.LIST, apiKey = apiKey) { result: Feed.Result ->
            when (result) {
                is Feed.Result.Entities -> handleEntities(result, ViewType.LIST)
                is Feed.Result.Error -> handleError(result, ViewType.LIST)
            }
        }
        //listRequest.cancel()

        val mapRequest = VejdirektoratetSDK.request(entityTypes = listOf(EntityType.TRAFFIC_DENSITY), region = bounds, viewType = ViewType.MAP, apiKey = apiKey) { result: Feed.Result ->
            when (result) {
                is Feed.Result.Entities -> handleEntities(result, ViewType.MAP)
                is Feed.Result.Error -> handleError(result, ViewType.MAP)
            }
        }
        //mapRequest.cancel()

        val entityRequest = VejdirektoratetSDK.requestEntity("some_tag", ViewType.LIST, apiKey) { result: Feed.Result ->
            when (result) {
                is Feed.Result.Entity -> entity = result.entity
            }
            entity_button.isEnabled = buttonEnabled()
        }
        //entityRequest.cancel()

        entity_button.setOnClickListener {
            if (entity != null) {
                val intent = Intent(this, EntityActivity::class.java)
                intent.putExtra("entity", entity)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext,"No entities", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleEntities(result: Feed.Result.Entities, viewType: ViewType) {
        if (result.entities.isNotEmpty()) {
            this@MainActivity.runOnUiThread {
                val description = result.entities.joinToString(separator = "\n") { entity -> if (entity is ListEntity) entity.description else (entity as MapEntity).style.id}
                updateTexts(description, viewType)
            }
        }
    }

    private fun handleError(error: Feed.Result.Error, viewType: ViewType) {
        val header: String = when (error) {
            is Feed.Result.HttpError -> error.statusCode.toString()
            else -> "Error!"
        }

        this@MainActivity.runOnUiThread {
            updateTexts(error.exception.localizedMessage, viewType)
        }
    }

    private fun updateTexts(content: String, viewType: ViewType) {
        if (viewType == ViewType.LIST) {
            list_text.text = content
        } else {
            map_text.text = content
        }
    }

    private fun buttonEnabled(): Boolean = entity != null
}
