package com.example.pokemonapp.data.models

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.SET_NULL
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(
    tableName = "tb_pokemon",
    foreignKeys = [
        ForeignKey(
            entity = Trainer::class,
            parentColumns = arrayOf("trainer_id"),
            childColumns = arrayOf("fk_trainer"),
            onDelete = SET_NULL,
            onUpdate = CASCADE
        )
    ]
)
data class Pokemon(

    @PrimaryKey(autoGenerate = true)
    val pokemon_id: Int = 0,
    var name: String = "",
    var type: MutableList<String> = mutableListOf("", ""),
    var fk_trainer: Int? = null
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
