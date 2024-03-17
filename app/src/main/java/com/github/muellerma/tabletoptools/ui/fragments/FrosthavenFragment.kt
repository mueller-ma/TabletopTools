package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentFrosthavenBinding
import kotlin.math.ceil


class FrosthavenFragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentFrosthavenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFrosthavenBinding.inflate(inflater, container, false)
        setupScreenOn(binding.root)

        binding.scenarioLevelInput.addTextChangedListener {
            binding.scenarioLevelOutput.text =
                getString(R.string.frosthaven_scenario_level_out, calculateScenarioLevel(it.toString()))
        }

        return binding.root
    }

    companion object {
        private fun calculateScenarioLevel(characterLevels: String): Int {
            val avg = "\\d+".toRegex().findAll(characterLevels).map { it.value.toInt() }.average()
            return ceil(avg / 2).toInt()
        }
    }
}