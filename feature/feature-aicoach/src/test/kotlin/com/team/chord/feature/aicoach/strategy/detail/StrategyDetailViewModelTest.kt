package com.team.chord.feature.aicoach.strategy.detail

import androidx.lifecycle.SavedStateHandle
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.strategy.NeedManagement
import com.team.chord.core.domain.model.strategy.Strategy
import com.team.chord.core.domain.model.strategy.StrategyDetail
import com.team.chord.core.domain.model.strategy.StrategyProgressStatus
import com.team.chord.core.domain.repository.StrategyRepository
import com.team.chord.core.domain.usecase.strategy.CompleteStrategyUseCase
import com.team.chord.core.domain.usecase.strategy.GetStrategyDetailUseCase
import com.team.chord.core.domain.usecase.strategy.StartStrategyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StrategyDetailViewModelTest {

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
    fun `start action marks strategy in progress and exposes started message`() = runTest {
        val repository = FakeStrategyRepository(initialStatus = StrategyProgressStatus.NOT_STARTED)
        val viewModel = createViewModel(repository)
        advanceUntilIdle()

        viewModel.onPrimaryActionClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(StrategyProgressStatus.IN_PROGRESS, state.detail?.status)
        assertEquals("실행 중인 전략을 추가했어요", state.startedMessage)
        assertNull(state.completionPhrase)
        assertEquals(listOf(5L to "CAUTION"), repository.startRequests)
    }

    @Test
    fun `complete action emits completion phrase without mutating loaded detail`() = runTest {
        val repository = FakeStrategyRepository(initialStatus = StrategyProgressStatus.IN_PROGRESS)
        val viewModel = createViewModel(repository)
        advanceUntilIdle()

        viewModel.onPrimaryActionClick()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(StrategyProgressStatus.IN_PROGRESS, state.detail?.status)
        assertEquals("매출을 회복했어요", state.completionPhrase)
        assertTrue(repository.completeRequests.contains(5L to "CAUTION"))
    }

    private fun createViewModel(repository: FakeStrategyRepository): StrategyDetailViewModel =
        StrategyDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "strategyId" to 5L,
                    "type" to "CAUTION",
                ),
            ),
            getStrategyDetailUseCase = GetStrategyDetailUseCase(repository),
            startStrategyUseCase = StartStrategyUseCase(repository),
            completeStrategyUseCase = CompleteStrategyUseCase(repository),
        )
}

private class FakeStrategyRepository(
    private val initialStatus: StrategyProgressStatus,
) : StrategyRepository {
    val startRequests = mutableListOf<Pair<Long, String>>()
    val completeRequests = mutableListOf<Pair<Long, String>>()

    override suspend fun getWeeklyStrategies(year: Int, month: Int, weekOfMonth: Int): List<Strategy> {
        error("Not used in this test")
    }

    override suspend fun getSavedStrategies(year: Int, month: Int, isCompleted: Boolean): List<Strategy> {
        error("Not used in this test")
    }

    override suspend fun getNeedManagement(): NeedManagement = NeedManagement()

    override suspend fun getStrategyDetail(strategyId: Long, type: String): StrategyDetail =
        StrategyDetail(
            strategyId = strategyId,
            type = type,
            title = "전략 제목",
            weekLabel = "3월 4주차",
            status = initialStatus,
            diagnosisHeadline = "원인 분석",
            diagnosisBody = "상세 원인",
            guideBody = "실행 가이드",
            expectedEffectBody = "예상 효과",
            menuNames = listOf("아메리카노"),
        )

    override suspend fun startStrategy(strategyId: Long, type: String): Result<Unit> {
        startRequests += strategyId to type
        return Result.Success(Unit)
    }

    override suspend fun completeStrategy(strategyId: Long, type: String): Result<String> {
        completeRequests += strategyId to type
        return Result.Success("매출을 회복했어요")
    }

    override suspend fun saveStrategy(strategyId: Long, type: String, isSaved: Boolean): Result<Unit> {
        error("Not used in this test")
    }
}
