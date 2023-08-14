package com.enz.ac.uclive.zba29.workouttracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo var name: String,
    @ColumnInfo var workoutId: Long,
    var sets: List<ExerciseSet>
)
