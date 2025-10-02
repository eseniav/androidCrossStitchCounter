package com.example.androidcrossstitchcounter.services

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarUtils {
    companion object {
        fun formatDate(calendar: Calendar): String {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return formatter.format(calendar.time)
        }

        fun parseDate(dateString: String): Calendar? {
            val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            try {
                val date = formatter.parse(dateString)
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                return calendar
            } catch (e: Exception) {
                return null
            }
        }
        fun getCurrentDate(): Calendar {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar
        }
    }
}