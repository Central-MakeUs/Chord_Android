package com.team.chord.core.domain.model.ingredient

data class RecentSearch(
    val id: Long,
    val query: String,
    val timestamp: Long,
)
