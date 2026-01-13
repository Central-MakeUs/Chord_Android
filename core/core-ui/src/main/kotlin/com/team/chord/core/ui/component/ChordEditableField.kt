package com.team.chord.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.chord.core.ui.R
import com.team.chord.core.ui.theme.Grayscale500
import com.team.chord.core.ui.theme.Grayscale900
import com.team.chord.core.ui.theme.PretendardFontFamily

enum class EditableFieldIcon {
    Arrow,
    Pencil,
}

@Composable
fun ChordEditableField(
    label: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: EditableFieldIcon = EditableFieldIcon.Arrow,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grayscale500,
                ),
            )
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = PretendardFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Grayscale900,
                ),
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        Icon(
            painter = painterResource(
                when (trailingIcon) {
                    EditableFieldIcon.Arrow -> R.drawable.ic_arrow_right
                    EditableFieldIcon.Pencil -> R.drawable.ic_edit
                },
            ),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Grayscale500,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChordEditableFieldArrowPreview() {
    ChordEditableField(
        label = "가격",
        value = "5,600원",
        onClick = {},
        trailingIcon = EditableFieldIcon.Arrow,
    )
}

@Preview(showBackground = true)
@Composable
private fun ChordEditableFieldPencilPreview() {
    ChordEditableField(
        label = "제조시간",
        value = "1분 30초",
        onClick = {},
        trailingIcon = EditableFieldIcon.Pencil,
    )
}
