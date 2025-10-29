package com.example.androidcrossstitchcounter.services

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

        fun getCurrentDateStringCompat(): String {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val currentDate = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                currentDate.format(formatter)
            } else {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                dateFormat.format(calendar.time)
            }
        }

        fun setDisplayCalendar(context: Context, date: EditText): DatePickerDialog {
            val calendar = android.icu.util.Calendar.getInstance()
            val year = calendar.get(android.icu.util.Calendar.YEAR)
            val month = calendar.get(android.icu.util.Calendar.MONTH)
            val day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH)

            val datePickerdialog =
                DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
                    val formatedDate = String.format(
                        "%02d.%02d.%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    date.setText(formatedDate)
                }, year, month, day)
            return datePickerdialog
        }
    }
}
