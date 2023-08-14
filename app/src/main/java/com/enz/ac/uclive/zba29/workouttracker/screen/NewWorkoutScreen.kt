package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewWorkoutScreen(navController: NavController) {
    var workoutName by remember {
        mutableStateOf("")
    }
    var workoutNameError by remember {
        mutableStateOf("")
    }
    var workoutNameHasError by remember {
        mutableStateOf(false)
    }
    val exerciseInputs = remember { mutableStateListOf(ExerciseInputState("", "")) }
    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text("Workout Tracker") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save Workout") },
                onClick = {
                    when {
                        workoutName.isEmpty() -> {
                            workoutNameHasError = true
                            workoutNameError = "This field is required"
                        }
                        else -> {
                            navController.navigate(Screen.MainScreen.route)
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = workoutName,
                onValueChange = {
                    workoutName = it
                },
                label = { Text(text = "WorkoutName") },
                isError = workoutNameHasError,
                modifier = Modifier.fillMaxWidth()
            )
            if (workoutNameHasError) {
                Text(
                    text = workoutNameError,
                    color = Color.Red,
                    style = MaterialTheme.typography.caption
                )
            }

            Spacer(modifier = Modifier.padding(15.dp))

            exerciseInputs.forEachIndexed { index, exerciseInputState ->
                exerciseInput(
                    exerciseInputState = exerciseInputState,
                    onExerciseInputChange = { newExercise ->
                        exerciseInputs[index] = newExercise
                    },
                    onSetInputChange = { newSet ->
                        exerciseInputs[index] = exerciseInputState.copy(set = newSet)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                exerciseInputs.add(ExerciseInputState("", ""))
            }) {
                Text(text = "Add Exercise")
            }
        }
    }
}

data class ExerciseInputState(val exercise: String, val set: String)

@Composable
fun exerciseInput(
    exerciseInputState: ExerciseInputState,
    onExerciseInputChange: (ExerciseInputState) -> Unit,
    onSetInputChange: (String) -> Unit
) {
    var exerciseName by remember { mutableStateOf("")}
    var setNum by remember { mutableStateOf("") }
    Row{
        TextField(
            value = exerciseInputState.exercise,
            onValueChange = {
                onExerciseInputChange(exerciseInputState.copy(exercise = it))
            },
            label = { Text(text = "Excercise") },
            placeholder = { Text(text = "Exercise Name") },
        )

        Spacer(modifier = Modifier.padding(2.dp))

        TextField(
            value = exerciseInputState.set,
            onValueChange = onSetInputChange,
            label = { Text(text = "Sets") },
            placeholder = { Text(text = "0") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

    }
}