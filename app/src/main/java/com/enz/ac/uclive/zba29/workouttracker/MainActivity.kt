package com.enz.ac.uclive.zba29.workouttracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout.Alignment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.WorkoutTrackerTheme

val workouts = listOf<Workout> (
    Workout("workout1", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout2", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout3", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout4", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout5", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout6", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout7", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout8", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4))))),
    Workout("workout9", listOf(Excercise("excercise1", listOf(ExcerciseSet(3, 2), ExcerciseSet(4, 4)))))
)

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                Scaffold(
                    topBar = {
                        TopAppBar (
                            title = { Text(getString(R.string.app_name)) }
                        )
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text("New Workout") },
                            onClick = { /* .... */ } //TODO
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    isFloatingActionButtonDocked = true,
                ) {
                    WorkoutList(workouts)

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
        WorkoutList(workouts)
    }
}