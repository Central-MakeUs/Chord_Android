package com.team.chord.feature.setting.storeedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordAnchoredTooltipIcon
import com.team.chord.core.ui.component.ChordCheckboxItem
import com.team.chord.core.ui.component.ChordLabeledUnderlineTextField
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.DigitGroupingVisualTransformation
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun StoreEditScreen(
    onNavigateBack: () -> Unit,
    onStoreEditComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            onStoreEditComplete()
            viewModel.onSubmitSuccessConsumed()
        }
    }

    StoreEditScreenContent(
        uiState = uiState,
        onStoreNameChanged = viewModel::onStoreNameChanged,
        onEmployeeCountChanged = viewModel::onEmployeeCountChanged,
        onOwnerSoloChanged = viewModel::onOwnerSoloChanged,
        onHourlyWageChanged = viewModel::onHourlyWageChanged,
        onIncludeWeeklyHolidayPayChanged = viewModel::onIncludeWeeklyHolidayPayChanged,
        onSubmitClicked = viewModel::onSubmitClicked,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

@Composable
internal fun StoreEditScreenContent(
    uiState: StoreEditUiState,
    onStoreNameChanged: (String) -> Unit,
    onEmployeeCountChanged: (String) -> Unit,
    onOwnerSoloChanged: (Boolean) -> Unit,
    onHourlyWageChanged: (String) -> Unit,
    onIncludeWeeklyHolidayPayChanged: (Boolean) -> Unit,
    onSubmitClicked: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showWageTooltip by remember { mutableStateOf(false) }
    val hourlyWageVisualTransformation = remember { DigitGroupingVisualTransformation() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ChordTopAppBar(
                title = "",
                onBackClick = onNavigateBack,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                ChordLabeledUnderlineTextField(
                    label = "매장명",
                    value = uiState.storeName,
                    onValueChange = onStoreNameChanged,
                    placeholder = "예)코치카페",
                )

                Spacer(modifier = Modifier.height(16.dp))

                ChordLabeledUnderlineTextField(
                    label = "직원수",
                    value = uiState.employeeCountInput,
                    onValueChange = onEmployeeCountChanged,
                    placeholder = "사장님을 제외한 직원수 입력",
                    unitText = "명",
                    keyboardType = KeyboardType.Number,
                    enabled = !uiState.ownerSolo,
                )

                ChordCheckboxItem(
                    checked = uiState.ownerSolo,
                    onCheckedChange = onOwnerSoloChanged,
                    checkedIconRes = R.drawable.ic_square_checkbox,
                    uncheckedIconRes = R.drawable.ic_empty_square_checkbox,
                    iconSize = 18.dp,
                ) {
                    Text(
                        text = "사장님 혼자 근무중이신가요?",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "인건비",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    )
                    ChordAnchoredTooltipIcon(
                        visible = showWageTooltip,
                        text = "현재 근무중인 직원의 평균 시급",
                        onClick = { showWageTooltip = !showWageTooltip },
                        iconRes = R.drawable.ic_question,
                        iconSize = 16.dp,
                        tint = Color.Unspecified,
                        tooltipDirection = TooltipDirection.Down,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ChordLabeledUnderlineTextField(
                    label = "",
                    value = uiState.hourlyWageInput,
                    onValueChange = onHourlyWageChanged,
                    placeholder = "시급 기준으로 입력",
                    unitText = "원",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = hourlyWageVisualTransformation,
                )

                ChordCheckboxItem(
                    checked = uiState.includeWeeklyHolidayPay,
                    onCheckedChange = onIncludeWeeklyHolidayPayChanged,
                    checkedIconRes = R.drawable.ic_square_checkbox,
                    uncheckedIconRes = R.drawable.ic_empty_square_checkbox,
                    iconSize = 18.dp,
                ) {
                    Text(
                        text = "주휴수당 포함",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                ReferenceInfoCard()

                Spacer(modifier = Modifier.weight(1f))

                ChordLargeButton(
                    text = "수정하기",
                    onClick = onSubmitClicked,
                    enabled = uiState.isSubmitEnabled,
                    modifier = Modifier.padding(bottom = 32.dp),
                )
            }
        }
    }
}

@Composable
private fun ReferenceInfoCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PrimaryBlue100,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
    ) {
        Text(
            text = "참고해보세요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = PrimaryBlue500,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "2026년 기준 최저시급은 10,320원이에요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale700,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreEditScreenContentPreview() {
    StoreEditScreenContent(
        uiState = StoreEditUiState(),
        onStoreNameChanged = {},
        onEmployeeCountChanged = {},
        onOwnerSoloChanged = {},
        onHourlyWageChanged = {},
        onIncludeWeeklyHolidayPayChanged = {},
        onSubmitClicked = {},
        onNavigateBack = {},
    )
}
