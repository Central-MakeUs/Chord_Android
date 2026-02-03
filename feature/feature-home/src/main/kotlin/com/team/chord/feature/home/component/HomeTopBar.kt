package com.team.chord.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale700
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PrimaryBlue500
import com.team.chord.feature.home.R as HomeR

@Composable
fun HomeTopBar(
    onNotificationClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = HomeR.drawable.ic_home_logo),
                contentDescription = "Chord 로고",
                tint = PrimaryBlue500,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CHORD",
                style = MaterialTheme.typography.titleLarge,
                color = Grayscale900,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = HomeR.drawable.ic_notification),
                contentDescription = "알림",
                tint = Grayscale700,
                modifier =
                    Modifier
                        .size(24.dp)
                        .clickable(onClick = onNotificationClick),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "메뉴",
                tint = Grayscale700,
                modifier =
                    Modifier
                        .size(24.dp)
                        .clickable(onClick = onMenuClick),
            )
        }
    }
}
