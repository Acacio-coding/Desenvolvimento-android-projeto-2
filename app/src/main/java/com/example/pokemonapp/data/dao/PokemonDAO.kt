package com.example.pokemonapp.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.pokemonapp.data.models.Pokemon
import com.example.pokemonapp.data.models.PokemonSkillCrossRef
import com.example.pokemonapp.data.models.PokemonWithSkills
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {

    @Insert(onConflict = REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Update
    suspend fun update(pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)

    @Query("SELECT * FROM tb_pokemon")
    fun getAllPokemon() : Flow<List<Pokemon>>

    @Insert(onConflict = REPLACE)
    suspend fun insertPokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef)

    @Update
    suspend fun updatePokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef)

    @Delete
    suspend fun deletePokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef)

    @Transaction
    @Query("SELECT * FROM tb_pokemon")
    fun getPokemonsWithSkills() : Flow<List<PokemonWithSkills>>
}