package com.team.chord.feature.menu.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.R as CoreUiR
import com.team.chord.core.ui.component.ChordToast
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.menu.ingredient.component.EditIngredientBottomSheet
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

private const val SUCCESS_TOAST_MESSAGE = "재료의 사용량이 수정됐어요"

@Composable
fun IngredientEditScreen(
    onNavigateBack: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IngredientEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IngredientEditScreenContent(
        uiState = uiState,
        onNavigateBack = { onNavigateBack(viewModel.hasChanges()) },
        onRecipeClick = viewModel::showEditBottomSheet,
        onDismissSheet = viewModel::hideEditBottomSheet,
        onAmountInputChange = viewModel::onAmountInputChange,
        onSubmitAmountUpdate = viewModel::submitAmountUpdate,
        onToastShown = viewModel::onToastShown,
        onErrorShown = viewModel::onErrorShown,
        modifier = modifier,
    )
}

@Composable
internal fun IngredientEditScreenContent(
    uiState: IngredientEditUiState,
    onNavigateBack: () -> Unit,
    onRecipeClick: (EditableRecipeUi) -> Unit,
    onDismissSheet: () -> Unit,
    onAmountInputChange: (String) -> Unit,
    onSubmitAmountUpdate: () -> Unit,
    onToastShown: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onToastShown()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Grayscale100,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                ChordToast(
                    text = snackbarData.visuals.message,
                    leadingIcon = if (snackbarData.visuals.message == SUCCESS_TOAST_MESSAGE) {
                        CoreUiR.drawable.ic_check
                    } else {
                        null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Grayscale100)
                .padding(paddingValues),
        ) {
            ChordTopAppBar(
                title = uiState.menuName,
                onBackClick = onNavigateBack,
                titleStyle = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Grayscale900,
                ),
                actionContent = {
                    Icon(
                        painter = painterResource(CoreUiR.drawable.ic_ellipsis),
                        contentDescription = null,
                        tint = Grayscale900,
                    )
                },
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = PrimaryBlue500)
                }
            } else {
                IngredientRecipeList(
                    recipes = uiState.recipes,
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    uiState.sheetState?.let { sheet ->
        EditIngredientBottomSheet(
            sheetState = sheet,
            isSubmitting = uiState.isSubmitting,
            onDismiss = onDismissSheet,
            onAmountInputChange = onAmountInputChange,
            onConfirm = onSubmitAmountUpdate,
        )
    }
}

@Composable
private fun IngredientRecipeList(
    recipes: List<EditableRecipeUi>,
    onRecipeClick: (EditableRecipeUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (recipes.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "등록된 재료가 없어요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale600,
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        items(
            items = recipes,
            key = { it.recipeId },
        ) { recipe ->
            IngredientRecipeRow(
                recipe = recipe,
                onClick = { onRecipeClick(recipe) },
            )
            HorizontalDivider(color = Grayscale300)
        }
    }
}

@Composable
private fun IngredientRecipeRow(
    recipe: EditableRecipeUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = recipe.name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Grayscale900,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${formatAmount(recipe.amount)}${recipe.unit.displayName}",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale600,
        )
        Text(
            text = "${numberFormat.format(recipe.price)}원",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale700,
            modifier = Modifier.padding(start = 16.dp),
        )
        Icon(
            painter = painterResource(CoreUiR.drawable.ic_chevron_right),
            contentDescription = null,
            tint = Grayscale500,
            modifier = Modifier
                .padding(start = 10.dp)
                .size(16.dp),
        )
    }
}

private fun formatAmount(value: Double): String {
    return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString()
}
