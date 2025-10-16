package com.example.androidcrossstitchcounter.models

import android.content.Context
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

@Database(entities = [User::class, ProjStatus::class, Project::class], version = 2)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projDao(): ProjDao
    abstract fun statusDao(): ProjStatusDao
    companion object {
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
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
                    ProjStatus(statusName = "Завершенный")
                )
                statusDao.insertAll(initialStatuses)
            }
        }
    }
}

object DataBaseProvider {
    private var INSTANCE: AppDataBase? = null
    fun getDB(context: Context): AppDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "app_db")
                .fallbackToDestructiveMigration(false).build()
            INSTANCE = instance
            instance
        }
    }
}
