/**
 *  ListEntityValidatorTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-12-02.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.entity.EntityValidator
import dk.vejdirektoratet.vejdirektoratetsdk.entity.Roadwork
import dk.vejdirektoratet.vejdirektoratetsdk.entity.Traffic
import org.json.JSONObject
import kotlin.test.Test

class ListEntityValidatorTest {

    @Test
    fun `test valid Traffic entity`() {
        val entity = """{
                "heading": "",
                "description": "",
                "tag": "",
                "entityType": "latextraffic",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        Traffic.TrafficValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test valid Roadwork entity`() {
        val entity = """{
                "heading": "",
                "description": "",
                "tag": "",
                "entityType": "latexroadwork",
                "timestamp": "2019-09-12T09:20:26.000+0000",
                "bounds": {
                    "southWest": {
                        "lat": 55.707131,
                        "lng":12.589524
                    },
                    "northEast": {
                        "lat":55.70728,
                        "lng":12.59096
                    }
                }
            }"""

        Roadwork.RoadworkValidator().validate(JSONObject(entity))
    }

    @Test(expected = MissingRequiredFieldException::class)
    fun `test invalid Traffic entity`() {
        val entity = """{}"""

        Traffic.TrafficValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing fields`() {
        val validator = Traffic.TrafficValidator()

        val missingHeadingEntity = """{
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingDescriptionEntity = """{
                "heading": "headingText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingTagEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingEntityTypeEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingTimestampEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}"
            }"""

        assertThrowException<MissingRequiredFieldException>(missingHeadingEntity, validator)
        assertThrowException<MissingRequiredFieldException>(missingDescriptionEntity, validator)
        assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        assertThrowException<MissingRequiredFieldException>(missingTimestampEntity, validator)
    }

    @Test
    fun `test null fields`() {
        val validator = Traffic.TrafficValidator()

        val nullHeadingEntity = """{
                "heading": null,
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullDescriptionEntity = """{
                "heading": "headingText",
                "description": null,
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullTagEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": null,
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullEntityTypeEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": null,
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullTimestampEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": null
            }"""

        assertThrowException<MissingRequiredValueException>(nullHeadingEntity, validator)
        assertThrowException<MissingRequiredValueException>(nullDescriptionEntity, validator)
        assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        assertThrowException<MissingRequiredValueException>(nullTimestampEntity, validator)
    }

    @Test(expected = IncorrectValueException::class)
    fun `test invalid EntityType`() {
        val entity = """{
                        "heading": "headingText",
                        "description": "descriptionText",
                        "tag": "UniqueTag",
                        "entityType": "invalid entity type",
                        "timestamp": "2019-09-12T09:20:26.000+0000"
                        }"""

        Traffic.TrafficValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test empty bounds entity`() {
        val validator = Traffic.TrafficValidator()

        val noBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "latextraffic",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "latextraffic",
                "timestamp": "2019-09-12T09:20:26.000+0000",
                "bounds": null
            }"""

        validator.validate(JSONObject(noBoundsEntity))
        validator.validate(JSONObject(nullBoundsEntity))
    }

    @Test
    fun `test invalid timestamp entity`() {
        val entity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "latextraffic",
                "timestamp": "2019-09-12 09:20:26"
            }"""

        assertThrowException<IllegalDateFormatException>(entity, Traffic.TrafficValidator())
    }

    private inline fun <reified T> assertThrowException(data: String, validator: EntityValidator) {
        try {
            validator.validate(JSONObject(data))
            throw MissingExceptionException(T::class, data)
        } catch (e: Exception) {
            if (e is MissingExceptionException) {
                throw MissingExceptionException(T::class, data)
            } else if (e !is T) {
                throw IncorrectExceptionException(T::class, e::class, data)
            }
        }
    }
}
