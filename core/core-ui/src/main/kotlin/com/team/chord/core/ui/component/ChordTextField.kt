package com.team.chord.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.StatusDanger

@Composable
fun ChordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    unitText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onClear: (() -> Unit)? = null,
    cornerRadius: Int = 8,
    borderColor: Color = Grayscale300,
    focusedBorderColor: Color = PrimaryBlue500,
) {
    val shape = RoundedCornerShape(cornerRadius.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val currentBorderColor = when {
        isError -> StatusDanger
        isFocused -> focusedBorderColor
        else -> borderColor
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color = Grayscale100,
                    shape = shape,
                )
                .border(
                    width = 1.dp,
                    color = currentBorderColor,
                    shape = shape,
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Grayscale500,
                            ),
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = if (isError) StatusDanger else Grayscale900,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        keyboardActions = keyboardActions,
                        cursorBrush = SolidColor(Grayscale900),
                        interactionSource = interactionSource,
                    )
                }

                if (unitText != null) {
                    Text(
                        text = unitText,
                        style = TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Grayscale900,
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                if (onClear != null && value.isNotEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_circle),
                        contentDescription = "Clear",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(20.dp)
                            .clickable { onClear() },
                        tint = Grayscale500,
                    )
                }
            }
        }

        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = StatusDanger,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordTextFieldEmptyPreview() {
    ChordTextField(
        value = "",
        onValueChange = {},
        placeholder = "가격을 입력해주세요",
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTextFieldWithValuePreview() {
    ChordTextField(
        value = "5,600",
        onValueChange = {},
        unitText = "원",
        onClear = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordTextFieldErrorPreview() {
    ChordTextField(
        value = "가나다라",
        onValueChange = {},
        isError = true,
        errorMessage = "숫자만 입력할 수 있어요",
    )
}
