package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.ingredient.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeRecentSearchDataSource @Inject constructor() : RecentSearchDataSource {

    private val recentSearches = MutableStateFlow<List<RecentSearch>>(
        listOf(
            RecentSearch(1L, "레몬티", System.currentTimeMillis() - 100000),
            RecentSearch(2L, "말차 가루", System.currentTimeMillis() - 200000),
        )
    )
    private var nextId = 3L

    override fun getRecentSearches(): Flow<List<RecentSearch>> =
        recentSearches.map { it.sortedByDescending { r -> r.timestamp }.take(10) }

    override suspend fun addRecentSearch(query: String) {
        if (query.isBlank()) return
        recentSearches.update { list ->
            val filtered = list.filter { it.query != query }
            listOf(RecentSearch(nextId++, query, System.currentTimeMillis())) + filtered
        }
    }

    override suspend fun deleteRecentSearch(id: Long) {
        recentSearches.update { list -> list.filter { it.id != id } }
    }

    override suspend fun clearRecentSearches() {
        recentSearches.value = emptyList()
    }
}
