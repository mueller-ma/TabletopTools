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

    @Test
    fun testRandomizeListOneElement() {
        val list = RandomListFragment.randomizeList("foo")
            .split("\n")

        assertEquals(1, list.size)
        assertTrue("foo" in list)
    }

    @Test
    fun testSortList() {
        val list = RandomListFragment.sortList("foo\nbar\n\n    \n foobar \n")
            .split("\n")

        assertEquals(3, list.size)
        assertTrue(list[0] == "bar")
        assertTrue(list[1] == "foo")
        assertTrue(list[2] == "foobar")
    }
}