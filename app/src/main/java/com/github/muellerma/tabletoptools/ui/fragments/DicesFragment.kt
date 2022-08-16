package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.Prefs
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import kotlinx.parcelize.Parcelize

class DicesFragment : AbstractBaseFragment() {
    private lateinit var dicesCountSlider: Slider
    private lateinit var incSlider: Slider
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dices, container, false)

        dicesCountSlider = root.findViewById(R.id.dices_count_slider)
        val sliderHint = root.findViewById<TextView>(R.id.dices_slider_hint)
        sliderHint.text = getString(R.string.dices_slider_hint, dicesCountSlider.value.toInt())
        dicesCountSlider.addOnChangeListener { _, value, _ ->
            sliderHint.text = getString(R.string.dices_slider_hint, value.toInt())
        }

        incSlider = root.findViewById(R.id.dices_inc_slider)
        val incSliderHint = root.findViewById<TextView>(R.id.dices_inc_slider_hint)
        incSliderHint.text = getString(R.string.dices_inc_slider_hint, incSlider.value.toInt())
        incSlider.addOnChangeListener { _, value, _ ->
            incSliderHint.text = getString(R.string.dices_inc_slider_hint, value.toInt())
        }

        result = root.findViewById(R.id.dices_result_text)

        mapOf(
                R.id.dices_button_3 to 3,
                R.id.dices_button_4 to 4,
                R.id.dices_button_6 to 6,
                R.id.dices_button_8 to 8,
                R.id.dices_button_10 to 10,
                R.id.dices_button_12 to 12,
                R.id.dices_button_20 to 20,
                R.id.dices_button_100 to 100
        ).forEach { dice ->
            root.findViewById<MaterialButton>(dice.key).apply {
                setOnClickListener {
                    roll(dice.value)
                }
            }
        }

        root.findViewById<MaterialButton>(R.id.dices_button_10_2).apply {
            setOnClickListener {
                roll(10, 10)
            }
        }

        return root
    }

    override fun onResume() {
        result.text = (savedData as DicesData?)?.results
        super.onResume()
        dicesCountSlider.valueTo = Prefs(requireContext()).maxDiceCount.toFloat()
    }

    private fun roll(max: Int, multiplier: Int = 1) {
        val resultString = StringBuilder()
        val numberOfDices = dicesCountSlider.value.toInt()
        val diceIncrement = incSlider.value.toInt()

        resultString.append("$numberOfDices${getString(R.string.dices_d_d, max)}")
        if (diceIncrement > 0) {
            resultString.append("+$diceIncrement")
        }
        resultString.append(": ")

        val firstDice = (1..max).shuffled().first().times(multiplier)
        Log.d(TAG, "Rolled $firstDice")
        resultString.append(firstDice).append(" ")
        var sum = firstDice

        for (i in 2..numberOfDices) {
            val rolledDice = (1..max).shuffled().first().times(multiplier)
            Log.d(TAG, "Rolled $rolledDice")
            sum += rolledDice
            resultString.append("+ ").append(rolledDice).append(" ")
        }

        if (diceIncrement > 0) {
            sum += diceIncrement
            Log.d(TAG, "+ increment: $diceIncrement")
            resultString.append("(+ $diceIncrement) ")
        }

        if (numberOfDices > 1 || diceIncrement > 0) {
            resultString.append("= $sum")
        }

        resultString.appendLine().append(result.text)
        resultString.toString().apply {
            savedData = DicesData(this)
            result.text = this
        }
    }

    companion object {
        private var TAG = DicesFragment::class.java.simpleName
    }

    @Parcelize
    data class DicesData(val results: String) : SavedData
}
