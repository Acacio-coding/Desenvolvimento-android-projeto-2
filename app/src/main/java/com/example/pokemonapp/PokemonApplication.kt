package com.example.pokemonapp

import android.app.Application
import com.example.pokemonapp.data.database.PokemonAppDatabase

class PokemonApplication : Application() {

    val database: PokemonAppDatabase by lazy {
        PokemonAppDatabase.getInstance(this)
    }
}