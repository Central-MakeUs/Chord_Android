package com.team.chord.core.ui.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.component.ChordTopAppBar
import com.team.chord.core.ui.theme.Grayscale100
import com.team.chord.core.ui.theme.Grayscale600
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

data class LegalDocumentContent(
    val headline: String,
    val introBlocks: List<LegalDocumentBlock> = emptyList(),
    val sections: List<LegalDocumentSection>,
)

data class LegalDocumentSection(
    val title: String,
    val blocks: List<LegalDocumentBlock>,
)

sealed interface LegalDocumentBlock {
    data class Heading(
        val text: String,
    ) : LegalDocumentBlock

    data class Paragraph(
        val text: String,
    ) : LegalDocumentBlock

    data class Bullet(
        val text: String,
        val prefix: String = "•",
    ) : LegalDocumentBlock
}

@Composable
fun LegalDocumentScreen(
    topBarTitle: String,
    content: LegalDocumentContent,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Grayscale100),
    ) {
        ChordTopAppBar(
            title = topBarTitle,
            onBackClick = onNavigateBack,
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(20.dp),
        ) {
            item {
                Text(
                    text = content.headline,
                    style =
                        TextStyle(
                            fontFamily = PretendardFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Grayscale900,
                        ),
                )

                if (content.introBlocks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LegalDocumentBlocks(blocks = content.introBlocks)
                }
            }

            items(content.sections) { section ->
                LegalDocumentSectionItem(section = section)
            }
        }
    }
}

@Composable
private fun LegalDocumentSectionItem(
    section: LegalDocumentSection,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = section.title,
            style =
                TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Grayscale900,
                ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        LegalDocumentBlocks(blocks = section.blocks)
    }
}

@Composable
private fun LegalDocumentBlocks(
    blocks: List<LegalDocumentBlock>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEachIndexed { index, block ->
            when (block) {
                is LegalDocumentBlock.Heading -> {
                    Text(
                        text = block.text,
                        style =
                            TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Grayscale900,
                            ),
                    )
                }

                is LegalDocumentBlock.Paragraph -> {
                    Text(
                        text = block.text,
                        style =
                            TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 25.sp,
                                color = Grayscale600,
                            ),
                    )
                }

                is LegalDocumentBlock.Bullet -> {
                    Text(
                        text = "${block.prefix} ${block.text}",
                        style =
                            TextStyle(
                                fontFamily = PretendardFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                lineHeight = 25.sp,
                                color = Grayscale600,
                            ),
                            modifier = Modifier.padding(start = 4.dp),
                    )
                }
            }

            if (index != blocks.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
