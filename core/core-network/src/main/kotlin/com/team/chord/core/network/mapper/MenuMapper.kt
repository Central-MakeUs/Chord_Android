package com.team.chord.core.network.mapper

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import com.team.chord.core.network.dto.menu.CheckDupResponseDto
import com.team.chord.core.network.dto.menu.MenuCategoryDto
import com.team.chord.core.network.dto.menu.MenuDetailDto
import com.team.chord.core.network.dto.menu.MenuDto
import com.team.chord.core.network.dto.menu.RecipeDto
import com.team.chord.core.network.dto.menu.SearchMenusDto
import com.team.chord.core.network.dto.menu.TemplateBasicDto

fun MenuCategoryDto.toDomain(): Category =
    Category(
        code = categoryCode,
        name = categoryName,
        displayOrder = displayOrder,
    )

fun MenuDto.toDomain(): Menu =
    Menu(
        id = menuId,
        name = menuName,
        price = sellingPrice,
        categoryCode = "",
        preparationTimeSeconds = 0,
        ingredients = emptyList(),
        totalCost = 0,
        costRatio = costRate,
        marginRatio = marginRate,
        contributionProfit = 0,
        marginGrade = MarginGrade(code = marginGradeCode, name = marginGradeName, message = ""),
        recommendedPrice = null,
        recommendedPriceMessage = null,
    )

fun MenuDetailDto.toDomain(): Menu =
    Menu(
        id = menuId,
        name = menuName,
        price = sellingPrice,
        categoryCode = "",
        preparationTimeSeconds = workTime,
        ingredients = emptyList(),
        totalCost = totalCost,
        costRatio = costRate,
        marginRatio = marginRate,
        contributionProfit = contributionMargin,
        marginGrade = MarginGrade(code = marginGradeCode, name = marginGradeName, message = marginGradeMessage),
        recommendedPrice = recommendedPrice,
        recommendedPriceMessage = null,
    )

fun RecipeDto.toDomain(): MenuRecipe =
    MenuRecipe(
        recipeId = recipeId,
        menuId = menuId,
        ingredientId = ingredientId,
        ingredientName = ingredientName,
        amount = amount,
        unitCode = unitCode,
        price = price,
    )

fun SearchMenusDto.toDomain(): MenuTemplate =
    MenuTemplate(
        templateId = templateId,
        menuName = menuName,
        defaultSellingPrice = 0,
        categoryCode = "",
        workTime = 0,
    )

fun TemplateBasicDto.toDomain(): MenuTemplate =
    MenuTemplate(
        templateId = templateId,
        menuName = menuName,
        defaultSellingPrice = defaultSellingPrice,
        categoryCode = categoryCode,
        workTime = workTime,
    )

fun CheckDupResponseDto.toDomain(): CheckDupResult =
    CheckDupResult(
        menuNameDuplicate = menuNameDuplicate,
        dupIngredientNames = dupIngredientNames,
    )
