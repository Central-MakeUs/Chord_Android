package com.team.chord.feature.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordSmallButton
import com.team.chord.core.ui.component.ChordSmallButtonVariant
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

/**
 * Store information card displayed at the top of the Setting screen.
 *
 * Shows the store name, employee count, labor cost, and an edit button.
 *
 * @param storeName The name of the store.
 * @param employeeCount The number of employees.
 * @param laborCost The formatted labor cost string (e.g., "12,000원").
 * @param onEditClick Callback invoked when the edit button is clicked.
 * @param modifier Optional [Modifier] for this composable.
 */
@Composable
fun StoreInfoCard(
    storeName: String,
    employeeCount: Int,
    laborCost: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Grayscale100)
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "매장 정보",
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Grayscale600,
                ),
            )
            ChordSmallButton(
                text = "수정",
                onClick = onEditClick,
                variant = ChordSmallButtonVariant.Secondary,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = storeName,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Grayscale900,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            InfoLabel(label = "직원", value = "${employeeCount}명")
            Spacer(modifier = Modifier.width(16.dp))
            InfoLabel(label = "인건비", value = laborCost)
        }
    }
}

@Composable
private fun InfoLabel(
    label: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grayscale600,
            ),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Grayscale700,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F7FB)
@Composable
private fun StoreInfoCardPreview() {
    StoreInfoCard(
        storeName = "코치카페",
        employeeCount = 3,
        laborCost = "12,000원",
        onEditClick = {},
        modifier = Modifier.padding(16.dp),
    )
}
