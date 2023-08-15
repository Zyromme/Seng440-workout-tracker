package com.enz.ac.uclive.zba29.workouttracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSetDao {

    @Query("SELECT * FROM exercise_set")
    fun getAllExerciseSets(): Flow<List<ExerciseSet>>

    @Query("SELECT * FROM exercise_set WHERE exerciseId = :exerciseId")
    fun getExerciseSetsForExercise(exerciseId: Long): Flow<List<ExerciseSet>>

    @Insert
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long
}