import com.example.androidcrossstitchcounter.CalendarUtils
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.UUID

interface Identifiable {
    fun getID() = UUID.randomUUID().toString()
}

data class User(
    var login: String = "",
    var password: String = "",
    var userName: String? = null,
    var userLastName: String? = null,
    var phoneNumber: String = "",
    var email: String = "",
    var birthDate: Calendar? = null,
    val regDate: Calendar = CalendarUtils.getCurrentDate()
): Identifiable {

    val userID: String = getID()

    fun print() {
        println(this)
    }

    companion object {
        val minName = 2
        val maxName = 30
        val minLogin = 6
        val maxLogin = 15
        val minAge = 10L
        val maxAge = 100L

        fun checkName(input: String) = input.isNotBlank() && Validation.checkLength(input.length, minName, maxName)
        fun checkLogin(input: String) = Validation.checkWhiteSpace(input) && Validation.checkLength(input.length, minLogin, maxLogin)
        fun checkDate(input: Calendar): Boolean {
            val today = CalendarUtils.getCurrentDate()
            val minDate = CalendarUtils.getCurrentDate().apply { add(Calendar.YEAR, -maxAge.toInt()) }
            val maxDate = CalendarUtils.getCurrentDate().apply { add(Calendar.YEAR, -minAge.toInt()) }
            return input.timeInMillis >= minDate.timeInMillis && input.timeInMillis <= maxDate.timeInMillis
        }
    }
}
