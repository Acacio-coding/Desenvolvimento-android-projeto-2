package com.example.pokemonapp.view.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.pokemonapp.R
import com.example.pokemonapp.data.models.Trainer

@Composable
fun AddEditTrainerScreen(
    navController: NavController,
    trainerViewModel: TrainerViewModel,
    trainer: Trainer,
) {
    Form(
        navController = navController,
        trainerViewModel = trainerViewModel,
        trainer = trainer
    )
}

@Composable
fun Form(
    navController: NavController,
    trainerViewModel: TrainerViewModel,
    trainer: Trainer
) {
    var name by remember {
        mutableStateOf(trainer.name)
    }

    var age by remember {
        mutableStateOf(trainer.age)
    }

    var gender by remember {
        mutableStateOf(trainer.gender)
    }

    var city by remember {
        mutableStateOf(trainer.city)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var selectedGender by remember {
        mutableStateOf(
            if (gender == "Male" || gender == "Female") {
                gender
            } else {
                ""
            }
        )
    }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    val genderOptions = listOf("Male", "Female")

    var size by remember { mutableStateOf(Size.Zero)}

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
                    if (trainer.trainer_id != -1) {
                        trainerViewModel.deleteTrainer(trainer)
                    }

                    navController.navigate("trainer") {
                        popUpTo("trainer") {
                            inclusive = true
                        }
                    }
                }
            ) {
                if (trainer.trainer_id == -1) {
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
                        if (name != "" && age > 0 && gender != "" && city != "") {
                            if (trainer.trainer_id == -1) {
                                trainerViewModel.createTrainer(name, age, gender, city)
                            } else {
                                trainer.name = name
                                trainer.age = age
                                trainer.gender = gender
                                trainer.city = city
                                trainerViewModel.updateTrainer(trainer)
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
            value = name,
            label = {
                Text(
                    text = "Name",
                    color = White
                )
            },
            onValueChange = { newName ->
                name = newName
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
            if (age == -1)
                ""
            else
                "$age"
            ,

            label = {
                Text(
                    text = "Age",
                    color = White
                )
            },

            onValueChange = { newAge ->
                age = if (newAge != "") {
                    newAge.toInt()
                } else {
                    -1
                }
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

                value = selectedGender,

                onValueChange = {
                    selectedGender = it
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
                            selectedGender = label
                            gender = label
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
            value = city,
            label = {
                Text(
                    text = "City",
                    color = White
                )
            },
            onValueChange = { newCity ->
                city = newCity
            }
        )

        if (trainer.trainer_id != -1) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 2.dp)
                    .size(55.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                onClick = {
                    navController.navigate("trainer") {
                        popUpTo("trainer") {
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pokemon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Owned pokemon",
                    tint = White
                )
            }
        }
    }
}
