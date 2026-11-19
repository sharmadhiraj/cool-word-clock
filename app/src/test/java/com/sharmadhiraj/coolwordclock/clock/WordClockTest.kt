package com.sharmadhiraj.coolwordclock.clock

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalTime

class WordClockTest {

    private fun sentence(hour: Int, minute: Int) = WordClock.sentence(LocalTime.of(hour, minute))

    @Test
    fun everyFiveMinuteStep() {
        val expected = listOf(
            "IT IS THREE O'CLOCK",
            "IT IS FIVE MINUTES PAST THREE",
            "IT IS TEN MINUTES PAST THREE",
            "IT IS QUARTER PAST THREE",
            "IT IS TWENTY MINUTES PAST THREE",
            "IT IS TWENTY FIVE MINUTES PAST THREE",
            "IT IS HALF PAST THREE",
            "IT IS TWENTY FIVE MINUTES TO FOUR",
            "IT IS TWENTY MINUTES TO FOUR",
            "IT IS QUARTER TO FOUR",
            "IT IS TEN MINUTES TO FOUR",
            "IT IS FIVE MINUTES TO FOUR",
        )
        expected.forEachIndexed { step, text ->
            assertEquals(text, sentence(15, step * 5))
            assertEquals(text, sentence(15, step * 5 + 4))
        }
    }

    @Test
    fun midnightAndNoonAreTwelve() {
        assertEquals("IT IS TWELVE O'CLOCK", sentence(0, 0))
        assertEquals("IT IS TWELVE O'CLOCK", sentence(12, 2))
    }

    @Test
    fun hourWrapsBeforeMidnightAndNoon() {
        assertEquals("IT IS QUARTER TO TWELVE", sentence(23, 45))
        assertEquals("IT IS FIVE MINUTES TO ONE", sentence(12, 55))
        assertEquals("IT IS HALF PAST TWELVE", sentence(0, 30))
    }

    @Test
    fun extraMinutesCountsWithinStep() {
        assertEquals(0, WordClock.extraMinutes(LocalTime.of(9, 40)))
        assertEquals(3, WordClock.extraMinutes(LocalTime.of(9, 43)))
    }
}
