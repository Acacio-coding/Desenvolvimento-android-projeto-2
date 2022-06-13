package com.example.pokemonapp.view.trainer

import androidx.lifecycle.*
import com.example.pokemonapp.data.dao.TrainerDAO
import com.example.pokemonapp.data.models.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TrainerViewModel(private val trainerDAO: TrainerDAO) : ViewModel() {

    val allTrainers: LiveData<List<Trainer>> = trainerDAO.getAllTrainers().asLiveData()

    fun getTrainer(id: Int) : Trainer {
        val allTrainerList = allTrainers.value ?: listOf()

        return allTrainerList.find { currentTrainer : Trainer ->
            currentTrainer.trainer_id == id
        } ?: Trainer(-1,"", -1, "", "")
    }

    private fun insert(trainer: Trainer) {
        viewModelScope.launch {
            trainerDAO.insert(trainer = trainer)
        }
    }

    private fun getNewTrainerEntry(name: String, age: Int,
                                   gender: String, city: String) : Trainer {
        return Trainer(name = name, age = age, gender = gender, city = city)
    }

    fun createTrainer(name: String, age: Int,
                              gender: String, city: String) {
        insert(getNewTrainerEntry(name, age, gender, city))
    }

    fun updateTrainer(trainer: Trainer) {
        viewModelScope.launch {
            trainerDAO.update(trainer = trainer)
        }
    }

    fun deleteTrainer(trainer: Trainer) {
        viewModelScope.launch {
            trainerDAO.delete(trainer = trainer)
        }
    }
}

class TrainerViewModelFactory(private val trainerDAO: TrainerDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerViewModel::class.java)) {
            return TrainerViewModel(trainerDAO = trainerDAO) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }
}