package com.enz.ac.uclive.zba29.workouttracker.viewModel

import androidx.lifecycle.*
import com.enz.ac.uclive.zba29.workouttracker.Model.ExerciseSet
import com.enz.ac.uclive.zba29.workouttracker.repository.ExerciseSetRepository
import kotlinx.coroutines.launch

class ExerciseSetViewModel(
    private val exerciseSetRepository: ExerciseSetRepository
) : ViewModel() {

    val exerciseSets : LiveData<List<ExerciseSet>> = exerciseSetRepository.exerciseSets.asLiveData()

    fun insertExerciseSet(exerciseSet: ExerciseSet) {
        viewModelScope.launch {
            val exerciseSetId = exerciseSetRepository.insertExerciseSet(exerciseSet)
        }
    }
}