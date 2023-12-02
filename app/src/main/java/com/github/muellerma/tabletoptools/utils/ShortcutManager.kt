package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.ui.SecondInstance

fun Context.createShortcuts() {
    val intent = Intent(this, SecondInstance::class.java)
        .setAction(Intent.ACTION_VIEW)
        .setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    val info = ShortcutInfoCompat.Builder(this, "second_instance")
        .setIntent(intent)
        .setShortLabel(getString(R.string.second_instance))
        .setLongLabel(getString(R.string.second_instance))
        .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher))
        .setAlwaysBadged()
        .build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
        ShortcutManagerCompat.addDynamicShortcuts(this, listOf(info))
    }
}

