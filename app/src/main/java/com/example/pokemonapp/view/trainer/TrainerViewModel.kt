package com.example.pokemonapp.view.trainer

import androidx.lifecycle.*
import com.example.pokemonapp.data.dao.TrainerDAO
import com.example.pokemonapp.data.models.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class TrainerViewModel(private val trainerDAO: TrainerDAO) : ViewModel() {

    val allTrainers: LiveData<List<Trainer>> = trainerDAO.getAllTrainers().asLiveData()

    val trainerWithPokemon: LiveData<TrainerWithPokemons>
        get() {
            return if (_trainerId.value != -1) {
                trainerDAO.getTrainerWithPokemon(_trainerId.value ?: -1).asLiveData()
            } else {
                MutableLiveData(TrainerWithPokemons(Trainer(-1),
                    mutableListOf(Pokemon(-1, fk_trainer = -1))
                ))
            }
        }

    private val _trainerId: MutableLiveData<Int> = MutableLiveData(-1)

    val trainerId: LiveData<Int>
        get() = _trainerId

    fun findTrainerWithPokemon(id: Int) {
        _trainerId.value = id
    }

    //TrainerName
    private val _name: MutableLiveData<String> = MutableLiveData("")

    val name: LiveData<String>
        get() = _name

    fun changeName(newName: String) {
        _name.value = newName
    }

    //TrainerAge
    private val _age: MutableLiveData<Int> = MutableLiveData(0)

    val age: LiveData<Int>
        get() = _age

    fun changeAge(newAge: Int) {
        _age.value = newAge
    }

    //TrainerGender
    private val _gender: MutableLiveData<String> = MutableLiveData("")

    val gender: LiveData<String>
        get() = _gender

    fun changeGender(newGender: String) {
        _gender.value = newGender
    }

    //TrainerCity
    private val _city: MutableLiveData<String> = MutableLiveData("")

    val city: LiveData<String>
        get() = _city

    fun changeCity(newCity: String) {
        _city.value = newCity
    }

    //TrainerPokemon
    private val _pokemons: MutableLiveData<MutableList<Pokemon>> =
        MutableLiveData(mutableListOf(Pokemon(-1, fk_trainer = -1)))

    val pokemons: LiveData<MutableList<Pokemon>>
        get() = _pokemons

    fun changePokemons(newPokemons: MutableList<Pokemon>) {
        _pokemons.value = newPokemons
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