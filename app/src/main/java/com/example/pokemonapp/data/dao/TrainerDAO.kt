package com.example.pokemonapp.data.dao

import androidx.room.*
import com.example.pokemonapp.data.models.Trainer
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trainer: Trainer)

    @Update
    suspend fun update(trainer: Trainer)

    @Delete
    suspend fun delete(trainer: Trainer)

    @Query("SELECT * FROM tb_trainer")
    fun getAllTrainers() : Flow<List<Trainer>>
}