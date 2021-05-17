package com.github.muellerma.tabletoptools.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.github.muellerma.tabletoptools.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BuzzersFragment : AbstractBaseFragment() {
    private lateinit var buzzerA: Button
    private lateinit var buzzerB: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_buzzers, container, false)

        buzzerA = root.findViewById(R.id.buzzer_a)
        buzzerB = root.findViewById(R.id.buzzer_b)

        buzzerA.setOnClickListener { temporarilyDisableButton(buzzerB) }
        buzzerB.setOnClickListener { temporarilyDisableButton(buzzerA) }

        return root
    }

    private fun temporarilyDisableButton(button: Button) {
        Log.d(TAG, "Disable button ${button.text}")
        button.isEnabled = false
        GlobalScope.launch {
            delay(5 * 1000)
            Handler(Looper.getMainLooper()).post {
                button.isEnabled = true
            }
        }
    }

    companion object {
        private var TAG = BuzzersFragment::class.java.simpleName
    }
}