package com.team.chord.feature.menu.management.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreparationTimeBottomSheet(
    currentSeconds: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var minutes by remember { mutableStateOf((currentSeconds / 60).toString()) }
    var seconds by remember { mutableStateOf((currentSeconds % 60).toString()) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "제조시간을 입력해주세요",
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ChordTextField(
                    value = minutes,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            minutes = input
                        }
                    },
                    placeholder = "0",
                    unitText = "분",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                )
                ChordTextField(
                    value = seconds,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            val sec = input.toIntOrNull() ?: 0
                            if (sec < 60) {
                                seconds = input
                            }
                        }
                    },
                    placeholder = "0",
                    unitText = "초",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f),
                )
            }
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = {
                    val totalSeconds = (minutes.toIntOrNull() ?: 0) * 60 + (seconds.toIntOrNull() ?: 0)
                    onConfirm(totalSeconds)
                },
                enabled = minutes.isNotBlank() || seconds.isNotBlank(),
            )
        },
    )
}
