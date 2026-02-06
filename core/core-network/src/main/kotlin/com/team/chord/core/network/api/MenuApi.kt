package com.team.chord.core.network.api

import com.team.chord.core.network.dto.menu.AmountUpdateDto
import com.team.chord.core.network.dto.menu.CheckDupRequestDto
import com.team.chord.core.network.dto.menu.CheckDupResponseDto
import com.team.chord.core.network.dto.menu.DeleteRecipesDto
import com.team.chord.core.network.dto.menu.MenuCategoryDto
import com.team.chord.core.network.dto.menu.MenuCategoryUpdateDto
import com.team.chord.core.network.dto.menu.MenuCreateRequestDto
import com.team.chord.core.network.dto.menu.MenuDetailDto
import com.team.chord.core.network.dto.menu.MenuDto
import com.team.chord.core.network.dto.menu.MenuNameUpdateDto
import com.team.chord.core.network.dto.menu.MenuPriceUpdateDto
import com.team.chord.core.network.dto.menu.MenuWorktimeUpdateDto
import com.team.chord.core.network.dto.menu.NewRecipeCreateRequestDto
import com.team.chord.core.network.dto.menu.RecipeCreateRequestDto
import com.team.chord.core.network.dto.menu.RecipeListDto
import com.team.chord.core.network.dto.menu.RecipeTemplateDto
import com.team.chord.core.network.dto.menu.SearchMenusDto
import com.team.chord.core.network.dto.menu.TemplateBasicDto
import com.team.chord.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MenuApi {
    @GET("catalog/menu-categories")
    suspend fun getCategories(): Response<ApiResponse<List<MenuCategoryDto>>>

    @GET("catalog/menus/search")
    suspend fun searchMenuTemplates(
        @Query("keyword") keyword: String?,
    ): Response<ApiResponse<List<SearchMenusDto>>>

    @GET("catalog/menus/templates/{templateId}")
    suspend fun getTemplateBasic(
        @Path("templateId") templateId: Long,
    ): Response<ApiResponse<TemplateBasicDto>>

    @GET("catalog/menus/templates/{templateId}/ingredients")
    suspend fun getTemplateIngredients(
        @Path("templateId") templateId: Long,
    ): Response<ApiResponse<List<RecipeTemplateDto>>>

    @GET("catalog/menus")
    suspend fun getMenuList(
        @Query("category") categoryCode: String? = null,
    ): Response<ApiResponse<List<MenuDto>>>

    @GET("catalog/menus/{menuId}")
    suspend fun getMenuDetail(
        @Path("menuId") menuId: Long,
    ): Response<ApiResponse<MenuDetailDto>>

    @GET("catalog/menus/{menuId}/recipes")
    suspend fun getMenuRecipes(
        @Path("menuId") menuId: Long,
    ): Response<ApiResponse<RecipeListDto>>

    @POST("catalog/menus/check-dup")
    suspend fun checkMenuDuplicate(
        @Body request: CheckDupRequestDto,
    ): Response<ApiResponse<CheckDupResponseDto>>

    @POST("catalog/menus")
    suspend fun createMenu(
        @Body request: MenuCreateRequestDto,
    ): Response<ApiResponse<Unit>>

    @POST("catalog/menus/{menuId}/recipes/existing")
    suspend fun addExistingRecipe(
        @Path("menuId") menuId: Long,
        @Body request: RecipeCreateRequestDto,
    ): Response<ApiResponse<Unit>>

    @POST("catalog/menus/{menuId}/recipes/new")
    suspend fun addNewRecipe(
        @Path("menuId") menuId: Long,
        @Body request: NewRecipeCreateRequestDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/menus/{menuId}")
    suspend fun updateMenuName(
        @Path("menuId") menuId: Long,
        @Body request: MenuNameUpdateDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/menus/{menuId}/price")
    suspend fun updateMenuPrice(
        @Path("menuId") menuId: Long,
        @Body request: MenuPriceUpdateDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/menus/{menuId}/category")
    suspend fun updateMenuCategory(
        @Path("menuId") menuId: Long,
        @Body request: MenuCategoryUpdateDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/menus/{menuId}/worktime")
    suspend fun updateMenuWorktime(
        @Path("menuId") menuId: Long,
        @Body request: MenuWorktimeUpdateDto,
    ): Response<ApiResponse<Unit>>

    @PATCH("catalog/menus/{menuId}/recipes/{recipeId}")
    suspend fun updateRecipeAmount(
        @Path("menuId") menuId: Long,
        @Path("recipeId") recipeId: Long,
        @Body request: AmountUpdateDto,
    ): Response<ApiResponse<Unit>>

    @HTTP(method = "DELETE", path = "catalog/menus/{menuId}/recipes", hasBody = true)
    suspend fun deleteRecipes(
        @Path("menuId") menuId: Long,
        @Body request: DeleteRecipesDto,
    ): Response<ApiResponse<Unit>>

    @DELETE("catalog/menus/{menuId}")
    suspend fun deleteMenu(
        @Path("menuId") menuId: Long,
    ): Response<ApiResponse<Unit>>
}
