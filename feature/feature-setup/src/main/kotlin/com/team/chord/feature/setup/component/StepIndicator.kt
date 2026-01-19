package com.team.chord.feature.setup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale400
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.core.ui.theme.PretendardFontFamily

/**
 * Step indicator showing progress through a multi-step process.
 * Format: 1 (filled) --- 2 (empty)
 *
 * @param currentStep The current step (1-indexed)
 * @param totalSteps Total number of steps
 * @param modifier Modifier for the component
 */
@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (step in 1..totalSteps) {
            val isActive = step <= currentStep

            // Step circle with number
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isActive) PrimaryBlue500 else Grayscale400,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = step.toString(),
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Grayscale100,
                )
            }

            // Connecting line between steps (not after last step)
            if (step < totalSteps) {
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .width(16.dp)
                        .height(2.dp)
                        .background(Grayscale400),
                )
                Spacer(modifier = Modifier.width(2.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepIndicatorStep1Preview() {
    StepIndicator(
        currentStep = 1,
        totalSteps = 2,
    )
}

@Preview(showBackground = true)
@Composable
private fun StepIndicatorStep2Preview() {
    StepIndicator(
        currentStep = 2,
        totalSteps = 2,
    )
}

@Preview(showBackground = true)
@Composable
private fun StepIndicator3StepsPreview() {
    StepIndicator(
        currentStep = 2,
        totalSteps = 3,
    )
}
