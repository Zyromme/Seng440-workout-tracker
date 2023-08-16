package com.enz.ac.uclive.zba29.workouttracker.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.R
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication.Companion.exerciseRepository
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication.Companion.exerciseSetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                title = { Text(text = stringResource(R.string.app_name)) },
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
                    Column(modifier = Modifier
                        .weight(0.65f)) {
                        Text(
                            text = "$workout",
                            style = MaterialTheme.typography.h5,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(text = "${workout.date}",
                            style = MaterialTheme.typography.subtitle1)
                    }
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(onClick = {
                            scope. launch {
                                handleSend(workout, exercises, context)
                            }
                        }) {
                            Icon(Icons.Default.Share, null)
                        }
                    }
                }
                LazyColumn {
                    items(exercises.value) { exercise ->
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Text(
                                exercise.name,
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
}

suspend fun handleSend(workout: Workout, exercises: MutableState<List<Exercise>>, context: Context) {

    var message = "Workout: ${workout.name}\n"
    for (exercise in exercises.value) {
        message += " Exercise: ${exercise.name} \n"

        val exerciseSets = exerciseSetRepository.getExerciseSetsForExercise(exercise.id).first()
        exerciseSets.forEachIndexed { index, exerciseSet ->
            message += "  Set ${index + 1}: ${exerciseSet.weight} for ${exerciseSet.reps} reps \n"
        }
    }
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
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
    Row(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.log_table_weight_title), fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.log_table_reps_title), fontWeight = FontWeight.Bold)
    }
    for (exerciseSet in exerciseSets) {
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
