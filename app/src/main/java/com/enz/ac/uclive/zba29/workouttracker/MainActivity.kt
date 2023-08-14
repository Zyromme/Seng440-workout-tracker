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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.screen.NewWorkoutScreen
import com.enz.ac.uclive.zba29.workouttracker.screen.Screen
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.WorkoutTrackerTheme
import com.enz.ac.uclive.zba29.workouttracker.viewModel.WorkoutViewModel




class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                    composable(route = Screen.MainScreen.route) {
                        MainScreen(navController = navController)
                    }
                    composable(route = Screen.NewWorkoutScreen.route) {
                        NewWorkoutScreen(navController = navController)
                    }
                    composable(
                        route = Screen.LogWorkoutScreen.route,
                        arguments = listOf(
                            navArgument("workoutId") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                            entry ->
                        LogWorkoutScreen(workoutId = entry.arguments?.getString("workoutId"))
                    }
                }
            }
        }
    }
}

@Composable
fun workoutCard(workout: Workout) {
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
                modifier = Modifier.align(CenterVertically),
                text = "${workout.name}!"
            )
            Button(onClick = { },
                modifier = Modifier.align(CenterVertically))
            {
                Text(text = "Start Workout")
            }
        }
    }
}

@Composable
fun WorkoutList(workouts: List<Workout>) {
    LazyColumn {
        items(workouts) { workout ->
            workoutCard(workout)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WorkoutTrackerTheme {
//        val navController = rememberNavController()
//        WorkoutList(workouts)
//        NewWorkoutScreen(navController)
//        excerciseInput()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavController) {
    val viewModel: WorkoutViewModel = viewModel()
    val workouts by viewModel.workouts.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text("Workout Tracker") }
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
        WorkoutList(workouts)
    }
}

@Composable
fun LogWorkoutScreen(workoutId: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        if (workoutId != null) {
            Text(text = workoutId)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {

        }) {
            Text(text = "Add Exercise")
        }
    }
}