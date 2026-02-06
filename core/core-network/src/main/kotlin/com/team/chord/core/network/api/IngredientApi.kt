package com.team.chord.core.network.api

import com.team.chord.core.network.dto.ingredient.IngredientCategoryDto
import com.team.chord.core.network.dto.ingredient.IngredientCreateRequestDto
import com.team.chord.core.network.dto.ingredient.IngredientDetailDto
import com.team.chord.core.network.dto.ingredient.IngredientDto
import com.team.chord.core.network.dto.ingredient.IngredientUpdateDto
import com.team.chord.core.network.dto.ingredient.PriceHistoryDto
import com.team.chord.core.network.dto.ingredient.SearchIngredientDto
import com.team.chord.core.network.dto.ingredient.SearchMyIngredientDto
import com.team.chord.core.network.dto.ingredient.SupplierUpdateDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IngredientApi {
    @GET("catalog/ingredient-categories")
    suspend fun getCategories(): Response<ApiResponse<List<IngredientCategoryDto>>>

    @GET("catalog/ingredients")
    suspend fun getIngredientList(
        @Query("category") categoryCode: String? = null,
    ): Response<ApiResponse<List<IngredientDto>>>

    @GET("catalog/ingredients/{ingredientId}")
    suspend fun getIngredientDetail(
        @Path("ingredientId") ingredientId: Long,
    ): Response<ApiResponse<IngredientDetailDto>>

    @GET("catalog/ingredients/{ingredientId}/price-history")
    suspend fun getPriceHistory(
        @Path("ingredientId") ingredientId: Long,
    ): Response<ApiResponse<List<PriceHistoryDto>>>

    @GET("catalog/ingredients/search")
    suspend fun searchIngredients(
        @Query("keyword") keyword: String?,
    ): Response<ApiResponse<List<SearchIngredientDto>>>

    @GET("catalog/ingredients/check-dup")
    suspend fun checkDuplicate(
        @Query("name") name: String,
    ): Response<ApiResponse<Unit>>

    @GET("catalog/ingredients/search/my")
    suspend fun searchMyIngredients(
        @Query("keyword") keyword: String?,
    ): Response<ApiResponse<List<SearchMyIngredientDto>>>

    @POST("catalog/ingredients")
    suspend fun createIngredient(
        @Body request: IngredientCreateRequestDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/ingredients/{ingredientId}/favorite")
    suspend fun toggleFavorite(
        @Path("ingredientId") ingredientId: Long,
        @Query("favorite") favorite: Boolean,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/ingredients/{ingredientId}/supplier")
    suspend fun updateSupplier(
        @Path("ingredientId") ingredientId: Long,
        @Body request: SupplierUpdateDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/ingredients/{ingredientId}")
    suspend fun updateIngredient(
        @Path("ingredientId") ingredientId: Long,
        @Body request: IngredientUpdateDto,
    ): Response<ApiResponse<Unit>>

    @DELETE("catalog/ingredients/{ingredientId}")
    suspend fun deleteIngredient(
        @Path("ingredientId") ingredientId: Long,
    ): Response<ApiResponse<Unit>>
}
