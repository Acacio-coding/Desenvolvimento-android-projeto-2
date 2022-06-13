package com.example.pokemonapp.view.pokemon

import android.util.Log
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

    private val allPokemonWithSkills: LiveData<List<PokemonWithSkills>> =
        pokemonDAO.getPokemonsWithSkills().asLiveData()

    fun getPokemon(id: Int) : PokemonWithSkills {
        val list = allPokemonWithSkills.value ?: listOf()

        Log.d("PokemonWithSkills", allPokemonWithSkills.value.toString())

        return list.find {
            it.pokemon.pokemon_id == id
        } ?: PokemonWithSkills(Pokemon(-1), listOf())
    }

    private fun insert(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonDAO.insert(pokemon = pokemon)
        }
    }

    private fun getNewPokemonEntry(name: String, type: List<String>) : Pokemon {
        return Pokemon(name = name, type = type)
    }

    fun createPokemon(name: String, type: List<String>) {
        insert(getNewPokemonEntry(name = name, type = type))
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

    fun updatePokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef) {
        viewModelScope.launch {
            pokemonDAO.updatePokemonCrossRef(pokemonSkillCrossRef)
        }
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