package com.team.chord.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val onboardingRepository: OnboardingRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(OnboardingUiState())
        val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

        fun onPageChanged(page: Int) {
            _uiState.update { it.copy(currentPage = page) }
        }

        fun onNextClicked() {
            val currentState = _uiState.value
            if (currentState.currentPage < currentState.totalPages - 1) {
                _uiState.update { it.copy(currentPage = it.currentPage + 1) }
            } else {
                completeOnboarding()
            }
        }

        private fun completeOnboarding() {
            viewModelScope.launch {
                onboardingRepository.setOnboardingCompleted()
                _uiState.update { it.copy(isCompleted = true) }
            }
        }
    }
