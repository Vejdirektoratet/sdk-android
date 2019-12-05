/**
 *  ListEntityValidatorTest.kt
 *  VejdirektoratetSDK
 *
 *  Created by Søren Krogh Sørensen on 2019-12-02.
 *  Copyright © 2019 Vejdirektoratet. All rights reserved.
 */

package dk.vejdirektoratet.vejdirektoratetsdk

import dk.vejdirektoratet.vejdirektoratetsdk.entity.Roadwork
import dk.vejdirektoratet.vejdirektoratetsdk.entity.Traffic
import dk.vejdirektoratet.vejdirektoratetsdk.utils.Utils
import org.json.JSONObject
import kotlin.test.Test

class ListEntityValidatorTest {

    @Test
    fun `test valid Traffic entity`() {
        val entity = """{
                "heading": "",
                "description": "",
                "tag": "",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        Traffic.TrafficValidator().validate(JSONObject(entity))
    }

    @Test(expected = MissingRequiredFieldException::class)
    fun `test invalid Traffic entity`() {
        val entity = "{}"

        Traffic.TrafficValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing Traffic fields`() {
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

        Utils.assertThrowException<MissingRequiredFieldException>(missingHeadingEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingDescriptionEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTimestampEntity, validator)
    }

    @Test
    fun `test null Traffic fields`() {
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

        Utils.assertThrowException<MissingRequiredValueException>(nullHeadingEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullDescriptionEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTimestampEntity, validator)
    }

    @Test(expected = IncorrectValueException::class)
    fun `test invalid Traffic EntityType`() {
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
    fun `test Traffic empty bounds entity`() {
        val validator = Traffic.TrafficValidator()

        val noBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12T09:20:26.000+0000",
                "bounds": null
            }"""

        validator.validate(JSONObject(noBoundsEntity))
        validator.validate(JSONObject(nullBoundsEntity))
    }

    @Test
    fun `test Traffic invalid timestamp entity`() {
        val entity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_TRAFFIC}",
                "timestamp": "2019-09-12 09:20:26"
            }"""

        Utils.assertThrowException<IllegalDateFormatException>(entity, Traffic.TrafficValidator())
    }

    @Test
    fun `test valid Roadwork entity`() {
        val entity = """{
                "heading": "",
                "description": "",
                "tag": "",
                "entityType": "${Constants.LATEX_ROADWORK}",
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
    fun `test invalid Roadwork entity`() {
        val entity = "{}"

        Roadwork.RoadworkValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test missing Roadwork fields`() {
        val validator = Roadwork.RoadworkValidator()

        val missingHeadingEntity = """{
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingDescriptionEntity = """{
                "heading": "headingText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val missingTagEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "entityType": "${Constants.LATEX_ROADWORK}",
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
                "entityType": "${Constants.LATEX_ROADWORK}"
            }"""

        Utils.assertThrowException<MissingRequiredFieldException>(missingHeadingEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingDescriptionEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTagEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredFieldException>(missingTimestampEntity, validator)
    }

    @Test
    fun `test null Roadwork fields`() {
        val validator = Roadwork.RoadworkValidator()

        val nullHeadingEntity = """{
                "heading": null,
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullDescriptionEntity = """{
                "heading": "headingText",
                "description": null,
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullTagEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": null,
                "entityType": "${Constants.LATEX_ROADWORK}",
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
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": null
            }"""

        Utils.assertThrowException<MissingRequiredValueException>(nullHeadingEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullDescriptionEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTagEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullEntityTypeEntity, validator)
        Utils.assertThrowException<MissingRequiredValueException>(nullTimestampEntity, validator)
    }

    @Test(expected = IncorrectValueException::class)
    fun `test invalid Roadwork EntityType`() {
        val entity = """{
                        "heading": "headingText",
                        "description": "descriptionText",
                        "tag": "UniqueTag",
                        "entityType": "invalid entity type",
                        "timestamp": "2019-09-12T09:20:26.000+0000"
                        }"""

        Roadwork.RoadworkValidator().validate(JSONObject(entity))
    }

    @Test
    fun `test Roadwork empty bounds entity`() {
        val validator = Roadwork.RoadworkValidator()

        val noBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000"
            }"""

        val nullBoundsEntity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12T09:20:26.000+0000",
                "bounds": null
            }"""

        validator.validate(JSONObject(noBoundsEntity))
        validator.validate(JSONObject(nullBoundsEntity))
    }

    @Test
    fun `test Roadwork invalid timestamp entity`() {
        val entity = """{
                "heading": "headingText",
                "description": "descriptionText",
                "tag": "UniqueTag",
                "entityType": "${Constants.LATEX_ROADWORK}",
                "timestamp": "2019-09-12 09:20:26"
            }"""

        Utils.assertThrowException<IllegalDateFormatException>(entity, Roadwork.RoadworkValidator())
    }
}
