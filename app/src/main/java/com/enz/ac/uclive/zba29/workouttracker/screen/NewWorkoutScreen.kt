package com.enz.ac.uclive.zba29.workouttracker.screen

import android.content.Context
import android.content.res.Configuration
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.R
import com.enz.ac.uclive.zba29.workouttracker.WorkoutTrackerApplication


@OptIn(ExperimentalMaterial3Api::class)
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
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Screen.MainScreen.route)}) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    when {
                        workoutName.isEmpty() -> {
                            workoutNameHasError = true
                            workoutNameError = context.getString(R.string.required_field_error)
                            val vibrationEffect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
                            vibrator.vibrate(vibrationEffect)
                        }
                        else -> {
                            Toast.makeText(context, context.getString(R.string.new_workout_success), Toast.LENGTH_LONG).show()
                            scope.launch {
                                submitWorkout(workoutName, exerciseInputs, navController)
                            }
                        }
                    }
                }
            ) {
                Text(stringResource(R.string.save_workout))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = {
            Box (
                modifier = Modifier.padding(it)
            ) {
                val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
                if (isLandscape) {
                    LandscapeLayout(workoutName = workoutName,
                        onWorkoutNameChange = { workoutName = it },
                        workoutNameHasError = workoutNameHasError,
                        workoutNameError = workoutNameError,
                        exerciseInputs = exerciseInputs)
                } else {
                    PortraitLayout(
                        workoutName = workoutName,
                        onWorkoutNameChange = { workoutName = it },
                        workoutNameHasError = workoutNameHasError,
                        workoutNameError = workoutNameError,
                        exerciseInputs = exerciseInputs)
                }
            }
        }
    )
}

@Composable
fun LandscapeLayout(
    workoutName: String,
    onWorkoutNameChange: (String) -> Unit,
    workoutNameHasError: Boolean,
    workoutNameError: String,
    exerciseInputs: SnapshotStateList<ExerciseInputState>
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
                onWorkoutNameChange(it)
            },
            label = { Text(stringResource(R.string.workout_name_label)) },
            isError = workoutNameHasError,
            modifier = Modifier.fillMaxWidth()
        )
        if (workoutNameHasError) {
            Text(
                text = workoutNameError,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.padding(15.dp))

        Row() {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.7f).weight(0.65f)
            ) {
                itemsIndexed(exerciseInputs) { index, exerciseInputState ->
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
            }
            Box(
                modifier = Modifier.weight(0.3f).fillMaxHeight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        exerciseInputs.add(ExerciseInputState("", ""))
                    }) {
                    Text(stringResource(R.string.button_add_exercise))
                }
            }
        }
    }
}

@Composable
fun PortraitLayout(
    workoutName: String,
    onWorkoutNameChange: (String) -> Unit,
    workoutNameHasError: Boolean,
    workoutNameError: String,
    exerciseInputs: SnapshotStateList<ExerciseInputState>
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
                onWorkoutNameChange(it)
            },
            label = { Text(stringResource(R.string.workout_name_label)) },
            isError = workoutNameHasError,
            modifier = Modifier.fillMaxWidth()
        )
        if (workoutNameHasError) {
            Text(
                text = workoutNameError,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
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
                        exerciseInputs[index] = newExercise
                    },
                    onSetInputChange = { newSet ->
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
            Text(stringResource(R.string.button_add_exercise))
        }
    }
}



suspend fun submitWorkout(workoutName: String, exerciseInputs: SnapshotStateList<ExerciseInputState>, navController: NavController) {
    val workoutRepository = WorkoutTrackerApplication.workoutRepository
    val exerciseRepository = WorkoutTrackerApplication.exerciseRepository
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
            label = { Text(stringResource(R.string.exercise)) },
            placeholder = { Text(stringResource(R.string.exercise_name_placeholder)) },
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.width(5.dp))
        TextField(
            modifier = Modifier.weight(0.25f),
            value = exerciseInputState.set,
            onValueChange = onSetInputChange,
            label = { Text(stringResource(R.string.new_workout_sets_title)) },
            placeholder = { Text(stringResource(R.string.set_placeholder)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            singleLine = true
        )

    }
}

