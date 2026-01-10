package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500

private val DefaultTitleStyle = TextStyle(
    fontFamily = PretendardFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
)

@Composable
fun ChordTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = DefaultTitleStyle,
    titleColor: Color = Grayscale900,
    onBackClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    actionContent: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Grayscale100),
    ) {
        // Back button (left)
        if (onBackClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .size(24.dp)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "뒤로가기",
                    tint = Grayscale900,
                )
            }
        }

        // Title (center when back button exists, left otherwise)
        Text(
            text = title,
            style = titleStyle,
            color = titleColor,
            modifier = if (onBackClick != null) {
                Modifier.align(Alignment.Center)
            } else {
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
            },
        )

        // Action (right)
        if (actionContent != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp)
                    .then(
                        if (onActionClick != null) {
                            Modifier.clickable { onActionClick() }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                actionContent()
            }
        }
    }
}

@Composable
fun ChordTopAppBarWithTextAction(
    title: String,
    actionText: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onActionClick: () -> Unit,
) {
    ChordTopAppBar(
        title = title,
        modifier = modifier,
        onBackClick = onBackClick,
        onActionClick = onActionClick,
        actionContent = {
            Text(
                text = actionText,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                ),
                color = Grayscale600,
            )
        },
    )
}

@Composable
fun ChordTopAppBarWithIconAction(
    title: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = DefaultTitleStyle,
    titleColor: Color = Grayscale900,
    onBackClick: (() -> Unit)? = null,
    actionIcon: TopAppBarActionIcon,
    onActionClick: () -> Unit,
) {
    ChordTopAppBar(
        title = title,
        modifier = modifier,
        titleStyle = titleStyle,
        titleColor = titleColor,
        onBackClick = onBackClick,
        onActionClick = onActionClick,
        actionContent = {
            Icon(
                painter = painterResource(
                    when (actionIcon) {
                        TopAppBarActionIcon.Search -> R.drawable.ic_search
                        TopAppBarActionIcon.Menu -> R.drawable.ic_menu
                    }
                ),
                contentDescription = when (actionIcon) {
                    TopAppBarActionIcon.Search -> "검색"
                    TopAppBarActionIcon.Menu -> "메뉴"
                },
                tint = Grayscale900,
            )
        },
    )
}

enum class TopAppBarActionIcon {
    Search,
    Menu,
}

@Preview(showBackground = true)
@Composable
private fun ChordTopAppBarPreview() {
    ChordTopAppBar(
        title = "건강기능식품",
        onBackClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTopAppBarWithTextActionPreview() {
    ChordTopAppBarWithTextAction(
        title = "건강기능식품",
        actionText = "스크랩하기",
        onBackClick = {},
        onActionClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTopAppBarLeftTitleWithSearchPreview() {
    ChordTopAppBarWithIconAction(
        title = "건강기능식품",
        actionIcon = TopAppBarActionIcon.Search,
        onActionClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTopAppBarLeftTitleWithMenuPreview() {
    ChordTopAppBarWithIconAction(
        title = "성분사전",
        actionIcon = TopAppBarActionIcon.Menu,
        onActionClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTopAppBarWithCustomTitleStylePreview() {
    ChordTopAppBarWithIconAction(
        title = "성분사전",
        titleColor = PrimaryBlue500,
        actionIcon = TopAppBarActionIcon.Menu,
        onActionClick = {},
    )
}
