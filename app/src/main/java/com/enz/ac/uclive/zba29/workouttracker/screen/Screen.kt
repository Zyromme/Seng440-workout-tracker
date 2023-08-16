package com.enz.ac.uclive.zba29.workouttracker.screen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object NewWorkoutScreen : Screen("new_workout_screen")
    object LogWorkoutScreen : Screen("log_workout_screen")
    object HistoryScreen : Screen("history_screen")
    object HomeScreen: Screen("home_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}