package com.github.muellerma.tabletoptools.utils

fun Char.isLatinLetter(): Boolean {
    return this in 'A'..'Z' || this in 'a'..'z'
}