package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.ingredient.Ingredient
import com.team.chord.core.domain.model.ingredient.IngredientCategory
import com.team.chord.core.domain.model.ingredient.IngredientSearchResult
import com.team.chord.core.domain.model.ingredient.PriceHistoryItem
import com.team.chord.core.domain.model.ingredient.UsedMenu
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.network.dto.ingredient.IngredientCategoryDto
import com.team.chord.core.network.dto.ingredient.IngredientDetailDto
import com.team.chord.core.network.dto.ingredient.IngredientDto
import com.team.chord.core.network.dto.ingredient.PriceHistoryDto
import com.team.chord.core.network.dto.ingredient.SearchIngredientDto

fun IngredientCategoryDto.toDomain(): IngredientCategory =
    IngredientCategory(
        code = categoryCode,
        name = categoryName,
        displayOrder = displayOrder,
    )

fun IngredientDto.toDomain(): Ingredient =
    Ingredient(
        id = ingredientId,
        name = ingredientName,
        categoryCode = ingredientCategoryCode,
        unit = unitCode.toIngredientUnit(),
        baseQuantity = baseQuantity,
        currentUnitPrice = currentUnitPrice,
    )

fun IngredientDetailDto.toDomain(): Ingredient =
    Ingredient(
        id = ingredientId,
        name = ingredientName,
        categoryCode = ingredientCategoryCode.orEmpty(),
        unit = unitCode.toIngredientUnit(),
        baseQuantity = baseQuantity,
        currentUnitPrice = unitPrice,
        supplier = supplier,
        isFavorite = isFavorite,
        originalAmount = originalAmount,
        originalPrice = originalPrice,
        usedMenus = menus.map { menu ->
            UsedMenu(
                id = menu.menuId ?: 0L,
                name = menu.menuName ?: "",
                usageAmount = menu.usageAmount ?: "",
            )
        },
    )

fun PriceHistoryDto.toDomain(): PriceHistoryItem =
    PriceHistoryItem(
        id = historyId,
        date = changeDate,
        price = unitPrice,
        unitAmount = baseQuantity,
        unit = unitCode.toIngredientUnit(),
    )

fun SearchIngredientDto.toDomain(): IngredientSearchResult =
    IngredientSearchResult(
        isTemplate = isTemplate,
        templateId = templateId,
        ingredientId = ingredientId,
        ingredientName = ingredientName,
        categoryCode = categoryCode,
        unitPrice = unitPrice,
        unitCode = unitCode,
        baseQuantity = baseQuantity,
        supplier = supplier,
    )

fun String.toIngredientUnit(): IngredientUnit =
    when (this.uppercase()) {
        "ML" -> IngredientUnit.ML
        "G" -> IngredientUnit.G
        "KG" -> IngredientUnit.KG
        "EA" -> IngredientUnit.EA
        else -> IngredientUnit.G
    }
