package com.example.pokemonapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.*
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "tb_trainer",
)
data class Trainer(

    @PrimaryKey(autoGenerate = true)
    val trainer_id: Int = 0,
    var name: String,
    var age: Int,
    var gender: String,
    var city: String,
)
