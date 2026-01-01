package com.team.chord.feature.setup.menumanagement

import com.team.chord.feature.setup.menuentry.MenuCategory

data class MenuManagementUiState(
    val beverageMenus: List<MenuItem> = emptyList(),
    val foodMenus: List<MenuItem> = emptyList(),
    val selectedMenuIds: Set<String> = emptySet(),
    val isDeleteMode: Boolean = false,
    val isCompleteEnabled: Boolean = false,
)

data class MenuItem(
    val id: String,
    val name: String,
    val price: Int,
    val category: MenuCategory,
    val status: MenuStatus = MenuStatus.ON_SALE,
    val isStatusDropdownExpanded: Boolean = false,
)

enum class MenuStatus(
    val displayName: String,
) {
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    HIDDEN("숨김"),
}
