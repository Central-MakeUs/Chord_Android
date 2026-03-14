package com.team.chord.feature.setting.storeedit

data class StoreEditUiState(
    val storeName: String = "",
    val employeeCountInput: String = "",
    val lastNonZeroEmployeeCountInput: String = "",
    val ownerSolo: Boolean = false,
    val hourlyWageInput: String = "",
    val includeWeeklyHolidayPay: Boolean = false,
    val submitSuccess: Boolean = false,
) {
    val employeeCount: Int?
        get() = parseEmployeeCount(employeeCountInput, ownerSolo)

    val laborCost: Int?
        get() = parseLaborCost(hourlyWageInput)

    val isSubmitEnabled: Boolean
        get() =
            storeName.isNotBlank() &&
                employeeCount != null &&
                laborCost != null
}

internal fun parseEmployeeCount(
    input: String,
    ownerSolo: Boolean,
): Int? {
    if (ownerSolo) {
        return 0
    }

    if (input.isEmpty() || !input.all { it.isDigit() }) {
        return null
    }

    val value = input.toIntOrNull() ?: return null
    return if (value in 1..99) value else null
}

internal fun parseLaborCost(input: String): Int? {
    if (input.isEmpty() || !input.all { it.isDigit() }) {
        return null
    }

    val value = input.toIntOrNull() ?: return null
    return if (value in 1..999_999) value else null
}

internal fun sanitizeHourlyWageInput(input: String): String {
    val digitsOnly = input.filter(Char::isDigit)
    if (digitsOnly.isEmpty()) {
        return ""
    }

    val trimmed = digitsOnly.trimStart('0')
    return if (trimmed.isEmpty()) "0" else trimmed
}
