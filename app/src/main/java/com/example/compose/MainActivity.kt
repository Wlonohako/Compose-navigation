@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {


    Scaffold(
        topBar = { AppTopBar() }
    ) {
        AppContent()
    }
}


@Composable
fun AppTopBar() {
    TopAppBar(
        title = { Text("Scam", color = Color.White) },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray
        )
    )
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    var person by remember { mutableStateOf(PersonUiState()) }

    Surface(
        color = Color.White, modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WelcomeUI(person, onPersonChange = { person = it })
            Spacer(modifier = Modifier.height(16.dp))
            NavigationButton(
                navController,
                "",
                Icons.Default.Person,
                "Sprawdź płeć swojego imienia",
                person
            )
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
    if (personUiState.name.isEmpty() || personUiState.surname.isEmpty() || personUiState.age == null || personUiState.weight == null || personUiState.height == null || personUiState.sex == null) {
        Toast.makeText(context, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
        return false
    }
    if (personUiState.age < 0 || personUiState.age > 120) {
        Toast.makeText(context, "Wiek musi być między 0 a 120", Toast.LENGTH_SHORT).show()
        return false
    }

    if (personUiState.weight < 0 || personUiState.weight > 300) {
        Toast.makeText(context, "Waga musi być między 0 a 300", Toast.LENGTH_SHORT).show()
        return false
    }
    if (personUiState.height < 0 || personUiState.height > 300) {
        Toast.makeText(context, "Wzrost musi być między 0 a 300", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}


@Composable
fun WelcomeUI(
    person: PersonUiState,
    onPersonChange: (PersonUiState) -> Unit,
) {

    Text("Witaj!", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
    InputStringField(
        value = person.name,
        label = "Wpisz swoje imię",
        icon = Icons.Default.Person,
        onValueChange = { newName ->
            onPersonChange(person.copy(name = newName))
        })
    InputStringField(
        value = person.surname,
        label = "Wpisz swoje nazwisko",
        icon = Icons.Default.AccountBox,
        onValueChange = { newSurname ->
            onPersonChange(person.copy(surname = newSurname))
        })
    InputIntField(
        value = person.age?.toString() ?: "",
        label = "Wpisz swój wiek",
        icon = Icons.Default.Edit,
        onValueChange = { newAge ->
            onPersonChange(person.copy(age = newAge.toInt()))
        })
    InputIntField(
        value = person.height?.toString() ?: "",
        label = "Wpisz swój wzrost",
        icon = Icons.Default.Edit,
        onValueChange = { newHeight ->
            onPersonChange(person.copy(height = newHeight.toInt()))
        })
    InputIntField(
        value = person.weight?.toString() ?: "",
        label = "Wpisz swóją wagę",
        icon = Icons.Default.Edit,
        onValueChange = { newWeight ->
            onPersonChange(person.copy(weight = newWeight.toInt()))
        })
    SelectDropdownField(
        value = (if (person.sex == null) "Brak" else if (person.sex) "Mężczyzna" else "Kobieta"),
        label = "Wybierz płeć",
        icon = Icons.Default.Edit,
        options = listOf("Kobieta", "Mężczyzna"),
        onValueChange = { newSex -> onPersonChange(person.copy(sex = if (newSex == "Kobieta") false else true)) }
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