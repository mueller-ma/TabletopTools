package com.github.muellerma.tabletoptools

import com.github.muellerma.tabletoptools.utils.isLatinLetter
import com.github.muellerma.tabletoptools.utils.positionInAlphabet
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

    @Test
    fun testPositionInAlphabet() {
        assertEquals('A'.positionInAlphabet(), 1)
        assertEquals('B'.positionInAlphabet(), 2)
        assertEquals('Z'.positionInAlphabet(), 26)
        assertEquals('a'.positionInAlphabet(), 1)
        assertEquals('b'.positionInAlphabet(), 2)
        assertEquals('z'.positionInAlphabet(), 26)
    }
}