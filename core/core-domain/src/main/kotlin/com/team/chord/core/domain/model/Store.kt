package com.team.chord.core.domain.model

data class Store(
    val name: String,
    val employees: Int,
    val laborCost: Int,
    val rentCost: Int?,
    val includeWeeklyHolidayPay: Boolean,
)
