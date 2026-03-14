package com.team.chord.core.ui.component

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class DigitGroupingVisualTransformationTest {
    private val transformation = DigitGroupingVisualTransformation()

    @Test
    fun `filter inserts grouping separators`() {
        val result = transformation.filter(AnnotatedString("10320"))

        assertEquals("10,320", result.text.text)
    }

    @Test
    fun `offset mapping stays aligned around inserted commas`() {
        val result = transformation.filter(AnnotatedString("10320"))
        val mapping = result.offsetMapping

        assertEquals(0, mapping.originalToTransformed(0))
        assertEquals(2, mapping.originalToTransformed(2))
        assertEquals(4, mapping.originalToTransformed(3))
        assertEquals(6, mapping.originalToTransformed(5))

        assertEquals(2, mapping.transformedToOriginal(2))
        assertEquals(2, mapping.transformedToOriginal(3))
        assertEquals(3, mapping.transformedToOriginal(4))
        assertEquals(5, mapping.transformedToOriginal(6))
    }
}
