package com.enz.ac.uclive.zba29.workouttracker.screen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object NewWorkoutScreen : Screen("new_workout_screen")
    object LogWorkoutScreen : Screen("log_workout_screen")
}