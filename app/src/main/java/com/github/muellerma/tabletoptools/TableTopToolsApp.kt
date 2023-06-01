package com.github.muellerma.tabletoptools

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors

class TableTopToolsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        PreferenceManager.setDefaultValues(this, R.xml.pref_main, false)
    }
}