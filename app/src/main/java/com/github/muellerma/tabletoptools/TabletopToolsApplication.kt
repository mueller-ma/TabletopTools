package com.github.muellerma.tabletoptools

import android.app.Application
import com.google.android.material.color.DynamicColors

class TabletopToolsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}