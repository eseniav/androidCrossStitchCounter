package com.example.androidcrossstitchcounter.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    var finishDreamDate: Calendar? = null,
    var stitchedCrossBeforeRegistration: Int = 0,
    var startDate: Calendar? = null,
    var finishDate: Calendar? = null,
    val registrationDate: Calendar = CalendarUtils.getCurrentDate(),
    val userId: Int,
    var projStatusId: Int
)
