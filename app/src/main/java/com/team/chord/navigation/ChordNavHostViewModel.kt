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
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChordNavHostViewModel
    @Inject
    constructor(
        onboardingRepository: OnboardingRepository,
        authRepository: AuthRepository,
    ) : ViewModel() {
        // TODO: 테스트 후 주석 해제 필요
//        val navigationState: StateFlow<NavigationState> =
//            combine(
//                onboardingRepository.isOnboardingCompleted(),
//                authRepository.observeAuthState(),
//            ) { isOnboardingCompleted, authState ->
//                when {
//                    authState is AuthState.Loading -> {
//                        NavigationState.Loading
//                    }
//
//                    authState is AuthState.Authenticated -> {
//                        NavigationState.Ready(
//                            startDestination = StartDestination.HOME,
//                        )
//                    }
//
//                    !isOnboardingCompleted -> {
//                        NavigationState.Ready(
//                            startDestination = StartDestination.ONBOARDING,
//                        )
//                    }
//
//                    else -> {
//                        NavigationState.Ready(
//                            startDestination = StartDestination.LOGIN,
//                        )
//                    }
//                }
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = NavigationState.Loading,
//            )

        // 테스트용: 항상 HOME에서 시작
        val navigationState: StateFlow<NavigationState> =
            combine(
                onboardingRepository.isOnboardingCompleted(),
                authRepository.observeAuthState(),
            ) { _, _ ->
                NavigationState.Ready(
                    startDestination = StartDestination.HOME,
                )
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
