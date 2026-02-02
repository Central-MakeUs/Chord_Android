package com.team.chord.feature.setup.storeinfo

data class StoreInfoUiState(
    val screenState: StoreInfoScreenState = StoreInfoScreenState.StoreNameInput,
    val storeName: String = "",
    val employeeCount: Int = 1,
    val employeeCountInput: String = "",
    val ownerSolo: Boolean = false,
    val hourlyWageInput: String = "",
    val includeWeeklyAllowance: Boolean = false,
    val isEmployeeCountBottomSheetVisible: Boolean = false,
) {
    val normalizedEmployeeCountInput: String?
        get() = normalizeEmployeeCountInput(employeeCountInput, ownerSolo)

    val employeeCountValue: Int?
        get() = parseEmployeeCount(employeeCountInput, ownerSolo)

    val normalizedHourlyWageInput: String?
        get() = normalizeHourlyWageInput(hourlyWageInput)

    val hourlyWageValue: Int?
        get() = parseHourlyWage(hourlyWageInput)

    val isPostStoreNameNextEnabled: Boolean
        get() = isPostStoreNameNextEnabled(employeeCountInput, ownerSolo, hourlyWageInput)
}

sealed interface StoreInfoScreenState {
    data object StoreNameInput : StoreInfoScreenState
    data object PostStoreName : StoreInfoScreenState
    data object Completed : StoreInfoScreenState
}
