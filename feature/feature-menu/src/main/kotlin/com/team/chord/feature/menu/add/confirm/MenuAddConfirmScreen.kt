package com.team.chord.feature.menu.add.confirm

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
import androidx.compose.ui.unit.dp
import com.team.chord.core.domain.model.Result
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.Typography
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MenuAddConfirmScreen(
    registeredMenus: List<RegisteredMenuSummary>,
    onNavigateBack: () -> Unit,
    onAddAnother: () -> Unit,
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
            style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Grayscale900,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
        )

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            registeredMenus.forEach { menu ->
                MenuSummaryCard(menu = menu)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ChordLargeButton(
                text = "이전",
                onClick = onNavigateBack,
                enabled = !isRegistering,
                modifier = Modifier.weight(1f),
                backgroundColor = Grayscale400,
                textColor = Grayscale600,
            )
            ChordLargeButton(
                text = "추가 등록",
                onClick = onAddAnother,
                enabled = !isRegistering,
                modifier = Modifier.weight(1f),
                backgroundColor = Grayscale400,
                textColor = Grayscale600,
            )
        }

        ChordLargeButton(
            text = if (isRegistering) "등록 중..." else "마치기",
            onClick = {
                if (isRegistering) {
                    return@ChordLargeButton
                }
                coroutineScope.launch {
                    isRegistering = true
                    when (onRegisterMenus()) {
                        is Result.Success -> onRegisterSuccess()
                        is Result.Error -> {
                            Toast.makeText(context, "메뉴 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        is Result.Loading -> Unit
                    }
                    isRegistering = false
                }
            },
            enabled = !isRegistering && registeredMenus.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 24.dp),
        )
    }
}

@Composable
private fun MenuSummaryCard(
    menu: RegisteredMenuSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .border(1.5.dp, PrimaryBlue100, RoundedCornerShape(24.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFFE8F0FF), Color.White)))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

        Text(
            text = menu.name,
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = formatPrice(menu.price),
            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = Grayscale700,
        )

        Spacer(modifier = Modifier.height(20.dp))

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
    return "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원"
}

private fun formatPriceWithoutUnit(price: Int): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(price)
}
