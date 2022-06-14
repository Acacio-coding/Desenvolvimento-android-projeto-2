package com.example.pokemonapp.view.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokemonapp.R
import com.example.pokemonapp.data.models.Pokemon

@Composable
fun PokemonListScreen(
    pokemonViewModel: PokemonViewModel,
    navController: NavController
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Color.Red,
                onClick = {
                    navController.navigate("addeditpokemon")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pokemon add",
                    tint = Color.White,
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.almost_back))
                .padding(horizontal = 4.dp, vertical = 4.dp),
        ) {
            val pokemons by pokemonViewModel.allPokemons.observeAsState(listOf())

            PokemonList(
                pokemons = pokemons,
                navController = navController,
            )
        }
    }
}


@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    navController: NavController,
) {
    LazyColumn {
        items(pokemons) { pokemon ->
            PokemonEntry(pokemon = pokemon) {
                navController.navigate("addeditpokemon?id=${pokemon.pokemon_id}")
            }
        }
    }
}

@Composable
fun PokemonEntry(
    pokemon: Pokemon,
    onDetails: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onDetails()
            }
            .padding(vertical = 4.dp)
            .background(Color.Black),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 10.dp, top = 11.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color.Red,
                        CircleShape
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(id = R.drawable.pokemon),
                    contentDescription = stringResource(id = R.string.pokemon),
                    tint = Color.White
                )
            }

            Text(
                text = pokemon.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Icon(
            modifier = Modifier.padding(end = 16.dp),
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Pokemon details",
            tint = Color.White
        )
    }
}