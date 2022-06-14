package com.example.pokemonapp.view.pokemon

import androidx.lifecycle.*
import com.example.pokemonapp.data.dao.PokemonDAO
import com.example.pokemonapp.data.models.Pokemon
import com.example.pokemonapp.data.models.PokemonSkillCrossRef
import com.example.pokemonapp.data.models.PokemonWithSkills
import com.example.pokemonapp.data.models.Skill
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PokemonViewModel(private val pokemonDAO: PokemonDAO) : ViewModel() {

    val allPokemons: LiveData<List<Pokemon>> = pokemonDAO.getAllPokemon().asLiveData()

    val crossRef: LiveData<MutableList<PokemonSkillCrossRef>>
        get() {
            return if (pokemonId.value != -1) {
                pokemonDAO.getPokemonCrossRef(pokemonId.value ?: -1).asLiveData()
            } else {
                MutableLiveData(mutableListOf(PokemonSkillCrossRef(-1, -1)))
            }
        }

    val pokemonWithSkills: LiveData<PokemonWithSkills>
        get() {
            return if (pokemonId.value != -1) {
                pokemonDAO.getPokemonWithSkills(pokemonId.value ?: -1).asLiveData()
            } else {
                MutableLiveData(PokemonWithSkills(Pokemon(-1), mutableListOf()))
            }
        }

    private val pokemonId: MutableLiveData<Int> = MutableLiveData(-1)

    fun findPokemonWithSkills(id: Int) {
        pokemonId.value = id
    }

    //PokemonWithSkillName
    private val _name: MutableLiveData<String> = MutableLiveData("")

    val name: LiveData<String>
        get() = _name

    fun changeName(newName: String) {
        _name.value = newName
    }

    //PokemonWithSkillType
    private val _type: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf("", ""))

    val type: LiveData<MutableList<String>>
        get() = _type

    fun changeType(newType: MutableList<String>) {
        _type.value = newType
    }

    //Skills
    private val _skills: MutableLiveData<MutableList<Skill>> =
        MutableLiveData(
            mutableListOf(Skill(-1), Skill(-1), Skill(-1), Skill(-1))
        )

    val skills: LiveData<MutableList<Skill>>
        get() = _skills

    fun changeSkills(newSkills: MutableList<Skill>) {
        _skills.value = newSkills
    }

    private fun insert(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDAO.insert(pokemon = pokemon)
        }
    }

    private fun getNewPokemonEntry(name: String, type: MutableList<String>) : Pokemon {
        return Pokemon(name = name, type = type)
    }

    fun createPokemon(name: String, type: MutableList<String>) {
        insert(getNewPokemonEntry(name = name, type = type))
    }

    private fun getNewPokemonEntry(name: String, type: MutableList<String>, fk_trainer: Int) : Pokemon {
        return Pokemon(name = name, type = type, fk_trainer = fk_trainer)
    }

    fun createPokemon(name: String, type: MutableList<String>, fk_trainer: Int) {
        insert(getNewPokemonEntry(name = name, type = type, fk_trainer = fk_trainer))
    }

    fun updatePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDAO.update(pokemon = pokemon)
        }
    }

    fun deletePokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDAO.delete(pokemon = pokemon)
        }
    }

    private fun insertPokemonCrossRefEntry(pokemonSkillCrossRef: PokemonSkillCrossRef) {
        viewModelScope.launch {
            pokemonDAO.insertPokemonCrossRef(pokemonSkillCrossRef)
        }
    }

    private fun getNewPokemonCrossRefEntry(pokemon_id: Int, skill_id: Int): PokemonSkillCrossRef {
        return PokemonSkillCrossRef(pokemon_id = pokemon_id, skill_id = skill_id)
    }

    fun createPokemonCrossRef(pokemon_id: Int, skill_id: Int) {
        insertPokemonCrossRefEntry(getNewPokemonCrossRefEntry(pokemon_id = pokemon_id,
            skill_id = skill_id))
    }

    fun deletePokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef) {
        viewModelScope.launch {
            pokemonDAO.deletePokemonCrossRef(pokemonSkillCrossRef)
        }
    }
}

class PokemonViewModelFactory(private val pokemonDAO: PokemonDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            return PokemonViewModel(pokemonDAO = pokemonDAO) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }
}