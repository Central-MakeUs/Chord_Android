package com.team.chord.feature.setting.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
fun TermsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sections = listOf(
        TermsSection(
            title = "1. 목적 및 정의",
            bullets = listOf(
                "본 약관은 코치코치앱(이하 \"서비스\")의 이용 조건 및 절차, 권리·의무를 규정합니다.",
                "서비스는 카페 운영에 필요한 원가·수익성 분석을 돕기 위한 참고용 정보 제공 목적입니다.",
                "계산 결과는 법적·세무적·회계적 자문이 아닙니다.",
            ),
        ),
        TermsSection(
            title = "2. 회원 가입 및 계정 관리",
            bullets = listOf(
                "회원은 정확한 정보를 입력해야 합니다.",
                "계정 정보 관리 책임은 사용자에게 있습니다.",
                "타인의 계정 사용, 양도, 대여를 금지합니다.",
            ),
        ),
        TermsSection(
            title = "3. 서비스 내용 및 제공 범위",
            bullets = listOf(
                "사용자가 입력한 메뉴, 재료비, 비용 정보를 바탕으로 원가, 마진율, 공헌이익, 권장 가격 등의 분석 정보를 제공합니다.",
                "서비스 내용은 운영 정책에 따라 변경·추가·중단될 수 있습니다.",
            ),
        ),
        TermsSection(
            title = "4. 사용자 입력 정보의 책임",
            bullets = listOf(
                "모든 분석 결과는 사용자가 입력한 정보에 의존합니다.",
                "입력 정보의 정확성, 최신성에 대한 책임은 사용자에게 있습니다.",
                "잘못된 입력으로 인한 손실에 대해 서비스는 책임을 지지 않습니다.",
            ),
        ),
        TermsSection(
            title = "5. 서비스 이용의 제한 및 중단",
            bullets = listOf(
                "시스템 점검, 장애, 불가항력 사유 시 서비스가 일시 중단될 수 있습니다.",
                "회사는 사전 공지 후 또는 불가피한 경우 사후 공지로 서비스 중단이 가능합니다.",
            ),
        ),
        TermsSection(
            title = "6. 지식재산권",
            bullets = listOf(
                "서비스에 포함된 UI, 콘텐츠, 분석 로직, 계산 방식의 저작권은 회사에 귀속됩니다.",
                "사용자는 개인적인 서비스 이용 범위 내에서만 사용할 수 있습니다.",
            ),
        ),
        TermsSection(
            title = "7. 책임의 제한 및 면책",
            bullets = listOf(
                "서비스에서 제공되는 분석, 가이드, 권장 가격은 의사결정을 돕는 참고 자료입니다.",
                "실제 경영 판단, 손익 결과에 대한 책임은 사용자에게 있습니다.",
                "회사는 간접 손해, 영업 손실, 기대 수익 손실에 대해 책임을 지지 않습니다.",
                "서비스의 일부 기능은 자동화된 분석 또는 AI 기반 로직을 활용합니다.",
                "AI 분석 결과는 참고용 정보이며, 항상 정확하거나 최신임을 보장하지 않습니다.",
                "AI 결과에 대한 최종 판단 및 책임은 사용자에게 있습니다.",
            ),
        ),
        TermsSection(
            title = "8. 계약 해지 및 회원 탈퇴",
            bullets = listOf(
                "사용자는 언제든지 앱 내 기능을 통해 회원 탈퇴가 가능합니다.",
                "탈퇴 시 관련 법령에 따라 일부 정보는 보관될 수 있습니다.",
                "구체적 보관 내용은 개인정보 처리방침을 따릅니다.",
            ),
        ),
        TermsSection(
            title = "9. 약관의 변경",
            bullets = listOf(
                "약관 변경 시 앱 내 공지 또는 기타 합리적인 방법으로 고지합니다.",
                "변경 후에도 서비스를 계속 이용할 경우 동의로 간주합니다.",
            ),
        ),
        TermsSection(
            title = "10. 준거법 및 분쟁 해결",
            bullets = listOf(
                "본 약관은 대한민국 법을 준거법으로 합니다.",
            ),
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "이용약관",
            onBackClick = onNavigateBack,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            item {
                Text(
                    text = "코치코치 서비스 이용약관",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Grayscale900,
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "구성 개요",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1. 목적 및 정의\n2. 회원 가입 및 계정 관리\n3. 서비스 내용 및 제공 범위\n4. 사용자 입력 정보의 책임\n5. 서비스 이용의 제한 및 중단\n6. 지식재산권\n7. 책임의 제한 및 면책\n8. 계약 해지 및 회원 탈퇴\n9. 약관의 변경\n10. 준거법 및 분쟁 해결",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        color = Grayscale600,
                    ),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "항목별 핵심 내용 가이드",
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    ),
                )
            }

            items(sections) { section ->
                TermsSectionItem(section = section)
            }
        }
    }
}

@Composable
private fun TermsSectionItem(
    section: TermsSection,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = section.title,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Grayscale900,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        section.bullets.forEach { bullet ->
            Text(
                text = "• $bullet",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 25.sp,
                    color = Grayscale600,
                ),
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
    }
}

private data class TermsSection(
    val title: String,
    val bullets: List<String>,
)

@Preview(showBackground = true)
@Composable
private fun TermsScreenPreview() {
    TermsScreen(onNavigateBack = {})
}
