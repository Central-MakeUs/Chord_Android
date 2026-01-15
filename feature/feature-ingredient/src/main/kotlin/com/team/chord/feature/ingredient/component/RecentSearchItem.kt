package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

/**
 * Recent search item component displaying a search query with a delete button.
 *
 * @param query The search query text to display.
 * @param onClick Callback when the item is clicked.
 * @param onDeleteClick Callback when the delete button is clicked.
 * @param modifier Modifier to be applied to the item.
 */
@Composable
fun RecentSearchItem(
    query: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = query,
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Grayscale900,
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "삭제",
            tint = Grayscale900,
            modifier = Modifier
                .size(8.dp)
                .clickable(onClick = onDeleteClick),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentSearchItemPreview() {
    RecentSearchItem(
        query = "레몬티",
        onClick = {},
        onDeleteClick = {},
    )
}
