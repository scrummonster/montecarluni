package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
import javafx.scene.control.TableView
import org.junit.Assert.assertEquals

class DistributionSteps(world: World) : Scenario(world) {

    init {
        Then("^I should see the distribution$", 2000, {expectedDistributionAsOneLine : String ->
            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")

            val distributionControl = Stirry.findInRoot<TableView<Map<String, Int>>> {
                it.id == "distributionOutput"
            }.value
            val distributions = distributionControl.items.map { it["numberOfStories"].toString()}
                    .joinToString(separator = "\n")

            assertEquals(expectedDistribution, distributions)
        });
    }
}