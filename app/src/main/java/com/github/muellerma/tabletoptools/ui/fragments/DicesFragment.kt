package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentDicesBinding
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
    ): View {
        val binding = FragmentDicesBinding.inflate(inflater, container, false)

        dicesCountSlider = binding.dicesCountSlider
        val sliderHint = binding.dicesSliderHint
        sliderHint.text = getString(R.string.dices_slider_hint, dicesCountSlider.value.toInt())
        dicesCountSlider.addOnChangeListener { _, value, _ ->
            sliderHint.text = getString(R.string.dices_slider_hint, value.toInt())
        }

        incSlider = binding.dicesIncSlider
        val incSliderHint = binding.dicesIncSliderHint
        incSliderHint.text = getString(R.string.dices_inc_slider_hint, incSlider.value.toInt())
        incSlider.addOnChangeListener { _, value, _ ->
            incSliderHint.text = getString(R.string.dices_inc_slider_hint, value.toInt())
        }

        result = binding.dicesResultText

        mapOf(
            binding.dicesButton3 to 3,
            binding.dicesButton4 to 4,
            binding.dicesButton6 to 6,
            binding.dicesButton8 to 8,
            binding.dicesButton10 to 10,
            binding.dicesButton12 to 12,
            binding.dicesButton20 to 20,
            binding.dicesButton100 to 100
        ).forEach { dice ->
            dice.key.setOnClickListener {
                roll(dice.value)
            }
        }

        binding.dicesButton102.apply {
            setOnClickListener {
                roll(10, 10)
            }
        }

        return binding.root
    }

    override fun onResume() {
        result.text = (savedData as DicesData?)?.results
        super.onResume()
        val maxDices = Prefs(requireContext()).maxDiceCount.toFloat()
        if (dicesCountSlider.value > maxDices) {
            dicesCountSlider.value = 1f
        }
        dicesCountSlider.valueTo = maxDices
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
