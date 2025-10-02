import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.androidcrossstitchcounter.services.CalendarUtils
import com.example.androidcrossstitchcounter.services.Validation
import java.util.Calendar

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var login: String = "",
    var password: String = "",
    var userName: String? = null,
    var userLastName: String? = null,
    var phoneNumber: String = "",
    var email: String = "",
    //var birthDate: Calendar? = null,
    //val regDate: Calendar = CalendarUtils.getCurrentDate()
)
{
    fun print() {
        println(this)}
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

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)
    @Query("SELECT * FROM users WHERE login = :login LIMIT 1" )
    suspend fun getUserByLogin(login: String): User?
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1" )
    suspend fun getUserById(id: Int): User?
    @Update
    suspend fun updateUser(user: User)
}

@Database(entities = [User::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
}

object DataBaseProvider {
    private var INSTANCE: AppDataBase? = null
    fun getDB(context: Context): AppDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "app_db").build()
            INSTANCE = instance
            instance
        }
    }
}
