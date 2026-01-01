package com.team.chord.feature.setup.menumanagement

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.feature.setup.component.SetupButton
import com.team.chord.feature.setup.component.SetupTopBar
import com.team.chord.feature.setup.component.SmallDropdown
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuManagementScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMenuEntry: () -> Unit,
    onNavigateToComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuManagementViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MenuManagementScreenContent(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onAddClick = onNavigateToMenuEntry,
        onDeleteModeToggle = viewModel::onDeleteModeToggle,
        onMenuSelected = viewModel::onMenuSelected,
        onDeleteSelectedMenus = viewModel::onDeleteSelectedMenus,
        onMenuStatusChanged = viewModel::onMenuStatusChanged,
        onMenuStatusDropdownToggle = viewModel::onMenuStatusDropdownToggle,
        onMenuStatusDropdownDismiss = viewModel::onMenuStatusDropdownDismiss,
        onCompleteClick = onNavigateToComplete,
        modifier = modifier,
    )
}

@Composable
internal fun MenuManagementScreenContent(
    uiState: MenuManagementUiState,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteModeToggle: () -> Unit,
    onMenuSelected: (String) -> Unit,
    onDeleteSelectedMenus: () -> Unit,
    onMenuStatusChanged: (String, MenuStatus) -> Unit,
    onMenuStatusDropdownToggle: (String) -> Unit,
    onMenuStatusDropdownDismiss: (String) -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
    ) {
        SetupTopBar(
            title = "메뉴 관리",
            onBackClick = onBackClick,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ActionButton(
                icon = Icons.Default.Delete,
                text =
                    if (uiState.isDeleteMode && uiState.selectedMenuIds.isNotEmpty()) {
                        "삭제 (${uiState.selectedMenuIds.size})"
                    } else {
                        "삭제"
                    },
                onClick = {
                    if (uiState.isDeleteMode && uiState.selectedMenuIds.isNotEmpty()) {
                        onDeleteSelectedMenus()
                    } else {
                        onDeleteModeToggle()
                    }
                },
                isActive = uiState.isDeleteMode,
            )
            ActionButton(
                icon = Icons.Default.Add,
                text = "추가",
                onClick = onAddClick,
            )
        }

        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
        ) {
            if (uiState.beverageMenus.isNotEmpty()) {
                item {
                    SectionHeader(title = "음료")
                }
                items(uiState.beverageMenus, key = { it.id }) { menu ->
                    MenuItemRow(
                        menu = menu,
                        isDeleteMode = uiState.isDeleteMode,
                        isSelected = menu.id in uiState.selectedMenuIds,
                        onMenuSelected = { onMenuSelected(menu.id) },
                        onStatusChanged = { status -> onMenuStatusChanged(menu.id, status) },
                        onStatusDropdownToggle = { onMenuStatusDropdownToggle(menu.id) },
                        onStatusDropdownDismiss = { onMenuStatusDropdownDismiss(menu.id) },
                    )
                    HorizontalDivider(color = Grayscale300, thickness = 1.dp)
                }
            }

            if (uiState.foodMenus.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "푸드")
                }
                items(uiState.foodMenus, key = { it.id }) { menu ->
                    MenuItemRow(
                        menu = menu,
                        isDeleteMode = uiState.isDeleteMode,
                        isSelected = menu.id in uiState.selectedMenuIds,
                        onMenuSelected = { onMenuSelected(menu.id) },
                        onStatusChanged = { status -> onMenuStatusChanged(menu.id, status) },
                        onStatusDropdownToggle = { onMenuStatusDropdownToggle(menu.id) },
                        onStatusDropdownDismiss = { onMenuStatusDropdownDismiss(menu.id) },
                    )
                    HorizontalDivider(color = Grayscale300, thickness = 1.dp)
                }
            }

            if (uiState.beverageMenus.isEmpty() && uiState.foodMenus.isEmpty()) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "등록된 메뉴가 없습니다.\n'추가' 버튼을 눌러 메뉴를 등록해주세요.",
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Grayscale500,
                        )
                    }
                }
            }
        }

        SetupButton(
            text = "완료",
            onClick = onCompleteClick,
            enabled = uiState.isCompleteEnabled,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        )
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
) {
    Row(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = if (isActive) PrimaryBlue600 else Grayscale300,
                    shape = RoundedCornerShape(8.dp),
                ).clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(18.dp),
            tint = if (isActive) PrimaryBlue600 else Grayscale500,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = if (isActive) PrimaryBlue600 else Grayscale900,
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Grayscale900,
        modifier = modifier.padding(vertical = 12.dp),
    )
}

@Composable
private fun MenuItemRow(
    menu: MenuItem,
    isDeleteMode: Boolean,
    isSelected: Boolean,
    onMenuSelected: () -> Unit,
    onStatusChanged: (MenuStatus) -> Unit,
    onStatusDropdownToggle: () -> Unit,
    onStatusDropdownDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(enabled = isDeleteMode, onClick = onMenuSelected)
                .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isDeleteMode) {
            Icon(
                imageVector = if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = if (isSelected) "선택됨" else "선택 안됨",
                modifier =
                    Modifier
                        .size(24.dp)
                        .clickable(onClick = onMenuSelected),
                tint = if (isSelected) PrimaryBlue600 else Grayscale300,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = menu.name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Grayscale900,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(menu.price)}원",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale500,
            )
        }

        if (!isDeleteMode) {
            SmallDropdown(
                selectedValue = menu.status.displayName,
                options = MenuStatus.entries.map { it.displayName },
                expanded = menu.isStatusDropdownExpanded,
                onExpandedChange = onStatusDropdownToggle,
                onOptionSelected = { displayName ->
                    MenuStatus.entries.find { it.displayName == displayName }?.let {
                        onStatusChanged(it)
                    }
                },
                onDismiss = onStatusDropdownDismiss,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "순서 변경",
                modifier = Modifier.size(24.dp),
                tint = Grayscale500,
            )
        }
    }
}
