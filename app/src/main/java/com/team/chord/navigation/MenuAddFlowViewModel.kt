package com.team.chord.navigation

import androidx.lifecycle.ViewModel
import com.team.chord.core.domain.model.Result
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.NewRecipeInfo
import com.team.chord.core.domain.repository.MenuRepository
import com.team.chord.feature.menu.add.confirm.IngredientSummary
import com.team.chord.feature.menu.add.confirm.RegisteredMenuSummary
import com.team.chord.feature.menu.add.ingredientinput.SelectedIngredient
import com.team.chord.feature.menu.add.menudetail.MenuCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuAddFlowViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
) : ViewModel() {
    private val _registeredMenus = MutableStateFlow<List<RegisteredMenu>>(emptyList())
    val registeredMenus: StateFlow<List<RegisteredMenu>> = _registeredMenus.asStateFlow()

    private val _currentMenuDraft = MutableStateFlow<MenuDraft?>(null)
    val currentMenuDraft: StateFlow<MenuDraft?> = _currentMenuDraft.asStateFlow()

    fun startNewMenu(
        name: String,
        isTemplateApplied: Boolean,
        templatePrice: Int? = null,
        templateId: Long? = null,
        categoryCode: String? = null,
    ) {
        _currentMenuDraft.update {
            MenuDraft(
                name = name,
                price = templatePrice ?: 0,
                isTemplateApplied = isTemplateApplied,
                templateId = templateId,
                categoryCode = categoryCode,
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

        val registeredMenu = RegisteredMenu(
            name = draft.name,
            price = draft.price,
            category = draft.category,
            preparationTimeSeconds = draft.preparationTimeSeconds,
            ingredients = draft.ingredients,
            categoryCode = draft.categoryCode,
        )

        _registeredMenus.update { current ->
            current + registeredMenu
        }

        _currentMenuDraft.update { null }
    }

    fun getRegisteredMenuSummaries(): List<RegisteredMenuSummary> {
        return _registeredMenus.value.mapIndexed { index, menu ->
            RegisteredMenuSummary(
                index = index + 1,
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
    }

    suspend fun registerMenus(): Result<Unit> {
        val menus = _registeredMenus.value
        for (menu in menus) {
            val existingRecipes = menu.ingredients
                .filter { it.id > 0 }
                .map { ingredient ->
                    MenuRecipe(
                        recipeId = 0L,
                        menuId = 0L,
                        ingredientId = ingredient.id,
                        ingredientName = ingredient.name,
                        amount = ingredient.amount,
                        unitCode = ingredient.unit.name,
                        price = ingredient.price,
                    )
                }
            val newRecipes = menu.ingredients
                .filter { it.id == 0L }
                .map { ingredient ->
                    NewRecipeInfo(
                        amount = ingredient.amount,
                        price = ingredient.price,
                        unitCode = ingredient.unit.name,
                        ingredientCategoryCode = ingredient.categoryCode,
                        ingredientName = ingredient.name,
                        supplier = ingredient.supplier.ifEmpty { null },
                    )
                }
            val result = menuRepository.createMenu(
                categoryCode = menu.categoryCode ?: menu.category.name,
                menuName = menu.name,
                sellingPrice = menu.price,
                workTime = menu.preparationTimeSeconds,
                recipes = existingRecipes.ifEmpty { null },
                newRecipes = newRecipes.ifEmpty { null },
            )
            if (result is Result.Error) {
                return result
            }
        }
        return Result.Success(Unit)
    }

    fun clearAll() {
        _registeredMenus.update { emptyList() }
        _currentMenuDraft.update { null }
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
