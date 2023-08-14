package com.enz.ac.uclive.zba29.workouttracker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseRepository
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseSetRepository
import com.enz.ac.uclive.zba29.workouttracker.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> Flow<T>.asLiveData(): LiveData<T> = this.asLiveData()

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository,
    private val exerciseSetRepository: ExerciseSetRepository
) : ViewModel() {

    val workouts: LiveData<List<Workout>> = workoutRepository.workouts.asLiveData()

    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            val workoutId = workoutRepository.insertWorkout(workout)
        }
    }
}
