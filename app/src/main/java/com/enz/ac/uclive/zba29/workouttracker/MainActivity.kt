package com.enz.ac.uclive.zba29.workouttracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enz.ac.uclive.zba29.workouttracker.screen.*
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.WorkoutTrackerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
                    composable(route = Screen.HomeScreen.route) {
                        HomeScreen(navController = navController)
                    }
                    composable(route = Screen.MainScreen.route) {
                        MainScreen(navController = navController)
                    }
                    composable(route = Screen.NewWorkoutScreen.route,
                    ) {
                        NewWorkoutScreen(navController = navController)
                    }
                    composable(
                        route = Screen.LogWorkoutScreen.route + "/{workoutId}",
                        arguments = listOf(
                            navArgument("workoutId") {
                                type = NavType.StringType
                                nullable = false
                            }
                        )
                    ) {
                            entry ->
                        LogWorkoutScreen(workoutId = entry.arguments?.getString("workoutId"), navController = navController)
                    }
                    composable(route = Screen.HistoryScreen.route) {
                        HistoryScreen(navController = navController)
                    }
                }
            }
        }
    }
}
