package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Prefs(context: Context) {
    var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        private set

    val maxDiceCount: Int
        get() = sharedPrefs.getString("dices_max_count", "10")?.toInt() ?: 10

    val showDicesIncSlider: Boolean
        get() = sharedPrefs.getBoolean("dices_show_inc_slider", false)
}