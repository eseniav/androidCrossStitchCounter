package com.example.androidcrossstitchcounter.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidcrossstitchcounter.models.User
import com.example.androidcrossstitchcounter.models.UserDao

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
