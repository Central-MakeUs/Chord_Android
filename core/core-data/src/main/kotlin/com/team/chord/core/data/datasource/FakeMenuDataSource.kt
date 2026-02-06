package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.CheckDupResult
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.model.menu.MenuTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMenuDataSource @Inject constructor() : MenuDataSource {

    private val categories = MutableStateFlow(createInitialCategories())
    private val menus = MutableStateFlow(createInitialMenus())

    override fun getMenuList(categoryCode: String?): Flow<List<Menu>> =
        if (categoryCode == null) menus
        else menus.map { list -> list.filter { it.categoryCode == categoryCode } }

    override suspend fun getMenuDetail(menuId: Long): Menu? =
        menus.value.find { it.id == menuId }

    override suspend fun getMenuRecipes(menuId: Long): Pair<List<MenuRecipe>, Int> {
        val menu = menus.value.find { it.id == menuId } ?: return emptyList<MenuRecipe>() to 0
        val recipes = menu.ingredients.mapIndexed { index, ing ->
            MenuRecipe(
                recipeId = index.toLong() + 1,
                menuId = menuId,
                ingredientId = ing.id,
                ingredientName = ing.name,
                amount = ing.quantity.toInt(),
                unitCode = ing.unit.name,
                price = ing.totalPrice,
            )
        }
        return recipes to menu.totalCost
    }

    override suspend fun checkMenuDuplicate(menuName: String, ingredientNames: List<String>?): CheckDupResult {
        val isDup = menus.value.any { it.name == menuName }
        return CheckDupResult(menuNameDuplicate = isDup, dupIngredientNames = emptyList())
    }

    override suspend fun createMenu(
        categoryCode: String,
        menuName: String,
        sellingPrice: Int,
        workTime: Int,
        recipes: List<Pair<Long?, Int>>?,
        newRecipes: List<NewRecipeInput>?,
    ) {
        val newId = (menus.value.maxOfOrNull { it.id } ?: 0) + 1
        val menu = Menu(
            id = newId,
            name = menuName,
            price = sellingPrice,
            categoryCode = categoryCode,
            preparationTimeSeconds = workTime,
            ingredients = emptyList(),
            totalCost = 0,
            costRatio = 0f,
            marginRatio = 0f,
            contributionProfit = 0,
            marginGrade = MarginGrade(code = "A", name = "우수", message = ""),
            recommendedPrice = null,
            recommendedPriceMessage = null,
        )
        menus.update { it + menu }
    }

    override suspend fun addExistingRecipe(menuId: Long, ingredientId: Long?, amount: Int) {
        // no-op for fake
    }

    override suspend fun addNewRecipe(
        menuId: Long, amount: Int, price: Int, unitCode: String,
        ingredientCategoryCode: String, ingredientName: String, supplier: String?,
    ) {
        // no-op for fake
    }

    override suspend fun updateMenuName(menuId: Long, name: String) {
        menus.update { list ->
            list.map { if (it.id == menuId) it.copy(name = name) else it }
        }
    }

    override suspend fun updateMenuPrice(menuId: Long, price: Int) {
        menus.update { list ->
            list.map { if (it.id == menuId) it.copy(price = price) else it }
        }
    }

    override suspend fun updateMenuCategory(menuId: Long, categoryCode: String) {
        menus.update { list ->
            list.map { if (it.id == menuId) it.copy(categoryCode = categoryCode) else it }
        }
    }

    override suspend fun updateMenuWorktime(menuId: Long, workTime: Int) {
        menus.update { list ->
            list.map { if (it.id == menuId) it.copy(preparationTimeSeconds = workTime) else it }
        }
    }

    override suspend fun updateRecipeAmount(menuId: Long, recipeId: Long, amount: Int) {
        // no-op for fake
    }

    override suspend fun deleteRecipes(menuId: Long, recipeIds: List<Long>?) {
        // no-op for fake
    }

    override suspend fun deleteMenu(menuId: Long) {
        menus.update { list -> list.filter { it.id != menuId } }
    }

    override fun getCategories(): Flow<List<Category>> = categories

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> {
        if (query.isBlank()) return flowOf(emptyList())
        val filtered = menuTemplates.filter { it.menuName.contains(query, ignoreCase = true) }
        return flowOf(filtered)
    }

    override suspend fun getTemplateBasic(templateId: Long): MenuTemplate {
        return menuTemplates.first { it.templateId == templateId }
    }

    override suspend fun getTemplateIngredients(templateId: Long): List<MenuRecipe> = emptyList()

    private val menuTemplates: List<MenuTemplate> by lazy { createMenuTemplates() }

    private fun createMenuTemplates(): List<MenuTemplate> = listOf(
        MenuTemplate(templateId = 1L, menuName = "흑임자라떼", defaultSellingPrice = 5500, categoryCode = "BEVERAGE", workTime = 120),
        MenuTemplate(templateId = 2L, menuName = "흑임자스콘", defaultSellingPrice = 4000, categoryCode = "DESSERT", workTime = 30),
        MenuTemplate(templateId = 3L, menuName = "아메리카노", defaultSellingPrice = 4500, categoryCode = "BEVERAGE", workTime = 60),
        MenuTemplate(templateId = 4L, menuName = "카페라떼", defaultSellingPrice = 5000, categoryCode = "BEVERAGE", workTime = 90),
    )

    private fun createInitialCategories(): List<Category> = listOf(
        Category(code = "BEVERAGE", name = "음료", displayOrder = 1),
        Category(code = "DESSERT", name = "디저트", displayOrder = 2),
        Category(code = "FOOD", name = "푸드", displayOrder = 3),
    )

    private fun createInitialMenus(): List<Menu> = listOf(
        Menu(
            id = 1L, name = "돌체 라떼", price = 5500, categoryCode = "BEVERAGE",
            preparationTimeSeconds = 90,
            ingredients = listOf(
                MenuIngredient(1L, "원두", 30.0, IngredientUnit.G, 15, 450),
                MenuIngredient(2L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                MenuIngredient(3L, "연유", 30.0, IngredientUnit.ML, 10, 300),
                MenuIngredient(4L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
            ),
            totalCost = 1840, costRatio = 33.4f, marginRatio = 23.2f, contributionProfit = 3660,
            marginGrade = MarginGrade(code = "D", name = "위험", message = "원가율이 높아요! 가격을 조절해주세요"),
            recommendedPrice = 5300, recommendedPriceMessage = "원가율이 높아요! 가격을 조절해주세요",
        ),
        Menu(
            id = 2L, name = "바닐라 라떼", price = 6500, categoryCode = "BEVERAGE",
            preparationTimeSeconds = 120,
            ingredients = listOf(
                MenuIngredient(5L, "원두", 20.0, IngredientUnit.G, 15, 300),
                MenuIngredient(6L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                MenuIngredient(7L, "바닐라시럽", 20.0, IngredientUnit.ML, 25, 500),
            ),
            totalCost = 1590, costRatio = 24.4f, marginRatio = 23.2f, contributionProfit = 4910,
            marginGrade = MarginGrade(code = "C", name = "주의", message = "마진율이 낮아요! 가격을 조정해보세요"),
            recommendedPrice = 4600, recommendedPriceMessage = "마진율이 낮아요! 가격을 조정해보세요",
        ),
        Menu(
            id = 3L, name = "레몬티", price = 5500, categoryCode = "BEVERAGE",
            preparationTimeSeconds = 60, ingredients = emptyList(),
            totalCost = 1840, costRatio = 33.4f, marginRatio = 23.2f, contributionProfit = 3660,
            marginGrade = MarginGrade(code = "B", name = "보통", message = ""),
            recommendedPrice = null, recommendedPriceMessage = null,
        ),
        Menu(
            id = 4L, name = "초콜릿 케익", price = 6000, categoryCode = "DESSERT",
            preparationTimeSeconds = 30, ingredients = emptyList(),
            totalCost = 1800, costRatio = 30.0f, marginRatio = 25.0f, contributionProfit = 4200,
            marginGrade = MarginGrade(code = "A", name = "안정", message = ""),
            recommendedPrice = null, recommendedPriceMessage = null,
        ),
    )
}
