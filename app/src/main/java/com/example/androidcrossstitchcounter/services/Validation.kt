package com.example.androidcrossstitchcounter.services

import android.app.DatePickerDialog
import java.util.Calendar

class Validation {
    companion object {
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$"
        val phonePattern = Regex("""^(\+7|7|8)?[\s\-]?\(?[0-9]{3}\)?[\s\-]?[0-9]{3}[\s\-]?[0-9]{2}[\s\-]?[0-9]{2}$""")
        val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"

        fun checkLength(value: Int, min: Int, max: Int) = value >= min && value <= max
        fun checkPassword(password: String) = password.matches(passwordPattern.toRegex())
        fun checkMatch(s1: String, s2: String) = s1 == s2
        fun checkWhiteSpace(str: String) = !str.any { it.isWhitespace() }

        fun checkDateRange(input: Calendar, min: Calendar, max: Calendar): Boolean {
            return input.timeInMillis >= min.timeInMillis && input.timeInMillis <= max.timeInMillis
        }
        fun checkPhone(input: String) = phonePattern.matches(input)
        fun checkEmail(input: String) = input.matches(emailPattern.toRegex())
        fun checkBirthDate(datePickerdialog: DatePickerDialog) {
            CalendarUtils.setCalendarBorders(datePickerdialog, 120, 5)
        }
        fun checkStartFinishDate(datePickerdialog: DatePickerDialog) {
            CalendarUtils.setCalendarBorders(datePickerdialog, 100, 0)
        }
        fun checkPlanDate(datePickerdialog: DatePickerDialog) {
            CalendarUtils.setCalendarBorders(datePickerdialog, -1, 100)
        }
    }
}