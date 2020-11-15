package com.github.muellerma.tabletoptools

import com.github.muellerma.tabletoptools.utils.isLatinLetter
import org.junit.Test

import org.junit.Assert.*

class CharUtilsUnitTest {
    @Test
    fun testLatinLetterCheck() {
        assertTrue('a'.isLatinLetter())
        assertTrue('z'.isLatinLetter())
        assertTrue('A'.isLatinLetter())
        assertTrue('Z'.isLatinLetter())
        assertFalse('Ä'.isLatinLetter())
        assertFalse('é'.isLatinLetter())
    }
}