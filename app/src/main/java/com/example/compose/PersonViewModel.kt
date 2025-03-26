package com.example.compose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PersonUiState(
    val name: String = "",
    val surname: String = "",
    val age: Int? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val sex: Boolean? = null
)

class PersonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PersonUiState())
    val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

    fun updatePerson(
        name: String? = null,
        surname: String? = null,
        age: Int? = null,
        weight: Int? = null,
        height: Int? = null,
        sex: Boolean? = null
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name ?: currentState.name,
                surname = surname ?: currentState.surname,
                age = age ?: currentState.age,
                weight = weight ?: currentState.weight,
                height = height ?: currentState.height,
                sex = sex ?: currentState.sex
            )
        }
    }
}
