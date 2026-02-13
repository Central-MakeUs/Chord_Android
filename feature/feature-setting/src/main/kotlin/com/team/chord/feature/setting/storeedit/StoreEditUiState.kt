package com.team.chord.feature.setting.storeedit

import java.text.NumberFormat
import java.util.Locale

data class StoreEditUiState(
    val storeName: String = "코치카페",
    val employeeCountInput: String = "3",
    val ownerSolo: Boolean = false,
    val hourlyWageInput: String = "12000",
    val includeWeeklyHolidayPay: Boolean = false,
    val submitSuccess: Boolean = false,
) {
    val employeeCount: Int?
        get() = parseEmployeeCount(employeeCountInput, ownerSolo)

    val laborCost: Int?
        get() = parseLaborCost(hourlyWageInput)

    val formattedHourlyWage: String
        get() = formatWithComma(hourlyWageInput)

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

internal fun formatWithComma(value: String): String {
    val digits = value.filter { it.isDigit() }
    if (digits.isEmpty()) return ""

    val number = digits.toLongOrNull() ?: return digits
    return NumberFormat.getNumberInstance(Locale.KOREA).format(number)
}
