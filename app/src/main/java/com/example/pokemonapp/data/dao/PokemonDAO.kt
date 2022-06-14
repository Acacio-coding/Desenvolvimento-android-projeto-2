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

    @Delete
    suspend fun deletePokemonCrossRef(pokemonSkillCrossRef: PokemonSkillCrossRef)

    @Query("SELECT * FROM tb_pokemon_skill_cross_ref WHERE pokemon_id = :id")
    fun getPokemonCrossRef(id: Int) : Flow<MutableList<PokemonSkillCrossRef>>

    @Transaction
    @Query("SELECT * FROM tb_pokemon WHERE pokemon_id = :id")
    fun getPokemonWithSkills(id: Int) : Flow<PokemonWithSkills>
}