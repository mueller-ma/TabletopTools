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

        setupCounter(1, binding.counter1)
        setupCounter(2, binding.counter2)
        setupCounter(3, binding.counter3)
        setupCounter(4, binding.counter4)
        setupCounter(5, binding.counter5)
    }

    private fun setupCounter(id: Int, counter: CounterBinding) {
        fun getInput() = counter.count.text.toString().toIntOrNull() ?: 1

        counter.less.setOnClickListener {
            counter.count.setText(getInput().dec().toString())
        }
        counter.more.setOnClickListener {
            counter.count.setText(getInput().inc().toString())
        }

        val prefs = prefs.Counter(id)
        counter.label.setText(prefs.label)
        counter.label.doOnTextChanged { text, _, _, _ -> prefs.label = text?.toString() }

        counter.count.setText(prefs.count.toString())
        counter.count.doOnTextChanged { _, _, _, _ -> prefs.count = getInput() }
    }
}
