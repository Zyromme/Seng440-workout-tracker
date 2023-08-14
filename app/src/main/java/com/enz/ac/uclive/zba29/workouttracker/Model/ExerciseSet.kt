package com.enz.ac.uclive.zba29.workouttracker.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "exercise_set")
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo var weight: Int,
    @ColumnInfo var reps: Int,
    @ColumnInfo var exerciseId: Long
)