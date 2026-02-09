package com.team.chord.feature.setting.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.repository.AuthRepository
import com.team.chord.core.domain.usecase.user.DeleteMeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val deleteMeUseCase: DeleteMeUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawUiState())
    val uiState: StateFlow<WithdrawUiState> = _uiState.asStateFlow()

    fun onWithdrawClicked() {
        _uiState.update { it.copy(showConfirmDialog = true) }
    }

    fun onDismissConfirmDialog() {
        _uiState.update { it.copy(showConfirmDialog = false) }
    }

    fun onConfirmWithdraw() {
        if (_uiState.value.isSubmitting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    showConfirmDialog = false,
                    errorMessage = null,
                )
            }

            when (val result = deleteMeUseCase()) {
                is Result.Success -> {
                    authRepository.signOut()
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            deleteSuccess = true,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = result.exception.message ?: "회원탈퇴에 실패했어요",
                        )
                    }
                }

                Result.Loading -> Unit
            }
        }
    }

    fun onDeleteSuccessConsumed() {
        _uiState.update { it.copy(deleteSuccess = false) }
    }

    fun onErrorMessageConsumed() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
