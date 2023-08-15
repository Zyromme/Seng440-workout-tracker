package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {

    var workouts = WorkoutLoggerApplication.workoutRepository.workouts

    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text("Workout Tracker") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.HistoryScreen.route) }) {
                        Icon(Icons.Default.CalendarMonth, null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("New Workout") },
                onClick = {
                    navController.navigate(Screen.NewWorkoutScreen.route)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) {
        val workoutList = workouts.collectAsState(emptyList())
        WorkoutList(workoutList.value, navController)
    }
}


@Composable
fun WorkoutList(workouts: List<Workout>, navController: NavController) {
    LazyColumn {
        items(workouts) { workout ->
            workoutCard(workout, navController)
        }
    }
}

@Composable
fun workoutCard(workout: Workout, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.6f),
                text = "${workout.name}!",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Button(
                onClick = { navController.navigate(Screen.LogWorkoutScreen.withArgs(workout.id.toString())) },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.25f)
            )
            {
                Text(text = "Start")
            }
        }
    }
}
