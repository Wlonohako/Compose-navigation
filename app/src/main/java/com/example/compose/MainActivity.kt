@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: PersonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp(viewModel)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(viewModel: PersonViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(
            navController,
            drawerState = drawerState,
            scope = scope
        ) }
    ) {
        NavHost(navController, startDestination = "home") {
            composable("home") {
                Scaffold(
                    topBar = { AppTopBar { scope.launch { drawerState.open() } } }
                ) {
                    AppContent(navController, viewModel)
                }
            }
            composable("heartScreen") { backStackEntry ->
                HeartScreen(navController, viewModel)
            }
            composable("starScreen/{name}/{surname}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val surname = backStackEntry.arguments?.getString("surname") ?: ""
                StarScreen(navController, name, surname)
            }

        }
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState, scope: CoroutineScope) {
    ModalDrawerSheet {
        Text("Menu główne", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
        HorizontalDivider()
        DrawerItem("Gwiazdka", isSelected = false, navController, "starScreen//", drawerState, scope)
        DrawerItem("Serduszko", isSelected = false, navController, "heartScreen//", drawerState, scope)
    }
}

@Composable
fun DrawerItem(
    label: String,
    isSelected: Boolean = false,
    navController: NavController,
    route: String,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = isSelected,
        onClick = {
            scope.launch { drawerState.close() }
            navController.navigate(route.replace("{name}", "anon").replace("{surname}", "user"))
        },
        modifier = Modifier.padding(vertical = 8.dp)
    )
}


@Composable
fun AppTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text("Moja aplikacja", color = Color.White) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Gray)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray)
    )
}

@Composable
fun AppContent(navController: NavController, viewModel: PersonViewModel) {
//    var name by remember { mutableStateOf("") }
//    var surname by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//    var weight by remember { mutableStateOf("") }
//    var height by remember { mutableStateOf("") }
    var person by remember { mutableStateOf(PersonUiState()) }
    
    Surface(color = Color.White, modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WelcomeUI(person, onPersonChange = { person = it },viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            HeartButton(navController = navController, viewModel, person)
            StarButton(navController = navController, name = person.name, surname = person.surname
            )
        }
    }
}

@Composable
fun HeartButton(navController: NavController, viewModel: PersonViewModel, person: PersonUiState) {
    Button(
        onClick = { navController.navigate("heartScreen")
            if (person.age != null && person.weight != null && person.height != null) {
                viewModel.addData(person.name, person.surname, person.age, person.weight,
                    person.height
                ) }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Serduszko",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Przejdź do serduszka")
    }
}

@Composable
fun StarButton(navController: NavController, name: String, surname: String) {
    Button(
        onClick = {
            navController.navigate("starScreen/$name/$surname") {
                popUpTo("heartScreen") { inclusive = true }
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Gwiazdka",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Przejdź do gwiazdki")
    }
}

@Composable
fun WelcomeUI(
    person: PersonUiState,
    onPersonChange: (PersonUiState) -> Unit,
    viewModel: PersonViewModel
) {
    var greeting by remember { mutableStateOf("") }

    Text("Witaj w Compose!", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
    InputStringField(value = person.name, label = "Wpisz swoje imię", icon = Icons.Default.Person, onValueChange = { newName ->
        onPersonChange(person.copy(name = newName))
    })
    InputStringField(value = person.surname, label = "Wpisz swoje nazwisko", icon = Icons.Default.AccountBox, onValueChange = { newSurname ->
        onPersonChange(person.copy(surname = newSurname))
    })
    InputIntField(value = person.age?.toString() ?: "", label = "Wpisz swój wiek", icon = Icons.Default.Edit, onValueChange = { newAge ->
        onPersonChange(person.copy(age = newAge.toInt()))
    })
    InputIntField(value = person.weight?.toString() ?: "", label = "Wpisz swóją wagę", icon = Icons.Default.Edit, onValueChange = { newWeight ->
        onPersonChange(person.copy(weight = newWeight.toInt()))
    })
    InputIntField(value = person.height?.toString() ?: "", label = "Wpisz swój wzrost", icon = Icons.Default.Edit, onValueChange = { newHeight ->
        onPersonChange(person.copy(height = newHeight.toInt()))
    })


    Button(
        onClick = { greeting = "Cześć, ${person.name} ${person.surname}!"
            if (person.age != null && person.weight != null && person.height != null) {
            viewModel.addData(person.name, person.surname, person.age, person.weight,
                person.height
            ) } },
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(Color.Gray),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Pokaż powitanie", color = Color.White)
    }

    if (greeting.isNotBlank()) {
        Text(greeting, fontSize = 20.sp)
    }
}

@Composable
fun InputStringField(value: String, label: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
}
@Composable
fun InputIntField(value: String, label: String, icon: ImageVector, onValueChange: (String) -> Unit){
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
}