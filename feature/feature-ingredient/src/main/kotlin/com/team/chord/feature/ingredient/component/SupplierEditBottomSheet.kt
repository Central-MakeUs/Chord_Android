package com.team.chord.feature.ingredient.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierEditBottomSheet(
    supplierName: String,
    onSupplierNameChange: (String) -> Unit,
    onClear: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        modifier = modifier,
        containerColor = Grayscale100,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp, bottom = 34.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "공급업체",
                fontFamily = PretendardFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Grayscale900,
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChordTextField(
                value = supplierName,
                onValueChange = onSupplierNameChange,
                placeholder = "공급업체명을 입력해주세요",
                onClear = onClear,
                cornerRadius = 24,
                borderColor = Grayscale700,
                focusedBorderColor = Grayscale700,
            )

            Spacer(modifier = Modifier.height(34.dp))

            ChordLargeButton(
                text = "완료",
                onClick = onConfirm,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SupplierEditBottomSheetPreview() {
    SupplierEditBottomSheet(
        supplierName = "쿠팡",
        onSupplierNameChange = {},
        onClear = {},
        onConfirm = {},
        onDismiss = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SupplierEditBottomSheetEmptyPreview() {
    SupplierEditBottomSheet(
        supplierName = "",
        onSupplierNameChange = {},
        onClear = {},
        onConfirm = {},
        onDismiss = {},
    )
}
