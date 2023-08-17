package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.provider.Settings.System.getString
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.R
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.Typography
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class SetState(val weight: Int, val reps: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWorkoutScreen(workoutId: String?, navController: NavController) {
    val scope = rememberCoroutineScope()
    val workoutRepository = WorkoutLoggerApplication.workoutRepository
    val exerciseRepository = WorkoutLoggerApplication.exerciseRepository
    val setStates = remember { mutableStateListOf<SetState>() }
    val workout = remember {
        mutableStateOf<Workout?>(null)
    }
    val exercises = remember {
        mutableStateOf<List<Exercise>>(emptyList())
    }
    val dateFormatterPattern = stringResource(R.string.date_pattern)
    var setIndex = 0
    LaunchedEffect(workoutId) {
        if (workoutId != null) {
            workout.value = workoutRepository.getWorkoutById(workoutId.toLong())
            exercises.value = exerciseRepository.getExercisesForWorkout(workoutId.toLong()).first()
            setStates.clear()
            for (exercise in exercises.value) {
                for (setNum in 1..exercise.setNum) {
                    setStates.add(SetState(weight = 0, reps = 0))
                }
            }
        }

    }
    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                workout.value?.let {
                    Text(text = it.name,
                        style = Typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center)
                }

                Box(modifier = Modifier.weight(0.9f)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {
                        item(workoutId) {
                            var setIndex = 0
                            for (exercise in exercises.value) {
                                Column {
                                    Text(text = exercise.name, style = Typography.displayMedium)

                                    Spacer(modifier = Modifier.height(8.dp))
                                    for (setNum in 0 until exercise.setNum) {

                                        SetRow(
                                            setState = setStates[setIndex],
                                            index = setIndex,
                                            setStates = setStates
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        setIndex += 1
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(0.1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                workout.value?.let { it1 -> submitWorkoutLog(navController, it1.name, exercises, setStates, dateFormatterPattern) }
                            }
                        }) {
                        Text(stringResource(R.string.finish_workout))
                    }
                }
            }
        }
    )
}

suspend fun submitWorkoutLog(navController: NavController,
                             workoutName: String, exercises: MutableState<List<Exercise>>,
                             setStates: MutableList<SetState>,
                             dateFormatterPattern: String) {
    val workoutRepository = WorkoutLoggerApplication.workoutRepository
    val exerciseRepository = WorkoutLoggerApplication.exerciseRepository
    val exerciseSetRepository = WorkoutLoggerApplication.exerciseSetRepository
    val dateFormatter = DateTimeFormatter.ofPattern(dateFormatterPattern)
    val currentDate = LocalDate.now().format(dateFormatter)
    val newWorkoutLog = Workout (
        name = workoutName,
        date = currentDate
            )
    val workoutId = workoutRepository.insertWorkout(newWorkoutLog)

    var counter = 0
    for (exercise in exercises.value) {
        var newExerciseLog = Exercise (
            name = exercise.name,
            setNum = exercise.setNum,
            workoutId = workoutId
                )
        val exerciseId = exerciseRepository.insertExercise(newExerciseLog)
        for (i in counter..counter + exercise.setNum - 1) {
            var newExerciseSetLog = ExerciseSet (
                weight = setStates[i].weight,
                reps = setStates[i].reps,
                exerciseId = exerciseId
                    )
            exerciseSetRepository.insertExerciseSet(newExerciseSetLog)
        }
        counter += exercise.setNum-1
    }
    navController.navigate(Screen.HistoryScreen.route)
}

@Composable
fun SetRow(
    setState: SetState,
    index: Int,
    setStates: MutableList<SetState>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = setState.weight.toString(),
            onValueChange = { newValue ->
                setStates[index] = setStates[index].copy(weight = newValue.toIntOrNull()?: 0)
            },
            label = { Text(stringResource(R.string.log_table_weight_title)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        TextField(
            value = setState.reps.toString(),
            onValueChange = { newValue ->
                setStates[index] = setStates[index].copy(reps = newValue.toIntOrNull()?: 0)
            },
            label = { Text(stringResource(R.string.log_table_reps_title)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

