package com.team.chord.core.network.api

import com.team.chord.core.network.dto.home.HomeMenusResponseDto
import com.team.chord.core.network.dto.home.HomeStrategiesResponseDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @GET("home/menus")
    suspend fun getHomeMenus(): Response<ApiResponse<HomeMenusResponseDto>>

    @GET("home/insights")
    suspend fun getHomeStrategies(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("weekOfMonth") weekOfMonth: Int,
    ): Response<ApiResponse<HomeStrategiesResponseDto>>
}
