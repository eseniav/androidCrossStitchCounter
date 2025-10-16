package com.example.androidcrossstitchcounter.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projStatuses")
data class ProjStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val statusName: String = ""
)
