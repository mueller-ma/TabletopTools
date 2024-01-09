package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.github.muellerma.tabletoptools.databinding.CounterBinding
import com.github.muellerma.tabletoptools.databinding.FragmentCounterBinding


class CounterFragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentCounterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterBinding.inflate(inflater, container, false)

        setupScreenOn(binding.root)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        listOf(
            binding.counter1,
            binding.counter2,
            binding.counter3,
            binding.counter4,
            binding.counter5
        ).forEachIndexed { index, counterBinding ->
            setupCounter(index + 1, counterBinding)
        }
    }

    private fun setupCounter(id: Int, counter: CounterBinding) {
        fun getInput() = counter.count.text.toString().toIntOrNull() ?: 0

        counter.less1.setOnClickListener {
            counter.count.setText(getInput().dec().toString())
        }
        counter.less10.setOnClickListener {
            counter.count.setText(getInput().minus(10).toString())
        }
        counter.more1.setOnClickListener {
            counter.count.setText(getInput().inc().toString())
        }
        counter.more10.setOnClickListener {
            counter.count.setText(getInput().plus(10).toString())
        }

        val prefs = prefs.Counter(id)
        counter.label.setText(prefs.label)
        counter.label.doOnTextChanged { text, _, _, _ -> prefs.label = text?.toString() }

        counter.count.setText(prefs.count.toString())
        counter.count.doOnTextChanged { _, _, _, _ -> prefs.count = getInput() }
    }
}
