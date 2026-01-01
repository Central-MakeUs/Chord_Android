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
                it.copy(storeName = name).updateNextEnabled()
            }
        }

        fun onLocationChanged(location: String) {
            _uiState.update {
                it.copy(location = location).updateNextEnabled()
            }
        }

        fun onEmployeeCountSelected(count: String) {
            _uiState.update {
                it
                    .copy(
                        employeeCount = count,
                        isEmployeeCountDropdownExpanded = false,
                    ).updateNextEnabled()
            }
        }

        fun onEmployeeCountDropdownToggle() {
            _uiState.update {
                it.copy(isEmployeeCountDropdownExpanded = !it.isEmployeeCountDropdownExpanded)
            }
        }

        fun onEmployeeCountDropdownDismiss() {
            _uiState.update {
                it.copy(isEmployeeCountDropdownExpanded = false)
            }
        }

        private fun StoreInfoUiState.updateNextEnabled(): StoreInfoUiState =
            copy(
                isNextEnabled =
                    storeName.isNotBlank() &&
                        location.isNotBlank() &&
                        employeeCount.isNotBlank(),
            )
    }
