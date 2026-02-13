package com.team.chord.feature.setting.storeedit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class StoreEditViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(StoreEditUiState())
    val uiState: StateFlow<StoreEditUiState> = _uiState.asStateFlow()

    fun onStoreNameChanged(name: String) {
        _uiState.update { it.copy(storeName = name) }
    }

    fun onEmployeeCountChanged(text: String) {
        if (text.isNotEmpty() && !text.all { it.isDigit() }) {
            return
        }

        _uiState.update { state ->
            if (state.ownerSolo) {
                state
            } else {
                state.copy(employeeCountInput = text)
            }
        }
    }

    fun onOwnerSoloChanged(checked: Boolean) {
        _uiState.update { state ->
            if (checked) {
                state.copy(
                    ownerSolo = true,
                    employeeCountInput = "0",
                    includeWeeklyHolidayPay = false,
                )
            } else {
                state.copy(
                    ownerSolo = false,
                    employeeCountInput = if (state.employeeCountInput == "0") "" else state.employeeCountInput,
                )
            }
        }
    }

    fun onHourlyWageChanged(text: String) {
        if (text.isNotEmpty() && !text.all { it.isDigit() }) {
            return
        }

        _uiState.update { it.copy(hourlyWageInput = text) }
    }

    fun onIncludeWeeklyHolidayPayChanged(checked: Boolean) {
        _uiState.update { state ->
            if (state.ownerSolo) {
                state.copy(includeWeeklyHolidayPay = false)
            } else {
                state.copy(includeWeeklyHolidayPay = checked)
            }
        }
    }

    fun onSubmitClicked() {
        val state = _uiState.value
        if (!state.isSubmitEnabled) return

        _uiState.update { it.copy(submitSuccess = true) }
    }

    fun onSubmitSuccessConsumed() {
        _uiState.update { it.copy(submitSuccess = false) }
    }
}
