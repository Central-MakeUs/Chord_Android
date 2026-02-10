package com.team.chord.feature.setup.storeinfo

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordCheckboxItem
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.ChordTooltipIcon
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun StoreInfoScreen(
    onNavigateToMenuEntry: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StoreInfoScreenContent(
        uiState = uiState,
        onStoreNameChanged = viewModel::onStoreNameChanged,
        onStoreNameConfirmed = viewModel::onStoreNameConfirmed,
        onEmployeeCountChanged = viewModel::onEmployeeCountChanged,
        onIsOwnerSoloChanged = viewModel::onIsOwnerSoloChanged,
        onHourlyWageChanged = viewModel::onHourlyWageChanged,
        onIncludeWeeklyAllowanceChanged = viewModel::onIncludeWeeklyAllowanceChanged,
        onPostStoreNameNextClicked = viewModel::onPostStoreNameNextClicked,
        onBackClicked = {
            when (uiState.screenState) {
                StoreInfoScreenState.StoreNameInput -> onBackClick()
                else -> viewModel.onBackClicked()
            }
        },
        onNavigateToMenuEntry = onNavigateToMenuEntry,
        modifier = modifier,
    )
}

@Composable
internal fun StoreInfoScreenContent(
    uiState: StoreInfoUiState,
    onStoreNameChanged: (String) -> Unit,
    onStoreNameConfirmed: () -> Unit,
    onEmployeeCountChanged: (String) -> Unit,
    onIsOwnerSoloChanged: (Boolean) -> Unit,
    onHourlyWageChanged: (String) -> Unit,
    onIncludeWeeklyAllowanceChanged: (Boolean) -> Unit,
    onPostStoreNameNextClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNavigateToMenuEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        when (uiState.screenState) {
            StoreInfoScreenState.Completed -> {
                CompletedContent(
                    storeName = uiState.storeName,
                    employeeCount = uiState.employeeCount,
                    onNavigateToMenuEntry = onNavigateToMenuEntry,
                )
            }
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ChordTopAppBar(
                        title = "",
                        onBackClick = onBackClicked,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = when (uiState.screenState) {
                                StoreInfoScreenState.PostStoreName -> "매장 운영 정보를 알려주세요"
                                else -> "매장 정보를 알려주세요"
                            },
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Grayscale900,
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                    when (uiState.screenState) {
                        StoreInfoScreenState.StoreNameInput -> {
                            StoreNameInputContent(
                                storeName = uiState.storeName,
                                onStoreNameChanged = onStoreNameChanged,
                                onStoreNameConfirmed = onStoreNameConfirmed,
                            )
                        }
                        StoreInfoScreenState.PostStoreName -> {
                            PostStoreNameContent(
                                employeeCountInput = uiState.employeeCountInput,
                                ownerSolo = uiState.ownerSolo,
                                hourlyWageInput = uiState.hourlyWageInput,
                                includeWeeklyAllowance = uiState.includeWeeklyAllowance,
                                isNextEnabled = uiState.isPostStoreNameNextEnabled,
                                onEmployeeCountChanged = onEmployeeCountChanged,
                                onIsOwnerSoloChanged = onIsOwnerSoloChanged,
                                onHourlyWageChanged = onHourlyWageChanged,
                                onIncludeWeeklyAllowanceChanged = onIncludeWeeklyAllowanceChanged,
                                onNextClicked = onPostStoreNameNextClicked,
                            )
                        }
                        StoreInfoScreenState.Completed -> Unit
                    }
                    }
                }
            }
        }

    }
}

@Composable
private fun StoreNameInputContent(
    storeName: String,
    onStoreNameChanged: (String) -> Unit,
    onStoreNameConfirmed: () -> Unit,
) {
    Column {
        UnderlineTextField(
            label = "매장명",
            value = storeName,
            onValueChange = onStoreNameChanged,
            placeholder = "예)코치카페",
            onImeAction = {
                if (storeName.isNotBlank()) {
                    onStoreNameConfirmed()
                }
            },
        )
    }
}

@Composable
private fun PostStoreNameContent(
    employeeCountInput: String,
    ownerSolo: Boolean,
    hourlyWageInput: String,
    includeWeeklyAllowance: Boolean,
    isNextEnabled: Boolean,
    onEmployeeCountChanged: (String) -> Unit,
    onIsOwnerSoloChanged: (Boolean) -> Unit,
    onHourlyWageChanged: (String) -> Unit,
    onIncludeWeeklyAllowanceChanged: (Boolean) -> Unit,
    onNextClicked: () -> Unit,
) {
    var showWageTooltip by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        // 직원수 field
        UnderlineTextField(
            label = "직원수",
            value = if (ownerSolo) "0" else employeeCountInput,
            onValueChange = { if (!ownerSolo) onEmployeeCountChanged(it) },
            placeholder = "사장님을 제외한 직원수 입력",
            unitText = "명",
            keyboardType = KeyboardType.Number,
            enabled = !ownerSolo,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 사장님 혼자 checkbox
        ChordCheckboxItem(
            checked = ownerSolo,
            onCheckedChange = onIsOwnerSoloChanged,
        ) {
            Text(
                text = "사장님 혼자 근무중이신가요?",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale900,
            )
        }

        if (ownerSolo && hourlyWageInput.isEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            ChordTooltipBubble(
                text = "현재 근무중인 직원의 평균 시급을 입력해주세요",
                direction = TooltipDirection.UpLeft,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 인건비 label with tooltip icon
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
            ChordTooltipBubble(
                text = "현재 근무중인 직원의 평균 시급",
                direction = TooltipDirection.UpLeft,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hourly wage field (no label since we rendered it above)
        UnderlineTextField(
            label = "",
            value = formatWithComma(hourlyWageInput),
            onValueChange = { newValue ->
                val digitsOnly = newValue.filter { it.isDigit() }
                onHourlyWageChanged(digitsOnly)
            },
            placeholder = "시급 기준으로 입력",
            unitText = "원",
            keyboardType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 주휴수당 포함 checkbox
        ChordCheckboxItem(
            checked = includeWeeklyAllowance,
            onCheckedChange = onIncludeWeeklyAllowanceChanged,
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

        // Reference info card
        ReferenceInfoCard()

        Spacer(modifier = Modifier.weight(1f))

        // Next button (use built-in disabled styling)
        ChordLargeButton(
            text = "다음",
            onClick = onNextClicked,
            enabled = isNextEnabled,
            modifier = Modifier.padding(bottom = 32.dp),
        )
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
            text = "2026년기준 최저시급은 10,320원 이예요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale700,
        )
    }
}

@Composable
private fun CompletedContent(
    storeName: String,
    employeeCount: Int,
    onNavigateToMenuEntry: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onNavigateToMenuEntry()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 49.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_blue_linear_check),
            contentDescription = "완료",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "매장 설정이 완료됐어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = PrimaryBlue500,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Summary card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = PrimaryBlue100,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 12.dp, vertical = 16.dp),
        ) {
            Column {
                SummaryRow(label = "매장명", value = storeName)
                Spacer(modifier = Modifier.height(8.dp))
                SummaryRow(label = "직원수", value = "${employeeCount}명")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Grayscale600,
            modifier = Modifier.width(60.dp),
        )
        Text(
            text = value,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Grayscale900,
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
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    onImeAction: () -> Unit = {},
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
                color = Grayscale900,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onImeAction() }),
            cursorBrush = SolidColor(Grayscale800),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
                            color = Grayscale900,
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

