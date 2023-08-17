package com.enz.ac.uclive.zba29.workouttracker

import android.app.Application
import android.content.Context
import com.enz.ac.uclive.zba29.workouttracker.database.WorkoutLoggerDatabase
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseRepository
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseSetRepository
import com.enz.ac.uclive.zba29.workouttracker.repository.WorkoutRepository

class WorkoutTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

    }

    companion object {
        lateinit var appContext : Context
        val database by lazy { WorkoutLoggerDatabase.getDatabase(appContext) }
        val workoutRepository by lazy { WorkoutRepository(database.workoutDao()) }
        val exerciseRepository by lazy { ExerciseRepository(database.exerciseDao()) }
        val exerciseSetRepository by lazy { ExerciseSetRepository(database.exerciseSetDao()) }
    }

}
