package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue100

// ============================================================
// WheelPicker - Scrollable selector with snapping
// ============================================================

@Composable
fun <T> ChordWheelPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemToString: (T) -> String = { it.toString() },
) {
    val initialIndex = items.indexOf(selectedItem).coerceAtLeast(0)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex,
    )
    val itemHeightPx = with(LocalDensity.current) { 40.dp.toPx() }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && items.isNotEmpty()) {
            val offsetItemCount = ((listState.firstVisibleItemScrollOffset + (itemHeightPx / 2f)) / itemHeightPx).toInt()
            val targetIndex = (listState.firstVisibleItemIndex + offsetItemCount).coerceIn(items.indices)

            if (listState.firstVisibleItemIndex != targetIndex || listState.firstVisibleItemScrollOffset != 0) {
                listState.animateScrollToItem(targetIndex)
            }

            if (items[targetIndex] != selectedItem) {
                onItemSelected(items[targetIndex])
            }
        }
    }

    Box(
        modifier = modifier
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Grayscale200),
        contentAlignment = Alignment.Center,
    ) {
        // Selection highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(PrimaryBlue100),
        )

        // Scrollable items
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 80.dp),
        ) {
            items(items.size) { index ->
                val isSelected = items[index] == selectedItem
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clickable { onItemSelected(items[index]) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = itemToString(items[index]),
                        fontFamily = PretendardFontFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = if (isSelected) 20.sp else 16.sp,
                        color = if (isSelected) Grayscale900 else Grayscale500,
                    )
                }
            }
        }

        // Top gradient fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Grayscale200, Grayscale200.copy(alpha = 0f)),
                    ),
                ),
        )

        // Bottom gradient fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Grayscale200.copy(alpha = 0f), Grayscale200),
                    ),
                ),
        )
    }
}

// ============================================================
// TimeWheelPicker - Minutes + Seconds dual picker
// ============================================================

@Composable
fun ChordTimeWheelPicker(
    selectedMinutes: Int,
    selectedSeconds: Int,
    onTimeSelected: (minutes: Int, seconds: Int) -> Unit,
    modifier: Modifier = Modifier,
    minuteRange: IntRange = 0..59,
    secondRange: IntRange = 0..59,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChordWheelPicker(
            items = minuteRange.toList(),
            selectedItem = selectedMinutes,
            onItemSelected = { onTimeSelected(it, selectedSeconds) },
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "\uBD84",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Grayscale900,
            modifier = Modifier.padding(horizontal = 8.dp),
        )

        ChordWheelPicker(
            items = secondRange.toList(),
            selectedItem = selectedSeconds,
            onItemSelected = { onTimeSelected(selectedMinutes, it) },
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "\uCD08",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            color = Grayscale900,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}

// ============================================================
// Previews
// ============================================================

@Preview(showBackground = true)
@Composable
private fun ChordWheelPickerPreview() {
    ChordWheelPicker(
        items = (0..59).toList(),
        selectedItem = 30,
        onItemSelected = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTimeWheelPickerPreview() {
    ChordTimeWheelPicker(
        selectedMinutes = 1,
        selectedSeconds = 30,
        onTimeSelected = { _, _ -> },
    )
}
