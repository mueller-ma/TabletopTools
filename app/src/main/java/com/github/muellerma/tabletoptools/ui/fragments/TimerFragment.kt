package com.github.muellerma.tabletoptools.ui.fragments

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentTimerBinding
import com.github.muellerma.tabletoptools.ui.dialog.TimerPickerDialog
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes


class TimerFragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentTimerBinding
    private lateinit var timerView1: TextView
    private lateinit var timerView2: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private var timer: CountDownTimer? = null
    var setTime: Long = DEFAULT_TIME
    private var remainingTime: Long = setTime
    private var timerRunning = false
    private var player: MediaPlayer? = null
    override val forceAlwaysScreenOn = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTime = prefs.timerMillis
        remainingTime = setTime
        resetTimer()
        childFragmentManager.setFragmentResultListener(
            TimerPickerDialog.RESULT_KEY,
            this
        ) { _, bundle ->
            val minute = bundle.getInt(TimerPickerDialog.MINUTE_KEY)
            val seconds = bundle.getInt(TimerPickerDialog.SECOND_KEY)
            changeTimerTime(getMillisFromMinutesAndSeconds(minute, seconds))
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_timer, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.edit_time -> {
                        openTimerPickerDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupScreenOn(binding.root)
    }

    private fun changeTimerTime(time: Long) {
        if (time <= 0L) {
            return
        }
        setTime = time
        remainingTime = setTime
        prefs.timerMillis = time
        updateTimerView()
    }

    private fun getMillisFromMinutesAndSeconds(minutes: Int, seconds: Int): Long {
        var total = TimeUnit.MINUTES.toMillis(minutes.toLong())
        total += TimeUnit.SECONDS.toMillis(seconds.toLong())
        return total
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        timerView1 = binding.timerView1
        timerView2 = binding.timerView2
        startButton = binding.startButton
        resetButton = binding.resetButton

        startButton.setOnClickListener {
            toggleTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        savedInstanceState?.let {
            remainingTime = it.getLong("time")
            if (it.getBoolean("running")) {
                toggleTimer()
            }
        }

        updateTimerView()

        return binding.root
    }

    private fun resetTimer() {
        player?.stop()
        if (timerRunning) {
            toggleTimer()
        }

        remainingTime = setTime
        updateTimerView()
        startButton.isEnabled = true
        resetButton.isInvisible = true
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).post {
            Log.d(TAG, "Start button is ${startButton.height} high and minHeight ${startButton.minHeight}")
            timerView1.isVisible = startButton.height > startButton.minHeight
        }
    }

    private fun toggleTimer() {
        if (timerRunning) {
            timer?.cancel()
            timerRunning = false
            startButton.text = getString(R.string.timer_start)
            resetButton.isInvisible = false
        } else {
            startTimer(remainingTime)
            timerRunning = true
            startButton.text = getString(R.string.timer_pause)
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
                startButton.isEnabled = false

                val playerAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                val audioManager = requireContext().getSystemService<AudioManager>()!!
                player = MediaPlayer.create(
                    requireContext(),
                    R.raw.beeps,
                    playerAttributes,
                    audioManager.generateAudioSessionId()
                )
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

    private fun openTimerPickerDialog() {
        resetTimer()
        TimerPickerDialog().apply {
            arguments = TimerPickerDialog.createBundle(setTime)
        }.show(childFragmentManager, TimerPickerDialog.TAG)
    }

    companion object {
        private var TAG = TimerFragment::class.java.simpleName
        val DEFAULT_TIME = 5.minutes.inWholeMilliseconds
    }
}