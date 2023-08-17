package com.enz.ac.uclive.zba29.workouttracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    fun getAllExercise(): Flow<List<Exercise>>


    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<Exercise>>

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Query("DELETE FROM exercise WHERE workoutId = :workoutId")
    fun deleteExercisesByWorkoutId(workoutId: Long)
}