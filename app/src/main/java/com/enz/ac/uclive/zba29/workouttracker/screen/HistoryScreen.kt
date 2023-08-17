package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.R
import com.enz.ac.uclive.zba29.workouttracker.WorkoutTrackerApplication
import com.enz.ac.uclive.zba29.workouttracker.components.LogDetailDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint( "FlowOperatorInvokedInComposition")
@Composable
fun HistoryScreen(navController: NavController) {

    var workouts = WorkoutTrackerApplication.workoutRepository.workouts.map { workouts ->
        workouts.filter {
            it.date != null } }.collectAsState(emptyList())

    var reversedWorkouts = workouts.value.reversed()

    var workoutLog = remember {
        mutableStateOf<Workout?>(null)
    }

    var showLogDetail = remember {
        mutableStateOf(false)
    }

    if (showLogDetail.value) {
        workoutLog.value?.let {
            LogDetailDialog(
                workout = it,
                onDismiss = { showLogDetail.value = false })
        }
    }
    Scaffold(
        topBar = {
            TopAppBar (
                title = { Text(text = stringResource(R.string.past_workouts)) },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Screen.MainScreen.route)}) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        },
        content = {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
            ) {
                items(reversedWorkouts) { workout ->
                    workoutLogCard(navController = navController, workout = workout, workoutLog = workoutLog, showLogDetail = showLogDetail)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun workoutLogCard(navController: NavController, workout: Workout, workoutLog: MutableState<Workout?>, showLogDetail: MutableState<Boolean>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            when (dismissValue) {
                DismissValue.DismissedToStart -> {
                    scope.launch {
                        DeleteWorkoutLog(workout)
                    }
                    navController.navigate(Screen.HistoryScreen.route)
                }
                else -> {}
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color.Transparent
                DismissDirection.EndToStart -> Color.Red
                null -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 10.dp)
                    .background(color),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Box(modifier = Modifier.padding(end = 10.dp)) {
                    Icon(
                        imageVector = Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .clickable(onClick = {
                        workoutLog.value = workout
                        showLogDetail.value = true
                    }),

                ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$workout",
                        modifier = Modifier
                            .weight(0.65f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(text = "${workout.date}",
                        modifier = Modifier.weight(0.30f))
                }
            }
        }
    )

}

suspend fun DeleteWorkoutLog(workout: Workout) {

    val exerciseRepository = WorkoutTrackerApplication.exerciseRepository
    val exerciseSetRepository = WorkoutTrackerApplication.exerciseSetRepository
    val workoutRepository = WorkoutTrackerApplication.workoutRepository

    val exerciseFlow = exerciseRepository.getExercisesForWorkout(workout.id)

    GlobalScope.launch (Dispatchers.IO){
        exerciseFlow.collect { exercises ->
            for (exercise in exercises) {

                exerciseSetRepository.deleteExerciseSetsByExerciseId(exercise.id)
            }
            exerciseRepository.deleteExercisesByWorkoutId(workout.id)
        }
    }
    GlobalScope.launch (Dispatchers.IO){
        workoutRepository.deleteWorkoutById(workout.id)
    }

}