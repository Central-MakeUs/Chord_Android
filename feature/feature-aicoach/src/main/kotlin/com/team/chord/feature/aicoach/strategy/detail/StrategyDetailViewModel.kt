package com.team.chord.feature.aicoach.strategy.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.domain.usecase.strategy.CompleteStrategyUseCase
import com.team.chord.core.domain.usecase.strategy.GetStrategyDetailUseCase
import com.team.chord.core.domain.usecase.strategy.StartStrategyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StrategyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStrategyDetailUseCase: GetStrategyDetailUseCase,
    private val startStrategyUseCase: StartStrategyUseCase,
    private val completeStrategyUseCase: CompleteStrategyUseCase,
) : ViewModel() {

    private val strategyId: Long = requireNotNull(savedStateHandle[STRATEGY_ID_ARG])
    private val strategyType: String = savedStateHandle.get<String>(STRATEGY_TYPE_ARG)?.ifBlank { null } ?: "CAUTION"

    private val _uiState = MutableStateFlow(StrategyDetailUiState(isLoading = true))
    val uiState: StateFlow<StrategyDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun onPrimaryActionClick() {
        val detail = _uiState.value.detail ?: return
        if (_uiState.value.isSubmitting) return

        when (detail.status) {
            StrategyProgressStatus.NOT_STARTED -> startStrategy()
            StrategyProgressStatus.IN_PROGRESS -> completeStrategy()
            StrategyProgressStatus.COMPLETED -> Unit
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun consumeStartedMessage() {
        _uiState.update { it.copy(startedMessage = null) }
    }

    fun consumeCompletionPhrase() {
        _uiState.update { it.copy(completionPhrase = null) }
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val detail = getStrategyDetailUseCase(strategyId = strategyId, type = strategyType)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        detail = detail.toUi(),
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "전략 상세를 불러오지 못했어요.",
                    )
                }
            }
        }
    }

    private fun startStrategy() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }

            when (startStrategyUseCase(strategyId = strategyId, type = strategyType)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isSubmitting = false,
                            detail = state.detail?.copy(status = StrategyProgressStatus.IN_PROGRESS),
                            startedMessage = "실행 중인 전략을 추가했어요",
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = "전략 실행을 시작하지 못했어요.",
                        )
                    }
                }

                Result.Loading -> Unit
            }
        }
    }

    private fun completeStrategy() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }

            when (val result = completeStrategyUseCase(strategyId = strategyId, type = strategyType)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            completionPhrase = result.data,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = "전략 완료 처리에 실패했어요.",
                        )
                    }
                }

                Result.Loading -> Unit
            }
        }
    }

    private companion object {
        const val STRATEGY_ID_ARG = "strategyId"
        const val STRATEGY_TYPE_ARG = "type"
    }
}

private fun StrategyDetail.toUi(): StrategyDetailUi =
    StrategyDetailUi(
        strategyId = strategyId,
        type = type,
        weekLabel = weekLabel ?: "전략 상세",
        title = title,
        status = status,
        diagnosisHeadline = diagnosisHeadline,
        diagnosisBody = diagnosisBody,
        guideBody = guideBody,
        expectedEffectBody = expectedEffectBody,
        menuNames = menuNames,
    )
