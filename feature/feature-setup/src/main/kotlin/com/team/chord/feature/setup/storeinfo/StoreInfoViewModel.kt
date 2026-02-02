package com.team.chord.feature.setup.storeinfo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StoreInfoViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(StoreInfoUiState())
        val uiState: StateFlow<StoreInfoUiState> = _uiState.asStateFlow()

        fun onStoreNameChanged(name: String) {
            _uiState.update {
                it.copy(storeName = name)
            }
        }

        fun onStoreNameConfirmed() {
            _uiState.update {
                it.copy(screenState = StoreInfoScreenState.PostStoreName)
            }
        }

        fun onEmployeeCountChanged(text: String) {
            if (text.isNotEmpty() && !text.all { it.isDigit() }) {
                return
            }

            _uiState.update {
                if (it.ownerSolo) {
                    it.copy(employeeCountInput = "0")
                } else {
                    it.copy(employeeCountInput = text)
                }
            }
        }

        fun onIsOwnerSoloChanged(checked: Boolean) {
            _uiState.update {
                if (checked) {
                    it.copy(ownerSolo = true, employeeCountInput = "0")
                } else {
                    it.copy(ownerSolo = false)
                }
            }
        }

        fun onHourlyWageChanged(text: String) {
            if (text.isNotEmpty() && !text.all { it.isDigit() }) {
                return
            }

            _uiState.update {
                it.copy(hourlyWageInput = text)
            }
        }

        fun onIncludeWeeklyAllowanceChanged(checked: Boolean) {
            _uiState.update {
                it.copy(includeWeeklyAllowance = checked)
            }
        }

        fun onPostStoreNameNextClicked() {
            _uiState.update {
                if (it.isPostStoreNameNextEnabled) {
                    val employeeCountValue = it.employeeCountValue ?: it.employeeCount
                    it.copy(
                        employeeCount = employeeCountValue,
                        screenState = StoreInfoScreenState.Completed,
                    )
                } else {
                    it
                }
            }
        }

        fun onConfirmClicked() {
            _uiState.update {
                it.copy(isEmployeeCountBottomSheetVisible = true)
            }
        }

        fun onEmployeeCountIncrement() {
            _uiState.update {
                it.copy(employeeCount = it.employeeCount + 1)
            }
        }

        fun onEmployeeCountDecrement() {
            _uiState.update {
                if (it.employeeCount > 1) {
                    it.copy(employeeCount = it.employeeCount - 1)
                } else {
                    it
                }
            }
        }

        fun onEmployeeCountBottomSheetDismiss() {
            _uiState.update {
                it.copy(isEmployeeCountBottomSheetVisible = false)
            }
        }

        fun onCompleteClicked() {
            _uiState.update {
                it.copy(
                    isEmployeeCountBottomSheetVisible = false,
                    screenState = StoreInfoScreenState.Completed,
                )
            }
        }

        fun onBackClicked() {
            _uiState.update {
                when (it.screenState) {
                    StoreInfoScreenState.PostStoreName -> it.copy(screenState = StoreInfoScreenState.StoreNameInput)
                    else -> it
                }
            }
        }
    }
