package com.team.chord.feature.menu.management.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuNameBottomSheet(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf(currentName) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "메뉴명을 입력해주세요",
        content = {
            ChordTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "메뉴명을 입력해주세요",
                keyboardType = KeyboardType.Text,
                onClear = { name = "" },
            )
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank(),
            )
        },
    )
}
