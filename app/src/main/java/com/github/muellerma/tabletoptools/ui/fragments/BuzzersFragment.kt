package com.github.muellerma.tabletoptools.ui.fragments

import android.content.DialogInterface
import android.os.*
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.content.edit
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*


class BuzzersFragment : AbstractBaseFragment() {
    private val buzzers = ArrayList<Button>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_buzzers, container, false)

        listOf(R.id.buzzer_a, R.id.buzzer_b, R.id.buzzer_c, R.id.buzzer_d).forEach { id ->
            root.findViewById<Button>(id)?.apply {
                buzzers.add(this)
                setOnClickListener { buttonPressed(this) }
            }
        }
        setupButtonVisibility()

        setHasOptionsMenu(true)

        return root
    }

    private fun setupButtonVisibility() {
        val visible = context?.preferences()?.getInt("buzzer_count", 2) ?: 2
        buzzers.forEachIndexed {index, button ->
            button.isVisible = index <= visible - 1
        }
    }

    private fun buttonPressed(pressedButton: Button) {
        Log.d(TAG, "Pressed button ${pressedButton.text}")

        val vibrator = requireContext().getSystemService<Vibrator>()!!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }

        buzzers.forEach {
            if (it != pressedButton) {
                it.isEnabled = false
            }
        }

        launch {
            delay(5 * 1000)
            Handler(Looper.getMainLooper()).post {
                buzzers.forEach {
                    if (it != pressedButton) {
                        it.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_buzzers, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                val prefs = requireContext().preferences()
                val picker = NumberPicker(requireContext()).apply {
                    minValue = 1
                    maxValue = 4
                    value = prefs.getInt("buzzer_count", 2)
                    displayedValues = (minValue..maxValue).map {
                        resources.getQuantityString(R.plurals.buzzers_amount, it, it)
                    }.toTypedArray()
                }

                MaterialAlertDialogBuilder(requireContext())
                    .setView(picker)
                    .setTitle(R.string.settings)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        prefs.edit {
                            putInt("buzzer_count", picker.value)
                        }
                        setupButtonVisibility()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private var TAG = BuzzersFragment::class.java.simpleName
    }
}