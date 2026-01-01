package com.team.chord.feature.setup.storeinfo

data class StoreInfoUiState(
    val storeName: String = "",
    val location: String = "",
    val employeeCount: String = "",
    val isEmployeeCountDropdownExpanded: Boolean = false,
    val isNextEnabled: Boolean = false,
)

val employeeCountOptions = listOf("1명", "2명", "3명", "4명", "5명 이상")
