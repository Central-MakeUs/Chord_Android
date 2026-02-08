package com.team.chord.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.chord.core.ui.theme.Grayscale200
import com.team.chord.core.ui.theme.PrimaryBlue100
import com.team.chord.feature.home.component.HomeHeroSection
import com.team.chord.feature.home.component.HomePrimaryCtaBar
import com.team.chord.feature.home.component.HomeStatsRow
import com.team.chord.feature.home.component.HomeStrategyGuideCard
import com.team.chord.feature.home.component.HomeTopBar

@Composable
fun HomeScreen(
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onNavigateToSetting = onNavigateToSetting,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreenContent(
    uiState: HomeUiState,
    onNavigateToSetting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PrimaryBlue100, Grayscale200),
                        ),
                    ),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    HomeTopBar(
                        onNotificationClick = { },
                        onMenuClick = onNavigateToSetting,
                    )
                }
                item {
                    HomeHeroSection(title = uiState.heroTitle)
                }
                item {
                    HomePrimaryCtaBar(
                        title = uiState.ctaTitle,
                        count = uiState.ctaCount,
                        onClick = { },
                    )
                }
                item {
                    HomeStatsRow(stats = uiState.stats)
                }
                item {
                    HomeStrategyGuideCard(items = uiState.strategyItems)
                }
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
