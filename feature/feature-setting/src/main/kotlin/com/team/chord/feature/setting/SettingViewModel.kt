package com.team.chord.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SettingUiState())
        val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

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
    }
