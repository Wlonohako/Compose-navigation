package com.example.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMIScreen(viewModel: PersonViewModel, navController: NavController) {

    val uiState by viewModel.uiState.collectAsState()
    var bmiMessage by remember { mutableStateOf("") }
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


            val weight = uiState.weight.takeIf { it.isNotEmpty() }?.toIntOrNull() ?: 0
            val height = uiState.height.takeIf { it.isNotEmpty() }?.toIntOrNull()
                ?: 1

            val bmi = weight / (height / 100f * height / 100f)
            bmiMessage = generateBMIMessage(uiState.name, bmi)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = bmiMessage,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))

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

fun generateBMIMessage(name: String, bmi: Float): String {
    val formattedBMI = "%.2f".format(bmi)
    return when {
        bmi < 18.5 -> "Hej $name, jesteś poniżej normy. Twoje BMI wynosi $formattedBMI."
        bmi in 18.5..24.9 -> "Hej $name, jesteś w świetnej formie! Twoje BMI wynosi $formattedBMI."
        else -> "Hej $name, Twoje BMI wynosi $formattedBMI, warto zadbać o zdrowie!"
    }
}

