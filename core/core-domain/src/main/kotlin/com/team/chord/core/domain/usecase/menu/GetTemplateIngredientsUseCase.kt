package com.team.chord.core.domain.usecase.menu

import com.team.chord.core.domain.model.menu.MenuRecipe
import com.team.chord.core.domain.repository.MenuRepository
import javax.inject.Inject

class GetTemplateIngredientsUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
) {
    suspend operator fun invoke(templateId: Long): List<MenuRecipe> {
        return menuRepository.getTemplateIngredients(templateId)
    }
}
