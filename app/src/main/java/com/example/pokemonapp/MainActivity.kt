package com.example.pokemonapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapp.ui.theme.PokemonAppTheme
import com.example.pokemonapp.view.pokemon.AddEditPokemonScreen
import com.example.pokemonapp.view.pokemon.PokemonListScreen
import com.example.pokemonapp.view.pokemon.PokemonViewModel
import com.example.pokemonapp.view.pokemon.PokemonViewModelFactory
import com.example.pokemonapp.view.skill.AddEditSkillScreen
import com.example.pokemonapp.view.skill.SkillListScreen
import com.example.pokemonapp.view.skill.SkillViewModel
import com.example.pokemonapp.view.skill.SkillViewModelFactory
import com.example.pokemonapp.view.trainer.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val trainerViewModel: TrainerViewModel by viewModels {
            TrainerViewModelFactory(
                (this.applicationContext as PokemonApplication).database.trainerDao()
            )
        }

        val pokemonViewModel: PokemonViewModel by viewModels {
            PokemonViewModelFactory(
                (this.applicationContext as PokemonApplication).database.pokemonDao()
            )
        }

        val skillViewModel: SkillViewModel by viewModels {
            SkillViewModelFactory(
                (this.applicationContext as PokemonApplication).database.skillDao()
            )
        }

        setContent {
            PokemonAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    App(
                        trainerViewModel = trainerViewModel,
                        pokemonViewModel = pokemonViewModel,
                        skillViewModel = skillViewModel,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun App(
    trainerViewModel: TrainerViewModel,
    pokemonViewModel: PokemonViewModel,
    skillViewModel: SkillViewModel,
) {
    val navController = rememberNavController()

    //Código para inserir dados iniciais no banco de dados
    //loadInitialData(trainerViewModel, pokemonViewModel, skillViewModel)

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(60.dp),
                backgroundColor = colorResource(id = R.color.almost_back),
                elevation = 0.dp,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                BottomNavScreens.forEach { screen ->
                    val isCurrent = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true

                    BottomNavigationItem(
                        icon =  {
                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Color.Black,
                                            CircleShape
                                        )
                                        .padding(10.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(28.dp),
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = stringResource(id = screen.name),
                                        tint = Color.White
                                    )
                                }
                            } else {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = screen.icon),
                                    contentDescription = stringResource(id = screen.name),
                                    tint = Color.White
                                )
                            }
                        },

                        selected = isCurrent,

                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Screen.TrainerScreen.route
        ) {
            composable(route = Screen.TrainerScreen.route) {
                TrainerListScreen(
                    trainerViewModel = trainerViewModel,
                    navController = navController)
            }

            composable(
                route = "addedittrainer?id={id}",
                arguments = listOf(navArgument("id") {
                    defaultValue = -1
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getInt("id") ?: -1
                val trainer = trainerViewModel.getTrainer(id)

                AddEditTrainerScreen(
                    navController = navController,
                    trainerViewModel = trainerViewModel,
                    trainer = trainer
                )
            }

            composable(route = Screen.PokemonScreen.route) {
                PokemonListScreen(
                    navController = navController,
                    pokemonViewModel = pokemonViewModel
                )
            }

            composable(
                route = "addeditpokemon?id={id}",
                arguments = listOf(navArgument("id") {
                    defaultValue = -1
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getInt("id") ?: -1
                val pokemonWithSkills = pokemonViewModel.getPokemon(id)

                AddEditPokemonScreen(
                    navController = navController,
                    pokemonViewModel = pokemonViewModel,
                    pokemonWithSkills = pokemonWithSkills
                )
            }

            composable(route = Screen.SkillScreen.route) {
                SkillListScreen(
                    navController = navController,
                    skillViewModel = skillViewModel
                )
            }

            composable(
                route = "addeditskill?id={id}",
                arguments = listOf(navArgument("id") {
                    defaultValue = -1
                    type = NavType.IntType
                })
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getInt("id") ?: -1
                val skill = skillViewModel.getSkill(id)

                AddEditSkillScreen(
                    navController = navController,
                    skillViewModel =  skillViewModel,
                    skill = skill
                )
            }
        }
    }
}

private val BottomNavScreens = listOf(
    Screen.TrainerScreen,
    Screen.PokemonScreen,
    Screen.SkillScreen,
)

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val name: Int
) {
    object TrainerScreen: Screen("trainer", R.drawable.trainer, R.string.trainer)
    object PokemonScreen: Screen("pokemon", R.drawable.pokemon, R.string.pokemon)
    object SkillScreen: Screen("skill", R.drawable.skill, R.string.skill)
}

private fun loadInitialData(
    trainerViewModel: TrainerViewModel,
    pokemonViewModel: PokemonViewModel,
    skillViewModel: SkillViewModel,
) {
    trainerViewModel.createTrainer("Red", 14, "Male", "Pallet")
    trainerViewModel.createTrainer("Blue", 14, "Male", "Pallet")
    trainerViewModel.createTrainer("Ash Ketchum", 13, "Male", "Pallet")
    trainerViewModel.createTrainer("Brock", 16, "Male", "Pewter")
    trainerViewModel.createTrainer("Misty", 10, "Female", "Cerulean")
    trainerViewModel.createTrainer("Cynthia", 17, "Female", "Celestic")

    skillViewModel.createSkill("Scratch", "Normal", 30)
    skillViewModel.createSkill("Tackle", "Normal", 20)
    skillViewModel.createSkill("Growl", "Normal", 15)
    skillViewModel.createSkill("Vine Whip", "Grass", 20)
    skillViewModel.createSkill("Ember", "Fire", 20)
    skillViewModel.createSkill("Bubble", "Water", 20)

    skillViewModel.createSkill("Leech Seed", "Grass", 20)
    skillViewModel.createSkill("Dragon Rage", "Dragon", 20)
    skillViewModel.createSkill("Water Gun", "Water", 20)

    skillViewModel.createSkill("Razor Leaf", "Grass", 25)
    skillViewModel.createSkill("Body Slam", "Normal", 15)
    skillViewModel.createSkill("Sleep Powder", "Grass", 15)
    skillViewModel.createSkill("Toxic", "Poison", 10)

    skillViewModel.createSkill("Fire Spin", "Fire", 15)
    skillViewModel.createSkill("Fire Blast", "Fire", 5)
    skillViewModel.createSkill("Earthquake", "Ground", 10)
    skillViewModel.createSkill("Swords Dance", "Normal", 30)

    skillViewModel.createSkill("Surf", "Water", 5)
    skillViewModel.createSkill("Hydro Pump", "Water", 5)
    skillViewModel.createSkill("Ice Beam", "Ice", 10)
    skillViewModel.createSkill("Rest", "Psychic", 10)

    pokemonViewModel.createPokemon("Bulbasaur", listOf("Grass"))
    pokemonViewModel.createPokemonCrossRef(1, 1)
    pokemonViewModel.createPokemonCrossRef(1, 3)
    pokemonViewModel.createPokemonCrossRef(1, 4)

    pokemonViewModel.createPokemon("Ivysaur", listOf("Grass"))
    pokemonViewModel.createPokemonCrossRef(2, 1)
    pokemonViewModel.createPokemonCrossRef(2, 3)
    pokemonViewModel.createPokemonCrossRef(2, 4)
    pokemonViewModel.createPokemonCrossRef(2, 7)

    pokemonViewModel.createPokemon("Venusaur", listOf("Grass", "Poison"))
    pokemonViewModel.createPokemonCrossRef(3, 10)
    pokemonViewModel.createPokemonCrossRef(3, 11)
    pokemonViewModel.createPokemonCrossRef(3, 12)
    pokemonViewModel.createPokemonCrossRef(3, 13)

    pokemonViewModel.createPokemon("Charmander", type = listOf("Fire"))
    pokemonViewModel.createPokemonCrossRef(4, 1)
    pokemonViewModel.createPokemonCrossRef(4, 3)
    pokemonViewModel.createPokemonCrossRef(4, 5)

    pokemonViewModel.createPokemon("Charmeleon", type = listOf("Fire"))
    pokemonViewModel.createPokemonCrossRef(5, 1)
    pokemonViewModel.createPokemonCrossRef(5, 3)
    pokemonViewModel.createPokemonCrossRef(5, 5)
    pokemonViewModel.createPokemonCrossRef(5, 8)

    pokemonViewModel.createPokemon("Charizard", type = listOf("Fire", "Flying"))
    pokemonViewModel.createPokemonCrossRef(6, 14)
    pokemonViewModel.createPokemonCrossRef(6, 15)
    pokemonViewModel.createPokemonCrossRef(6, 16)
    pokemonViewModel.createPokemonCrossRef(6, 17)

    pokemonViewModel.createPokemon("Squirtle", type = listOf("Water"))
    pokemonViewModel.createPokemonCrossRef(7, 1)
    pokemonViewModel.createPokemonCrossRef(7, 3)
    pokemonViewModel.createPokemonCrossRef(7, 6)

    pokemonViewModel.createPokemon("Wartortle", type = listOf("Water"))
    pokemonViewModel.createPokemonCrossRef(8, 1)
    pokemonViewModel.createPokemonCrossRef(8, 3)
    pokemonViewModel.createPokemonCrossRef(8, 6)
    pokemonViewModel.createPokemonCrossRef(8, 9)

    pokemonViewModel.createPokemon("Blastoise", type = listOf("Water"))
    pokemonViewModel.createPokemonCrossRef(9, 18)
    pokemonViewModel.createPokemonCrossRef(9, 19)
    pokemonViewModel.createPokemonCrossRef(9, 20)
    pokemonViewModel.createPokemonCrossRef(9, 21)
}
