package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.menu.Category
import com.team.chord.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    operator fun invoke(): Flow<List<Category>> = menuRepository.getCategories()
}
