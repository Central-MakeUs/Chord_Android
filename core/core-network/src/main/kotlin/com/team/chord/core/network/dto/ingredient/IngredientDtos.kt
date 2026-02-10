package com.team.chord.core.network.dto.ingredient

import kotlinx.serialization.Serializable

@Serializable
data class IngredientCategoryDto(
    val categoryCode: String,
    val categoryName: String,
    val displayOrder: Int,
)

@Serializable
data class IngredientDto(
    val ingredientId: Long,
    val ingredientCategoryCode: String,
    val ingredientName: String,
    val unitCode: String,
    val baseQuantity: Int,
    val currentUnitPrice: Double,
)

@Serializable
data class IngredientDetailDto(
    val ingredientId: Long,
    val ingredientCategoryCode: String? = null,
    val ingredientName: String,
    val unitPrice: Int,
    val baseQuantity: Int,
    val unitCode: String,
    val supplier: String? = null,
    val menus: List<UsedMenuDto> = emptyList(),
    val originalAmount: Int? = null,
    val originalPrice: Int? = null,
    val isFavorite: Boolean = false,
)

@Serializable
data class UsedMenuDto(
    val menuId: Long? = null,
    val menuName: String? = null,
    val usageAmount: String? = null,
)

@Serializable
data class PriceHistoryDto(
    val historyId: Long,
    val changeDate: String,
    val unitPrice: Int,
    val unitCode: String,
    val baseQuantity: Int,
)

@Serializable
data class SearchIngredientDto(
    val isTemplate: Boolean,
    val templateId: Long? = null,
    val ingredientId: Long? = null,
    val ingredientName: String,
    val categoryCode: String? = null,
    val unitPrice: Int? = null,
    val unitCode: String? = null,
    val baseQuantity: Int? = null,
    val supplier: String? = null,
)

@Serializable
data class SearchMyIngredientDto(
    val ingredientId: Long,
    val ingredientName: String,
    val ingredientCategoryCode: String? = null,
    val unitCode: String? = null,
    val baseQuantity: Int? = null,
    val currentUnitPrice: Int? = null,
    val supplier: String? = null,
)

@Serializable
data class IngredientCreateRequestDto(
    val categoryCode: String,
    val ingredientName: String,
    val unitCode: String,
    val price: Int,
    val amount: Int,
    val supplier: String? = null,
)

@Serializable
data class IngredientUpdateDto(
    val category: String,
    val price: Int,
    val amount: Int,
    val unitCode: String,
)

@Serializable
data class SupplierUpdateDto(
    val supplier: String? = null,
)
