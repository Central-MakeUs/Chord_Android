package com.team.chord.core.domain.model.menu

data class MenuTemplate(
    val templateId: Long,
    val menuName: String,
    val defaultSellingPrice: Int,
    val categoryCode: String,
    val workTime: Int,
)
