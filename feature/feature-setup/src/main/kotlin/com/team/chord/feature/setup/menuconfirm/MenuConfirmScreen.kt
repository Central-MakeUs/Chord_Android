package com.team.chord.feature.setup.menuconfirm

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.domain.model.Result
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.Typography
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
                    is Result.Loading -> Unit
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
    val selectedMenu = uiState.registeredMenus.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "이대로 등록을 마칠까요?",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    lineHeight = 33.6.sp,
                ),
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(40.dp))

            selectedMenu?.let {
                MenuConfirmCard(menu = it)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        ChordLargeButton(
            text = "마치기",
            onClick = onComplete,
            enabled = !uiState.isRegistering && selectedMenu != null,
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
    val cardShape = RoundedCornerShape(24.dp)

    DisableSelection {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Grayscale100, shape = cardShape)
                .border(width = 1.dp, color = Grayscale300, shape = cardShape)
                .padding(top = 28.dp, bottom = 24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    text = menu.name,
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        lineHeight = 33.6.sp,
                    ),
                    color = Grayscale900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatPrice(menu.price),
                    style = TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        lineHeight = 33.6.sp,
                    ),
                    color = Grayscale700,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Grayscale300,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                menu.ingredients.forEach { ingredient ->
                    IngredientRow(ingredient = ingredient)
                }
            }
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: IngredientSummary,
    modifier: Modifier = Modifier,
) {
    val ingredientNameTextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 25.6.sp,
        letterSpacing = 0.2.sp,
    )
    val ingredientMetaTextStyle = TextStyle(
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 25.6.sp,
        letterSpacing = 0.3.sp,
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = ingredient.name,
            style = ingredientNameTextStyle,
            color = Grayscale700,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = ingredient.amount,
            style = ingredientMetaTextStyle,
            color = Grayscale500,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(76.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = formatPrice(ingredient.price),
            style = ingredientMetaTextStyle,
            color = Grayscale500,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(88.dp),
        )
    }
}

private fun formatPrice(price: Int): String {
    return "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원"
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
                    price = 6500,
                    ingredients = listOf(
                        IngredientSummary(name = "흑임자 토핑", amount = "20g", price = 5000),
                        IngredientSummary(name = "물", amount = "250ml", price = 2000),
                        IngredientSummary(name = "원두", amount = "30g", price = 20000),
                        IngredientSummary(name = "종이컵", amount = "1개", price = 3000),
                        IngredientSummary(name = "테이크아웃 홀더", amount = "1개", price = 1050),
                    ),
                ),
            ),
        ),
        onNavigateBack = {},
        onComplete = {},
    )
}
