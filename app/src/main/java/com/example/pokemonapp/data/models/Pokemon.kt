package com.example.pokemonapp.data.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.SET_NULL
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "tb_pokemon",
)
data class Pokemon(

    @PrimaryKey(autoGenerate = true)
    val pokemon_id: Int = 0,
    var name: String = "",
    var type: List<String> = listOf("", ""),
)

class TypeConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val type = object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromList(value: List<String>): String {
        return Gson().toJson(value)
    }
}
