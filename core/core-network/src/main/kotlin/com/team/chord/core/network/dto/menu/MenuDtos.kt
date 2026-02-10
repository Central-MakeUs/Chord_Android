package com.team.chord.core.network.dto.menu

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategoryDto(
    val categoryCode: String,
    val categoryName: String,
    val displayOrder: Int,
)

@Serializable
data class SearchMenusDto(
    val templateId: Long,
    val menuName: String,
    val defaultSellingPrice: Double? = null,
    val categoryCode: String? = null,
    val workTime: Int? = null,
)

@Serializable
data class TemplateBasicDto(
    val templateId: Long,
    val menuName: String,
    val defaultSellingPrice: Double,
    val categoryCode: String,
    val workTime: Int,
)

@Serializable
data class RecipeTemplateDto(
    val ingredientName: String,
    val defaultUsageAmount: Double,
    val defaultPrice: Double,
    val unitCode: String,
)

@Serializable
data class MenuDto(
    val menuId: Long,
    val menuName: String,
    val sellingPrice: Double,
    val categoryCode: String? = null,
    val workTime: Int? = null,
    val costRate: Float,
    val marginGradeCode: String,
    val marginGradeName: String,
    val marginRate: Float,
)

@Serializable
data class MenuDetailDto(
    val menuId: Long,
    val menuName: String,
    val workTime: Int,
    val categoryCode: String? = null,
    val sellingPrice: Double,
    val marginRate: Float,
    val totalCost: Double,
    val costRate: Float,
    val contributionMargin: Double,
    val marginGradeCode: String,
    val marginGradeName: String,
    val marginGradeMessage: String,
    val recommendedPrice: Double? = null,
    val recommendedPriceMessage: String? = null,
)

@Serializable
data class RecipeDto(
    val recipeId: Long,
    val menuId: Long,
    val ingredientId: Long,
    val ingredientName: String,
    val amount: Double,
    val unitCode: String,
    val price: Double,
)

@Serializable
data class RecipeListDto(
    val recipes: List<RecipeDto>,
    val totalCost: Double,
)

@Serializable
data class CheckDupRequestDto(
    val menuName: String,
    val ingredientNames: List<String>? = null,
)

@Serializable
data class CheckDupResponseDto(
    val menuNameDuplicate: Boolean,
    val dupIngredientNames: List<String>,
)

@Serializable
data class MenuCreateRequestDto(
    val menuCategoryCode: String,
    val menuName: String,
    val sellingPrice: Int,
    val workTime: Int,
    val recipes: List<RecipeCreateRequestDto>? = null,
    val newRecipes: List<NewRecipeCreateRequestDto>? = null,
)

@Serializable
data class RecipeCreateRequestDto(
    val ingredientId: Long? = null,
    val amount: Int,
)

@Serializable
data class NewRecipeCreateRequestDto(
    val amount: Int,
    val price: Int,
    val unitCode: String,
    val ingredientCategoryCode: String,
    val ingredientName: String,
    val supplier: String? = null,
)

@Serializable
data class MenuNameUpdateDto(
    val menuName: String,
)

@Serializable
data class MenuPriceUpdateDto(
    val sellingPrice: Int,
)

@Serializable
data class MenuCategoryUpdateDto(
    val category: String,
)

@Serializable
data class MenuWorktimeUpdateDto(
    val workTime: Int,
)

@Serializable
data class AmountUpdateDto(
    val amount: Int,
)

@Serializable
data class DeleteRecipesDto(
    val recipeIds: List<Long>? = null,
)
