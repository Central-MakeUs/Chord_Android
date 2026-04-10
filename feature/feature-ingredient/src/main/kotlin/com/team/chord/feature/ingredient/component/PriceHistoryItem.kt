package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.feature.ingredient.formatIngredientHistoryPriceText

@Composable
fun PriceHistoryItem(
    date: String,
    price: Int,
    unitAmount: Int,
    unitDisplayName: String,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(15.dp)
                    .background(
                        color = if (isFirst) PrimaryBlue100.copy(alpha = 0.4f) else Grayscale100,
                        shape = CircleShape,
                    )
                    .border(
                        width = 1.dp,
                        color = Grayscale300,
                        shape = CircleShape,
                    ),
            )

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(start = 7.dp)
                        .width(1.dp)
                        .height(69.dp)
                        .background(Grayscale300),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = date,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                color = Grayscale500,
            )

            Spacer(modifier = Modifier.height(if (isFirst) 6.dp else 4.dp))

            Text(
                text = formatIngredientHistoryPriceText(
                    price = price,
                    unitAmount = unitAmount,
                    unitDisplayName = unitDisplayName,
                ),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                color = Grayscale900,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceHistoryItemFirstPreview() {
    PriceHistoryItem(
        date = "25.11.12",
        price = 5000,
        unitAmount = 100,
        unitDisplayName = "g",
        isFirst = true,
        isLast = false,
    )
}

@Preview(showBackground = true)
@Composable
private fun PriceHistoryItemMiddlePreview() {
    PriceHistoryItem(
        date = "25.11.09",
        price = 5000,
        unitAmount = 100,
        unitDisplayName = "g",
        isFirst = false,
        isLast = false,
    )
}

@Preview(showBackground = true)
@Composable
private fun PriceHistoryItemLastPreview() {
    PriceHistoryItem(
        date = "25.09.08",
        price = 4800,
        unitAmount = 100,
        unitDisplayName = "g",
        isFirst = false,
        isLast = true,
    )
}
