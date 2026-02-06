package com.team.chord.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChordNavHostViewModel
    @Inject
    constructor(
        private val onboardingRepository: OnboardingRepository,
        authRepository: AuthRepository,
    ) : ViewModel() {
        suspend fun isOnboardingCompleted(): Boolean =
            onboardingRepository.isOnboardingCompleted().first()

        val navigationState: StateFlow<NavigationState> =
            combine(
                onboardingRepository.isOnboardingCompleted(),
                authRepository.observeAuthState(),
            ) { isOnboardingCompleted, authState ->
                when {
                    authState is AuthState.Loading -> {
                        NavigationState.Loading
                    }

                    authState is AuthState.Authenticated -> {
                        NavigationState.Ready(
                            startDestination = StartDestination.HOME,
                        )
                    }

                    !isOnboardingCompleted -> {
                        NavigationState.Ready(
                            startDestination = StartDestination.ONBOARDING,
                        )
                    }

                    else -> {
                        NavigationState.Ready(
                            startDestination = StartDestination.LOGIN,
                        )
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = NavigationState.Loading,
            )
    }

sealed interface NavigationState {
    data object Loading : NavigationState

    data class Ready(
        val startDestination: StartDestination,
    ) : NavigationState
}

enum class StartDestination {
    ONBOARDING,
    LOGIN,
    HOME,
}
