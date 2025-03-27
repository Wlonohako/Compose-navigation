package com.example.compose

import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.getValue
import androidx.activity.viewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameSexScreen(
    viewModel: PersonViewModel,
    genderViewModel: GenderViewModel,
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()

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

            genderViewModel.getGender(uiState.name)

            Spacer(modifier = Modifier.height(32.dp))

            genderViewModel.genderInfo?.let {
                Text(
                    text = "Name: ${it.name}",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gender: ${it.gender}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Probability: ${"%.2f".format(it.probability)}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Count: ${it.count}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

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


