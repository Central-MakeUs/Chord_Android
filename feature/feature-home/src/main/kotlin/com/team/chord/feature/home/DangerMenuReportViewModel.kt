package com.team.chord.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.usecase.strategy.GetNeedManagementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DangerMenuReportViewModel @Inject constructor(
    private val getNeedManagementUseCase: GetNeedManagementUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DangerMenuReportUiState())
    val uiState: StateFlow<DangerMenuReportUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching { getNeedManagementUseCase() }
                .onSuccess { needManagement ->
                    val filteredMenus = needManagement.menus
                        .filter { it.marginGradeCode.equals("DANGER", ignoreCase = true) }
                        .map { menu ->
                            DangerMenuReportMenuUi(
                                strategyId = menu.strategyId,
                                menuName = menu.menuName,
                                costRateText = formatPercent(menu.costRate),
                                marginRateText = formatPercent(menu.marginRate),
                            )
                        }

                    val strategyDate = needManagement.strategyDate ?: LocalDate.now()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            dateLabel = strategyDate.format(DateTimeFormatter.ofPattern("M월 d일 기준", Locale.KOREA)),
                            isStable = filteredMenus.isEmpty(),
                            menus = filteredMenus,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "진단 정보를 불러오지 못했어요.",
                        )
                    }
                }
        }
    }

    private fun formatPercent(value: Double): String =
        String.format(Locale.KOREA, "%.1f%%", value)
}
