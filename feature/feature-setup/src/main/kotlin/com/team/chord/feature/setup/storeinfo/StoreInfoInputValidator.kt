package com.team.chord.feature.setup.storeinfo

internal fun parseEmployeeCount(
    input: String,
    ownerSolo: Boolean,
): Int? {
    if (ownerSolo) {
        return 0
    }

    val normalized = normalizeDigitsOnly(input) ?: return null
    val value = normalized.toInt()
    return if (value in 1..99) value else null
}

internal fun sanitizeHourlyWageInput(input: String): String {
    val digitsOnly = input.filter(Char::isDigit)
    if (digitsOnly.isEmpty()) {
        return ""
    }

    val trimmed = digitsOnly.trimStart('0')
    return if (trimmed.isEmpty()) "0" else trimmed
}

internal fun parseHourlyWage(input: String): Int? {
    val normalized = normalizeDigitsOnly(input) ?: return null
    val value = normalized.toInt()
    return if (value in 1..999_999) value else null
}

internal fun isPostStoreNameNextEnabled(
    employeeCountInput: String,
    ownerSolo: Boolean,
    hourlyWageInput: String,
): Boolean {
    return parseEmployeeCount(employeeCountInput, ownerSolo) != null &&
        parseHourlyWage(hourlyWageInput) != null
}

private fun normalizeDigitsOnly(input: String): String? {
    if (input.isEmpty()) {
        return null
    }

    if (!input.all { it.isDigit() }) {
        return null
    }

    val trimmed = input.trimStart('0')
    return if (trimmed.isEmpty()) "0" else trimmed
}
