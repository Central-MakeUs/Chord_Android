package com.team.chord.feature.menu.management.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTimeWheelPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPreparationTimeBottomSheet(
    currentSeconds: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
) {
    var selectedMinutes by remember { mutableIntStateOf(currentSeconds / 60) }
    var selectedSeconds by remember { mutableIntStateOf(currentSeconds % 60) }

    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "제조 시간",
        content = {
            ChordTimeWheelPicker(
                selectedMinutes = selectedMinutes,
                selectedSeconds = selectedSeconds,
                onTimeSelected = { minutes, seconds ->
                    selectedMinutes = minutes
                    selectedSeconds = seconds
                },
            )
        },
        confirmButton = {
            ChordLargeButton(
                text = "확인",
                onClick = {
                    val totalSeconds = selectedMinutes * 60 + selectedSeconds
                    onConfirm(totalSeconds)
                },
            )
        },
    )
}
