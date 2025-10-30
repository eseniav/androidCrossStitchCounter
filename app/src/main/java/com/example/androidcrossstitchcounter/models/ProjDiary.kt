package com.example.androidcrossstitchcounter.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.androidcrossstitchcounter.services.CalendarUtils

@Entity(
    tableName = "projDiaries",
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["id"],
            childColumns = ["projId"]
        )
    ]
)
data class ProjDiary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String = CalendarUtils.getCurrentDateStringCompat(),
    var crossQuantity: Int = 0,
    val projId: Int
)

data class ProjDiaryEntry(val diary: ProjDiary, var done: Int, var remains: Int?)

@Dao
interface ProjDiaryDao {
    @Insert
    suspend fun insertProjDiary(projDiary: ProjDiary)
    @Query("SELECT * FROM projDiaries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Int): ProjDiary?
    @Query("SELECT * FROM projDiaries WHERE projId = :projId ORDER BY date")
    suspend fun getProjEntriesById(projId: Int): List<ProjDiary>
    @Query("UPDATE projDiaries " +
            "SET crossQuantity = crossQuantity + :amount " +
            "WHERE id = :diaryId")
    suspend fun increaseCrossQuantity(diaryId: Int, amount: Int)
    @Query("SELECT * FROM projDiaries WHERE date = :date")
    suspend fun getEntrуByDate(date: String): ProjDiary
    @Query("""
    UPDATE projDiaries 
    SET crossQuantity = crossQuantity + :amount 
    WHERE date = :date
            """)
    suspend fun increaseCrossQuantityByDate(
        date: String,
        amount: Int
    )

    @Update
    suspend fun updateProjDiary(projDiary: ProjDiary)
}
