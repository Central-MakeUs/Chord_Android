package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

/**
 * Search result item component displaying an ingredient row that opens detail.
 *
 * @param name The name of the ingredient.
 * @param onAddClick Callback when the row is clicked.
 * @param modifier Modifier to be applied to the item.
 */
@Composable
fun SearchResultItem(
    name: String,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Grayscale100)
            .clickable(onClick = onAddClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Grayscale900,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultItemPreview() {
    SearchResultItem(
        name = "원두",
        onAddClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchResultItemAddedPreview() {
    SearchResultItem(
        name = "우유",
        onAddClick = {},
    )
}
