package com.github.muellerma.tabletoptools.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.ui.dialog.TimerPickerDialog
import com.github.muellerma.tabletoptools.utils.preferences
import java.util.concurrent.TimeUnit


class TimerFragment : AbstractBaseFragment() {
    private lateinit var timerView1: TextView
    private lateinit var timerView2: TextView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private var timer: CountDownTimer? = null
    var originalTime: Long = DEFAULT_TIMER
    private var remainingTime: Long = originalTime
    private var timerRunning = false
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = activity?.preferences() ?: return
        val previousTimeMillis = sharedPref.getLong(PREVIOUS_MILLIS, DEFAULT_TIMER)
        originalTime = previousTimeMillis
        remainingTime = originalTime
        childFragmentManager.setFragmentResultListener(
            TimerPickerDialog.RESULT_KEY,
            this
        ) { _, bundle ->
            val minute = bundle.getInt(TimerPickerDialog.MINUTE_KEY)
            val seconds = bundle.getInt(TimerPickerDialog.SECOND_KEY)
            changeTimerTime(getMillisFromMinutesAndSeconds(minute, seconds))
        }
    }

    private fun changeTimerTime(time: Long) {
        if (time <= 0L) {
            return
        }
        originalTime = time
        remainingTime = originalTime
        activity?.preferences()?.edit {
            putLong(PREVIOUS_MILLIS, time)
        }
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
    ): View? {
        Log.d(TAG, "onCreateView()")
        val root = inflater.inflate(R.layout.fragment_timer, container, false)
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

            remainingTime = originalTime
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
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_timer, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_time -> {
                TimerPickerDialog().apply {
                    arguments = TimerPickerDialog.createBundle(originalTime)
                }.show(childFragmentManager, TimerPickerDialog.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private var TAG = TimerFragment::class.java.simpleName
        private const val PREVIOUS_MILLIS = "PREVIOUS_MILLIS"
        private const val DEFAULT_TIMER = 5 * 60 * 1000L
    }
}