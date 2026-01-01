package com.team.chord.feature.setup.storeinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.feature.setup.component.SetupButton
import com.team.chord.feature.setup.component.SetupDropdown
import com.team.chord.feature.setup.component.SetupTextField

@Composable
fun StoreInfoScreen(
    onNavigateToMenuEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StoreInfoScreenContent(
        uiState = uiState,
        onStoreNameChanged = viewModel::onStoreNameChanged,
        onLocationChanged = viewModel::onLocationChanged,
        onEmployeeCountSelected = viewModel::onEmployeeCountSelected,
        onEmployeeCountDropdownToggle = viewModel::onEmployeeCountDropdownToggle,
        onEmployeeCountDropdownDismiss = viewModel::onEmployeeCountDropdownDismiss,
        onNextClicked = onNavigateToMenuEntry,
        modifier = modifier,
    )
}

@Composable
internal fun StoreInfoScreenContent(
    uiState: StoreInfoUiState,
    onStoreNameChanged: (String) -> Unit,
    onLocationChanged: (String) -> Unit,
    onEmployeeCountSelected: (String) -> Unit,
    onEmployeeCountDropdownToggle: () -> Unit,
    onEmployeeCountDropdownDismiss: () -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100)
                .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "매장 정보 입력",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Grayscale900,
        )

        Spacer(modifier = Modifier.height(48.dp))

        FieldLabel(text = "매장명")
        Spacer(modifier = Modifier.height(8.dp))
        SetupTextField(
            value = uiState.storeName,
            onValueChange = onStoreNameChanged,
            placeholder = "매장명을 입력해주세요",
            imeAction = ImeAction.Next,
        )

        Spacer(modifier = Modifier.height(24.dp))

        FieldLabel(text = "위치")
        Spacer(modifier = Modifier.height(8.dp))
        SetupTextField(
            value = uiState.location,
            onValueChange = onLocationChanged,
            placeholder = "매장 위치를 입력해주세요",
            imeAction = ImeAction.Done,
        )

        Spacer(modifier = Modifier.height(24.dp))

        FieldLabel(text = "직원 수")
        Spacer(modifier = Modifier.height(8.dp))
        SetupDropdown(
            selectedValue = uiState.employeeCount,
            placeholder = "직원 수를 선택해주세요",
            options = employeeCountOptions,
            expanded = uiState.isEmployeeCountDropdownExpanded,
            onExpandedChange = onEmployeeCountDropdownToggle,
            onOptionSelected = onEmployeeCountSelected,
            onDismiss = onEmployeeCountDropdownDismiss,
        )

        Spacer(modifier = Modifier.weight(1f))

        SetupButton(
            text = "다음",
            onClick = onNextClicked,
            enabled = uiState.isNextEnabled,
            modifier = Modifier.padding(bottom = 32.dp),
        )
    }
}

@Composable
private fun FieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontFamily = PretendardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Grayscale900,
        modifier = modifier.fillMaxWidth(),
    )
}
