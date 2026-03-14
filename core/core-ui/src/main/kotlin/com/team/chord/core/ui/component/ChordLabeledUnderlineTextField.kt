package com.team.chord.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PretendardFontFamily
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ChordLabeledUnderlineTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    unitText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeAction: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = if (isFocused) PrimaryBlue500 else Grayscale900,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            enabled = enabled,
            textStyle = TextStyle(
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = if (enabled) Grayscale900 else Grayscale500,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = if (onImeAction != null) ImeAction.Done else ImeAction.Default,
            ),
            keyboardActions = if (onImeAction != null) {
                KeyboardActions(onDone = { onImeAction() })
            } else {
                KeyboardActions.Default
            },
            cursorBrush = SolidColor(Grayscale800),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f, fill = false)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                color = Grayscale500,
                            )
                        }
                        innerTextField()
                    }
                    if (value.isNotEmpty() && unitText != null) {
                        Text(
                            text = unitText,
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = if (enabled) Grayscale900 else Grayscale500,
                        )
                    }
                }
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Grayscale300,
            thickness = 1.dp,
        )
    }
}

class DigitGroupingVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        if (rawText.isEmpty() || rawText.any { !it.isDigit() }) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val transformedText = formatDigitsWithGrouping(rawText)
        if (transformedText == rawText) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = DigitGroupingOffsetMapping(transformedText),
        )
    }
}

private class DigitGroupingOffsetMapping(
    transformedText: String,
) : OffsetMapping {
    private val originalToTransformed = IntArray(transformedText.count(Char::isDigit) + 1)
    private val transformedToOriginal = IntArray(transformedText.length + 1)

    init {
        var digitCount = 0
        originalToTransformed[0] = 0

        transformedText.forEachIndexed { index, character ->
            transformedToOriginal[index] = digitCount
            if (character.isDigit()) {
                digitCount += 1
                originalToTransformed[digitCount] = index + 1
            }
        }

        transformedToOriginal[transformedText.length] = digitCount
    }

    override fun originalToTransformed(offset: Int): Int =
        originalToTransformed[offset.coerceIn(0, originalToTransformed.lastIndex)]

    override fun transformedToOriginal(offset: Int): Int =
        transformedToOriginal[offset.coerceIn(0, transformedToOriginal.lastIndex)]
}

private fun formatDigitsWithGrouping(value: String): String {
    val number = value.toLongOrNull() ?: return value
    return NumberFormat.getNumberInstance(Locale.KOREA).format(number)
}
