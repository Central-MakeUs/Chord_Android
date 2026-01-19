package com.team.chord.feature.setup

import androidx.lifecycle.ViewModel
import com.team.chord.feature.setup.ingredientinput.SelectedIngredient
import com.team.chord.feature.setup.menuconfirm.IngredientSummary
import com.team.chord.feature.setup.menuconfirm.RegisteredMenuSummary
import com.team.chord.feature.setup.menudetail.MenuCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Shared ViewModel for the onboarding menu registration flow.
 * This ViewModel is scoped to the navigation graph and shared across all screens
 * in the menu registration flow (MenuSearch -> MenuDetail -> IngredientInput -> MenuConfirm).
 */
@HiltViewModel
class OnboardingMenuViewModel @Inject constructor() : ViewModel() {

    private val _registeredMenus = MutableStateFlow<List<RegisteredMenu>>(emptyList())
    val registeredMenus: StateFlow<List<RegisteredMenu>> = _registeredMenus.asStateFlow()

    private val _currentMenuDraft = MutableStateFlow<MenuDraft?>(null)
    val currentMenuDraft: StateFlow<MenuDraft?> = _currentMenuDraft.asStateFlow()

    /**
     * Start creating a new menu with the given name.
     * Called when user selects a menu from search or enters a new menu name.
     *
     * @param name The name of the menu
     * @param isTemplateApplied Whether a template is applied to this menu
     * @param templatePrice The suggested price from template (if applicable)
     */
    fun startNewMenu(
        name: String,
        isTemplateApplied: Boolean,
        templatePrice: Int? = null,
    ) {
        _currentMenuDraft.update {
            MenuDraft(
                name = name,
                price = templatePrice ?: 0,
                isTemplateApplied = isTemplateApplied,
            )
        }
    }

    /**
     * Update menu detail information.
     * Called from MenuDetailScreen when user enters price, category, and preparation time.
     *
     * @param price The price of the menu
     * @param category The category of the menu
     * @param preparationSeconds Total preparation time in seconds
     */
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

    /**
     * Add ingredients to the current menu draft.
     * Called from IngredientInputScreen when user completes ingredient selection.
     *
     * @param ingredients List of selected ingredients
     */
    fun addIngredients(ingredients: List<SelectedIngredient>) {
        _currentMenuDraft.update { current ->
            current?.copy(ingredients = ingredients)
        }
    }

    /**
     * Complete the current menu draft and add it to the registered menus list.
     * Called when user finishes the menu registration flow.
     */
    fun completeCurrentMenu() {
        val draft = _currentMenuDraft.value ?: return

        val registeredMenu = RegisteredMenu(
            name = draft.name,
            price = draft.price,
            category = draft.category,
            preparationTimeSeconds = draft.preparationTimeSeconds,
            ingredients = draft.ingredients,
        )

        _registeredMenus.update { current ->
            current + registeredMenu
        }

        // Clear the draft for next menu
        _currentMenuDraft.update { null }
    }

    /**
     * Get summaries of all registered menus for display in MenuConfirmScreen.
     *
     * @return List of registered menu summaries with indexed display information
     */
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

    /**
     * Clear all registered menus and current draft.
     * Used when starting fresh or when setup is complete.
     */
    fun clearAll() {
        _registeredMenus.update { emptyList() }
        _currentMenuDraft.update { null }
    }
}

/**
 * Draft state for a menu being created.
 * This holds temporary data while user is going through the registration flow.
 */
data class MenuDraft(
    val name: String = "",
    val price: Int = 0,
    val category: MenuCategory = MenuCategory.BEVERAGE,
    val preparationTimeSeconds: Int = 90,
    val ingredients: List<SelectedIngredient> = emptyList(),
    val isTemplateApplied: Boolean = false,
)

/**
 * Completed menu with all required information.
 * This represents a fully registered menu.
 */
data class RegisteredMenu(
    val name: String,
    val price: Int,
    val category: MenuCategory,
    val preparationTimeSeconds: Int,
    val ingredients: List<SelectedIngredient>,
)
