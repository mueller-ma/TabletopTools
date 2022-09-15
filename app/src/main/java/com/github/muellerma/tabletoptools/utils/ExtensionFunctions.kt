package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import com.github.muellerma.tabletoptools.utils.Constants.SHARED_PREFERENCES_KEY

fun Context.preferences(): SharedPreferences {
    return this.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
}

inline fun <reified T> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

object Constants {
    const val SHARED_PREFERENCES_KEY = "tabletop_tools_shared_prefs"
}