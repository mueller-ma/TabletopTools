package com.github.muellerma.tabletoptools.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomDie(
    @PrimaryKey val id: Int,
    val label: String,
    val from: Int = 1,
    val to: Int,
    val dieCount: Int = 1,
    val addToResult: Int = 0,
    val addToEach: Int = 0
)
