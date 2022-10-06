package com.github.muellerma.tabletoptools

import android.app.Application
import com.google.android.material.color.DynamicColors

class TableTopToolsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}