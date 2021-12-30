package com.github.muellerma.tabletoptools.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.github.muellerma.tabletoptools.R

class TimerPickerDialog : DialogFragment(), NumberPicker.OnValueChangeListener {
    private var minutesAndSeconds: Pair<Int, Int> = Pair(5, 0)
    private lateinit var finishButton: Button
    private lateinit var minutesPicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_count_down_picker, container, false)
        arguments?.apply {
            minutesAndSeconds = convertMillisToMinutesAndSeconds(getLong(MILLIS_BUNDLE_KEY))
        }
        minutesPicker = v.findViewById<NumberPicker>(R.id.minute_number_picker).apply {
            minValue = MIN_TIME
            maxValue = MAX_TIME
            value = minutesAndSeconds.first

            setOnValueChangedListener(this@TimerPickerDialog)
        }
        secondsPicker = v.findViewById<NumberPicker>(R.id.second_number_picker).apply {
            minValue = MIN_TIME
            maxValue = MAX_TIME
            value = minutesAndSeconds.second

            setOnValueChangedListener(this@TimerPickerDialog)
        }
        finishButton = v.findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                setFragmentResult(
                    RESULT_KEY,
                    bundleOf(MINUTE_KEY to minutesPicker.value, SECOND_KEY to secondsPicker.value)
                )
                dismiss()
            }
        }
        return v
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        finishButton.isEnabled = minutesPicker.value != 0 || secondsPicker.value != 0
    }

    private fun convertMillisToMinutesAndSeconds(millis: Long): Pair<Int, Int> {
        val minutes = millis / 1000 / 60
        val seconds = millis / 1000 % 60
        return Pair(minutes.toInt(), seconds.toInt())
    }

    companion object {
        fun createBundle(millis: Long): Bundle {
            return bundleOf(
                MILLIS_BUNDLE_KEY to millis
            )
        }

        private const val MILLIS_BUNDLE_KEY = "MILLIS_BUNDLE_KEY"

        const val MIN_TIME = 0
        const val MAX_TIME = 59
        const val TAG = "CountDownPickerDialog"
        const val RESULT_KEY = "TIME_RESULT_KEY"
        const val MINUTE_KEY = "MINUTE_KEY"
        const val SECOND_KEY = "SECOND_KEY"
    }
}