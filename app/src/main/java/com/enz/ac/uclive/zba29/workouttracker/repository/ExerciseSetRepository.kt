package com.enz.ac.uclive.zba29.workouttracker.repository

import androidx.annotation.WorkerThread
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.dao.ExerciseSetDao
import kotlinx.coroutines.flow.Flow

class ExerciseSetRepository(private val exerciseSetDao: ExerciseSetDao) {

    val exerciseSets: Flow<List<ExerciseSet>> = exerciseSetDao.getAllExerciseSets()

    suspend fun getExerciseSetsForExercise(exerciseId: Long): Flow<List<ExerciseSet>> {
        return exerciseSetDao.getExerciseSetsForExercise(exerciseId)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet): Long {
        return exerciseSetDao.insertExerciseSet(exerciseSet)
    }
}