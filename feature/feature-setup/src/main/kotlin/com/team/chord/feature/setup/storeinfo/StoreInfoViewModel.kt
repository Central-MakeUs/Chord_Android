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

        fun onLocationChanged(location: String) {
            _uiState.update {
                it.copy(location = location).updateConfirmEnabled()
            }
        }

        fun onLocationSelected(location: String) {
            _uiState.update {
                it.copy(
                    location = location,
                    screenState = StoreInfoScreenState.Confirmation,
                ).updateConfirmEnabled()
            }
        }

        fun onStoreNameConfirmed() {
            _uiState.update {
                it.copy(screenState = StoreInfoScreenState.LocationInput)
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
                    StoreInfoScreenState.LocationInput -> it.copy(screenState = StoreInfoScreenState.StoreNameInput)
                    StoreInfoScreenState.Confirmation -> it.copy(screenState = StoreInfoScreenState.LocationInput)
                    else -> it
                }
            }
        }

        private fun StoreInfoUiState.updateConfirmEnabled(): StoreInfoUiState =
            copy(
                isConfirmEnabled = storeName.isNotBlank() && location.isNotBlank(),
            )
    }
