package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import kotlinx.coroutines.flow.count
import kotlin.time.Duration.Companion.nanoseconds

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HistoryScreen(navController: NavController) {

    var workouts = WorkoutLoggerApplication.workoutRepository.workouts.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text("Workout Tracker") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Screen.MainScreen.route)}) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) {
        LazyColumn{
            items(workouts.value) { workout ->
                Card() {
                    Text(text = "$workout.name")
                }
            }
        }
    }
}