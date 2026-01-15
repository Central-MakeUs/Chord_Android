package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue200
import com.team.chord.core.ui.theme.PrimaryBlue400
import com.team.chord.core.ui.theme.PrimaryBlue500

/**
 * Search result item component displaying an ingredient with an add button.
 *
 * @param name The name of the ingredient.
 * @param isAdded Whether the ingredient has been added to the list.
 * @param onAddClick Callback when the add button is clicked.
 * @param modifier Modifier to be applied to the item.
 */
@Composable
fun SearchResultItem(
    name: String,
    isAdded: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Grayscale100)
            .clickable(enabled = !isAdded, onClick = onAddClick)
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

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (isAdded) Grayscale400 else PrimaryBlue200,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "추가",
                tint = PrimaryBlue400,
                modifier = Modifier.size(11.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultItemPreview() {
    SearchResultItem(
        name = "원두",
        isAdded = false,
        onAddClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchResultItemAddedPreview() {
    SearchResultItem(
        name = "우유",
        isAdded = true,
        onAddClick = {},
    )
}
