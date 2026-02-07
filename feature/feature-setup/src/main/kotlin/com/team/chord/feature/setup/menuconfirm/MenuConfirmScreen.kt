package com.team.chord.feature.setup.menuconfirm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuConfirmScreen(
    registeredMenus: List<RegisteredMenuSummary>,
    onNavigateBack: () -> Unit,
    onAddMore: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MenuConfirmScreenContent(
        uiState = MenuConfirmUiState(registeredMenus = registeredMenus),
        onNavigateBack = onNavigateBack,
        onAddMore = onAddMore,
        onComplete = onComplete,
        modifier = modifier,
    )
}

@Composable
internal fun MenuConfirmScreenContent(
    uiState: MenuConfirmUiState,
    onNavigateBack: () -> Unit,
    onAddMore: () -> Unit,
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
        MenuConfirmTopBar(onBackClick = onNavigateBack)

        // Title
        Text(
            text = "이대로 등록을 마칠까요?",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Grayscale900,
            ),
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

        // Bottom Buttons
        BottomButtons(
            onAddMore = onAddMore,
            onComplete = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun MenuConfirmTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "뒤로가기",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp)
                .clickable(onClick = onBackClick),
            tint = Grayscale900,
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
            .clip(RoundedCornerShape(16.dp))
            .background(PrimaryBlue100)
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
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Grayscale100,
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu Name
        Text(
            text = menu.name,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Grayscale900,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Menu Price
        Text(
            text = formatPrice(menu.price),
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Grayscale800,
            ),
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
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale800,
            ),
        )

        Text(
            text = "${ingredient.amount}/${formatPriceWithoutUnit(ingredient.price)}원",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale800,
            ),
        )
    }
}

@Composable
private fun BottomButtons(
    onAddMore: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Add More Button (outline style)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = Grayscale300,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(Grayscale100)
                .clickable(onClick = onAddMore),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "추가 등록",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale500,
                ),
            )
        }

        // Complete Button (filled style)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryBlue500)
                .clickable(onClick = onComplete),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "마치기",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Grayscale100,
                ),
            )
        }
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
        onAddMore = {},
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
        onAddMore = {},
        onComplete = {},
    )
}
