package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication.Companion.workoutRepository
import com.enz.ac.uclive.zba29.workouttracker.viewModel.WorkoutViewModel
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewWorkoutScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
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
                            scope.launch {
                                submitWorkout(workoutName, exerciseInputs, navController)
                            }
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

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.7f)
            ) {
                itemsIndexed(exerciseInputs) { index, exerciseInputState ->
                    exerciseInput(
                        exerciseInputState = exerciseInputState,
                        onExerciseInputChange = { newExercise ->
                            // Handle exercise input change with index
                            exerciseInputs[index] = newExercise
                        },
                        onSetInputChange = { newSet ->
                            // Handle set input change with index
                            exerciseInputs[index] = exerciseInputState.copy(set = newSet)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
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

suspend fun submitWorkout(workoutName: String, exerciseInputs: SnapshotStateList<ExerciseInputState>, navController: NavController) {
    val workoutRepository = WorkoutLoggerApplication.workoutRepository
    val exerciseRepository = WorkoutLoggerApplication.exerciseRepository
    val newWorkout = Workout (
        name = workoutName
    )
    val workoutId = workoutRepository.insertWorkout(newWorkout)
    for (exercise in exerciseInputs) {
        var exerciseName = exercise.exercise.trim()
        var numSets = 0
        if (!exercise.set.isEmpty()) {
            numSets = exercise.set.toInt()
        }


        if (!(exerciseName.isEmpty() || (numSets == 0))) {
            var newExercise = Exercise (
                name = exerciseName,
                workoutId = workoutId,
                setNum = numSets)
            exerciseRepository.insertExercise(newExercise)
        }
    }
    navController.navigate(Screen.MainScreen.route)
}

data class ExerciseInputState(val exercise: String, val set: String)

@Composable
fun exerciseInput(
    exerciseInputState: ExerciseInputState,
    onExerciseInputChange: (ExerciseInputState) -> Unit,
    onSetInputChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        TextField(
            modifier = Modifier.weight(0.6f),
            value = exerciseInputState.exercise,
            onValueChange = {
                onExerciseInputChange(exerciseInputState.copy(exercise = it))
            },
            label = { Text(text = "Excercise") },
            placeholder = { Text(text = "Exercise Name") },
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.width(5.dp))
        TextField(
            modifier = Modifier.weight(0.25f),
            value = exerciseInputState.set,
            onValueChange = onSetInputChange,
            label = { Text(text = "Sets") },
            placeholder = { Text(text = "0") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            singleLine = true
        )

    }
}

