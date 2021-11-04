package com.github.muellerma.tabletoptools

import com.github.muellerma.tabletoptools.ui.fragments.RandomListFragment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RandomListTest {
    @Test
    fun testRandomizeList() {
        val list = RandomListFragment.randomizeList("foo\nbar\n\n    \n foobar \n")
            .split("\n")

        assertEquals(3, list.size)
        assertTrue("foo" in list)
        assertTrue("bar" in list)
        assertTrue("foobar" in list)
    }
}