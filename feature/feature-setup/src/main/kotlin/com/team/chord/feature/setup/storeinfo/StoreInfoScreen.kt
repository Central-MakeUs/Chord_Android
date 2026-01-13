package com.team.chord.feature.setup.storeinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
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
        onLocationChanged = viewModel::onLocationChanged,
        onLocationSelected = viewModel::onLocationSelected,
        onConfirmClicked = viewModel::onConfirmClicked,
        onEmployeeCountIncrement = viewModel::onEmployeeCountIncrement,
        onEmployeeCountDecrement = viewModel::onEmployeeCountDecrement,
        onEmployeeCountBottomSheetDismiss = viewModel::onEmployeeCountBottomSheetDismiss,
        onCompleteClicked = viewModel::onCompleteClicked,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StoreInfoScreenContent(
    uiState: StoreInfoUiState,
    onStoreNameChanged: (String) -> Unit,
    onStoreNameConfirmed: () -> Unit,
    onLocationChanged: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onConfirmClicked: () -> Unit,
    onEmployeeCountIncrement: () -> Unit,
    onEmployeeCountDecrement: () -> Unit,
    onEmployeeCountBottomSheetDismiss: () -> Unit,
    onCompleteClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onNavigateToMenuEntry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        when (uiState.screenState) {
            StoreInfoScreenState.Completed -> {
                CompletedContent(
                    storeName = uiState.storeName,
                    location = uiState.location,
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
                        modifier = Modifier.padding(horizontal = 24.dp),
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = "매장 정보를 알려주세요",
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
                        StoreInfoScreenState.LocationInput -> {
                            LocationInputContent(
                                storeName = uiState.storeName,
                                location = uiState.location,
                                onLocationChanged = onLocationChanged,
                                onLocationSelected = onLocationSelected,
                            )
                        }
                        StoreInfoScreenState.Confirmation -> {
                            ConfirmationContent(
                                storeName = uiState.storeName,
                                location = uiState.location,
                                isConfirmEnabled = uiState.isConfirmEnabled,
                                onConfirmClicked = onConfirmClicked,
                            )
                        }
                        StoreInfoScreenState.Completed -> Unit
                    }
                    }
                }
            }
        }

        // Employee count bottom sheet
        if (uiState.isEmployeeCountBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onEmployeeCountBottomSheetDismiss,
                sheetState = sheetState,
                containerColor = Grayscale100,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Grayscale300),
                    )
                },
            ) {
                EmployeeCountBottomSheetContent(
                    employeeCount = uiState.employeeCount,
                    onIncrement = onEmployeeCountIncrement,
                    onDecrement = onEmployeeCountDecrement,
                    onCompleteClicked = onCompleteClicked,
                )
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
private fun LocationInputContent(
    storeName: String,
    location: String,
    onLocationChanged: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
) {
    Column {
        UnderlineSearchField(
            label = "매장 위치",
            value = location,
            onValueChange = onLocationChanged,
            placeholder = "예)청파동",
            onSearch = { onLocationSelected(location) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        UnderlineTextField(
            label = "매장명",
            value = storeName,
            onValueChange = {},
            placeholder = "",
            enabled = false,
        )
    }
}

@Composable
private fun ConfirmationContent(
    storeName: String,
    location: String,
    isConfirmEnabled: Boolean,
    onConfirmClicked: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        UnderlineTextField(
            label = "매장 위치",
            value = location,
            onValueChange = {},
            placeholder = "",
            enabled = false,
        )

        Spacer(modifier = Modifier.height(24.dp))

        UnderlineTextField(
            label = "매장명",
            value = storeName,
            onValueChange = {},
            placeholder = "",
            enabled = false,
        )

        Spacer(modifier = Modifier.weight(1f))

        ChordLargeButton(
            text = "확인",
            onClick = onConfirmClicked,
            enabled = isConfirmEnabled,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}

@Composable
private fun EmployeeCountBottomSheetContent(
    employeeCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onCompleteClicked: () -> Unit,
) {
    var isTooltipVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "현재 근무중인 직원수를 알려주세요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "언제든지 수정할 수 있어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grayscale600,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Employee count row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "직원수",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Grayscale900,
                )
                Spacer(modifier = Modifier.width(4.dp))
                ChordTooltipIcon(onClick = { isTooltipVisible = !isTooltipVisible })
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Minus button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, Grayscale300, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onDecrement() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_minus),
                        contentDescription = "감소",
                        modifier = Modifier.size(16.dp),
                        tint = Grayscale700,
                    )
                }

                Text(
                    text = employeeCount.toString(),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                // Plus button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, Grayscale300, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onIncrement() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "증가",
                        modifier = Modifier.size(16.dp),
                        tint = Grayscale700,
                    )
                }
            }
        }

        // Tooltip bubble below the row
        if (isTooltipVisible) {
            ChordTooltipBubble(
                text = "사장님을 포함한 직원수를 의미해요",
                direction = TooltipDirection.UpLeft,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ChordLargeButton(
            text = "완료",
            onClick = onCompleteClicked,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}

@Composable
private fun CompletedContent(
    storeName: String,
    location: String,
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
                SummaryRow(label = "위치", value = location)
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
    onImeAction: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = if (isFocused) PrimaryBlue500 else Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            enabled = enabled,
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Grayscale900,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onImeAction() }),
            cursorBrush = SolidColor(Grayscale800),
            decorationBox = { innerTextField ->
                Box {
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
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Grayscale300,
            thickness = 1.dp,
        )
    }
}

@Composable
private fun UnderlineSearchField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = if (isFocused) PrimaryBlue500 else Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isFocused = it.isFocused },
                textStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = Grayscale900,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                cursorBrush = SolidColor(Grayscale800),
                decorationBox = { innerTextField ->
                    Box {
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
                },
            )

            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "검색",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSearch() },
                tint = Grayscale700,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Grayscale300,
            thickness = 1.dp,
        )
    }
}
