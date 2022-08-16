package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Prefs(context: Context) {
    var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        private set

    val maxDiceCount: Int
        get() = sharedPrefs.getString("dices_max_count", "10")?.toInt() ?: 10
}