package com.team.chord.feature.setup.menusearch

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordOutlinedButton
import com.team.chord.core.ui.component.ChordTooltipBubble
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.TooltipDirection
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue200
import com.team.chord.feature.setup.component.StepIndicator

@Composable
fun MenuSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailWithTemplate: (MenuTemplate) -> Unit,
    onNavigateToDetailWithoutTemplate: (menuName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navigate when full template details are fetched
    LaunchedEffect(uiState.navigateWithTemplate) {
        uiState.navigateWithTemplate?.let { template ->
            onNavigateToDetailWithTemplate(template)
            viewModel.onNavigationHandled()
        }
    }

    MenuSearchScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onClearSearch = viewModel::onClearSearch,
        onSearchSubmit = viewModel::onSearchSubmit,
        onTemplateSelected = viewModel::onTemplateSelected,
        onDismissTemplateDialog = viewModel::onDismissTemplateDialog,
        onConfirmTemplateApply = viewModel::onConfirmTemplateApply,
        onNavigateBack = onNavigateBack,
        onNavigateToDetailWithoutTemplate = onNavigateToDetailWithoutTemplate,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuSearchScreenContent(
    uiState: MenuSearchUiState,
    onSearchQueryChanged: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSearchSubmit: () -> Unit,
    onTemplateSelected: (MenuTemplate) -> Unit,
    onDismissTemplateDialog: () -> Unit,
    onConfirmTemplateApply: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetailWithoutTemplate: (menuName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Top App Bar
            ChordTopAppBar(
                title = "",
                onBackClick = onNavigateBack,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Step Indicator
                StepIndicator(
                    currentStep = 1,
                    totalSteps = 2,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Menu name label and search field
                MenuSearchField(
                    query = uiState.searchQuery,
                    onQueryChanged = onSearchQueryChanged,
                    onClear = onClearSearch,
                    onDone = {
                        onSearchSubmit()
                        focusManager.clearFocus()
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tooltip (only visible when search field is empty)
                if (uiState.searchQuery.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        ChordTooltipBubble(
                            text = "등록하실 메뉴명을 입력해주세요",
                            direction = TooltipDirection.UpRight,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search results or empty state
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    when {
                        uiState.searchResults.isNotEmpty() -> {
                            // Search results list
                            SearchResultsList(
                                results = uiState.searchResults,
                                searchQuery = uiState.searchQuery,
                                onTemplateClick = onTemplateSelected,
                            )
                        }
                        uiState.searchQuery.isNotEmpty() && uiState.searchResults.isEmpty() -> {
                            // No results state (show even while loading)
                            NoResultsContent(
                                searchQuery = uiState.searchQuery,
                                onDirectInputClick = {
                                    onNavigateToDetailWithoutTemplate(uiState.searchQuery)
                                },
                            )
                        }
                    }
                }

                // Bottom buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ChordLargeButton(
                        text = "이전",
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    )
                    ChordLargeButton(
                        text = "다음",
                        onClick = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                onNavigateToDetailWithoutTemplate(uiState.searchQuery)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = uiState.searchQuery.isNotEmpty() && uiState.searchResults.isEmpty(),
                    )
                }
            }
        }

        // Template apply dialog
        if (uiState.showTemplateDialog) {
            ModalBottomSheet(
                onDismissRequest = onDismissTemplateDialog,
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
                TemplateApplyDialogContent(
                    onDismiss = onDismissTemplateDialog,
                    onConfirm = onConfirmTemplateApply,
                )
            }
        }
    }
}

@Composable
private fun MenuSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClear: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "메뉴명",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Grayscale900,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onDone() }),
                cursorBrush = SolidColor(Grayscale800),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = "예)아메리카노",
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

            if (query.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_circle),
                    contentDescription = "검색어 삭제",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClear() },
                    tint = Grayscale500,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Grayscale300,
            thickness = 1.dp,
        )
    }
}

