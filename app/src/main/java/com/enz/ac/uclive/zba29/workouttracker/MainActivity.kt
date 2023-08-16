package com.enz.ac.uclive.zba29.workouttracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication.Companion.workoutRepository
import com.enz.ac.uclive.zba29.workouttracker.screen.*
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.WorkoutTrackerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
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
