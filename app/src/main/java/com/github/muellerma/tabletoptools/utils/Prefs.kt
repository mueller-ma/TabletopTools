package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.github.muellerma.tabletoptools.ui.fragments.TimerFragment

class Prefs(context: Context) {
    var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        private set

    val maxDiceCount: Int
        get() = sharedPrefs.getString("dices_max_count", "10")?.toInt() ?: 10

    val showDicesIncSlider: Boolean
        get() = sharedPrefs.getBoolean("dices_show_inc_slider", false)

    var buzzerCount: Int
        get() = sharedPrefs.getInt("buzzer_count", 2)
        set(value) {
            sharedPrefs.edit {
                putInt("buzzer_count", value)
            }
        }

    var lastRandomList: String
        get() = sharedPrefs.getString("random_list_last_list", "") ?: ""
        set(value) {
            sharedPrefs.edit {
                putString("random_list_last_list", value)
            }
        }

    var timerMillis: Long
        get() = sharedPrefs.getLong("timer_millis", TimerFragment.DEFAULT_TIME)
        set(value) {
            sharedPrefs.edit {
                putLong("timer_millis", value)
            }
        }
}