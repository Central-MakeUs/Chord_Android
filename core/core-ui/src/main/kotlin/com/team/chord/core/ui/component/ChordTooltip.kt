package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.PretendardFontFamily

enum class TooltipDirection {
    Up,
    UpLeft,
    UpRight,
    Down,
    DownLeft,
    DownRight,
    Left,
    Right,
}

/**
 * Tooltip bubble only (without icon).
 * Use this when you need to position the bubble separately from the trigger icon.
 */
@Composable
fun ChordTooltipBubble(
    text: String,
    modifier: Modifier = Modifier,
    direction: TooltipDirection = TooltipDirection.UpLeft,
) {
    when (direction) {
        TooltipDirection.Left, TooltipDirection.Right -> {
            TooltipBubbleHorizontal(
                text = text,
                isLeftArrow = direction == TooltipDirection.Right,
                modifier = modifier,
            )
        }
        else -> {
            TooltipBubbleVertical(
                text = text,
                direction = direction,
                modifier = modifier,
            )
        }
    }
}

/**
 * Tooltip icon that can be clicked to toggle visibility.
 */
@Composable
fun ChordTooltipIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(R.drawable.ic_tooltip),
        contentDescription = "도움말",
        modifier = modifier
            .size(10.dp)
            .clickable { onClick() },
        tint = Grayscale500,
    )
}

/**
 * Combined tooltip with icon and bubble together.
 * For simple use cases where icon and bubble are adjacent.
 */
@Composable
fun ChordTooltip(
    text: String,
    modifier: Modifier = Modifier,
    direction: TooltipDirection = TooltipDirection.UpLeft,
) {
    var isVisible by remember { mutableStateOf(false) }

    when (direction) {
        TooltipDirection.Left -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isVisible) {
                    TooltipBubbleHorizontal(text = text, isLeftArrow = false)
                }
                ChordTooltipIcon(onClick = { isVisible = !isVisible })
            }
        }
        TooltipDirection.Right -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChordTooltipIcon(onClick = { isVisible = !isVisible })
                if (isVisible) {
                    TooltipBubbleHorizontal(text = text, isLeftArrow = true)
                }
            }
        }
        else -> {
            Column(
                modifier = modifier,
                horizontalAlignment = when (direction) {
                    TooltipDirection.UpLeft, TooltipDirection.DownLeft -> Alignment.Start
                    TooltipDirection.UpRight, TooltipDirection.DownRight -> Alignment.End
                    else -> Alignment.CenterHorizontally
                },
            ) {
                val isUpDirection = direction in listOf(
                    TooltipDirection.Up,
                    TooltipDirection.UpLeft,
                    TooltipDirection.UpRight,
                )

                if (isUpDirection) {
                    ChordTooltipIcon(onClick = { isVisible = !isVisible })
                    if (isVisible) {
                        TooltipBubbleVertical(text = text, direction = direction)
                    }
                } else {
                    if (isVisible) {
                        TooltipBubbleVertical(text = text, direction = direction)
                    }
                    ChordTooltipIcon(onClick = { isVisible = !isVisible })
                }
            }
        }
    }
}

@Composable
private fun TooltipBubbleVertical(
    text: String,
    direction: TooltipDirection,
    modifier: Modifier = Modifier,
) {
    val isUpDirection = direction in listOf(
        TooltipDirection.Up,
        TooltipDirection.UpLeft,
        TooltipDirection.UpRight,
    )

    val arrowAlignment = when (direction) {
        TooltipDirection.UpLeft, TooltipDirection.DownLeft -> Alignment.Start
        TooltipDirection.UpRight, TooltipDirection.DownRight -> Alignment.End
        else -> Alignment.CenterHorizontally
    }

    // Offset for arrow position
    val arrowStartPadding = when (direction) {
        TooltipDirection.UpLeft, TooltipDirection.DownLeft -> 20.dp
        else -> 0.dp
    }
    val arrowEndPadding = when (direction) {
        TooltipDirection.UpRight, TooltipDirection.DownRight -> 20.dp
        else -> 0.dp
    }

    Column(
        modifier = modifier.offset(y = if (isUpDirection) (-2).dp else 2.dp),
        horizontalAlignment = arrowAlignment,
    ) {
        if (isUpDirection) {
            // Arrow pointing up
            Box(
                modifier = Modifier
                    .padding(start = arrowStartPadding, end = arrowEndPadding)
                    .size(width = 12.dp, height = 6.dp)
                    .clip(TriangleUpShape)
                    .background(Grayscale800),
            )
        }

        // Bubble
        Box(
            modifier = Modifier
                .background(
                    color = Grayscale800,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = text,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Grayscale100,
            )
        }

        if (!isUpDirection) {
            // Arrow pointing down
            Box(
                modifier = Modifier
                    .padding(start = arrowStartPadding, end = arrowEndPadding)
                    .size(width = 12.dp, height = 6.dp)
                    .clip(TriangleDownShape)
                    .background(Grayscale800),
            )
        }
    }
}

@Composable
private fun TooltipBubbleHorizontal(
    text: String,
    isLeftArrow: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isLeftArrow) {
            // Arrow pointing left
            Box(
                modifier = Modifier
                    .size(width = 6.dp, height = 12.dp)
                    .clip(TriangleLeftShape)
                    .background(Grayscale800),
            )
        }

        // Bubble
        Box(
            modifier = Modifier
                .background(
                    color = Grayscale800,
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = text,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Grayscale100,
            )
        }

        if (!isLeftArrow) {
            // Arrow pointing right
            Box(
                modifier = Modifier
                    .size(width = 6.dp, height = 12.dp)
                    .clip(TriangleRightShape)
                    .background(Grayscale800),
            )
        }
    }
}

// Triangle pointing up (for Up directions - bubble below icon)
private val TriangleUpShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

// Triangle pointing down (for Down directions - bubble above icon)
private val TriangleDownShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

// Triangle pointing left (for Right direction - bubble right of icon)
private val TriangleLeftShape = GenericShape { size, _ ->
    moveTo(0f, size.height / 2f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    close()
}

// Triangle pointing right (for Left direction - bubble left of icon)
private val TriangleRightShape = GenericShape { size, _ ->
    moveTo(size.width, size.height / 2f)
    lineTo(0f, 0f)
    lineTo(0f, size.height)
    close()
}

@Preview(showBackground = true)
@Composable
private fun ChordTooltipBubbleUpLeftPreview() {
    ChordTooltipBubble(
        text = "사장님을 포함한 직원수를 의미해요",
        direction = TooltipDirection.UpLeft,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTooltipBubbleDownLeftPreview() {
    ChordTooltipBubble(
        text = "사장님을 포함한 직원수를 의미해요",
        direction = TooltipDirection.DownLeft,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTooltipUpLeftPreview() {
    ChordTooltip(
        text = "사장님을 포함한 직원수를 의미해요",
        direction = TooltipDirection.UpLeft,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTooltipDownPreview() {
    ChordTooltip(
        text = "사장님을 포함한 직원수를 의미해요",
        direction = TooltipDirection.Down,
    )
}
