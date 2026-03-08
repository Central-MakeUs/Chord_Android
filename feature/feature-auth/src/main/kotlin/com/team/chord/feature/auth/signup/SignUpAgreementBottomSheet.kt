package com.team.chord.feature.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpAgreementBottomSheet(
    allAgreed: Boolean,
    serviceTermsAgreed: Boolean,
    privacyCollectionAgreed: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onAllAgreedChange: (Boolean) -> Unit,
    onServiceTermsChange: (Boolean) -> Unit,
    onPrivacyCollectionChange: (Boolean) -> Unit,
    onTermsDetailClick: () -> Unit,
    onPrivacyDetailClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = Grayscale100,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier =
                        Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                color = Grayscale400,
                                shape = RoundedCornerShape(2.dp),
                            ),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Text(
                text = "서비스 이용약관에\n동의해주세요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 34.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(48.dp))

            AgreementRow(
                text = "약관에 모두 동의",
                checked = allAgreed,
                emphasized = true,
                onCheckedChange = { onAllAgreedChange(!allAgreed) },
            )

            Spacer(modifier = Modifier.height(8.dp))

            AgreementRow(
                text = "코치코치 서비스 이용약관(필수)",
                checked = serviceTermsAgreed,
                onCheckedChange = { onServiceTermsChange(!serviceTermsAgreed) },
                onDetailClick = onTermsDetailClick,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AgreementRow(
                text = "개인정보 수집 및 이용동의(필수)",
                checked = privacyCollectionAgreed,
                onCheckedChange = { onPrivacyCollectionChange(!privacyCollectionAgreed) },
                onDetailClick = onPrivacyDetailClick,
            )

            Spacer(modifier = Modifier.height(40.dp))

            ChordLargeButton(
                text = "확인",
                onClick = onConfirmClick,
                enabled = serviceTermsAgreed && privacyCollectionAgreed && !isLoading,
            )
        }
    }
}

@Composable
private fun AgreementRow(
    text: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
    onDetailClick: (() -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .clickable(onClick = onCheckedChange)
                    .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = CoreUiR.drawable.ic_check_rounded),
                contentDescription = if (checked) "선택됨" else "선택 안됨",
                modifier = Modifier.size(24.dp),
                tint = if (checked) PrimaryBlue500 else Grayscale500,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontFamily = PretendardFontFamily,
                fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp,
                color = if (emphasized) Grayscale900 else Grayscale700,
            )
        }

        if (onDetailClick != null) {
            IconButton(onClick = onDetailClick) {
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_arrow_right),
                    contentDescription = "$text 상세 보기",
                    tint = Grayscale500,
                )
            }
        }
    }
}
