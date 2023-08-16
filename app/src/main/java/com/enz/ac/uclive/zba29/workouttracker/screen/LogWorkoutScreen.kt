package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.Typography
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class SetState(val weight: Int, val reps: Int)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                title = { Text("Workout Tracker") }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            workout.value?.let {
                Text(text = it.name,
                style = Typography.h3,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center)
            }

            Box(modifier = Modifier.weight(0.9f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp)
                ) {
                    itemsIndexed(exercises.value) { exerciseIndex, exercise ->
                        Column {
                            Text(text = exercise.name, style = MaterialTheme.typography.h5)

                            Spacer(modifier = Modifier.height(8.dp))
                            for (setNum in 1..exercise.setNum) {
                                val setIndex =
                                    calculateSetIndex(exerciseIndex, setNum, exercises.value)
                                SetRow(
                                    setState = setStates[setIndex],
                                    onWeightChange = { weight ->
                                        setStates[setIndex] =
                                            setStates[setIndex].copy(weight = weight)
                                    },
                                    onRepsChange = { reps ->
                                        setStates[setIndex] = setStates[setIndex].copy(reps = reps)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.weight(0.1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            workout.value?.let { it1 -> submitWorkoutLog(navController, it1.name, exercises, setStates) }
                        }
                    }) {
                    Text(text = "Finish Workout")
                }
            }
        }
    }
}

suspend fun submitWorkoutLog(navController: NavController,
                             workoutName: String, exercises: MutableState<List<Exercise>>,
                             setStates: MutableList<SetState>) {
    val workoutRepository = WorkoutLoggerApplication.workoutRepository
    val exerciseRepository = WorkoutLoggerApplication.exerciseRepository
    val exerciseSetRepository = WorkoutLoggerApplication.exerciseSetRepository
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
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
    }
    navController.navigate(Screen.HistoryScreen.route)
}

private fun calculateSetIndex(exerciseIndex: Int, setNum: Int, exercises: List<Exercise>): Int {
    var index = 0
    for (i in 0 until exerciseIndex) {
        index += exercises[i].setNum
    }
    return index + (setNum - 1)
}


@Composable
fun SetRow(
    setState: SetState,
    onWeightChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = setState.weight.toString(),
            onValueChange = { newValue ->
                onWeightChange(newValue.toIntOrNull() ?: 0)
            },
            label = { Text(text = "Weight") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        TextField(
            value = setState.reps.toString(),
            onValueChange = { newValue ->
                onRepsChange(newValue.toIntOrNull() ?: 0)
            },
            label = { Text(text = "Reps") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

