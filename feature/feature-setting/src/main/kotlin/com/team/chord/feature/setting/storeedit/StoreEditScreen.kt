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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.component.ChordCheckboxItem
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.ChordTooltipIcon
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale800
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

                UnderlineTextField(
                    label = "매장명",
                    value = uiState.storeName,
                    onValueChange = onStoreNameChanged,
                    placeholder = "예)코치카페",
                )

                Spacer(modifier = Modifier.height(16.dp))

                UnderlineTextField(
                    label = "직원수",
                    value = if (uiState.ownerSolo) "0" else uiState.employeeCountInput,
                    onValueChange = onEmployeeCountChanged,
                    placeholder = "사장님을 제외한 직원수 입력",
                    unitText = "명",
                    keyboardType = KeyboardType.Number,
                    enabled = !uiState.ownerSolo,
                )

                Spacer(modifier = Modifier.height(4.dp))

                ChordCheckboxItem(
                    checked = uiState.ownerSolo,
                    onCheckedChange = onOwnerSoloChanged,
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
                    ChordTooltipIcon(onClick = { showWageTooltip = !showWageTooltip })
                }

                if (showWageTooltip) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ChordTooltipBubble(
                        text = "현재 근무중인 직원의 평균 시급",
                        direction = TooltipDirection.UpLeft,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                UnderlineTextField(
                    label = "",
                    value = uiState.formattedHourlyWage,
                    onValueChange = { input -> onHourlyWageChanged(input.filter { it.isDigit() }) },
                    placeholder = "시급 기준으로 입력",
                    unitText = "원",
                    keyboardType = KeyboardType.Number,
                )

                Spacer(modifier = Modifier.height(4.dp))

                ChordCheckboxItem(
                    checked = uiState.includeWeeklyHolidayPay,
                    onCheckedChange = onIncludeWeeklyHolidayPayChanged,
                    enabled = !uiState.ownerSolo,
                ) {
                    Text(
                        text = "주휴수당 포함",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = if (uiState.ownerSolo) Grayscale500 else Grayscale900,
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
                shape = RoundedCornerShape(16.dp),
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

@Composable
private fun UnderlineTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    unitText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isFocused) PrimaryBlue500 else Grayscale900,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            enabled = enabled,
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = if (enabled) Grayscale900 else Grayscale500,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            cursorBrush = SolidColor(Grayscale800),
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f, fill = false)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Grayscale500,
                            )
                        }
                        innerTextField()
                    }

                    if (value.isNotEmpty() && unitText != null) {
                        Text(
                            text = unitText,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = if (enabled) Grayscale900 else Grayscale500,
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Grayscale300,
            thickness = 1.dp,
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
