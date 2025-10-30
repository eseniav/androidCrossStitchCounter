package com.example.androidcrossstitchcounter.models

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.androidcrossstitchcounter.models.User
import com.example.androidcrossstitchcounter.models.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.androidcrossstitchcounter.models.ProjStatusDao

@Database(entities = [User::class, ProjStatus::class, Project::class], version = 8)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projDao(): ProjDao
    abstract fun statusDao(): ProjStatusDao
    abstract fun diaryDao(): ProjDiaryDao
    companion object {
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        public fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_database"
            ).addCallback(callback = object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        insertInitialStatuses(context)
                    }
                }
            }).fallbackToDestructiveMigration(false).build()
        }

        private suspend fun insertInitialStatuses(context: Context) {
            val db = getInstance(context)
            val statusDao = db.statusDao()

            if (statusDao.getCount() == 0) {
                val initialStatuses = listOf(
                    ProjStatus(statusName = "Будущий"),
                    ProjStatus(statusName = "Текущий"),
                    ProjStatus(statusName = "Завершенный"),
                    ProjStatus(statusName = "Архив")
                )
                statusDao.insertAll(initialStatuses)
                Log.d("AppDataBase", "Inserted ${initialStatuses.size} initial statuses")
            }
        }
    }
}

object DataBaseProvider {
    fun getDB(context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }
}
