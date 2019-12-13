/**
 *  EntityActivity.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-12-13.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdkexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.vejdirektoratet.vejdirektoratetsdk.entity.ListEntity
import kotlinx.android.synthetic.main.activity_entity.*
import java.text.SimpleDateFormat
import java.util.Date

class EntityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entity)

        val entity: ListEntity = intent.getParcelableExtra("entity")

        entity_type.text = entity.entityType.name
        entity_tag.text = entity.tag
        entity_time.text = getTimeString(entity.timestamp)
        entity_heading.text = entity.heading
        entity_description.text = entity.description
    }

    private fun getTimeString(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        return formatter.format(date)
    }
}
