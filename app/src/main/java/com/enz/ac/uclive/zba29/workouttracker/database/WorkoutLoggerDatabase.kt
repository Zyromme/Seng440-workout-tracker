package com.enz.ac.uclive.zba29.workouttracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.dao.ExerciseDao
import com.enz.ac.uclive.zba29.workouttracker.dao.ExerciseSetDao
import com.enz.ac.uclive.zba29.workouttracker.dao.WorkoutDao

@Database(
    entities = [Workout::class, Exercise::class, ExerciseSet::class],
    version = 1,
    exportSchema = false
)
abstract class WorkoutLoggerDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: WorkoutLoggerDatabase? = null

        fun getDatabase(context: Context): WorkoutLoggerDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutLoggerDatabase::class.java,
                    "workout_logger_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}