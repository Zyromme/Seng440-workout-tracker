package com.enz.ac.uclive.zba29.workouttracker.repository

import androidx.annotation.WorkerThread
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.dao.ExerciseDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {

    val exercises: Flow<List<Exercise>> = exerciseDao.getAllExercise()

    suspend fun getExercisesForWorkout(workoutId: Long): Flow<List<Exercise>> {
        return exerciseDao.getExercisesForWorkout(workoutId)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertExercise(exercise: Exercise): Long {
        return exerciseDao.insertExercise(exercise)
    }
}