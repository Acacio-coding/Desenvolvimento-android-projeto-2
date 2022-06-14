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
import com.example.pokemonapp.data.models.Pokemon
import com.example.pokemonapp.data.models.PokemonSkillCrossRef
import com.example.pokemonapp.data.models.PokemonWithSkills
import com.example.pokemonapp.data.models.Skill
import com.example.pokemonapp.view.skill.SkillViewModel
import com.example.pokemonapp.view.trainer.TrainerViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddEditPokemonScreen(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    skillViewModel: SkillViewModel,
    trainerViewModel: TrainerViewModel,
    wasFromTrainer: Boolean
) {
    val pokemonWithSkills by pokemonViewModel.pokemonWithSkills.observeAsState(
        PokemonWithSkills(Pokemon(), mutableListOf())
    )

    pokemonViewModel.changeName(pokemonWithSkills.pokemon.name)
    pokemonViewModel.changeType(pokemonWithSkills.pokemon.type)

    if (pokemonWithSkills.pokemon.name != "" && pokemonWithSkills.skills.isNotEmpty()) {
        pokemonViewModel.changeSkills(pokemonWithSkills.skills)
    } else {
        pokemonViewModel.changeSkills(mutableListOf(
            Skill(-1), Skill(-1), Skill(-1), Skill(-1)))
    }

    Form(
        navController = navController,
        pokemonViewModel = pokemonViewModel,
        skillViewModel =  skillViewModel,
        pokemonWithSkills = pokemonWithSkills,
        trainerViewModel = trainerViewModel,
        wasFromTrainer = wasFromTrainer
    )
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Form(
    navController: NavController,
    pokemonViewModel: PokemonViewModel,
    skillViewModel: SkillViewModel,
    pokemonWithSkills: PokemonWithSkills,
    trainerViewModel: TrainerViewModel,
    wasFromTrainer: Boolean
) {
    val trainerId by trainerViewModel.trainerId.observeAsState()

    val name = pokemonViewModel.name.observeAsState("")

    val type = pokemonViewModel.type.observeAsState(mutableListOf("", ""))

    val skills = pokemonViewModel.skills.observeAsState(mutableListOf(
        Skill(-1), Skill(-1), Skill(-1), Skill(-1)))

    val allSkills = skillViewModel.allSkills.observeAsState(mutableListOf()).value

    val skillsOptions = mutableListOf<Skill>()

    allSkills.forEach { skill ->
        skillsOptions.add(skill)
    }

    val crossRef by pokemonViewModel.crossRef.observeAsState(
        mutableListOf(PokemonSkillCrossRef(-1, -1))
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    var expanded1 by remember {
        mutableStateOf(false)
    }

    var expanded2 by remember {
        mutableStateOf(false)
    }

    var expanded3 by remember {
        mutableStateOf(false)
    }

    var expanded4 by remember {
        mutableStateOf(false)
    }

    var expanded5 by remember {
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

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon2 = if (expanded1)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon3 = if (expanded2)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon4 = if (expanded3)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon5 = if (expanded4)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val icon6 = if (expanded5)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    var size by remember {
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
                        if (crossRef.isNotEmpty()) {
                            crossRef.forEach {
                                pokemonViewModel.deletePokemonCrossRef(it)
                            }
                        }

                        pokemonViewModel.deletePokemon(pokemonWithSkills.pokemon)
                    }

                    if (!wasFromTrainer) {
                        navController.navigate("pokemons") {
                            popUpTo("pokemons") {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate("addedittrainer?id=${trainerId}")
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
                        if (name.value != "" && type.value.isNotEmpty()) {
                            if (pokemonWithSkills.pokemon.pokemon_id == -1) {
                                pokemonViewModel.createPokemon(name.value, type.value)

                                if (!wasFromTrainer) {
                                    navController.navigate("pokemons") {
                                        popUpTo("pokemons") {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    navController.navigate("addedittrainer?id=${trainerId}")
                                }
                            } else {
                                Log.d("Teste", skills.value.toString())
                                if (skills.value.size == 4 &&
                                    !skills.value.contains(Skill(-1))) {
                                    pokemonWithSkills.pokemon.name = name.value
                                    pokemonWithSkills.pokemon.type = type.value
                                    pokemonViewModel.updatePokemon(pokemonWithSkills.pokemon)

                                    if (crossRef.isNotEmpty()) {
                                        crossRef.forEach {
                                            pokemonViewModel.deletePokemonCrossRef(it)
                                        }
                                    }

                                    skills.value.forEach {
                                        pokemonViewModel
                                            .createPokemonCrossRef(
                                                pokemonWithSkills.pokemon.pokemon_id,
                                                it.skill_id
                                            )
                                    }

                                    if (!wasFromTrainer) {
                                        navController.navigate("pokemons") {
                                            popUpTo("pokemons") {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        navController.navigate("addedittrainer?id=${trainerId}")
                                    }
                                }
                            }
                        }
                    }
                    .padding(end = 16.dp)
            )
        }

        Text(
            text = "Pokemon info",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )

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
            value = name.value,

            label = {
                Text(
                    text = "Name",
                    color = Color.White
                )
            },

            onValueChange = { newName ->
                pokemonViewModel.changeName(newName)
            }
        )

        //Type1
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

                value = type.value[0],

                onValueChange = {
                    type.value[0] = it
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
                    .width(with(LocalDensity.current) { size.width.toDp() }),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                typeOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            if (type.value[0] != "" && label != type.value[0]) {
                                bkp1 = type.value[0]
                            }

                            type.value[0] = label
                            expanded = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }

            if (bkp1 != "") {
                typeOptions.add(bkp1)
            }

            typeOptions.removeIf { it == type.value[0] && type.value[0] != "" }
        }


        //Type2
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

                value = type.value[1],

                onValueChange = {
                    type.value[1] = it
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
                    .width(with(LocalDensity.current) { size.width.toDp() }),
                expanded = expanded1,
                onDismissRequest = {
                    expanded1 = false
                }
            ) {
                typeOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            if (type.value[1] != "" && label != type.value[1]) {
                                bkp2 = type.value[1]
                            }

                            type.value[1] = label
                            expanded1 = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }

            if (bkp2 != "") {
                typeOptions.add(bkp2)
            }

            typeOptions.removeIf { it == type.value[1] && type.value[1] != "" }
        }

        if (pokemonWithSkills.pokemon.pokemon_id != -1) {
            Text(
                text = "Skills",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )


            //Skill1
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

                    value = skills.value[0].name,

                    onValueChange = {
                        skills.value[0] = allSkills.find { current ->
                            current.name == it
                        } ?: Skill(-1)
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
                            text = "Skill 1",
                            color = Color.White
                        )
                    },

                    trailingIcon = {
                        Icon(
                            icon3,
                            "Skill 1",
                            modifier = Modifier
                                .clickable {
                                    expanded2 = !expanded2
                                },
                            tint = Color.White
                        )
                    }
                )

                var bkp1 = Skill(-1)
                DropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { size.width.toDp() }),
                    expanded = expanded2,
                    onDismissRequest = {
                        expanded2 = false
                    }
                ) {
                    skillsOptions.forEach { skill ->
                        DropdownMenuItem(
                            onClick = {
                                if (skills.value[0].skill_id != -1
                                    && skill != skills.value[0]
                                ) {
                                    bkp1 = skills.value[0]
                                }

                                skills.value[0] = skill
                                expanded2 = false
                            }
                        ) {
                            Text(text = skill.name)
                        }
                    }
                }

                if (bkp1 != Skill(-1)) {
                    skillsOptions.add(bkp1)
                }

                skillsOptions.removeIf {
                    it == skills.value[0]
                            && skills.value[0].name != ""
                }
            }

            //Skill2
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

                    value = skills.value[1].name,

                    onValueChange = {
                        skills.value[1] = allSkills.find { current ->
                            current.name == it
                        } ?: Skill(-1)
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
                            text = "Skill 2",
                            color = Color.White
                        )
                    },

                    trailingIcon = {
                        Icon(
                            icon4,
                            "Skill 2",
                            modifier = Modifier
                                .clickable {
                                    expanded3 = !expanded3
                                },
                            tint = Color.White
                        )
                    }
                )

                var bkp2 = Skill(-1)
                DropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { size.width.toDp() }),
                    expanded = expanded3,
                    onDismissRequest = {
                        expanded3 = false
                    }
                ) {
                    skillsOptions.forEach { skill ->
                        DropdownMenuItem(
                            onClick = {
                                if (skills.value[1].skill_id != -1
                                    && skill != skills.value[1]
                                ) {
                                    bkp2 = skills.value[1]
                                }

                                skills.value[1] = skill
                                expanded3 = false
                            }
                        ) {
                            Text(text = skill.name)
                        }
                    }
                }

                if (bkp2 != Skill(-1)) {
                    skillsOptions.add(bkp2)
                }

                skillsOptions.removeIf {
                    it == skills.value[1]
                            && skills.value[1].name != ""
                }
            }

            //Skill3
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

                    value = skills.value[2].name,

                    onValueChange = {
                        skills.value[2] = allSkills.find { current ->
                            current.name == it
                        } ?: Skill(-1)
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
                            text = "Skill 3",
                            color = Color.White
                        )
                    },

                    trailingIcon = {
                        Icon(
                            icon5,
                            "Skill 3",
                            modifier = Modifier
                                .clickable {
                                    expanded4 = !expanded4
                                },
                            tint = Color.White
                        )
                    }
                )

                var bkp3 = Skill(-1)
                DropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { size.width.toDp() }),
                    expanded = expanded4,
                    onDismissRequest = {
                        expanded4 = false
                    }
                ) {
                    skillsOptions.forEach { skill ->
                        DropdownMenuItem(
                            onClick = {
                                if (skills.value[2].skill_id != -1
                                    && skill != skills.value[2]
                                ) {
                                    bkp3 = skills.value[2]
                                }

                                skills.value[2] = skill
                                expanded4 = false
                            }
                        ) {
                            Text(text = skill.name)
                        }
                    }
                }

                if (bkp3 != Skill(-1)) {
                    skillsOptions.add(bkp3)
                }

                skillsOptions.removeIf {
                    it == skills.value[2]
                            && skills.value[2].name != ""
                }
            }

            //Skill4
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

                    value = skills.value[3].name,

                    onValueChange = {
                        skills.value[3] = allSkills.find { current ->
                            current.name == it
                        } ?: Skill(-1)
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
                            text = "Skill 4",
                            color = Color.White
                        )
                    },

                    trailingIcon = {
                        Icon(
                            icon6,
                            "Skill 4",
                            modifier = Modifier
                                .clickable {
                                    expanded5 = !expanded5
                                },
                            tint = Color.White
                        )
                    }
                )

                var bkp4 = Skill(-1)
                DropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { size.width.toDp() }),
                    expanded = expanded5,
                    onDismissRequest = {
                        expanded5 = false
                    }
                ) {
                    skillsOptions.forEach { skill ->
                        DropdownMenuItem(
                            onClick = {
                                if (skills.value[3].skill_id != -1
                                    && skill != skills.value[3]
                                ) {
                                    bkp4 = skills.value[3]
                                }

                                skills.value[3] = skill
                                expanded5 = false
                            }
                        ) {
                            Text(text = skill.name)
                        }
                    }
                }

                if (bkp4 != Skill(-1)) {
                    skillsOptions.add(bkp4)
                }

                skillsOptions.removeIf {
                    it == skills.value[3]
                            && skills.value[3].name != ""
                }
            }
        }
    }
}



