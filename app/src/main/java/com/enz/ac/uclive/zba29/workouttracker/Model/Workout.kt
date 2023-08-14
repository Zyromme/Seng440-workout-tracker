package com.enz.ac.uclive.zba29.workouttracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "workout")
data class Workout (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo var name: String,
    var exercises: List<Exercise>) {
    override fun toString() = name
}
