package com.team.chord.core.network.api

import com.team.chord.core.network.dto.strategy.StrategyDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface StrategyApi {
    @GET("insights/strategies/weekly")
    suspend fun getWeeklyStrategies(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("weekOfMonth") weekOfMonth: Int,
    ): Response<ApiResponse<List<StrategyDto>>>

    @GET("insights/strategies/saved")
    suspend fun getSavedStrategies(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("isCompleted") isCompleted: Boolean,
    ): Response<ApiResponse<List<StrategyDto>>>

    @PATCH("insights/strategies/{strategyId}/start")
    suspend fun startStrategy(
        @Path("strategyId") strategyId: Long,
        @Query("type") type: String,
    ): Response<ApiResponse<Unit>>

    @PATCH("insights/strategies/{strategyId}/save")
    suspend fun saveStrategy(
        @Path("strategyId") strategyId: Long,
        @Query("type") type: String,
        @Query("isSaved") isSaved: Boolean,
    ): Response<ApiResponse<Unit>>
}
