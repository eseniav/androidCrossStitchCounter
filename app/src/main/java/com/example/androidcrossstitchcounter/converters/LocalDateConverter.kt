package com.example.androidcrossstitchcounter.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        // Форматируем дату в строку без использования DateTimeFormatter.ISO_LOCAL_DATE
        return date?.let {
            "${it.year.toString().padStart(4, '0')}-" +
                    "${it.monthValue.toString().padStart(2, '0')}-" +
                    "${it.dayOfMonth.toString().padStart(2, '0')}"
        }
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        // Разбираем строку формата "yyyy-MM-dd" вручную и создаём LocalDate
        return dateString?.let {
            val parts = it.split("-")
            if (parts.size == 3) {
                val year = parts[0].toIntOrNull()
                val month = parts[1].toIntOrNull()
                val day = parts[2].toIntOrNull()
                if (year != null && month != null && day != null) {
                    LocalDate.of(year, month, day)
                } else null
            } else null
        }
    }
}
