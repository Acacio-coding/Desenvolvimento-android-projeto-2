package com.example.pokemonapp.data.models

import androidx.room.Entity

@Entity(
    tableName = "tb_pokemon_skill_cross_ref",
    primaryKeys = ["pokemon_id", "skill_id"]
)
data class PokemonSkillCrossRef(

    val pokemon_id: Int,
    var skill_id: Int
)