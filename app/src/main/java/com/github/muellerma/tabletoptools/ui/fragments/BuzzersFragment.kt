package com.github.muellerma.tabletoptools.ui.fragments

import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.getSystemService
import com.github.muellerma.tabletoptools.R
import kotlinx.coroutines.*


class BuzzersFragment : AbstractBaseFragment() {
    private val buzzers = ArrayList<Button>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_buzzers, container, false)

        listOf(R.id.buzzer_a, R.id.buzzer_b).forEach { id ->
            root.findViewById<Button>(id)?.apply {
                buzzers.add(this)
                setOnClickListener { buttonPressed(this) }
            }
        }

        return root
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

    companion object {
        private var TAG = BuzzersFragment::class.java.simpleName
    }
}