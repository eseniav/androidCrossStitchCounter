package com.example.androidcrossstitchcounter.models

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.androidcrossstitchcounter.services.CalendarUtils
import java.util.Calendar

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = ProjStatus::class,
            parentColumns = ["id"],
            childColumns = ["projStatusId"]
        )
    ]
)
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var projName: String = "",
    var projDesigner: String? = null,
    var totalCross: Int? = null,
    var finishDreamDate: String? = null,
    var stitchedCrossBeforeRegistration: Int = 0,
    var startDate: String? = null,
    var finishDate: String? = null,
    val registrationDate: String = CalendarUtils.getCurrentDateStringCompat(),
    var width: Int = 0,
    var height: Int = 0,
    val userId: Int,
    var projStatusId: Int
)

@Dao
interface ProjDao {
    @Insert
    suspend fun insertProject(project: Project)
    @Query("SELECT * FROM projects WHERE userId = :userId" )
    suspend fun getProjectByUserId(userId: Int): List<Project>
    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1" )
    suspend fun getProjectById(id: Int): Project?
    @Query("SELECT * FROM projects WHERE userId = :userId AND projStatusId = :projStatusId" )
    suspend fun getProjectByUserIdAndStatus(userId: Int, projStatusId: Int): Project?
    @Update
    suspend fun updateProject(project: Project)
}
