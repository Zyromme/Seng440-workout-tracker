package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import com.enz.ac.uclive.zba29.workouttracker.ui.theme.Purple200
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "FlowOperatorInvokedInComposition")
@Composable
fun HistoryScreen(navController: NavController) {

    var workouts = WorkoutLoggerApplication.workoutRepository.workouts.map { workouts ->
        workouts.filter {
            it.date != null } }.collectAsState(emptyList())

    var workoutLog = remember {
        mutableStateOf<Workout?>(null)
    }

    var showLogDetail = remember {
        mutableStateOf(false)
    }

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
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                ) {
            items(workouts.value) { workout ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 5.dp)
                        .clickable(onClick = {
                            workoutLog.value = workout
                            showLogDetail.value = true
                        }),
                    elevation = 10.dp,

                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$workout",
                            modifier = Modifier
                                .weight(0.7f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(text = "${workout.date}",
                            modifier = Modifier.weight(0.25f))
                    }
                }
            }
        }
    }
    if (showLogDetail.value) {
        workoutLog.value?.let {
            LogDetailDialog(
                workout = it,
                onDismiss = { showLogDetail.value = false })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LogDetailDialog(
    workout: Workout,
    onDismiss:() -> Unit
) {
    val exerciseRepository = WorkoutLoggerApplication.exerciseRepository
    val exercises = remember {
        mutableStateOf<List<Exercise>>(emptyList())
    }

    LaunchedEffect(workout) {
        val exerciseFlow = exerciseRepository.getExercisesForWorkout(workout.id)
        exerciseFlow.collect { collectedExercises ->
            exercises.value = collectedExercises
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = 5.dp,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .border(1.dp, color = Purple200, shape = RoundedCornerShape(15.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$workout",
                        modifier = Modifier
                            .weight(0.65f),
                        style = MaterialTheme.typography.h5,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(text = "${workout.date}",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.weight(0.3f))
                }
                for (exercise in exercises.value) {

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Text(
                            exercise.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        ExerciseLogTable(exercise)
                    }

                }
            }
        }
    }
}

@Composable
fun ExerciseLogTable(exercise: Exercise) {
    val exerciseSetRepository = WorkoutLoggerApplication.exerciseSetRepository
    var exerciseSets by remember { mutableStateOf(emptyList<ExerciseSet>()) }

    LaunchedEffect(exercise) {
        val exerciseSetsFlow = exerciseSetRepository.getExerciseSetsForExercise(exercise.id)
        exerciseSetsFlow.collect { collectedExerciseSets ->
            exerciseSets = collectedExerciseSets
        }
    }

    LazyColumn(
    ) {
        item(exerciseSets) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Weight (kg)", fontWeight = FontWeight.Bold)
                Text("Reps", fontWeight = FontWeight.Bold)
            }
        }
        items(exerciseSets) { exerciseSet ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(exerciseSet.weight.toString())
                Text(exerciseSet.reps.toString())
            }
        }
    }
}
