package com.team.chord.feature.auth.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.PretendardFontFamily
import com.team.chord.core.ui.theme.PrimaryBlue600
import kotlinx.coroutines.delay

@Composable
fun SignUpCompleteScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onNavigateToLogin()
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(80.dp)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        PrimaryBlue600,
                                        PrimaryBlue600.copy(alpha = 0.7f),
                                    ),
                            ),
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        androidx.compose.material3.Text(
            text = "가입이 완료됐어요",
            fontFamily = PretendardFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = PrimaryBlue600,
        )
    }
}
