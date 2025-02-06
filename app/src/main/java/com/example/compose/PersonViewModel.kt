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
)

class PersonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PersonUiState())
    val uiState: StateFlow<PersonUiState> = _uiState.asStateFlow()

    fun addData(name: String, surname: String, age: Int, weight: Int, height: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                surname = surname,
                age = age,
                weight = weight,
                height = height
            )
        }
    }

    fun getData(): PersonUiState {
        return _uiState.value
    }

}