package com.team.chord.feature.setup.menuconfirm

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.Typography
import com.team.chord.core.domain.model.Result
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuConfirmScreen(
    registeredMenus: List<RegisteredMenuSummary>,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    onRegisterMenus: suspend () -> Result<Unit>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isRegistering by remember { mutableStateOf(false) }

    MenuConfirmScreenContent(
        uiState = MenuConfirmUiState(
            registeredMenus = registeredMenus,
            isRegistering = isRegistering,
        ),
        onNavigateBack = onNavigateBack,
        onComplete = {
            coroutineScope.launch {
                isRegistering = true
                val result = onRegisterMenus()
                isRegistering = false
                when (result) {
                    is Result.Success -> onComplete()
                    is Result.Error -> {
                        Toast.makeText(context, "메뉴 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> { /* no-op */ }
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun MenuConfirmScreenContent(
    uiState: MenuConfirmUiState,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        // Top Bar with only back button
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
        )

        // Title
        Text(
            text = "이대로 등록을 마칠까요?",
            style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Grayscale900,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        )

        // Menu Cards List (scrollable)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            uiState.registeredMenus.forEach { menu ->
                MenuConfirmCard(menu = menu)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Button
        ChordLargeButton(
            text = "마치기",
            onClick = onComplete,
            enabled = !uiState.isRegistering,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun MenuConfirmCard(
    menu: RegisteredMenuSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, PrimaryBlue100, RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFFE8F0FF), Color.White))
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Menu Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(PrimaryBlue500)
                .padding(horizontal = 16.dp, vertical = 6.dp),
        ) {
            Text(
                text = "메뉴 ${menu.index}",
                style = Typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Grayscale100,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Name
        Text(
            text = menu.name,
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Menu Price
        Text(
            text = formatPrice(menu.price),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = Grayscale700,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Ingredients List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            menu.ingredients.forEach { ingredient ->
                IngredientRow(ingredient = ingredient)
            }
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientSummary,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = ingredient.name,
            style = Typography.bodyMedium,
            color = Grayscale700,
        )

        Text(
            text = "${ingredient.amount}/${formatPriceWithoutUnit(ingredient.price)}원",
            style = Typography.bodyMedium,
            color = Grayscale700,
        )
    }
}

private fun formatPrice(price: Int): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    return "${numberFormat.format(price)}원"
}

private fun formatPriceWithoutUnit(price: Int): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
    return numberFormat.format(price)
}

@Preview(showBackground = true)
@Composable
private fun MenuConfirmScreenContentPreview() {
    MenuConfirmScreenContent(
        uiState = MenuConfirmUiState(
            registeredMenus = listOf(
                RegisteredMenuSummary(
                    index = 1,
                    name = "흑임자 라떼",
                    price = 4500,
                    ingredients = listOf(
                        IngredientSummary(name = "원두", amount = "30g", price = 800),
                        IngredientSummary(name = "물", amount = "250ml", price = 150),
                        IngredientSummary(name = "종이컵", amount = "1개", price = 100),
                        IngredientSummary(name = "테이크아웃 홀더", amount = "1개", price = 150),
                        IngredientSummary(name = "흑임자 토핑", amount = "1개", price = 150),
                    ),
                ),
            ),
        ),
        onNavigateBack = {},
        onComplete = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuConfirmScreenContentMultipleMenusPreview() {
    MenuConfirmScreenContent(
        uiState = MenuConfirmUiState(
            registeredMenus = listOf(
                RegisteredMenuSummary(
                    index = 1,
                    name = "흑임자 라떼",
                    price = 4500,
                    ingredients = listOf(
                        IngredientSummary(name = "원두", amount = "30g", price = 800),
                        IngredientSummary(name = "물", amount = "250ml", price = 150),
                        IngredientSummary(name = "종이컵", amount = "1개", price = 100),
                        IngredientSummary(name = "테이크아웃 홀더", amount = "1개", price = 150),
                        IngredientSummary(name = "흑임자 토핑", amount = "1개", price = 150),
                    ),
                ),
                RegisteredMenuSummary(
                    index = 2,
                    name = "다른 라떼",
                    price = 4500,
                    ingredients = listOf(
                        IngredientSummary(name = "원두", amount = "30g", price = 800),
                        IngredientSummary(name = "물", amount = "250ml", price = 150),
                    ),
                ),
            ),
        ),
        onNavigateBack = {},
        onComplete = {},
    )
}
