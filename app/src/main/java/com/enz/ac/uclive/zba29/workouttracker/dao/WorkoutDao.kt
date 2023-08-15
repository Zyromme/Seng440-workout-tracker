package com.enz.ac.uclive.zba29.workouttracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT COUNT(*) FROM workout")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM workout WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Long) : Workout

    @Insert
    suspend fun insertWorkout(workout: Workout): Long
}