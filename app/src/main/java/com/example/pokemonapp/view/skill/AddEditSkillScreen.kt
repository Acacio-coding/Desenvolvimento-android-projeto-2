package com.example.pokemonapp.view.skill

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.pokemonapp.R
import com.example.pokemonapp.data.models.Skill

@Composable
fun AddEditSkillScreen(
    navController: NavController,
    skillViewModel: SkillViewModel,
    skill: Skill,
) {
    Form(
        navController = navController,
        skillViewModel = skillViewModel,
        skill = skill
    )
}

@Composable
fun Form(
    navController: NavController,
    skillViewModel: SkillViewModel,
    skill: Skill,
) {
    var name by remember {
        mutableStateOf(skill.name)
    }

    var type by remember {
        mutableStateOf(skill.type)
    }

    var pp by remember {
        mutableStateOf(skill.pp)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedType by remember {
        mutableStateOf(
            if (type != "") {
                type
            } else {
                ""
            }
        )
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


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

    var size by remember { mutableStateOf(Size.Zero) }

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
                    if (skill.skill_id != -1) {
                        skillViewModel.deleteSkill(skill)
                    }

                    navController.navigate("skill") {
                        popUpTo("skill") {
                            inclusive = true
                        }
                    }
                }
            ) {
                if (skill.skill_id == -1) {
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
                        if (name != "" && pp >= 5 && type != "") {
                            if (skill.skill_id == -1) {
                                skillViewModel.createSkill(name, type, pp)
                            } else {
                                skill.name = name
                                skill.type = type
                                skill.pp = pp
                                skillViewModel.updateSkill(skill)
                            }

                            navController.navigate("skill") {
                                popUpTo("skill") {
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

                value = selectedType,

                onValueChange = {
                    selectedType = it
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
                        text = "Type",
                        color = Color.White
                    )
                },

                trailingIcon = {
                    Icon(
                        icon,
                        "Skill type",
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
                typeOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            selectedType = label
                            type = label
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
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                cursorColor = Color.White,
                textColor = Color.White
            ),

            value =
                if (pp == 0)
                    ""
                else
                    "$pp",

            label = {
                Text(
                    text = "PP",
                    color = Color.White
                )
            },

            onValueChange = { newPP ->
                pp = if (newPP != "") {
                    newPP.toInt()
                } else {
                    0
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
        )
    }
}
