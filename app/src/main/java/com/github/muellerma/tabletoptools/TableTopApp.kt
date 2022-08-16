package com.github.muellerma.tabletoptools

import android.app.Application
import androidx.preference.PreferenceManager

class TableTopApp : Application() {
    override fun onCreate() {
        super.onCreate()

        PreferenceManager.setDefaultValues(this, R.xml.pref_main, false)
    }
}