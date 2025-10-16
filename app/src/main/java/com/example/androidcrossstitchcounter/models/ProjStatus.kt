package com.example.androidcrossstitchcounter.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "projStatuses")
data class ProjStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val statusName: String = ""
)

@Dao
interface ProjStatusDao {
    @Insert
    suspend fun insert(status: ProjStatus)

    @Insert
    suspend fun insertAll(statuses: List<ProjStatus>)

    @Query("SELECT * FROM projStatuses")
    suspend fun getAll(): List<ProjStatus>

    @Query("SELECT * FROM projStatuses WHERE statusName = :statusName LIMIT 1")
    suspend fun getByStatusName(statusName: String): ProjStatus?

    @Query("SELECT COUNT(*) FROM projStatuses")
    suspend fun getCount(): Int
}
