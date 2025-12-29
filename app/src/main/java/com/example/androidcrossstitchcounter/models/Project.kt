package com.example.androidcrossstitchcounter.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import com.example.androidcrossstitchcounter.services.CalendarUtils
import java.time.LocalDate
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
    var finishDate: LocalDate? = null,
    val registrationDate: String = CalendarUtils.getCurrentDateStringCompat(),
    var width: Int = 0,
    var height: Int = 0,
    val userId: Int,
    var projStatusId: Int,
    var isArchived: Boolean = false
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
    @Query("""
    SELECT SUM(pd.crossQuantity) AS totalCrossStitched
    FROM projDiaries pd
    JOIN projects p ON pd.projId = p.id
    WHERE p.userId = :userId""")
    suspend fun getTotalCrossStitchedByUserId(userId: Int): Int?
    @Update
    suspend fun updateProject(project: Project)
    @Delete
    suspend fun deleteProject(project: Project)
    @Query ("UPDATE projects SET isArchived = 1 WHERE id = :id")
    suspend fun archiveProject(id: Int)
    @Query ("UPDATE projects SET isArchived = 0 WHERE id = :id")
    suspend fun restoreProject(id: Int)
}
