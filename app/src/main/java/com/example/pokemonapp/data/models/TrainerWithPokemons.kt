package com.example.pokemonapp.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class TrainerWithPokemons (

    @Embedded
    val trainer: Trainer,

    @Relation(
        parentColumn = "trainer_id",
        entityColumn = "fk_trainer"
    )
    val pokemons: MutableList<Pokemon>
)
