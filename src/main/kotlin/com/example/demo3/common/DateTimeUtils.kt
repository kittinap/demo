package com.example.demo3.common

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern

object DateTimeUtils {

    const val PATTERN_DATE_TIME_SECOND_DASH = "yyyy-MM-dd'T'HH:mm:ssz"


    fun convertToLocalDateTime(date: String, formatter: String): LocalDateTime {
        return LocalDateTime.parse(date, ofPattern(formatter))
    }

}