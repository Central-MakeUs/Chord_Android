package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import java.text.NumberFormat
import java.util.Locale
import com.team.chord.core.ui.R as CoreUiR

@Composable
fun IngredientListItem(
    name: String,
    price: Int,
    usage: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDeleteMode: Boolean = false,
    isSelected: Boolean = false,
    onCheckedChange: (() -> Unit)? = null,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Grayscale100
            )
            .clickable(onClick = if (isDeleteMode) { onCheckedChange ?: {} } else onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isDeleteMode) {
                Icon(
                    painter = painterResource(
                        if (isSelected) CoreUiR.drawable.ic_checkbox
                        else CoreUiR.drawable.ic_un_checkbox
                    ),
                    contentDescription = if (isSelected) "Selected" else "Not selected",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onCheckedChange?.invoke() },
                    tint = if (isSelected) Color.Unspecified else Grayscale300,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = name,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Grayscale900,
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "${numberFormat.format(price)}원",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = PrimaryBlue500,
            )
            Text(
                text = usage,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Grayscale500,
            )
        }
    }
}
