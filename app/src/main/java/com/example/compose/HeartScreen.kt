package com.example.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeartScreen(navController: NavController, viewModel: PersonViewModel) {
    val person = viewModel.getData()
    val bmi = if (person.height != null  && person.height > 0 )   person.weight?.div(((person.height / 100.0) * (person.height / 100.0) )) else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ekran Serduszka", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Wróć", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Ikona serca",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cześć, ${person.name} ${person.surname}!", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Wiek: ${person.age}", fontSize = 18.sp)
            Text(text = "Wzrost: ${person.height} cm", fontSize = 18.sp)
            Text(text = "Waga: ${person.weight} kg", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Twoje BMI: %.2f".format(bmi), fontSize = 18.sp, color = Color.Blue)
        }
    }
}