@Composable
private fun SearchResultsList(
    results: List<MenuTemplate>,
    searchQuery: String,
    onTemplateClick: (MenuTemplate) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        items(
            items = results,
            key = { it.templateId },
        ) { template ->
            SearchResultItem(
                template = template,
                searchQuery = searchQuery,
                onClick = { onTemplateClick(template) },
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    template: MenuTemplate,
    searchQuery: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Menu name with highlighted search query
        Text(
            text = buildAnnotatedString {
                val name = template.menuName
                val startIndex = name.indexOf(searchQuery, ignoreCase = true)

                if (startIndex >= 0) {
                    // Before match
                    append(name.substring(0, startIndex))

                    // Matched part (highlighted)
                    withStyle(style = SpanStyle(color = Grayscale400)) {
                        append(name.substring(startIndex, startIndex + searchQuery.length))
                    }

                    // After match
                    append(name.substring(startIndex + searchQuery.length))
                } else {
                    append(name)
                }
            },
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = Grayscale900,
        )

        // Add button in circle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PrimaryBlue200)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = "템플릿 선택",
                modifier = Modifier.size(11.dp),
                tint = PrimaryBlue500,
            )
        }
    }
}

@Composable
private fun NoResultsContent(
    searchQuery: String,
    onDirectInputClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "찾으시는 메뉴가 없나요?",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Grayscale500,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "'$searchQuery'",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = PrimaryBlue500,
                modifier = Modifier.clickable { onDirectInputClick() },
            )
            Text(
                text = " 직접입력",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = PrimaryBlue500,
                modifier = Modifier.clickable { onDirectInputClick() },
            )
        }
    }
}

@Composable
private fun TemplateApplyDialogContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "템플릿을 적용할까요?",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "메뉴 구성과 재료 항목이 자동으로 채워져요\n적용후에도 자유롭게 수정할 수 있어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Grayscale700,
            lineHeight = 25.6.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ChordLargeButton(
                text = "아니요",
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                backgroundColor = Grayscale400,
                textColor = Grayscale600
            )
            ChordLargeButton(
                text = "적용하기",
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuSearchScreenEmptyPreview() {
    MenuSearchScreenContent(
        uiState = MenuSearchUiState(),
        onSearchQueryChanged = {},
        onClearSearch = {},
        onSearchSubmit = {},
        onTemplateSelected = {},
        onDismissTemplateDialog = {},
        onConfirmTemplateApply = {},
        onNavigateBack = {},
        onNavigateToDetailWithoutTemplate = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuSearchScreenWithResultsPreview() {
    val sampleResults = listOf(
        MenuTemplate(templateId = 1L, menuName = "흑임자라떼", defaultSellingPrice = 5500, categoryCode = "BEVERAGE", workTime = 120),
        MenuTemplate(templateId = 2L, menuName = "흑임자스콘", defaultSellingPrice = 4000, categoryCode = "FOOD", workTime = 30),
        MenuTemplate(templateId = 3L, menuName = "흑임자케익", defaultSellingPrice = 6500, categoryCode = "FOOD", workTime = 30),
        MenuTemplate(templateId = 4L, menuName = "흑임자우유", defaultSellingPrice = 4500, categoryCode = "BEVERAGE", workTime = 60),
    )

    MenuSearchScreenContent(
        uiState = MenuSearchUiState(
            searchQuery = "흑임자",
            searchResults = sampleResults,
        ),
        onSearchQueryChanged = {},
        onClearSearch = {},
        onSearchSubmit = {},
        onTemplateSelected = {},
        onDismissTemplateDialog = {},
        onConfirmTemplateApply = {},
        onNavigateBack = {},
        onNavigateToDetailWithoutTemplate = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuSearchScreenNoResultsPreview() {
    MenuSearchScreenContent(
        uiState = MenuSearchUiState(
            searchQuery = "템플릿 없는 메뉴 입력",
            searchResults = emptyList(),
        ),
        onSearchQueryChanged = {},
        onClearSearch = {},
        onSearchSubmit = {},
        onTemplateSelected = {},
        onDismissTemplateDialog = {},
        onConfirmTemplateApply = {},
        onNavigateBack = {},
        onNavigateToDetailWithoutTemplate = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun TemplateApplyDialogPreview() {
    TemplateApplyDialogContent(
        onDismiss = {},
        onConfirm = {},
    )
}
