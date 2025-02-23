package com.example.newsapp_api.helper

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateTimeUtils {
    companion object {
        fun formatDateTime(dateTimeString: String): String {
            // 1. Parse the input string into an Instant
            val instant = Instant.parse(dateTimeString)

            // 2. Convert the Instant to a LocalDateTime in the device's default timezone
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

            // 3. Define the desired output format
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM HH.mm", Locale("in", "ID"))

            // 4. Format the LocalDateTime using the formatter
            return localDateTime.format(formatter)
        }
    }
}