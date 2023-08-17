package com.enz.ac.uclive.zba29.workouttracker.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.R
import com.enz.ac.uclive.zba29.workouttracker.WorkoutLoggerApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
                            style = MaterialTheme.typography.displaySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        Text(text = "${workout.date}",
                            style = MaterialTheme.typography.titleMedium)
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
                                style = MaterialTheme.typography.labelLarge
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

        val exerciseSets = WorkoutLoggerApplication.exerciseSetRepository.getExerciseSetsForExercise(exercise.id).first()
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
        Text(stringResource(R.string.log_table_weight_title), style = MaterialTheme.typography.labelMedium)
        Text(stringResource(R.string.log_table_reps_title), style = MaterialTheme.typography.labelMedium)
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
