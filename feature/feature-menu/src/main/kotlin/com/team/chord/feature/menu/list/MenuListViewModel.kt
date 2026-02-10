package com.team.chord.feature.menu.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.usecase.menu.GetCategoriesUseCase
import com.team.chord.core.domain.usecase.menu.GetMenuListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val getMenuListUseCase: GetMenuListUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuListUiState())
    val uiState: StateFlow<MenuListUiState> = _uiState.asStateFlow()

    private val selectedCategoryCode = MutableStateFlow<String?>(null)
    private var loadJob: Job? = null

    init {
        loadData()
    }

    fun onCategorySelected(categoryCode: String?) {
        selectedCategoryCode.value = categoryCode
        _uiState.update { it.copy(selectedCategoryCode = categoryCode) }
    }

    fun refresh() {
        loadData(isRefresh = true)
    }

    private fun loadData(isRefresh: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                if (isRefresh) it.copy(isRefreshing = true)
                else it.copy(isLoading = true)
            }

            try {
                combine(
                    getCategoriesUseCase(),
                    getMenuListUseCase(),
                    selectedCategoryCode,
                ) { categories, menus, selectedCode ->
                    val categoryCodes = resolveFixedCategoryCodes(categories)
                    val filteredMenus = if (selectedCode == null) {
                        menus
                    } else {
                        val selectedGroup = resolveCategoryGroup(selectedCode, categoryCodes)
                        menus.filter { menu ->
                            if (selectedGroup == null) {
                                normalizeCategoryCode(menu.categoryCode) == normalizeCategoryCode(selectedCode)
                            } else {
                                resolveCategoryGroup(menu.categoryCode, categoryCodes) == selectedGroup
                            }
                        }
                    }

                    MenuListUiState(
                        isLoading = false,
                        isRefreshing = false,
                        categories = categories,
                        selectedCategoryCode = selectedCode,
                        menuItems = filteredMenus.map { menu ->
                            MenuItemUi(
                                id = menu.id,
                                name = menu.name,
                                sellingPrice = menu.price,
                                costRatio = menu.costRatio,
                                marginRatio = menu.marginRatio,
                                marginGrade = menu.marginGrade,
                            )
                        },
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, isRefreshing = false, errorMessage = e.message) }
            }
        }
    }

    private fun normalizeCategoryCode(categoryCode: String): String {
        return when (categoryCode.trim().uppercase()) {
            "BEVERAGE", "DRINK", "DRINKS", "음료" -> "BEVERAGE"
            "DESSERT", "디저트" -> "DESSERT"
            "FOOD", "FOODS", "푸드" -> "FOOD"
            else -> categoryCode.trim().uppercase()
        }
    }

    private fun resolveCategoryGroup(categoryCode: String, categoryCodes: FixedCategoryCodes): CategoryGroup? {
        val normalizedCode = normalizeCategoryCode(categoryCode)
        val normalizedBeverageCode = normalizeCategoryCode(categoryCodes.beverageCode)
        val normalizedDessertCode = normalizeCategoryCode(categoryCodes.dessertCode)
        val normalizedFoodCode = normalizeCategoryCode(categoryCodes.foodCode)

        return when {
            normalizedCode == "BEVERAGE" || normalizedCode == normalizedBeverageCode -> CategoryGroup.BEVERAGE
            normalizedCode == "DESSERT" || normalizedCode == normalizedDessertCode -> CategoryGroup.DESSERT
            normalizedCode == "FOOD" || normalizedCode == normalizedFoodCode -> CategoryGroup.FOOD
            else -> null
        }
    }

    private fun resolveFixedCategoryCodes(categories: List<Category>): FixedCategoryCodes {
        val sortedCategories = categories.sortedBy { it.displayOrder }

        val beverageCode = categories.firstOrNull { isBeverageCategory(it) }?.code
            ?: sortedCategories.getOrNull(0)?.code
            ?: "BEVERAGE"
        val dessertCode = categories.firstOrNull { isDessertCategory(it) }?.code
            ?: sortedCategories.getOrNull(1)?.code
            ?: "DESSERT"
        val foodCode = categories.firstOrNull { isFoodCategory(it) }?.code
            ?: sortedCategories.getOrNull(2)?.code
            ?: "FOOD"

        return FixedCategoryCodes(
            beverageCode = beverageCode,
            dessertCode = dessertCode,
            foodCode = foodCode,
        )
    }

    private fun isBeverageCategory(category: Category): Boolean {
        val code = category.code.trim().uppercase()
        val name = category.name.trim().uppercase()
        return code == "BEVERAGE" ||
            code == "DRINK" ||
            code == "DRINKS" ||
            name == "음료" ||
            name == "BEVERAGE" ||
            name == "DRINK"
    }

    private fun isDessertCategory(category: Category): Boolean {
        val code = category.code.trim().uppercase()
        val name = category.name.trim().uppercase()
        return code == "DESSERT" ||
            code == "DESSERTS" ||
            name == "디저트" ||
            name == "DESSERT"
    }

    private fun isFoodCategory(category: Category): Boolean {
        val code = category.code.trim().uppercase()
        val name = category.name.trim().uppercase()
        return code == "FOOD" ||
            code == "FOODS" ||
            name == "푸드" ||
            name == "FOOD"
    }

    private enum class CategoryGroup {
        BEVERAGE,
        DESSERT,
        FOOD,
    }

    private data class FixedCategoryCodes(
        val beverageCode: String,
        val dessertCode: String,
        val foodCode: String,
    )
}
