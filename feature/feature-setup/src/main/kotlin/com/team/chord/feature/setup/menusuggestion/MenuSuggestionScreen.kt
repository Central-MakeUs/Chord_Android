package com.team.chord.feature.setup.menusuggestion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.setup.menudetail.MenuCategory

@Composable
fun MenuSuggestionScreen(
    onStartMenuRegistration: (MenuCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCategoryBottomSheet by remember { mutableStateOf(false) }

    MenuSuggestionScreenContent(
        onStartMenuRegistration = { showCategoryBottomSheet = true },
        modifier = modifier,
    )

    if (showCategoryBottomSheet) {
        CategoryPickerBottomSheet(
            onDismiss = { showCategoryBottomSheet = false },
            onConfirm = { category ->
                showCategoryBottomSheet = false
                onStartMenuRegistration(category)
            },
        )
    }
}

@Composable
internal fun MenuSuggestionScreenContent(
    onStartMenuRegistration: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grayscale100),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Calculator icon
            Image(
                painter = painterResource(id = R.drawable.ic_3d_calculator),
                contentDescription = "계산기 아이콘",
                modifier = Modifier.size(144.dp),
            )

            Spacer(modifier = Modifier.height(17.dp))

            // Main text
            Text(
                text = "메뉴를 등록해주시면\n원가와 마진을 계산해드릴게요",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Grayscale900,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            // Start menu registration button
            ChordLargeButton(
                text = "메뉴 등록 시작하기",
                onClick = onStartMenuRegistration,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryPickerBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (MenuCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    var tempSelection by remember { mutableStateOf(MenuCategory.BEVERAGE) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "메뉴 카테고리",
        modifier = modifier,
        content = {
            Column {
                MenuCategory.entries.forEachIndexed { index, category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tempSelection = category }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = category.displayName,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (category == tempSelection) PrimaryBlue500 else Grayscale900,
                        )
                        if (category == tempSelection) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = "선택됨",
                                modifier = Modifier.size(20.dp),
                                tint = PrimaryBlue500,
                            )
                        }
                    }
                    if (index < MenuCategory.entries.lastIndex) {
                        HorizontalDivider(color = Grayscale200, thickness = 1.dp)
                    }
                }
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = { onConfirm(tempSelection) },
            )
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MenuSuggestionScreenPreview() {
    MenuSuggestionScreen(
        onStartMenuRegistration = {},
    )
}
