package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import java.text.NumberFormat
import java.util.Locale

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
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier.height(72.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Timeline indicator
        Column(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Circle indicator
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(12.dp)
                    .background(
                        color = if (isFirst) PrimaryBlue500 else Grayscale400,
                        shape = CircleShape,
                    ),
            )

            // Vertical line (not shown for last item)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Grayscale400),
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Date and price content
        Column {
            Text(
                text = date,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${numberFormat.format(price)}원/${unitAmount}${unitDisplayName}",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
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
