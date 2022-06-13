package com.example.pokemonapp.view.pokemon

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.pokemonapp.data.models.PokemonWithSkills

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddEditPokemonScreen(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    pokemonWithSkills: PokemonWithSkills,
) {
    Form(
        navController = navController,
        pokemonViewModel = pokemonViewModel,
        pokemonWithSkills = pokemonWithSkills
    )
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Form(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    pokemonWithSkills: PokemonWithSkills,
) {

    var name by remember {
        mutableStateOf(pokemonWithSkills.pokemon.name)
    }

    var type by remember {
        mutableStateOf(pokemonWithSkills.pokemon.type)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var expanded1 by remember {
        mutableStateOf(false)
    }

    val typeOptions = mutableListOf(
        "Normal",
        "Fighting",
        "Flying",
        "Poison",
        "Ground",
        "Rock",
        "Bug",
        "Ghost",
        "Steel",
        "Fire",
        "Water",
        "Grass",
        "Electric",
        "Psychic",
        "Ice",
        "Dragon",
        "Dark",
        "Fairy"
    )

    var type1 by remember {
        mutableStateOf(
            if (type.isNotEmpty()) {
                type[0]
            } else {
                ""
            }
        )
    }

    var type2 by remember {
        mutableStateOf(
            if (type.size > 1) {
                type[1]
            } else {
                ""
            }
        )
    }

    val selectedTypes by remember {
        mutableStateOf(mutableListOf<String>())
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon2 = if (expanded1)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var size by remember {
        mutableStateOf(Size.Zero)
    }

    var size2 by remember {
        mutableStateOf(Size.Zero)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = com.example.pokemonapp.R.color.almost_back)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(colorResource(id = com.example.pokemonapp.R.color.almost_back)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (pokemonWithSkills.pokemon.pokemon_id != -1) {
                        pokemonViewModel.deletePokemon(pokemonWithSkills.pokemon)
                    }

                    navController.navigate("pokemon") {
                        popUpTo("pokemon") {
                            inclusive = true
                        }
                    }
                }
            ) {
                if (pokemonWithSkills.pokemon.pokemon_id == -1) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back",
                        tint = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "SAVE",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier
                    .clickable {
                        if (type1 != "") {
                            selectedTypes.add(type1)
                        }

                        if (type2 != "") {
                            selectedTypes.add(type2)
                        }

                        if (selectedTypes.isNotEmpty()) {
                            type = selectedTypes
                        }

                        if (name != "" && type.isNotEmpty()) {
                            if (pokemonWithSkills.pokemon.pokemon_id == -1) {
                                pokemonViewModel.createPokemon(name, type)
                            } else {
                                pokemonWithSkills.pokemon.name = name
                                pokemonWithSkills.pokemon.type = type
                                pokemonViewModel.updatePokemon(pokemonWithSkills.pokemon)
                            }

                            navController.navigate("pokemon") {
                                popUpTo("pokemon") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    .padding(end = 16.dp)
            )
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White
            ),
            value = name,
            label = {
                Text(
                    text = "Name",
                    color = Color.White
                )
            },
            onValueChange = { newName ->
                name = newName
            }
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

                value = type1,

                onValueChange = {
                    type1 = it
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
                        text = "Type1",
                        color = Color.White
                    )
                },

                trailingIcon = {
                    Icon(
                        icon,
                        "Type 1",
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            },
                        tint = Color.White
                    )
                }
            )

            var bkp1 = ""
            DropdownMenu(
                modifier = Modifier
                    .width(with(LocalDensity.current){size.width.toDp()}),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                typeOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            if (type1 != "" && label != type1) {
                                bkp1 = type1
                            }

                            type1 = label
                            expanded = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }

            if (bkp1 != "") {
                typeOptions.add(bkp1)
                selectedTypes.remove(bkp1)
            }

            typeOptions.removeIf { it == type1 && type1 != "" }
        }

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

                value = type2,

                onValueChange = {
                    type2 = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        size2 = coordinates.size.toSize()
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp),

                label = {
                    Text(
                        text = "Type2",
                        color = Color.White
                    )
                },

                trailingIcon = {
                    Icon(
                        icon2,
                        "Type 2",
                        modifier = Modifier
                            .clickable {
                                expanded1 = !expanded1
                            },
                        tint = Color.White
                    )
                }
            )

            var bkp2 = ""
            DropdownMenu(
                modifier = Modifier
                    .width(with(LocalDensity.current){size2.width.toDp()}),
                expanded = expanded1,
                onDismissRequest = {
                    expanded1 = false
                }
            ) {
                typeOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            if (type2 != "" && label != type2) {
                                bkp2 = type2
                            }

                            type2 = label
                            expanded1 = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }

            if (bkp2 != "") {
                typeOptions.add(bkp2)
                selectedTypes.remove(bkp2)
            }

            typeOptions.removeIf { it == type2 && type2 != "" }
        }
    }
}
