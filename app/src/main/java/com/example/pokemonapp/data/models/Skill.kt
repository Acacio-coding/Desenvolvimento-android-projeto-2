package com.example.pokemonapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_skill"
)
data class Skill(

    @PrimaryKey(autoGenerate = true)
    val skill_id: Int = 0,
    var name: String = "",
    var type: String = "",
    var pp: Int = 0
)
