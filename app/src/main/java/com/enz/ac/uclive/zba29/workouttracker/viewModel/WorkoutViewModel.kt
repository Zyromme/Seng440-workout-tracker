package com.enz.ac.uclive.zba29.workouttracker.viewModel

import androidx.lifecycle.*
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val workouts: LiveData<List<Workout>> = workoutRepository.workouts.asLiveData()

    fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            val workoutId = workoutRepository.insertWorkout(workout)
        }
    }

    fun getWorkoutById(workoutId: String) {
        viewModelScope.launch {

        }
    }
}
