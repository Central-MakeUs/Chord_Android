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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordOutlinedButton
import com.team.chord.core.ui.component.ChordRadioGroup
import com.team.chord.core.ui.component.ChordTopAppBarWithBackTitle
import com.team.chord.core.ui.component.ChordOneButtonDialog
import com.team.chord.core.ui.component.ChordTwoButtonDialog
import com.team.chord.core.ui.component.RadioOption
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.management.component.EditMenuNameBottomSheet
import com.team.chord.feature.menu.management.component.EditPriceBottomSheet
import com.team.chord.feature.menu.management.component.EditPreparationTimeBottomSheet
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
    onCategorySelected: (Long) -> Unit,
    onShowDeleteDialog: () -> Unit,
    onHideDeleteDialog: () -> Unit,
    onConfirmDelete: () -> Unit,
    onConfirmDeleteSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBarWithBackTitle(
            title = "관리",
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
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                // 메뉴명 섹션
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowNameBottomSheet() }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = uiState.menuName,
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Grayscale900,
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = "메뉴명 수정",
                        modifier = Modifier.size(20.dp),
                        tint = Grayscale600,
                    )
                }

                // 구분선 (스페이서)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Grayscale200),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 가격 섹션
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowPriceBottomSheet() }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "가격",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Grayscale700,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${numberFormat.format(uiState.price)}원",
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = Grayscale900,
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Grayscale700,
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Grayscale300,
                )

                // 제조시간 섹션
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowTimeBottomSheet() }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "제조시간",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Grayscale700,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = formatTime(uiState.preparationTimeSeconds),
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
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

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Grayscale300,
                )

                // 카테고리 섹션
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "카테고리",
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Grayscale700,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                ChordRadioGroup(
                    options = uiState.categories.map { RadioOption(it.id.toString(), it.name) },
                    selectedOptionId = uiState.selectedCategoryId.toString(),
                    onOptionSelected = { id -> onCategorySelected(id.toLong()) },
                    modifier = Modifier.padding(horizontal = 20.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                // 메뉴 삭제 버튼
                ChordOutlinedButton(
                    text = "메뉴 삭제",
                    onClick = onShowDeleteDialog,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                )
            }
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
