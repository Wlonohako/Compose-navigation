@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {
    private val viewModel: PersonViewModel by viewModels()
    private val genderViewModel: GenderViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp(viewModel, genderViewModel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(viewModel: PersonViewModel, genderViewModel: GenderViewModel) {


    Scaffold(
        topBar = { AppTopBar() }
    ) {
        AppContent(viewModel, genderViewModel)
    }
}


@Composable
fun AppTopBar() {
    TopAppBar(
        title = { Text("App", color = Color.White) },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray
        )
    )
}

@Composable
fun AppContent(viewModel: PersonViewModel, genderViewModel: GenderViewModel) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            Surface(
                color = Color.White, modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    WelcomeUI(uiState, viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                    NavigationButton(
                        navController,
                        "NameSexScreen",
                        Icons.Default.PlayArrow,
                        "Przejdź do ekranu 2",
                        uiState,
                    )
                    NavigationButton(
                        navController,
                        "BMIScreen",
                        Icons.AutoMirrored.Filled.ArrowForward,
                        "Przejdź do ekranu 3",
                        uiState,
                    )
                }
            }
        }
        composable("NameSexScreen") { backStackEntry ->
            NameSexScreen(viewModel, genderViewModel, navController)
        }
        composable("BMIScreen") { backStackEntry ->
            BMIScreen(viewModel, navController)
        }
    }
}

@Composable
fun NavigationButton(
    navController: NavController,
    route: String,
    icon: ImageVector,
    text: String,
    personUiState: PersonUiState,
) {
    val context = LocalContext.current
    Button(
        onClick = {
            if (isPersonValidate(personUiState, context)) {
                navController.navigate(route)
            }
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}


fun isPersonValidate(personUiState: PersonUiState, context: Context): Boolean {
    if (personUiState.name.isEmpty() || personUiState.surname.isEmpty() || personUiState.sex == null) {
        Toast.makeText(context, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
        return false
    }
    if (personUiState.age.toInt() < 10 || personUiState.age.toInt() > 100) {
        Toast.makeText(context, "Wiek musi być między 10 a 120", Toast.LENGTH_SHORT).show()
        return false
    }

    if (personUiState.weight.toInt() < 20 || personUiState.weight.toInt() > 100) {
        Toast.makeText(context, "Waga musi być między 20 a 300", Toast.LENGTH_SHORT).show()
        return false
    }
    if (personUiState.height.toInt() < 80 || personUiState.height.toInt() > 200) {
        Toast.makeText(context, "Wzrost musi być między 80 a 300", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}


@Composable
fun WelcomeUI(
    person: PersonUiState,
    viewModel: PersonViewModel
) {
    Text("Witaj!", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

    InputStringField(
        value = person.name,
        label = "Wpisz swoje imię",
        icon = Icons.Default.Person,
        onValueChange = { newName ->
            viewModel.updatePerson(name = newName)
        })

    InputStringField(
        value = person.surname,
        label = "Wpisz swoje nazwisko",
        icon = Icons.Default.AccountBox,
        onValueChange = { newSurname ->
            viewModel.updatePerson(surname = newSurname)
        })

    InputIntField(
        value = person.age,
        label = "Wpisz swój wiek",
        icon = Icons.Default.Edit,
        onValueChange = { newAge ->
            viewModel.updatePerson(age = newAge)
        }
    )

    InputIntField(
        value = person.height,
        label = "Wpisz swój wzrost",
        icon = Icons.Default.Edit,
        onValueChange = { newHeight ->
            viewModel.updatePerson(height = newHeight)
        }
    )

    InputIntField(
        value = person.weight,
        label = "Wpisz swoją wagę",
        icon = Icons.Default.Edit,
        onValueChange = { newWeight ->
            viewModel.updatePerson(weight = newWeight)
        }
    )

    SelectDropdownField(
        value = when (person.sex) {
            null -> "Brak"
            true -> "Mężczyzna"
            false -> "Kobieta"
        },
        label = "Wybierz płeć",
        icon = Icons.Default.Edit,
        options = listOf("Kobieta", "Mężczyzna"),
        onValueChange = { newSex ->
            viewModel.updatePerson(sex = newSex == "Mężczyzna")
        }
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectDropdownField(
    value: String,
    label: String,
    icon: ImageVector,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .padding(bottom = 16.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun InputStringField(
    value: String,
    label: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
fun InputIntField(
    value: String,
    label: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}