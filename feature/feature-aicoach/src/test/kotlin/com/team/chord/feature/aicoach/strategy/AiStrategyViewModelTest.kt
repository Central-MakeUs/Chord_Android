package com.team.chord.feature.aicoach.strategy

import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.NeedManagement
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.domain.repository.StrategyRepository
import com.team.chord.core.domain.usecase.strategy.GetSavedStrategiesUseCase
import com.team.chord.core.domain.usecase.strategy.GetWeeklyStrategiesUseCase
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AiStrategyViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads weekly strategies and completed history for selected month`() = runTest {
        val repository = FakeStrategyRepository()

        val viewModel = AiStrategyViewModel(
            getWeeklyStrategiesUseCase = GetWeeklyStrategiesUseCase(repository),
            getSavedStrategiesUseCase = GetSavedStrategiesUseCase(repository),
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val selectedMonth = state.selectedMonth

        assertEquals(listOf(Triple(selectedMonth.year, selectedMonth.monthValue, repository.weekOfMonth)), repository.weeklyRequests)
        assertEquals(listOf(Triple(selectedMonth.year, selectedMonth.monthValue, true)), repository.savedRequests)
        assertFalse(state.isLoading)
        assertEquals(2, state.recommendedStrategies.size)
        assertEquals(StrategyState.NOT_STARTED, state.recommendedStrategies[0].state)
        assertEquals(StrategyState.IN_PROGRESS, state.recommendedStrategies[1].state)
        assertEquals(1, state.historyItems.size)
        assertEquals(201L, state.historyItems.first().id)
        assertTrue(state.generatedAtMessage.contains("3월 21일 18시"))
    }

    @Test
    fun `changing filter reloads saved strategies with matching completion flag`() = runTest {
        val repository = FakeStrategyRepository()
        val viewModel = AiStrategyViewModel(
            getWeeklyStrategiesUseCase = GetWeeklyStrategiesUseCase(repository),
            getSavedStrategiesUseCase = GetSavedStrategiesUseCase(repository),
        )
        advanceUntilIdle()

        viewModel.onFilterChange(StrategyFilter.INCOMPLETE)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val selectedMonth = state.selectedMonth

        assertEquals(Triple(selectedMonth.year, selectedMonth.monthValue, false), repository.savedRequests.last())
        assertEquals(1, state.historyItems.size)
        assertEquals(101L, state.historyItems.first().id)
        assertEquals("3월 3주차", state.historyItems.first().weekLabel)
    }
}

private class FakeStrategyRepository : StrategyRepository {
    val weeklyRequests = mutableListOf<Triple<Int, Int, Int>>()
    val savedRequests = mutableListOf<Triple<Int, Int, Boolean>>()
    var weekOfMonth: Int = -1

    private val weeklyStrategies = listOf(
        Strategy(
            id = 1L,
            title = "매출 방어 전략",
            description = "요약 A",
            weekLabel = "3월 4주차",
            status = StrategyProgressStatus.NOT_STARTED,
            type = "CAUTION",
            isSaved = false,
            createdAt = LocalDateTime.of(2026, 3, 20, 9, 0),
        ),
        Strategy(
            id = 2L,
            title = "재방문 유도 전략",
            description = "요약 B",
            weekLabel = "3월 4주차",
            status = StrategyProgressStatus.IN_PROGRESS,
            type = "HIGH_MARGIN",
            isSaved = true,
            createdAt = LocalDateTime.of(2026, 3, 21, 18, 30),
        ),
    )

    private val incompleteSavedStrategies = listOf(
        Strategy(
            id = 101L,
            title = "실행 중 전략",
            description = "미완료",
            weekLabel = "3월 3주차",
            status = StrategyProgressStatus.IN_PROGRESS,
            type = "CAUTION",
            isSaved = true,
            createdAt = LocalDateTime.of(2026, 3, 10, 11, 0),
        ),
    )

    private val completedSavedStrategies = listOf(
        Strategy(
            id = 201L,
            title = "완료 전략",
            description = "완료됨",
            weekLabel = "3월 2주차",
            status = StrategyProgressStatus.COMPLETED,
            type = "DANGER",
            isSaved = true,
            createdAt = LocalDateTime.of(2026, 3, 5, 8, 0),
        ),
    )

    override suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy> {
        this.weekOfMonth = weekOfMonth
        weeklyRequests += Triple(year, month, weekOfMonth)
        return weeklyStrategies
    }

    override suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy> {
        savedRequests += Triple(year, month, isCompleted)
        return if (isCompleted) completedSavedStrategies else incompleteSavedStrategies
    }

    override suspend fun getNeedManagement(): NeedManagement = NeedManagement()

    override suspend fun getStrategyDetail(strategyId: Long, type: String): StrategyDetail {
        error("Not used in this test")
    }

    override suspend fun startStrategy(strategyId: Long, type: String): Result<Unit> {
        error("Not used in this test")
    }

    override suspend fun completeStrategy(strategyId: Long, type: String): Result<String> {
        error("Not used in this test")
    }

    override suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean): Result<Unit> {
        error("Not used in this test")
    }
}
