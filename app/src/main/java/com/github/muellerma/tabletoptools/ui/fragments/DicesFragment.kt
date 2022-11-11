package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentDicesBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import kotlinx.parcelize.Parcelize

class DicesFragment : AbstractBaseFragment() {
    private lateinit var b: FragmentDicesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentDicesBinding.inflate(inflater, container, false)

        b.dicesCountHint.text = getString(R.string.dices_slider_hint, b.dicesCount.value.toInt())
        b.dicesCount.addOnChangeListener { _, value, _ ->
            b.dicesCountHint.text = getString(R.string.dices_slider_hint, value.toInt())
        }

        b.overallIncHint.text = getString(R.string.dices_inc_slider_hint, b.overallInc.value.toInt())
        b.overallInc.addOnChangeListener { _, value, _ ->
            b.overallIncHint.text = getString(R.string.dices_inc_slider_hint, value.toInt())
        }

        val prefs = Prefs(inflater.context)
        b.overallInc.isVisible = prefs.showDicesIncSlider
        b.dicesCountHint.isVisible = prefs.showDicesIncSlider


        mapOf(
            b.dicesButton3 to 3,
            b.dicesButton4 to 4,
            b.dicesButton6 to 6,
            b.dicesButton8 to 8,
            b.dicesButton10 to 10,
            b.dicesButton12 to 12,
            b.dicesButton20 to 20,
            b.dicesButton100 to 100
        ).forEach { dice ->
            dice.key.setOnClickListener {
                roll(dice.value)
            }
        }

        b.dicesButton102.apply {
            setOnClickListener {
                roll(10, 10)
            }
        }

        return b.root
    }

    override fun onResume() {
        b.result.text = (savedData as DicesData?)?.results
        super.onResume()
        val maxDices = Prefs(requireContext()).maxDiceCount.toFloat()
        if (b.dicesCount.value > maxDices) {
            b.dicesCount.value = maxDices
        }
        b.dicesCount.valueTo = maxDices
    }

    private fun roll(max: Int, multiplier: Int = 1) {
        val resultString = StringBuilder()
        val dicesCount = b.dicesCount.value.toInt()
        val overallInc = b.overallInc.value.toInt()

        // Start with e.g. "2D6"
        resultString.append("$dicesCount${getString(R.string.dices_d_d, max)}")
        var result = 0
        var showEquals = false
        if (overallInc != 0) {
            Log.d(TAG, "overallInc = $overallInc")

            resultString.append(if (overallInc > 0) " +" else " ")
            resultString.append(overallInc)
            result += overallInc

            showEquals = true
        }
        resultString.append(": ")

        val firstDice = (1..max).shuffled().first().times(multiplier)
        Log.d(TAG, "Rolled $firstDice")
        resultString.append(firstDice).append(" ")
        result += firstDice

        for (i in 2..dicesCount) {
            val rolledDice = (1..max).shuffled().first().times(multiplier)
            Log.d(TAG, "Rolled $rolledDice")
            resultString.append("+ $rolledDice ")
            result += rolledDice
            showEquals = true
        }

        if (showEquals) {
            resultString.append("= $result")
        }

        resultString.appendLine().append(b.result.text)
        resultString.toString().apply {
            savedData = DicesData(this)
            b.result.text = this
        }
    }

    companion object {
        private var TAG = DicesFragment::class.java.simpleName
    }

    @Parcelize
    data class DicesData(val results: String) : SavedData
}
