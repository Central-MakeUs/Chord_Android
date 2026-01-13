package com.team.chord.feature.setup.storeinfo

data class StoreInfoUiState(
    val screenState: StoreInfoScreenState = StoreInfoScreenState.StoreNameInput,
    val storeName: String = "",
    val location: String = "",
    val employeeCount: Int = 1,
    val isEmployeeCountBottomSheetVisible: Boolean = false,
    val isConfirmEnabled: Boolean = false,
)

sealed interface StoreInfoScreenState {
    data object StoreNameInput : StoreInfoScreenState
    data object LocationInput : StoreInfoScreenState
    data object Confirmation : StoreInfoScreenState
    data object Completed : StoreInfoScreenState
}
