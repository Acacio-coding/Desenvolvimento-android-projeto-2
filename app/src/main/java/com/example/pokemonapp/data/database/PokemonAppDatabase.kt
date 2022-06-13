package com.example.pokemonapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonapp.data.dao.*
import com.example.pokemonapp.data.models.*

@Database(
    entities = [Trainer::class, Pokemon::class, Skill::class, PokemonSkillCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class PokemonAppDatabase : RoomDatabase() {

    abstract fun trainerDao(): TrainerDAO
    abstract fun pokemonDao(): PokemonDAO
    abstract fun skillDao(): SkillDAO

    companion object {
        @Volatile
        private var INSTANCE: PokemonAppDatabase? = null

        fun getInstance(context: Context) : PokemonAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonAppDatabase::class.java,
                    "pokemonApp_db"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance

                return instance
            }
        }
    }
}