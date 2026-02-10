package com.team.chord.feature.menu.management

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.management.component.EditMenuNameBottomSheet
import com.team.chord.feature.menu.management.component.EditPreparationTimeBottomSheet
import com.team.chord.feature.menu.management.component.EditPriceBottomSheet
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuManagementScreen(
    onNavigateBack: () -> Unit,
    onMenuDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuManagementViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuManagementScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onShowNameBottomSheet = viewModel::showNameBottomSheet,
        onHideNameBottomSheet = viewModel::hideNameBottomSheet,
        onUpdateName = viewModel::updateMenuName,
        onShowPriceBottomSheet = viewModel::showPriceBottomSheet,
        onHidePriceBottomSheet = viewModel::hidePriceBottomSheet,
        onUpdatePrice = viewModel::updatePrice,
        onShowTimeBottomSheet = viewModel::showTimeBottomSheet,
        onHideTimeBottomSheet = viewModel::hideTimeBottomSheet,
        onUpdateTime = viewModel::updatePreparationTime,
        onCategorySelected = viewModel::updateCategory,
        onShowDeleteDialog = viewModel::showDeleteDialog,
        onHideDeleteDialog = viewModel::hideDeleteDialog,
        onConfirmDelete = viewModel::deleteMenu,
        onConfirmDeleteSuccess = {
            viewModel.hideDeleteSuccessDialog()
            onMenuDeleted()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuManagementScreenContent(
    uiState: MenuManagementUiState,
    onNavigateBack: () -> Unit,
    onShowNameBottomSheet: () -> Unit,
    onHideNameBottomSheet: () -> Unit,
    onUpdateName: (String) -> Unit,
    onShowPriceBottomSheet: () -> Unit,
    onHidePriceBottomSheet: () -> Unit,
    onUpdatePrice: (Int) -> Unit,
    onShowTimeBottomSheet: () -> Unit,
    onHideTimeBottomSheet: () -> Unit,
    onUpdateTime: (Int) -> Unit,
    onCategorySelected: (String) -> Unit,
    onShowDeleteDialog: () -> Unit,
    onHideDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    onConfirmDeleteSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    var showCategoryBottomSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = PrimaryBlue500)
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
            ) {
                // 메뉴명 섹션
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowNameBottomSheet() }
                        .padding(vertical = 16.dp),
                ) {
                    Text(
                        text = "메뉴명",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grayscale500,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.menuName,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Grayscale900,
                    )
                }

                HorizontalDivider(color = Grayscale300)

                // 가격 섹션
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowPriceBottomSheet() }
                        .padding(vertical = 16.dp),
                ) {
                    Text(
                        text = "가격",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grayscale500,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${numberFormat.format(uiState.price)}원",
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Grayscale900,
                    )
                }

                HorizontalDivider(color = Grayscale300)

                Spacer(modifier = Modifier.height(16.dp))

                // 카테고리 + 제조시간 카드
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // 카테고리 카드
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Grayscale200,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .clickable { showCategoryBottomSheet = true }
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "카테고리",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Grayscale500,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = uiState.categories
                                    .find { it.code == uiState.selectedCategoryCode }
                                    ?.name ?: "",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Grayscale900,
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Grayscale500,
                            )
                        }
                    }

                    // 제조시간 카드
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = Grayscale200,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .clickable { onShowTimeBottomSheet() }
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "제조시간",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Grayscale500,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = formatTime(uiState.preparationTimeSeconds),
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Grayscale900,
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Grayscale500,
                            )
                        }
                    }
                }
            }

            // 수정 완료 버튼
            ChordLargeButton(
                text = "수정 완료",
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
            )
        }
    }

    // Bottom Sheets
    if (uiState.showNameBottomSheet) {
        EditMenuNameBottomSheet(
            currentName = uiState.menuName,
            onDismiss = onHideNameBottomSheet,
            onConfirm = onUpdateName,
        )
    }

    if (uiState.showPriceBottomSheet) {
        EditPriceBottomSheet(
            currentPrice = uiState.price,
            onDismiss = onHidePriceBottomSheet,
            onConfirm = onUpdatePrice,
        )
    }

    if (uiState.showTimeBottomSheet) {
        EditPreparationTimeBottomSheet(
            currentSeconds = uiState.preparationTimeSeconds,
            onDismiss = onHideTimeBottomSheet,
            onConfirm = onUpdateTime,
        )
    }

    // Category Bottom Sheet
    if (showCategoryBottomSheet) {
        CategoryEditBottomSheet(
            categories = uiState.categories,
            selectedCategoryCode = uiState.selectedCategoryCode,
            onDismiss = { showCategoryBottomSheet = false },
            onConfirm = { code ->
                showCategoryBottomSheet = false
                onCategorySelected(code)
            },
        )
    }

    // Delete Dialog
    if (uiState.showDeleteDialog) {
        ChordTwoButtonDialog(
            title = "메뉴를 삭제하시겠어요?",
            onDismiss = onHideDeleteDialog,
            onConfirm = onConfirmDelete,
            dismissText = "아니요",
            confirmText = "삭제하기",
        )
    }

    // Delete Success Dialog
    if (uiState.showDeleteSuccessDialog) {
        ChordOneButtonDialog(
            title = "메뉴가 삭제 됐어요",
            onConfirm = onConfirmDeleteSuccess,
            confirmText = "확인",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryEditBottomSheet(
    categories: List<Category>,
    selectedCategoryCode: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var tempSelection by remember { mutableStateOf(selectedCategoryCode) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "카테고리",
        modifier = modifier,
        content = {
            Column {
                categories.forEachIndexed { index, category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tempSelection = category.code }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = category.name,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (category.code == tempSelection) PrimaryBlue500 else Grayscale900,
                        )
                        if (category.code == tempSelection) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "선택됨",
                                modifier = Modifier.size(20.dp),
                                tint = PrimaryBlue500,
                            )
                        }
                    }
                    if (index < categories.lastIndex) {
                        HorizontalDivider(color = Grayscale200, thickness = 1.dp)
                    }
                }
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = { onConfirm(tempSelection) },
            )
        },
    )
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0 && remainingSeconds > 0) {
        "${minutes}분 ${remainingSeconds}초"
    } else if (minutes > 0) {
        "${minutes}분"
    } else {
        "${remainingSeconds}초"
    }
}
