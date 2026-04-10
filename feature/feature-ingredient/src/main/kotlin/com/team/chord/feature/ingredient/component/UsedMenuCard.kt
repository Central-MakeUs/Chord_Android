package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun UsedMenuCard(
    menuName: String,
    usageAmount: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.width(107.dp),
        shape = RoundedCornerShape(12.dp),
        color = Grayscale200,
    ) {
        Column(
            modifier = Modifier
                .heightIn(min = 68.dp)
                .padding(horizontal = 10.dp, vertical = 12.dp),
        ) {
            Text(
                text = menuName,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 32.dp),
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Grayscale700,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = usageAmount,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = Grayscale600,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UsedMenuCardPreview() {
    UsedMenuCard(
        menuName = "라이트 키위 라임 블렌디드",
        usageAmount = "100g",
    )
}
