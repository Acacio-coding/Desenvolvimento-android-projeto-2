package com.example.pokemonapp.view.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.pokemonapp.R
import com.example.pokemonapp.data.models.Pokemon
import com.example.pokemonapp.data.models.Trainer
import com.example.pokemonapp.data.models.TrainerWithPokemons
import com.example.pokemonapp.view.pokemon.PokemonViewModel

@Composable
fun AddEditTrainerScreen(
    navController: NavController,
    trainerViewModel: TrainerViewModel,
    pokemonViewModel: PokemonViewModel,
) {
    val trainerWithPokemon by trainerViewModel.trainerWithPokemon.observeAsState(
        TrainerWithPokemons(Trainer(-1), mutableListOf(Pokemon(-1)))
    )

    trainerViewModel.changeName(trainerWithPokemon.trainer.name)
    trainerViewModel.changeAge(trainerWithPokemon.trainer.age)
    trainerViewModel.changeGender(trainerWithPokemon.trainer.gender)
    trainerViewModel.changeCity(trainerWithPokemon.trainer.city)

    if (trainerWithPokemon.trainer.name != ""
        && trainerWithPokemon.pokemons.isNotEmpty()) {
        trainerViewModel.changePokemons(trainerWithPokemon.pokemons)
    }

    Form(
        navController = navController,
        trainerViewModel = trainerViewModel,
        pokemonViewModel = pokemonViewModel,
        trainerWithPokemon = trainerWithPokemon
    )
}

@Composable
fun Form(
    navController: NavController,
    trainerViewModel: TrainerViewModel,
    pokemonViewModel: PokemonViewModel,
    trainerWithPokemon: TrainerWithPokemons
) {
    val name = trainerViewModel.name.observeAsState("")

    val age = trainerViewModel.age.observeAsState(0)

    val gender = trainerViewModel.gender.observeAsState("")

    val genderOptions = listOf("Male", "Female")

    val city = trainerViewModel.city.observeAsState("")

    var expanded by remember {
        mutableStateOf(false)
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var size by remember { mutableStateOf(Size.Zero)}

    val allPokemon by pokemonViewModel.allPokemons.observeAsState(listOf())

    val trainerPokemons = mutableListOf<Pokemon>()

    allPokemon.forEach {
        if (it.fk_trainer == trainerWithPokemon.trainer.trainer_id) {
            trainerPokemons.add(it)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Red,
                onClick = {
                    navController
                        .navigate("addtrainertopokemon")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pokemon add",
                    tint = White,
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.almost_back)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(colorResource(id = R.color.almost_back)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (trainerWithPokemon.trainer.trainer_id != -1) {
                            trainerViewModel.deleteTrainer(trainerWithPokemon.trainer)
                        }

                        navController.navigate("trainer") {
                            popUpTo("trainer") {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    if (trainerWithPokemon.trainer.trainer_id == -1) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = White
                        )
                    }
                }

                Text(
                    text = "SAVE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = White,
                    modifier = Modifier
                        .clickable {
                            if (name.value != "" && age.value > 0 && age.value < 70 && gender.value != ""
                                && city.value != ""
                            ) {
                                if (trainerWithPokemon.trainer.trainer_id == -1) {
                                    trainerViewModel.createTrainer(
                                        name.value,
                                        age.value, gender.value, city.value
                                    )
                                } else {
                                    trainerWithPokemon.trainer.name = name.value
                                    trainerWithPokemon.trainer.age = age.value
                                    trainerWithPokemon.trainer.gender = gender.value
                                    trainerWithPokemon.trainer.city = city.value
                                    trainerViewModel.updateTrainer(trainerWithPokemon.trainer)
                                }

                                navController.navigate("trainer") {
                                    popUpTo("trainer") {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                        .padding(end = 16.dp)
                )
            }

            Text(
                text = "Trainer info",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = White
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = White,
                    focusedBorderColor = White,
                    unfocusedLabelColor = White,
                    focusedLabelColor = White,
                    cursorColor = White,
                    textColor = White
                ),
                value = name.value,
                label = {
                    Text(
                        text = "Name",
                        color = White
                    )
                },
                onValueChange = { newName ->
                    trainerViewModel.changeName(newName)
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = White,
                    focusedBorderColor = White,
                    unfocusedLabelColor = White,
                    focusedLabelColor = White,
                    cursorColor = White,
                    textColor = White
                ),

                value =
                if (age.value == 0)
                    ""
                else
                    "${age.value}"
                ,

                label = {
                    Text(
                        text = "Age",
                        color = White
                    )
                },

                onValueChange = { newAge ->
                    trainerViewModel.changeAge(
                        if (newAge != "") {
                            newAge.toInt()
                        } else {
                            -1
                        }
                    )
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )

            Column {
                OutlinedTextField(
                    readOnly = true,

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = White,
                        focusedBorderColor = White,
                        unfocusedLabelColor = White,
                        focusedLabelColor = White,
                        cursorColor = White,
                        textColor = White
                    ),

                    value = gender.value,

                    onValueChange = { newGender ->
                        trainerViewModel.changeGender(newGender)
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
                            text = "Gender",
                            color = White
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
                            tint = White
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
                    genderOptions.forEach { label ->
                        DropdownMenuItem(
                            onClick = {
                                trainerViewModel.changeGender(label)
                                expanded = false
                            }
                        ) {
                            Text(text = label)
                        }
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = White,
                    focusedBorderColor = White,
                    unfocusedLabelColor = White,
                    focusedLabelColor = White,
                    cursorColor = White,
                    textColor = White
                ),
                value = city.value,
                label = {
                    Text(
                        text = "City",
                        color = White
                    )
                },
                onValueChange = { newCity ->
                    trainerViewModel.changeCity(newCity)
                }
            )

            if (trainerWithPokemon.trainer.trainer_id != -1 && trainerPokemons.isNotEmpty()) {
                Text(
                    text = "Pokemons",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                PokemonList(
                    pokemons = trainerPokemons,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    navController: NavController
) {
    LazyColumn {
        items(pokemons) { pokemon ->
            PokemonEntry(
                pokemon = pokemon,

                onPokemonDetails = {
                    navController
                        .navigate("addeditpokemon?id=${pokemon.pokemon_id}&wasFromTrainer=${true}")
                },
            )
        }
    }
}

@Composable
fun PokemonEntry(
    pokemon: Pokemon,
    onPokemonDetails: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onPokemonDetails()
            }
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Red,
                        CircleShape
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.pokemon),
                    contentDescription = stringResource(id = R.string.pokemon),
                    tint = White
                )
            }

            Text(
                text = pokemon.name,
                fontWeight = FontWeight.Bold,
                color = White,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Icon(
            modifier = Modifier.padding(end = 16.dp),
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Pokemon details",
            tint = White
        )
    }
}


