package com.lunivore.montecarluni.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WeeklyDistribution(val storiesClosed : List<StoriesClosedInWeek>) {
    companion object {
        val EMPTY = WeeklyDistribution(listOf())
    }

}

data class StoriesClosedInWeek(val range : DateRange, val count : Int) {
    fun  formatRange(formatter: DateTimeFormatter): Pair<String, String> {
        return range.format(formatter)
    }
}

data class DateRange(val start : LocalDate, val end : LocalDate) {
    fun  format(formatter: DateTimeFormatter): Pair<String, String> { return Pair(formatter.format(start), formatter.format(end))}
}
