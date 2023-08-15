package com.enz.ac.uclive.zba29.workouttracker.viewModel

import androidx.lifecycle.*
import com.enz.ac.uclive.zba29.workouttracker.Model.Exercise
import com.enz.ac.uclive.zba29.workouttracker.Model.Workout
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseRepository
import com.enz.ac.uclive.zba29.workouttracker.repository.WorkoutRepository
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    val excercises: LiveData<List<Exercise>> = exerciseRepository.exercises.asLiveData()

    fun insertExercise(excercise: Exercise) {
        viewModelScope.launch {
            val exerciseId = exerciseRepository.insertExercise(excercise)
        }
    }
}