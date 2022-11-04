package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.ui.fragments.TimerFragment

class Prefs(private val context: Context) {
    var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        private set

    val maxDiceCount: Int
        get() = sharedPrefs.getString("dices_max_count", "10")?.toInt() ?: 10

    val startPage: String
        get() = sharedPrefs.getString("start_page", context.getString(R.string.menu_dices_value))
            ?: context.getString(R.string.menu_dices_value)

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

    var randomKeepScreenOn: Boolean
        get() = sharedPrefs.getBoolean("random_keep_screen_on", true)
        set(value) {
            sharedPrefs.edit {
                putBoolean("random_keep_screen_on", value)
            }
        }

    var rot13KeepScreenOn: Boolean
        get() = sharedPrefs.getBoolean("rot13_keep_screen_on", true)
        set(value) {
            sharedPrefs.edit {
                putBoolean("rot13_keep_screen_on", value)
            }
        }
}