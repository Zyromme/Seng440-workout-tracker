package com.enz.ac.uclive.zba29.workouttracker.repository

import androidx.annotation.WorkerThread
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.dao.WorkoutDao
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    val workouts: Flow<List<Workout>> = workoutDao.getAllWorkouts()
    val numWorkouts: Flow<Int> = workoutDao.getCount()

    suspend fun getAllWorkouts(): Flow<List<Workout>> {
        return workoutDao.getAllWorkouts()
    }

    suspend fun getWorkoutById(workoutId: Long): Workout {
        return workoutDao.getWorkoutById(workoutId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertWorkout(workout: Workout): Long {
        return workoutDao.insertWorkout(workout)
    }
}