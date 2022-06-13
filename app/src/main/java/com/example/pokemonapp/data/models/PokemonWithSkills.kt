package com.example.pokemonapp.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonWithSkills(

    @Embedded
    val pokemon: Pokemon,

    @Relation(
        parentColumn = "pokemon_id",
        entityColumn = "skill_id",
        associateBy = Junction(PokemonSkillCrossRef::class)
    )
    val skills: List<Skill>
)

