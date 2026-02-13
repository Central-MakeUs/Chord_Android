package com.team.chord.feature.setting.faq

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun FaqScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val faqItems = listOf(
        FaqItem(
            question = "Q. 마진율이 뭔가요?",
            answer = "A. 마진율은 재료비와 인건비를 기준으로 계산한 추정 마진율이에요. 우리 가게의 운영 효율을 알려줘요.",
        ),
        FaqItem(
            question = "Q. 원가율이 뭔가요?",
            answer = "A. 가격 중 재료비가 차지하는 비중이에요. 재료비는 식재료와 운영재료(소모품)을 모두 포함해요.",
        ),
        FaqItem(
            question = "Q. 공헌이익이 뭔가요?",
            answer = "A. 한 잔당 판매가 만들어내는 수익을 말해요. 메뉴의 운영 상의 수익률을 알 수 있는 지표로 활용돼요.",
        ),
        FaqItem(
            question = "Q. 권장가격이 뭔가요?",
            answer = "A. 손해 없이 운영할 수 있는 최소 기준 가격을 의미해요. 권장 가격 이상으로 가격을 설정해야 수익성에 문제가 없어요.",
        ),
        FaqItem(
            question = "Q. 수익 등급은 어떻게 설정되나요?",
            answer = "A. 수익 등급은 안정/보통/주의/위험으로 구성되요.\n수익 등급은 원가율을 기준으로 다음과 같이 표시돼요.\n안정 : 원가율 25% 이하\n보통 : 원가율 25% 초과 ~ 35% 이하\n주의 : 원가율 25% 초과 ~ 40% 이하\n위험 : 원가율 40% 초과",
        ),
        FaqItem(
            question = "Q. AI 코치 가이드는 언제 생성되나요?",
            answer = "A. AI 코치는 일주일에 한번, 매주 일요일 21시 59분에 업데이트 됩니다.",
        ),
        FaqItem(
            question = "Q. 회원 탈퇴와 관련한 문의는 어디로 하나요?",
            answer = "A. 회원 탈퇴와 관련한 문의는 이메일을 통해 문의하실 수 있습니다.\ncoach.operation@gmail.com",
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "FAQ",
            onBackClick = onNavigateBack,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        ) {
            items(faqItems) { item ->
                FaqBodyItem(item = item)
            }

            item {
                Text(
                    text = "더 자세한 안내가 필요한 경우, 아래 이메일로 문의해 주세요.\ncoach.operation@gmail.com",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Grayscale600,
                    ),
                )
            }
        }
    }
}

@Composable
private fun FaqBodyItem(item: FaqItem) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = item.question,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Grayscale900,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.answer,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale600,
            ),
        )
    }
}

private data class FaqItem(
    val question: String,
    val answer: String,
)

@Preview(showBackground = true)
@Composable
private fun FaqScreenPreview() {
    FaqScreen(onNavigateBack = {})
}
