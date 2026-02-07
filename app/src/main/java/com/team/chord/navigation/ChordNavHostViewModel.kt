package com.team.chord.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.AuthState
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.repository.SetupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChordNavHostViewModel
    @Inject
    constructor(
        private val setupRepository: SetupRepository,
        authRepository: AuthRepository,
    ) : ViewModel() {

        fun markSetupCompleted() {
            viewModelScope.launch {
                setupRepository.setSetupCompleted()
            }
        }
        val navigationState: StateFlow<NavigationState> =
            combine(
                setupRepository.isSetupCompleted(),
                authRepository.observeAuthState(),
            ) { isSetupCompleted, authState ->
                when {
                    authState is AuthState.Loading -> {
                        NavigationState.Loading
                    }

                    authState is AuthState.Authenticated && isSetupCompleted -> {
                        NavigationState.Ready(
                            startDestination = StartDestination.HOME,
                        )
                    }

                    authState is AuthState.Authenticated && !isSetupCompleted -> {
                        NavigationState.Ready(
                            startDestination = StartDestination.SETUP,
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
    LOGIN,
    SETUP,
    HOME,
}
