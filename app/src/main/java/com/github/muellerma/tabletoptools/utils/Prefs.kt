package com.github.muellerma.tabletoptools.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.ui.fragments.TimerFragment

class Prefs(private val context: Context) {
    private var sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val maxDiceCount: Int
        get() = sharedPrefs.getString("dices_max_count", "10")?.toInt() ?: 10

    val startPage: String
        get() = sharedPrefs.getString("start_page", context.getString(R.string.menu_dice_value))
            ?: context.getString(R.string.menu_dice_value)

    val showDiceOverallIncSlider: Boolean
        get() = sharedPrefs.getBoolean("dices_show_inc_slider", false)

    val showDiceRollIncSlider: Boolean
        get() = sharedPrefs.getBoolean("dices_show_roll_inc_slider", false)

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

    var keepScreenOn: Boolean
        get() = sharedPrefs.getBoolean("keep_screen_on", true)
        set(value) {
            sharedPrefs.edit {
                putBoolean("keep_screen_on", value)
            }
        }

    val buzzerSound: Int?
        get() {
            return when (sharedPrefs.getString("buzzer_sound", null)) {
                "buzzer" -> R.raw.buzzer
                else -> null
            }
        }

    inner class Counter(private val id: Int) {
        var label: String?
            get() = sharedPrefs.getString("counter_${id}_label", null)
            set(value) {
                sharedPrefs.edit {
                    putString("counter_${id}_label", value)
                }
            }

        var count: Int
            get() = sharedPrefs.getInt("counter_${id}_count", 1)
            set(value) {
                sharedPrefs.edit {
                    putInt("counter_${id}_count", value)
                }
            }
    }
}