package com.github.muellerma.tabletoptools.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.DiceBinding
import com.github.muellerma.tabletoptools.databinding.FragmentDicesBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.toStringWithSign
import com.google.android.material.slider.Slider
import kotlinx.parcelize.Parcelize
import kotlin.math.min


class DicesFragment : AbstractBaseFragment() {
    private lateinit var b: FragmentDicesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentDicesBinding.inflate(inflater, container, false)

        setupSliderHints()

        val dices = intArrayOf(3, 4, 6, 8, 10, 12, 20, 100)

        val displayMetrics = inflater.context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        var spanCount = (screenWidthDp / 106 + 0.5).toInt()
        spanCount = min(spanCount, dices.size)

        b.dicesGrid.layoutManager = GridLayoutManager(inflater.context, spanCount)
        b.dicesGrid.adapter = DiceViewAdapter(dices)

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
        setVisibilityBasedOnPrefs(requireContext())
    }

    private fun setupSliderHints() {
        b.dicesCountHint.text = getString(R.string.dices_slider_hint, b.dicesCount.value.toInt())
        b.dicesCount.addOnChangeListener { _, value, _ ->
            b.dicesCountHint.text = getString(R.string.dices_slider_hint, value.toInt())
        }

        b.overallIncHint.text = getString(R.string.dices_overall_inc_slider_hint, b.overallInc.value.toInt())
        b.overallInc.addOnChangeListener { _, value, _ ->
            b.overallIncHint.text = getString(R.string.dices_overall_inc_slider_hint, value.toInt())
        }

        b.rollIncHint.text = getString(R.string.dices_roll_inc_slider_hint, b.rollInc.value.toInt())
        b.rollInc.addOnChangeListener { _, value, _ ->
            b.rollIncHint.text = getString(R.string.dices_roll_inc_slider_hint, value.toInt())
        }
    }

    private fun setVisibilityBasedOnPrefs(context: Context) {
        Log.d(TAG, "setVisibilityBasedOnPrefs()")
        fun setVisibility(slider: Slider, hint: TextView, show: Boolean) {
            slider.isVisible = show
            hint.isVisible = show
            if (!show) {
                slider.value = 0f
            }
        }

        val prefs = Prefs(context)
        setVisibility(b.overallInc, b.overallIncHint, prefs.showDicesOverallIncSlider)
        setVisibility(b.rollInc, b.rollIncHint, prefs.showDicesRollIncSlider)
    }

    private fun roll(max: Int, multiplier: Int = 1) {
        Log.d(TAG, "roll($max, $multiplier)")
        val resultString = StringBuilder()
        val dicesCount = b.dicesCount.value.toInt()
        val overallInc = b.overallInc.value.toInt()
        val rollInc = b.rollInc.value.toInt()

        // Start with e.g. "2D6"
        resultString.append("$dicesCount${getString(R.string.dices_d_d, max)}")
        var result = 0
        if (overallInc != 0) {
            Log.d(TAG, "overallInc = $overallInc")

            resultString.append(" ${overallInc.toStringWithSign()}")
            result += overallInc
        }
        resultString.append(": ")

        for (i in 1..dicesCount) {
            val rolledDice = (1..max).shuffled().first().times(multiplier)
            Log.d(TAG, "Rolled $rolledDice")

            if (i > 1) {
                resultString.append("+ ")
            }

            result += if (rollInc == 0) {
                resultString.append("$rolledDice ")
                rolledDice
            } else {
                val incrementedResult = rolledDice + rollInc
                resultString.append("$incrementedResult (=$rolledDice${rollInc.toStringWithSign()}) ")
                incrementedResult
            }
        }

        if (dicesCount > 1 || overallInc != 0) {
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

    inner class DiceViewAdapter(val dices: IntArray) : RecyclerView.Adapter<DiceViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceViewHolder {
            return DiceViewHolder(DiceBinding.inflate(layoutInflater))
        }

        override fun onBindViewHolder(holder: DiceViewHolder, position: Int) {
            holder.onBind(dices[position])
        }

        override fun getItemCount() = dices.size
    }

    inner class DiceViewHolder(val b: DiceBinding) : RecyclerView.ViewHolder(b.root), View.OnClickListener {
        private var max = 0

        fun onBind(max: Int) {
            this.max = max
            b.dicesButton.text = b.root.context.getString(R.string.dices_d_d, max)
            b.dicesButton.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            roll(max)
        }
    }
}
