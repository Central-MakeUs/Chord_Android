package com.team.chord.feature.menuadd.shared.confirm

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
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun MenuConfirmScreen(
    registeredMenu: RegisteredMenuSummary?,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onRegisterMenus: suspend () -> Result<Unit>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isRegistering by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = "",
            onBackClick = onNavigateBack,
        )

        Text(
            text = "이대로 등록을 마칠까요?",
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 33.6.sp,
            ),
            color = Grayscale900,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
        )

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            registeredMenu?.let { menu ->
                SingleMenuConfirmCard(menu = menu)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        ChordLargeButton(
            text = "마치기",
            onClick = {
                if (isRegistering) return@ChordLargeButton
                coroutineScope.launch {
                    isRegistering = true
                    when (onRegisterMenus()) {
                        is Result.Success -> onRegisterSuccess()
                        is Result.Error -> Toast.makeText(context, "메뉴 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        is Result.Loading -> Unit
                    }
                    isRegistering = false
                }
            },
            enabled = !isRegistering && registeredMenu != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
        )
    }
}

@Composable
private fun SingleMenuConfirmCard(
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
                    SingleMenuIngredientRow(ingredient = ingredient)
                }
            }
        }
    }
}

@Composable
private fun SingleMenuIngredientRow(
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
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = formatPrice(ingredient.price),
            style = ingredientMetaTextStyle,
            color = Grayscale700,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun formatPrice(price: Int): String = "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원"
