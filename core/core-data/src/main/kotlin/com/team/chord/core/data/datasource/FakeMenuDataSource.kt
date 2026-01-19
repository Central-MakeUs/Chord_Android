package com.team.chord.core.data.datasource

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.model.menu.IngredientUnit
import com.team.chord.core.domain.model.menu.MarginGrade
import com.team.chord.core.domain.model.menu.Menu
import com.team.chord.core.domain.model.menu.MenuIngredient
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

    override fun getMenuList(): Flow<List<Menu>> = menus

    override fun getMenuListByCategory(categoryId: Long): Flow<List<Menu>> =
        menus.map { list -> list.filter { it.category.id == categoryId } }

    override suspend fun getMenuDetail(menuId: Long): Menu? =
        menus.value.find { it.id == menuId }

    override suspend fun updateMenu(menu: Menu): Menu {
        menus.update { list ->
            list.map { if (it.id == menu.id) menu else it }
        }
        return menu
    }

    override suspend fun deleteMenu(menuId: Long) {
        menus.update { list -> list.filter { it.id != menuId } }
    }

    override suspend fun addIngredientToMenu(menuId: Long, ingredient: MenuIngredient): Menu? {
        var updatedMenu: Menu? = null
        menus.update { list ->
            list.map { menu ->
                if (menu.id == menuId) {
                    val newIngredients = menu.ingredients + ingredient
                    val newMenu = menu.copy(ingredients = newIngredients)
                    updatedMenu = newMenu
                    newMenu
                } else {
                    menu
                }
            }
        }
        return updatedMenu
    }

    override suspend fun updateIngredientInMenu(menuId: Long, ingredient: MenuIngredient): Menu? {
        var updatedMenu: Menu? = null
        menus.update { list ->
            list.map { menu ->
                if (menu.id == menuId) {
                    val newIngredients = menu.ingredients.map {
                        if (it.id == ingredient.id) ingredient else it
                    }
                    val newMenu = menu.copy(ingredients = newIngredients)
                    updatedMenu = newMenu
                    newMenu
                } else {
                    menu
                }
            }
        }
        return updatedMenu
    }

    override suspend fun removeIngredientsFromMenu(
        menuId: Long,
        ingredientIds: List<Long>,
    ): Menu? {
        var updatedMenu: Menu? = null
        menus.update { list ->
            list.map { menu ->
                if (menu.id == menuId) {
                    val newIngredients = menu.ingredients.filter { it.id !in ingredientIds }
                    val newMenu = menu.copy(ingredients = newIngredients)
                    updatedMenu = newMenu
                    newMenu
                } else {
                    menu
                }
            }
        }
        return updatedMenu
    }

    override fun getCategories(): Flow<List<Category>> = categories

    override fun searchMenuTemplates(query: String): Flow<List<MenuTemplate>> {
        if (query.isBlank()) return flowOf(emptyList())

        val filtered = menuTemplates.filter { template ->
            template.name.contains(query, ignoreCase = true)
        }
        return flowOf(filtered)
    }

    private val menuTemplates: List<MenuTemplate> by lazy {
        createMenuTemplates()
    }

    private fun createMenuTemplates(): List<MenuTemplate> {
        val beverageCategory = Category(id = 1L, name = "음료", order = 1)
        val dessertCategory = Category(id = 2L, name = "디저트", order = 2)

        return listOf(
            MenuTemplate(
                id = 1L,
                name = "흑임자라떼",
                suggestedPrice = 5500,
                category = beverageCategory,
                preparationTimeSeconds = 120,
                ingredients = listOf(
                    MenuIngredient(1L, "흑임자파우더", 30.0, IngredientUnit.G, 50, 1500),
                    MenuIngredient(2L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                    MenuIngredient(3L, "얼음", 100.0, IngredientUnit.G, 1, 100),
                    MenuIngredient(4L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
            ),
            MenuTemplate(
                id = 2L,
                name = "흑임자스콘",
                suggestedPrice = 4000,
                category = dessertCategory,
                preparationTimeSeconds = 30,
                ingredients = listOf(
                    MenuIngredient(5L, "흑임자스콘", 1.0, IngredientUnit.EA, 1200, 1200),
                    MenuIngredient(6L, "포장용기", 1.0, IngredientUnit.EA, 100, 100),
                ),
            ),
            MenuTemplate(
                id = 3L,
                name = "흑임자케익",
                suggestedPrice = 6500,
                category = dessertCategory,
                preparationTimeSeconds = 30,
                ingredients = listOf(
                    MenuIngredient(7L, "흑임자케이크", 1.0, IngredientUnit.EA, 2000, 2000),
                    MenuIngredient(8L, "포장용기", 1.0, IngredientUnit.EA, 200, 200),
                ),
            ),
            MenuTemplate(
                id = 4L,
                name = "흑임자우유",
                suggestedPrice = 4500,
                category = beverageCategory,
                preparationTimeSeconds = 60,
                ingredients = listOf(
                    MenuIngredient(9L, "흑임자파우더", 20.0, IngredientUnit.G, 50, 1000),
                    MenuIngredient(10L, "우유", 300.0, IngredientUnit.ML, 2, 600),
                    MenuIngredient(11L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
            ),
            MenuTemplate(
                id = 5L,
                name = "아메리카노",
                suggestedPrice = 4500,
                category = beverageCategory,
                preparationTimeSeconds = 60,
                ingredients = listOf(
                    MenuIngredient(12L, "원두", 20.0, IngredientUnit.G, 15, 300),
                    MenuIngredient(13L, "물", 200.0, IngredientUnit.ML, 1, 200),
                    MenuIngredient(14L, "얼음", 100.0, IngredientUnit.G, 1, 100),
                    MenuIngredient(15L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
            ),
            MenuTemplate(
                id = 6L,
                name = "카페라떼",
                suggestedPrice = 5000,
                category = beverageCategory,
                preparationTimeSeconds = 90,
                ingredients = listOf(
                    MenuIngredient(16L, "원두", 20.0, IngredientUnit.G, 15, 300),
                    MenuIngredient(17L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                    MenuIngredient(18L, "얼음", 100.0, IngredientUnit.G, 1, 100),
                    MenuIngredient(19L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
            ),
        )
    }

    private fun createInitialCategories(): List<Category> = listOf(
        Category(id = 1L, name = "음료", order = 1),
        Category(id = 2L, name = "디저트", order = 2),
        Category(id = 3L, name = "푸드", order = 3),
    )

    private fun createInitialMenus(): List<Menu> {
        val beverageCategory = Category(id = 1L, name = "음료", order = 1)
        val dessertCategory = Category(id = 2L, name = "디저트", order = 2)
        val foodCategory = Category(id = 3L, name = "푸드", order = 3)

        return listOf(
            Menu(
                id = 1L,
                name = "돌체 라떼",
                price = 5500,
                category = beverageCategory,
                preparationTimeSeconds = 90,
                ingredients = listOf(
                    MenuIngredient(1L, "원두", 30.0, IngredientUnit.G, 15, 450),
                    MenuIngredient(2L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                    MenuIngredient(3L, "연유", 30.0, IngredientUnit.ML, 10, 300),
                    MenuIngredient(4L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
                totalCost = 1840,
                costRatio = 33.4f,
                marginRatio = 23.2f,
                contributionProfit = 3660,
                marginGrade = MarginGrade.DANGER,
                recommendedPrice = 5300,
                recommendedPriceMessage = "원가율이 높아요! 가격을 조절해주세요",
            ),
            Menu(
                id = 2L,
                name = "바닐라 라떼",
                price = 6500,
                category = beverageCategory,
                preparationTimeSeconds = 120,
                ingredients = listOf(
                    MenuIngredient(5L, "원두", 20.0, IngredientUnit.G, 15, 300),
                    MenuIngredient(6L, "우유", 200.0, IngredientUnit.ML, 2, 400),
                    MenuIngredient(7L, "바닐라시럽", 20.0, IngredientUnit.ML, 25, 500),
                    MenuIngredient(8L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                    MenuIngredient(9L, "테이크아웃홀더", 1.0, IngredientUnit.EA, 50, 50),
                ),
                totalCost = 1590,
                costRatio = 24.4f,
                marginRatio = 23.2f,
                contributionProfit = 4910,
                marginGrade = MarginGrade.WARNING,
                recommendedPrice = 4600,
                recommendedPriceMessage = "마진율이 낮아요! 가격을 조정해보세요",
            ),
            Menu(
                id = 3L,
                name = "레몬티",
                price = 5500,
                category = beverageCategory,
                preparationTimeSeconds = 60,
                ingredients = listOf(
                    MenuIngredient(10L, "레몬", 2.0, IngredientUnit.EA, 500, 1000),
                    MenuIngredient(11L, "꿀", 30.0, IngredientUnit.ML, 15, 450),
                    MenuIngredient(12L, "뜨거운물", 300.0, IngredientUnit.ML, 1, 300),
                    MenuIngredient(13L, "종이컵", 1.0, IngredientUnit.EA, 100, 100),
                ),
                totalCost = 1840,
                costRatio = 33.4f,
                marginRatio = 23.2f,
                contributionProfit = 3660,
                marginGrade = MarginGrade.MID,
                recommendedPrice = null,
                recommendedPriceMessage = null,
            ),
            Menu(
                id = 4L,
                name = "초콜릿 케익",
                price = 6000,
                category = dessertCategory,
                preparationTimeSeconds = 30,
                ingredients = listOf(
                    MenuIngredient(14L, "초콜릿케이크", 1.0, IngredientUnit.EA, 1800, 1800),
                    MenuIngredient(15L, "포장용기", 1.0, IngredientUnit.EA, 200, 200),
                ),
                totalCost = 1800,
                costRatio = 30.0f,
                marginRatio = 25.0f,
                contributionProfit = 4200,
                marginGrade = MarginGrade.SAFE,
                recommendedPrice = null,
                recommendedPriceMessage = null,
            ),
            Menu(
                id = 5L,
                name = "바나나 브레드",
                price = 4200,
                category = foodCategory,
                preparationTimeSeconds = 30,
                ingredients = listOf(
                    MenuIngredient(16L, "바나나브레드", 1.0, IngredientUnit.EA, 1200, 1200),
                    MenuIngredient(17L, "포장용기", 1.0, IngredientUnit.EA, 100, 100),
                ),
                totalCost = 1200,
                costRatio = 28.5f,
                marginRatio = 20.0f,
                contributionProfit = 3000,
                marginGrade = MarginGrade.SAFE,
                recommendedPrice = null,
                recommendedPriceMessage = null,
            ),
        )
    }
}
