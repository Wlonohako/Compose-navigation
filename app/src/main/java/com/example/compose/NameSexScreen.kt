package com.example.compose

import GenderViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameSexScreen(
    viewModel: PersonViewModel,
    genderViewModel: GenderViewModel,
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val genderState = genderViewModel.genderState


    LaunchedEffect(uiState.name) {
        genderViewModel.getGender(uiState.name)
    }


    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))


            when (genderState) {
                is GenderState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }

                is GenderState.Error -> {
                    Text(
                        text = "Error: ${genderState.message}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }

                is GenderState.Success -> {
                    val genderInfo = genderState.data
                    Text(
                        text = "Name: ${genderInfo.name}",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gender: ${genderInfo.gender}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Probability: ${"%.2f".format(genderInfo.probability)}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Count: ${genderInfo.count}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Person Information:",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Name: ${uiState.name}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Surname: ${uiState.surname}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Age: ${uiState.age}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Weight: ${uiState.weight}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Height: ${uiState.height}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Sex: ${uiState.sex?.let { if (it) "Male" else "Female" } ?: "Not set"}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                navController.navigate("homeScreen") {
                    viewModel.clearUiState()
                }
            }) {
                Text("Powrót do ekranu startowego")
            }
        }
    }
}
