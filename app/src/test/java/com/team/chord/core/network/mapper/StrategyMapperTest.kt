package com.team.chord.core.network.mapper

import com.team.chord.core.network.dto.strategy.CompletionPhraseResponseDto
import org.junit.Assert.assertEquals
import org.junit.Test

class StrategyMapperTest {

    @Test
    fun `toDomainPhrase falls back when placeholder is unresolved`() {
        val dto = CompletionPhraseResponseDto(completionPhrase = "매출이 %p 증가했어요")

        assertEquals("전략 실행이 완료됐어요", dto.toDomainPhrase())
    }

    @Test
    fun `toDomainPhrase keeps valid completion phrase`() {
        val dto = CompletionPhraseResponseDto(completionPhrase = "고마진 메뉴 비중이 더 높아졌어요")

        assertEquals("고마진 메뉴 비중이 더 높아졌어요", dto.toDomainPhrase())
    }
}
