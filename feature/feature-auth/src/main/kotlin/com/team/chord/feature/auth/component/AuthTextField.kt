package com.team.chord.feature.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale300
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale800
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation =
        when {
            isPassword && !passwordVisible -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle =
                    TextStyle(
                        fontFamily = PretendardFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Grayscale900,
                    ),
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                        imeAction = imeAction,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { onImeAction() },
                        onNext = { onImeAction() },
                    ),
                cursorBrush = SolidColor(Grayscale800),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style =
                                    TextStyle(
                                        fontFamily = PretendardFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Grayscale500,
                                    ),
                            )
                        }
                        innerTextField()

                        if (isPassword) {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.align(Alignment.CenterEnd),
                            ) {
                                Icon(
                                    imageVector =
                                        if (passwordVisible) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                    contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기",
                                    tint = Grayscale500,
                                )
                            }
                        }
                    }
                },
            )
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Grayscale300,
        )
    }
}
