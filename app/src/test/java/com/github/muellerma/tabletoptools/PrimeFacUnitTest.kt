package com.github.muellerma.tabletoptools

import com.github.muellerma.tabletoptools.ui.fragments.PrimeFacFragment
import org.junit.Assert.assertEquals
import org.junit.Test

class PrimeFacUnitTest {
    @Test
    fun testFactor() {
        assertEquals(arrayListOf(1), PrimeFacFragment.factor(1))
        assertEquals(arrayListOf(2, 2), PrimeFacFragment.factor(4))
        assertEquals(arrayListOf(2, 5), PrimeFacFragment.factor(10))
        assertEquals(arrayListOf(2, 3, 5), PrimeFacFragment.factor(30))
    }
}