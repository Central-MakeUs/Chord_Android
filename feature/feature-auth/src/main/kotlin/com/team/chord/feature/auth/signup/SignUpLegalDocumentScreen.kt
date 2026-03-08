package com.team.chord.feature.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.team.chord.core.ui.legal.LegalDocumentScreen
import com.team.chord.core.ui.legal.coachCoachPrivacyDocument
import com.team.chord.core.ui.legal.coachCoachTermsDocument

@Composable
fun SignUpTermsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LegalDocumentScreen(
        topBarTitle = "",
        content = coachCoachTermsDocument(),
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

@Composable
fun SignUpPrivacyScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LegalDocumentScreen(
        topBarTitle = "",
        content = coachCoachPrivacyDocument(),
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}
