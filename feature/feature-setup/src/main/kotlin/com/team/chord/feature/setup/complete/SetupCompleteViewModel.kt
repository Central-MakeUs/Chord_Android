package com.team.chord.feature.setup.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.repository.SetupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupCompleteViewModel
    @Inject
    constructor(
        private val setupRepository: SetupRepository,
    ) : ViewModel() {
        init {
            viewModelScope.launch {
                setupRepository.setSetupCompleted()
            }
        }
    }
