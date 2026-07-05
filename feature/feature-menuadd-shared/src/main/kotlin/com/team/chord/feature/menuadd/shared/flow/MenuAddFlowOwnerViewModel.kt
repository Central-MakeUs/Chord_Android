package com.team.chord.feature.menuadd.shared.flow

import androidx.lifecycle.ViewModel
import com.team.chord.core.analytics.Analytics
import com.team.chord.core.analytics.AnalyticsEvent
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.NewRecipeInfo
import com.team.chord.core.domain.repository.MenuRepository
import com.team.chord.feature.menuadd.shared.confirm.IngredientSummary
import com.team.chord.feature.menuadd.shared.confirm.RegisteredMenuSummary
import com.team.chord.feature.menuadd.shared.ingredientinput.IngredientSourceType
import com.team.chord.feature.menuadd.shared.ingredientinput.SelectedIngredient
import com.team.chord.feature.menuadd.shared.menudetail.MenuCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MenuAddFlowOwnerViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
) : ViewModel() {
    private val _registeredMenu = MutableStateFlow<RegisteredMenu?>(null)
    val registeredMenu: StateFlow<RegisteredMenu?> = _registeredMenu.asStateFlow()

    private val _currentMenuDraft = MutableStateFlow<MenuDraft?>(null)
    val currentMenuDraft: StateFlow<MenuDraft?> = _currentMenuDraft.asStateFlow()

    private var _defaultCategory: MenuCategory = MenuCategory.BEVERAGE
    val defaultCategory: MenuCategory get() = _defaultCategory

    private var _initialCategoryCode: String? = null
    val initialCategoryCode: String? get() = _initialCategoryCode

    fun setDefaultCategory(category: MenuCategory) {
        _defaultCategory = category
    }

    fun setInitialCategoryCode(code: String) {
        _initialCategoryCode = code
    }

    fun startNewMenu(
        name: String,
        isTemplateApplied: Boolean,
        templatePrice: Int? = null,
        templateWorkSeconds: Int? = null,
        templateId: Long? = null,
        categoryCode: String? = null,
    ) {
        Analytics.track(AnalyticsEvent.MenuRegistrationStarted)
        _currentMenuDraft.update {
            MenuDraft(
                name = name,
                price = templatePrice ?: 0,
                preparationTimeSeconds = templateWorkSeconds ?: DEFAULT_PREPARATION_TIME_SECONDS,
                isTemplateApplied = isTemplateApplied,
                templateId = templateId,
                categoryCode = categoryCode ?: _initialCategoryCode,
            )
        }
    }

    fun updateMenuDetail(
        price: Int,
        category: MenuCategory,
        preparationSeconds: Int,
    ) {
        _currentMenuDraft.update { current ->
            current?.copy(
                price = price,
                category = category,
                preparationTimeSeconds = preparationSeconds,
            )
        }
    }

    fun addIngredients(ingredients: List<SelectedIngredient>) {
        _currentMenuDraft.update { current ->
            current?.copy(ingredients = ingredients)
        }
    }

    fun completeCurrentMenu() {
        val draft = _currentMenuDraft.value ?: return
        val completedMenu = RegisteredMenu(
            name = draft.name,
            price = draft.price,
            category = draft.category,
            preparationTimeSeconds = draft.preparationTimeSeconds,
            ingredients = draft.ingredients,
            categoryCode = draft.categoryCode,
        )
        _registeredMenu.update { completedMenu }
        _currentMenuDraft.update { null }
    }

    fun getRegisteredMenuSummary(): RegisteredMenuSummary? {
        val menu = _registeredMenu.value ?: return null
        return RegisteredMenuSummary(
            name = menu.name,
            price = menu.price,
            ingredients = menu.ingredients.map { ingredient ->
                IngredientSummary(
                    name = ingredient.name,
                    amount = "${ingredient.amount}${ingredient.unit.displayName}",
                    price = ingredient.price,
                )
            },
        )
    }

    suspend fun registerMenus(): Result<Unit> {
        val menu = _registeredMenu.value ?: return Result.Success(Unit)
        val existingRecipes = menu.ingredients.mapNotNull { it.toExistingRecipe() }
        val newRecipes = menu.ingredients.mapNotNull { it.toNewRecipeInfo() }
        val result = menuRepository.createMenu(
            categoryCode = menu.categoryCode ?: menu.category.name,
            menuName = menu.name,
            sellingPrice = menu.price,
            workTime = menu.preparationTimeSeconds,
            recipes = existingRecipes.ifEmpty { null },
            newRecipes = newRecipes.ifEmpty { null },
        )
        if (result is Result.Success) {
            Analytics.track(AnalyticsEvent.MenuRegistrationCompleted)
        }
        return result
    }

    fun clearAll() {
        _registeredMenu.update { null }
        _currentMenuDraft.update { null }
        _initialCategoryCode = null
        _defaultCategory = MenuCategory.BEVERAGE
    }
}

data class MenuDraft(
    val name: String = "",
    val price: Int = 0,
    val category: MenuCategory = MenuCategory.BEVERAGE,
    val preparationTimeSeconds: Int = 90,
    val ingredients: List<SelectedIngredient> = emptyList(),
    val isTemplateApplied: Boolean = false,
    val templateId: Long? = null,
    val categoryCode: String? = null,
)

data class RegisteredMenu(
    val name: String,
    val price: Int,
    val category: MenuCategory,
    val preparationTimeSeconds: Int,
    val ingredients: List<SelectedIngredient>,
    val categoryCode: String? = null,
)

private fun SelectedIngredient.toExistingRecipe(): MenuRecipe? {
    val ingredientId = serverIngredientId?.takeIf { it > 0L } ?: return null
    return MenuRecipe(
        recipeId = 0L,
        menuId = 0L,
        ingredientId = ingredientId,
        ingredientName = name,
        amount = amount.toDouble(),
        unitCode = unit.name,
        price = price,
    )
}

private fun SelectedIngredient.toNewRecipeInfo(): NewRecipeInfo? {
    val ingredientId = serverIngredientId
    return when {
        sourceType == IngredientSourceType.NEW -> NewRecipeInfo(
            amount = baseQuantity,
            usageAmount = amount,
            price = price,
            unitCode = unit.name,
            ingredientCategoryCode = categoryCode,
            ingredientName = name,
            supplier = supplier.ifEmpty { null },
        )
        sourceType == IngredientSourceType.TEMPLATE && (ingredientId == null || ingredientId <= 0L) -> NewRecipeInfo(
            amount = baseQuantity,
            usageAmount = amount,
            price = unitPrice,
            unitCode = unit.name,
            ingredientCategoryCode = categoryCode,
            ingredientName = name,
            supplier = supplier.ifEmpty { null },
        )
        else -> null
    }
}

private const val DEFAULT_PREPARATION_TIME_SECONDS = 90
