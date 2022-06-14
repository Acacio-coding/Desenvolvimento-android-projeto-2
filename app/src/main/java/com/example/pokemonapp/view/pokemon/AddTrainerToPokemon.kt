package com.example.pokemonapp.view.pokemon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.pokemonapp.R
import com.example.pokemonapp.data.models.Pokemon
import com.example.pokemonapp.view.trainer.TrainerViewModel

@Composable
fun AddTrainerToPokemonScreen(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    trainerViewModel: TrainerViewModel
) {
    Form(
        navController = navController,
        pokemonViewModel = pokemonViewModel,
        trainerViewModel = trainerViewModel
    )
}

@Composable
fun Form(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    trainerViewModel: TrainerViewModel
) {
    val trainerId by trainerViewModel.trainerId.observeAsState()

    var selectedPokemon by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var size by remember { mutableStateOf(Size.Zero)}

    val allPokemon by pokemonViewModel.allPokemons.observeAsState(listOf())

    val pokemonOptions = mutableListOf<Pokemon>()

    allPokemon.forEach {
        if (it.fk_trainer == null) {
            pokemonOptions.add(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.almost_back)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.almost_back)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate("addedittrainer?id=${trainerId}")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back",
                    tint = Color.White
                )
            }

            Text(
                text = "SAVE",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier
                    .clickable {
                        if (selectedPokemon != "" && trainerId != -1) {
                            var toUpdate = Pokemon(-1)

                            pokemonOptions.forEach {
                                if (it.name == selectedPokemon)
                                    toUpdate = it
                            }

                            if (toUpdate.pokemon_id != -1) {
                                toUpdate.fk_trainer = trainerId
                                pokemonViewModel.updatePokemon(toUpdate)
                                navController.navigate("addedittrainer?id=${trainerId}")
                            }
                        }
                    }
                    .padding(end = 16.dp)
            )
        }

        Text(
            text = "Pokemons",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )

        Column {
            OutlinedTextField(
                readOnly = true,

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),

                value = selectedPokemon,

                onValueChange = { newPokemon ->
                    selectedPokemon = newPokemon
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        size = coordinates.size.toSize()
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),

                label = {
                    Text(
                        text = "Pokemon",
                        color = Color.White
                    )
                },

                trailingIcon = {
                    Icon(
                        icon,
                        "Trainer gender",
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            },
                        tint = Color.White
                    )
                }
            )

            DropdownMenu(
                modifier = Modifier
                    .width(with(LocalDensity.current){size.width.toDp()}),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                pokemonOptions.forEach { pokemon ->
                    DropdownMenuItem(
                        onClick = {
                            selectedPokemon = pokemon.name
                            expanded = false
                        }
                    ) {
                        Text(text = pokemon.name)
                    }
                }
            }
        }
    }
}