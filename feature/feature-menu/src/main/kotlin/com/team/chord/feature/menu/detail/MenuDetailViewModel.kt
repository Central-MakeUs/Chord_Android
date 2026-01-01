package com.team.chord.feature.menu.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.team.chord.feature.menu.list.MenuStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val menuId: Long = savedStateHandle.get<Long>("menuId") ?: 0L

        private val _uiState = MutableStateFlow<MenuDetailUiState>(MenuDetailUiState.Loading)
        val uiState: StateFlow<MenuDetailUiState> = _uiState.asStateFlow()

        init {
            loadMenuDetail()
        }

        private fun loadMenuDetail() {
            val menuDetail = getMockMenuDetail(menuId)
            _uiState.value =
                if (menuDetail != null) {
                    MenuDetailUiState.Success(menuDetail)
                } else {
                    MenuDetailUiState.Error("메뉴를 찾을 수 없습니다")
                }
        }

        private fun getMockMenuDetail(id: Long): MenuDetailUi? =
            when (id) {
                1L -> {
                    MenuDetailUi(
                        id = 1L,
                        name = "아메리카노",
                        sellingPrice = 4500,
                        totalCost = 675,
                        costRatio = 0.15f,
                        marginRatio = 0.85f,
                        contributionProfit = 3825,
                        status = MenuStatus.SAFE,
                        ingredients =
                            listOf(
                                IngredientUi("원두", "18g", 450),
                                IngredientUi("정수", "200ml", 25),
                                IngredientUi("테이크아웃 컵", "1개", 200),
                            ),
                    )
                }

                2L -> {
                    MenuDetailUi(
                        id = 2L,
                        name = "카페라떼",
                        sellingPrice = 5000,
                        totalCost = 1100,
                        costRatio = 0.22f,
                        marginRatio = 0.78f,
                        contributionProfit = 3900,
                        status = MenuStatus.SAFE,
                        ingredients =
                            listOf(
                                IngredientUi("원두", "18g", 450),
                                IngredientUi("우유", "150ml", 450),
                                IngredientUi("테이크아웃 컵", "1개", 200),
                            ),
                    )
                }

                3L -> {
                    MenuDetailUi(
                        id = 3L,
                        name = "바닐라라떼",
                        sellingPrice = 5500,
                        totalCost = 1925,
                        costRatio = 0.35f,
                        marginRatio = 0.65f,
                        contributionProfit = 3575,
                        status = MenuStatus.WARNING,
                        ingredients =
                            listOf(
                                IngredientUi("원두", "18g", 450),
                                IngredientUi("우유", "150ml", 450),
                                IngredientUi("바닐라 시럽", "20ml", 825),
                                IngredientUi("테이크아웃 컵", "1개", 200),
                            ),
                    )
                }

                4L -> {
                    MenuDetailUi(
                        id = 4L,
                        name = "치즈케이크",
                        sellingPrice = 6000,
                        totalCost = 2700,
                        costRatio = 0.45f,
                        marginRatio = 0.55f,
                        contributionProfit = 3300,
                        status = MenuStatus.DANGER,
                        ingredients =
                            listOf(
                                IngredientUi("크림치즈", "100g", 1500),
                                IngredientUi("생크림", "50ml", 600),
                                IngredientUi("비스킷", "30g", 300),
                                IngredientUi("포장용기", "1개", 300),
                            ),
                    )
                }

                5L -> {
                    MenuDetailUi(
                        id = 5L,
                        name = "티라미수",
                        sellingPrice = 7000,
                        totalCost = 2240,
                        costRatio = 0.32f,
                        marginRatio = 0.68f,
                        contributionProfit = 4760,
                        status = MenuStatus.WARNING,
                        ingredients =
                            listOf(
                                IngredientUi("마스카포네", "80g", 1200),
                                IngredientUi("에스프레소", "30ml", 300),
                                IngredientUi("레이디핑거", "4개", 440),
                                IngredientUi("포장용기", "1개", 300),
                            ),
                    )
                }

                else -> {
                    null
                }
            }
    }
