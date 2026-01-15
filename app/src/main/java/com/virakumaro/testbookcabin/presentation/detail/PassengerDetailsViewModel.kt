package com.virakumaro.testbookcabin.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virakumaro.testbookcabin.core.Results
import com.virakumaro.testbookcabin.domain.model.Booking
import com.virakumaro.testbookcabin.domain.usecase.UpdatePassengerDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PassengerDetailsViewModel(
    private val updatePassengerDetailsUseCase: UpdatePassengerDetailsUseCase
) : ViewModel() {

    private val _saveState = MutableStateFlow<Results<Unit>?>(null)
    val saveState = _saveState.asStateFlow()

    var passportField by mutableStateOf("")
    var firstNameField by mutableStateOf("")
    var lastNameField by mutableStateOf("")
    var selectedGender by mutableStateOf(Gender.MALE)
    var contactNameField by mutableStateOf("")
    var contactNumberField by mutableStateOf("")
    var relationshipField by mutableStateOf("")

    var validationErrorMessage by mutableStateOf<String?>(null)

    fun saveDetails(booking: Booking) {
        val error = when {
            passportField.isBlank() -> "Passport Number is required"
            firstNameField.isBlank() -> "First Name is required"
            lastNameField.isBlank() -> "Last Name is required"
            contactNameField.isBlank() -> "Contact Name is required"
            contactNumberField.isBlank() -> "Contact Number is required"
            relationshipField.isBlank() -> "Relationship is required"
            else -> null
        }

        if (error != null) {
            validationErrorMessage = error
            return
        }

        validationErrorMessage = null
        viewModelScope.launch {
            updatePassengerDetailsUseCase(
                booking = booking,
                passport = passportField,
                firstName = firstNameField,
                lastName = lastNameField,
                gender = selectedGender.displayName,
                contactName = contactNameField,
                contactNumber = contactNumberField,
                relationship = relationshipField
            ).collect { result ->
                _saveState.value = result
            }
        }
    }

    fun setStateIdle() {
        _saveState.value = null
    }
}

enum class Gender(val displayName: String) {
    MALE("MALE"), FEMALE("FEMALE")
}