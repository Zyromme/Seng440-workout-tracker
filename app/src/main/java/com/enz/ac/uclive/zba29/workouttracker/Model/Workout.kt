package com.enz.ac.uclive.zba29.workouttracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "workout")
data class Workout (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var date: String? = null) {
    override fun toString() = name
}
