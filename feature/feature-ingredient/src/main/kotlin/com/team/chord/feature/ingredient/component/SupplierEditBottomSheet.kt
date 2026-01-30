package com.team.chord.feature.ingredient.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.team.chord.core.ui.component.ChordBottomSheet
import com.team.chord.core.ui.component.ChordLargeButton
import com.team.chord.core.ui.component.ChordTextField

/**
 * SupplierEditBottomSheet - 공급업체 수정 모달
 *
 * 식재료 상세 화면에서 공급업체 정보를 수정할 때 사용하는 BottomSheet.
 * ChordBottomSheet를 기반으로 하며 TextField와 완료 버튼을 포함합니다.
 *
 * @param supplierName 현재 공급업체명
 * @param onSupplierNameChange 공급업체명 변경 콜백
 * @param onClear TextField 초기화 버튼 클릭 콜백
 * @param onConfirm 완료 버튼 클릭 콜백
 * @param onDismiss BottomSheet 닫기 콜백
 * @param modifier Modifier
 */
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
    ChordBottomSheet(
        onDismissRequest = onDismiss,
        title = "공급업체",
        modifier = modifier,
        content = {
            ChordTextField(
                value = supplierName,
                onValueChange = onSupplierNameChange,
                placeholder = "공급업체명을 입력해주세요",
                onClear = onClear,
                cornerRadius = 12,
            )
        },
        confirmButton = {
            ChordLargeButton(
                text = "완료",
                onClick = onConfirm,
            )
        },
    )
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
