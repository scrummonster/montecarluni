package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.app.DataPointPresenter
import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.fireAndStir
import com.lunivore.stirry.pickDateAndStir
import com.lunivore.stirry.setTextAndStir
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import org.junit.Assert
import org.junit.Assert.assertEquals
import java.time.LocalDate

class ForecastSteps(val world: World) : Scenario(world) {

    init {
        Given("^we chose a start date of (\\d+-\\d+-\\d+)$", {date : String ->
            findInRoot<DatePicker>({it.id == "forecastStartDateInput"})?.value.pickDateAndStir(date)
        })

        When("^I ask for a forecast for the next (\\d+) work items$", { number: Int ->
            requestForecast(number)
        })

        When("^I ask for a forecast for (\\d+) work items starting on (.*)$", {number: Int, date : String ->
            findInRoot<DatePicker>({it.id == "forecastStartDateInput"})?.value.pickDateAndStir(date)
            requestForecast(number)
        })

        Given("^I asked for a forecast for (\\d+) work items using rows (\\d+) onwards", {numberOfWorkItems: Int, fromRow : Int ->
            SelectionSteps(world).select(fromRow)
            requestForecast(numberOfWorkItems)
        })

        When("^I ask for a forecast for (\\d+) work items using rows (\\d+) onwards$", {numberOfWorkItems: Int, fromRow : Int ->
            SelectionSteps(world).select(fromRow)
            requestForecast(numberOfWorkItems)
        })

        Then("^I should see a percentage forecast$", { expectedForecast: String ->
            val forecast = findInRoot<TableView<DataPointPresenter>>({it.id == "forecastOutput"}).value

            val forecastAsString = forecast.items.map { "${it.probabilityAsPercentageString} | ${it.dateAsString}" }
                    .joinToString(separator = "\n")

            assertEquals(expectedForecast, forecastAsString)
        })
    }

    private fun toDate(date: String): LocalDate? {
        val match = Regex("(\\d+)-(\\d+)-(\\d+)").matchEntire(date)
        if (match == null) {
            Assert.fail("Failed to match provided date to pattern yyyy-MM-dd")
            return null
        } else {
            return LocalDate.of(match.groupValues[1].toInt(), match.groupValues[2].toInt(), match.groupValues[3].toInt())
        }
    }

    private fun requestForecast(number: Int) {
        findInRoot<TextField>({ it.id == "numWorkItemsForecastInput" }).value.setTextAndStir(number.toString())
        findInRoot<Button>({ it.id == "forecastButton" }).value.fireAndStir()
    }
}