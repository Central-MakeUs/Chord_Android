package com.team.chord.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.usecase.user.GetStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val getStoreUseCase: GetStoreUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SettingUiState())
        val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

        init {
            loadStoreInfo()
        }

        fun refreshStoreInfo() {
            loadStoreInfo()
        }

        private fun loadStoreInfo() {
            viewModelScope.launch {
                when (val result = getStoreUseCase()) {
                    is Result.Success -> {
                        val store = result.data
                        _uiState.update {
                            it.copy(
                                storeName = store.name,
                                employeeCount = store.employees,
                                laborCost = formatCurrency(store.laborCost),
                            )
                        }
                    }

                    is Result.Error,
                    Result.Loading -> Unit
                }
            }
        }

        fun onShowLogoutDialog() {
            _uiState.update { it.copy(showLogoutDialog = true) }
        }

        fun onDismissLogoutDialog() {
            _uiState.update { it.copy(showLogoutDialog = false) }
        }

        fun onLogout() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                authRepository.signOut()
                _uiState.update { it.copy(isLoading = false, showLogoutDialog = false, logoutSuccess = true) }
            }
        }

        private fun formatCurrency(amount: Int): String =
            "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"
    }
