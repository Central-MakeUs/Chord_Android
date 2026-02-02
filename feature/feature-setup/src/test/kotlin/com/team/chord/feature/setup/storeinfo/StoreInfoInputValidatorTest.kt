package com.team.chord.feature.setup.storeinfo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreInfoInputValidatorTest {
    @Test
    fun `employee count validation rejects empty and zero values`() {
        assertNull(parseEmployeeCount("", ownerSolo = false))
        assertNull(parseEmployeeCount("0", ownerSolo = false))
        assertNull(parseEmployeeCount("00", ownerSolo = false))
    }

    @Test
    fun `employee count validation accepts 1 to 99`() {
        assertEquals(1, parseEmployeeCount("01", ownerSolo = false))
        assertEquals(99, parseEmployeeCount("99", ownerSolo = false))
        assertNull(parseEmployeeCount("100", ownerSolo = false))
    }

    @Test
    fun `employee count rejects non digits`() {
        assertNull(parseEmployeeCount("9a9", ownerSolo = false))
    }

    @Test
    fun `employee count owner solo forces zero`() {
        assertEquals("0", normalizeEmployeeCountInput("99", ownerSolo = true))
        assertEquals(0, parseEmployeeCount("99", ownerSolo = true))
        assertEquals(0, parseEmployeeCount("", ownerSolo = true))
    }

    @Test
    fun `hourly wage validation trims leading zeros and enforces range`() {
        assertNull(parseHourlyWage(""))
        assertNull(parseHourlyWage("0"))
        assertNull(parseHourlyWage("00"))
        assertEquals(1000, parseHourlyWage("001000"))
        assertEquals(999999, parseHourlyWage("999999"))
        assertNull(parseHourlyWage("1000000"))
    }

    @Test
    fun `hourly wage rejects non digits`() {
        assertNull(parseHourlyWage("12a"))
    }

    @Test
    fun `cta predicate requires both values valid`() {
        assertFalse(isPostStoreNameNextEnabled("", ownerSolo = false, hourlyWageInput = ""))
        assertFalse(isPostStoreNameNextEnabled("1", ownerSolo = false, hourlyWageInput = "0"))
        assertTrue(isPostStoreNameNextEnabled("01", ownerSolo = false, hourlyWageInput = "100"))
        assertTrue(isPostStoreNameNextEnabled("", ownerSolo = true, hourlyWageInput = "5000"))
    }
}
