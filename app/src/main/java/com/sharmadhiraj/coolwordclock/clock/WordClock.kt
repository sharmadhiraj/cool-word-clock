package com.sharmadhiraj.coolwordclock.clock

import java.time.LocalTime

enum class Word(val text: String) {
    IT("IT"), IS("IS"), HALF("HALF"), TEN_MINUTES("TEN"),
    QUARTER("QUARTER"), TWENTY("TWENTY"),
    FIVE_MINUTES("FIVE"), MINUTES("MINUTES"), TO("TO"),
    PAST("PAST"), ONE("ONE"), THREE("THREE"),
    TWO("TWO"), FOUR("FOUR"), FIVE("FIVE"),
    SIX("SIX"), SEVEN("SEVEN"), EIGHT("EIGHT"),
    NINE("NINE"), TEN("TEN"), ELEVEN("ELEVEN"),
    TWELVE("TWELVE"), O_CLOCK("O'CLOCK"),
}

object WordClock {

    val rows: List<List<Word>> = listOf(
        listOf(Word.IT, Word.IS, Word.HALF, Word.TEN_MINUTES),
        listOf(Word.QUARTER, Word.TWENTY),
        listOf(Word.FIVE_MINUTES, Word.MINUTES, Word.TO),
        listOf(Word.PAST, Word.ONE, Word.THREE),
        listOf(Word.TWO, Word.FOUR, Word.FIVE),
        listOf(Word.SIX, Word.SEVEN, Word.EIGHT),
        listOf(Word.NINE, Word.TEN, Word.ELEVEN),
        listOf(Word.TWELVE, Word.O_CLOCK),
    )

    private val hourWords = listOf(
        Word.ONE, Word.TWO, Word.THREE, Word.FOUR, Word.FIVE, Word.SIX,
        Word.SEVEN, Word.EIGHT, Word.NINE, Word.TEN, Word.ELEVEN, Word.TWELVE,
    )

    private val minutePhrases: List<List<Word>> = listOf(
        listOf(Word.O_CLOCK),
        listOf(Word.FIVE_MINUTES, Word.MINUTES, Word.PAST),
        listOf(Word.TEN_MINUTES, Word.MINUTES, Word.PAST),
        listOf(Word.QUARTER, Word.PAST),
        listOf(Word.TWENTY, Word.MINUTES, Word.PAST),
        listOf(Word.TWENTY, Word.FIVE_MINUTES, Word.MINUTES, Word.PAST),
        listOf(Word.HALF, Word.PAST),
        listOf(Word.TWENTY, Word.FIVE_MINUTES, Word.MINUTES, Word.TO),
        listOf(Word.TWENTY, Word.MINUTES, Word.TO),
        listOf(Word.QUARTER, Word.TO),
        listOf(Word.TEN_MINUTES, Word.MINUTES, Word.TO),
        listOf(Word.FIVE_MINUTES, Word.MINUTES, Word.TO),
    )

    private const val FIRST_STEP_BEFORE_NEXT_HOUR = 7

    fun litWords(time: LocalTime): Set<Word> {
        val step = time.minute / 5
        val hourOffset = if (step >= FIRST_STEP_BEFORE_NEXT_HOUR) 1 else 0
        val hour12 = (time.hour + hourOffset + 11) % 12
        return setOf(Word.IT, Word.IS, hourWords[hour12]) + minutePhrases[step]
    }

    fun sentence(time: LocalTime): String {
        val lit = litWords(time)
        return rows.flatten().filter { it in lit }.joinToString(" ") { it.text }
    }

    fun extraMinutes(time: LocalTime): Int = time.minute % 5
}
