package com.team.chord.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.core.ui.theme.PrimaryBlue200
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PrimaryBlue600
import com.team.chord.core.ui.theme.PretendardFontFamily

// ============================================================
// Large Button - 335x52, 12dp radius, Primary only
// ============================================================

@Composable
fun ChordLargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = PrimaryBlue500,
    textColor: Color = Grayscale100,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor,
        ),
        contentPadding = PaddingValues(vertical = 14.5.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            ),
        )
    }
}

// ============================================================
// Tab Button - 60x36 or 76x36, pill shape (18dp radius)
// Primary / Secondary / Tertiary
// ============================================================

enum class ChordTabButtonVariant {
    Primary,
    Secondary,
    Tertiary,
}

@Composable
fun ChordTabButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ChordTabButtonVariant = ChordTabButtonVariant.Primary,
    enabled: Boolean = true,
    @DrawableRes trailingIcon: Int? = null,
) {
    val containerColor: Color
    val contentColor: Color
    val border: BorderStroke?
    val iconTint: Color

    when (variant) {
        ChordTabButtonVariant.Primary -> {
            containerColor = PrimaryBlue600
            contentColor = Grayscale100
            border = null
            iconTint = Grayscale100
        }
        ChordTabButtonVariant.Secondary -> {
            containerColor = PrimaryBlue200
            contentColor = PrimaryBlue500
            border = BorderStroke(1.dp, PrimaryBlue500)
            iconTint = PrimaryBlue500
        }
        ChordTabButtonVariant.Tertiary -> {
            containerColor = Color.Transparent
            contentColor = Grayscale600
            border = BorderStroke(1.dp, Grayscale600)
            iconTint = Grayscale500
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        border = border,
        contentPadding = PaddingValues(vertical = 6.5.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                ),
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconTint,
                )
            }
        }
    }
}

// ============================================================
// Small Button - 78x39, 8dp radius
// Primary / Secondary with optional trailing icon
// ============================================================

enum class ChordSmallButtonVariant {
    Primary,
    Secondary,
}

@Composable
fun ChordSmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ChordSmallButtonVariant = ChordSmallButtonVariant.Primary,
    enabled: Boolean = true,
    @DrawableRes trailingIcon: Int? = null,
) {
    val containerColor: Color
    val contentColor: Color
    val border: BorderStroke?

    when (variant) {
        ChordSmallButtonVariant.Primary -> {
            containerColor = PrimaryBlue500
            contentColor = Grayscale100
            border = null
        }
        ChordSmallButtonVariant.Secondary -> {
            containerColor = PrimaryBlue100
            contentColor = Grayscale900
            border = BorderStroke(1.dp, PrimaryBlue500)
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(39.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        border = border,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                ),
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = contentColor,
                )
            }
        }
    }
}

// ============================================================
// Previews
// ============================================================

@Preview(showBackground = true)
@Composable
private fun ChordLargeButtonPreview() {
    ChordLargeButton(
        text = "Label",
        onClick = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTabButtonPrimaryPreview() {
    ChordTabButton(
        text = "담기",
        onClick = {},
        variant = ChordTabButtonVariant.Primary,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTabButtonSecondaryPreview() {
    ChordTabButton(
        text = "담기",
        onClick = {},
        variant = ChordTabButtonVariant.Secondary,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTabButtonTertiaryPreview() {
    ChordTabButton(
        text = "담기",
        onClick = {},
        variant = ChordTabButtonVariant.Tertiary,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTabButtonTertiaryWithIconPreview() {
    ChordTabButton(
        text = "담기",
        onClick = {},
        variant = ChordTabButtonVariant.Tertiary,
        trailingIcon = R.drawable.ic_close_circle,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordSmallButtonPrimaryPreview() {
    ChordSmallButton(
        text = "Label",
        onClick = {},
        variant = ChordSmallButtonVariant.Primary,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordSmallButtonPrimaryWithIconPreview() {
    ChordSmallButton(
        text = "Label",
        onClick = {},
        variant = ChordSmallButtonVariant.Primary,
        trailingIcon = R.drawable.ic_add,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordSmallButtonSecondaryPreview() {
    ChordSmallButton(
        text = "Label",
        onClick = {},
        variant = ChordSmallButtonVariant.Secondary,
    )
}

// ============================================================
// Outlined Button - Full width, 52dp height, 12dp radius
// Used for secondary actions like delete
// ============================================================

@Composable
fun ChordOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Grayscale600,
        ),
        border = BorderStroke(1.dp, Grayscale500),
        contentPadding = PaddingValues(vertical = 14.5.dp),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordOutlinedButtonPreview() {
    ChordOutlinedButton(
        text = "메뉴 삭제",
        onClick = {},
    )
}
