package com.example.pokemonapp.data.dao

import androidx.room.*
import com.example.pokemonapp.data.models.Skill
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(skill: Skill)

    @Update
    suspend fun update(skill: Skill)

    @Delete
    suspend fun delete(skill: Skill)

    @Query("SELECT * FROM tb_skill")
    fun getAllSkills() : Flow<MutableList<Skill>>
}