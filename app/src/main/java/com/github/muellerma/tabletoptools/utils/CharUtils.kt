package com.github.muellerma.tabletoptools.utils

fun Char.isLatinLetter(): Boolean {
    return this in 'A'..'Z' || this in 'a'..'z'
}

fun Char.positionInAlphabet(): Int {
    if (this in 'A'..'Z') {
        return code - 64
    }
    if (this in 'a'..'z') {
        return code - 96
    }

    return -1
}