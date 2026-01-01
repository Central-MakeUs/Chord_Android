package com.team.chord.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.repository.OnboardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChordNavHostViewModel
    @Inject
    constructor(
        onboardingRepository: OnboardingRepository,
    ) : ViewModel() {
        val isOnboardingCompleted: StateFlow<Boolean?> =
            onboardingRepository
                .isOnboardingCompleted()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = null,
                )
    }
