package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.RecentSearch
import kotlinx.coroutines.flow.Flow

interface RecentSearchDataSource {
    fun getRecentSearches(): Flow<List<RecentSearch>>
    suspend fun addRecentSearch(query: String)
    suspend fun deleteRecentSearch(id: Long)
    suspend fun clearRecentSearches()
}
