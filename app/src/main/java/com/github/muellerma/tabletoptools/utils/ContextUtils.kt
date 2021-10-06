package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import com.github.muellerma.tabletoptools.utils.Constants.SHARED_PREFERENCES_KEY

fun Context.preferences(): SharedPreferences {
    return this.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
}

object Constants {
    const val SHARED_PREFERENCES_KEY = "tabletop_tools_shared_prefs"
}