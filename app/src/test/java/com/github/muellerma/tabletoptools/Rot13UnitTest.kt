package com.github.muellerma.tabletoptools

import com.github.muellerma.tabletoptools.ui.fragments.Rot13Fragment
import org.junit.Assert.assertEquals
import org.junit.Test

class Rot13UnitTest {
    @Test
    fun testRotateUpper() {
        assertEquals('A', Rot13Fragment.rotateChar('A', 0))
        assertEquals('B', Rot13Fragment.rotateChar('A', 1))
        assertEquals('C', Rot13Fragment.rotateChar('A', 2))
        assertEquals('N', Rot13Fragment.rotateChar('A', 13))
        assertEquals('Z', Rot13Fragment.rotateChar('A', 25))
        assertEquals('A', Rot13Fragment.rotateChar('A', 26))
    }

    @Test
    fun testRotateLower() {
        assertEquals('a', Rot13Fragment.rotateChar('a', 0))
        assertEquals('b', Rot13Fragment.rotateChar('a', 1))
        assertEquals('c', Rot13Fragment.rotateChar('a', 2))
        assertEquals('n', Rot13Fragment.rotateChar('a', 13))
        assertEquals('z', Rot13Fragment.rotateChar('a', 25))
        assertEquals('a', Rot13Fragment.rotateChar('a', 26))
    }

    @Test
    fun testRotateNonLetters() {
        assertEquals('0', Rot13Fragment.rotateChar('0', 42))
        assertEquals('1', Rot13Fragment.rotateChar('1', 42))
        assertEquals(' ', Rot13Fragment.rotateChar(' ', 42))
        assertEquals('!', Rot13Fragment.rotateChar('!', 42))
        assertEquals('.', Rot13Fragment.rotateChar('.', 42))
    }

    @Test
    fun testRotateNonLatinLetters() {
        assertEquals('Ä', Rot13Fragment.rotateChar('Ä', 42))
        assertEquals('é', Rot13Fragment.rotateChar('é', 42))
    }

    @Test
    fun testRotateString() {
        assertEquals("Unyyb", Rot13Fragment.rotateString("Hallo", 13))
        assertEquals("Unyyb sbb", Rot13Fragment.rotateString("Hallo foo", 13))
    }
}