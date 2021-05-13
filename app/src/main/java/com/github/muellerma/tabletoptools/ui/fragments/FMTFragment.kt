package com.github.muellerma.tabletoptools.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.muellerma.tabletoptools.R


class FMTFragment : AbstractBaseFragment() {
    private lateinit var timerView1: TextView
    private lateinit var timerView2: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private var timer: CountDownTimer? = null
    private var remainingTime: Long = DEFAULT_TIMER
    private var timerRunning = false
    private var player: MediaPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        val root = inflater.inflate(R.layout.fragment_fmt, container, false)
        timerView1 = root.findViewById(R.id.timer_view1)
        timerView2 = root.findViewById(R.id.timer_view2)
        startButton = root.findViewById(R.id.start_button)
        resetButton = root.findViewById(R.id.reset_button)

        startButton.setOnClickListener {
            toggleTimer()
        }

        resetButton.setOnClickListener {
            player?.stop()
            if (timerRunning) {
                toggleTimer()
            }

            remainingTime = DEFAULT_TIMER
            updateTimerView()
            startButton.isEnabled = true
        }

        savedInstanceState?.let {
            remainingTime = it.getLong("time")
            if (it.getBoolean("running")) {
                toggleTimer()
            }
        }

        updateTimerView()

        return root
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).post {
            Log.d(TAG, "Start button is ${startButton.height} high")
            timerView1.isVisible = startButton.height > 20
        }
    }

    private fun toggleTimer() {
        if (timerRunning) {
            timer?.cancel()
            timerRunning = false
            startButton.text = getString(R.string.fmt_timer_start)
            resetButton.isInvisible = false
        } else {
            startTimer(remainingTime)
            timerRunning = true
            startButton.text = getString(R.string.fmt_timer_pause)
            resetButton.isInvisible = true
        }
    }

    private fun startTimer(millis: Long) {
        Log.d(TAG, "startTimer()")
        timer?.cancel()
        timer = object : CountDownTimer(millis, 500) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateTimerView()
            }

            override fun onFinish() {
                Log.d(TAG, "Timer finished")
                remainingTime = 0
                updateTimerView()
                toggleTimer()
                player = MediaPlayer.create(
                    requireContext(),
                    Settings.System.DEFAULT_RINGTONE_URI
                )
                startButton.isEnabled = false
                player?.start()
            }
        }.start()
    }

    private fun updateTimerView() {
        val text = timeToString()
        timerView1.text = text
        timerView2.text = text
    }

    private fun timeToString(): String {
        val remainingInSeconds = remainingTime / 1000
        val minutes = remainingInSeconds / 60
        val seconds = remainingInSeconds % 60

        return "%d:%02d".format(minutes, seconds)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        timer?.cancel()
        player?.stop()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("time", remainingTime)
        outState.putBoolean("running", timerRunning)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private var TAG = FMTFragment::class.java.simpleName
        private const val DEFAULT_TIMER = 5 * 60 * 1000L
    }
}