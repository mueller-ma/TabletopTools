package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.widget.addTextChangedListener
import com.github.muellerma.tabletoptools.databinding.FragmentNumberConverterBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import java.util.Locale

class NumberConverterFragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentNumberConverterBinding
    private var inputFromUser = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberConverterBinding.inflate(inflater, container, false)
        val numberConverter = binding.numberConverterScrollView
        val binInputText = binding.numberConverterInputBin
        val decInputText = binding.numberConverterInputDec
        val hexInputText = binding.numberConverterInputHex

        binInputText.addTextChangedListener { text ->
            if (!inputFromUser) {
                return@addTextChangedListener
            }
            val input = text?.toString()?.toIntOrNull(2) ?: return@addTextChangedListener
            Log.d(TAG, "Bin: $text")

            inputFromUser = false
            decInputText.setText(input.toString())
            hexInputText.setText(Integer.toHexString(input).uppercase(Locale.US))
            inputFromUser = true
        }

        decInputText.addTextChangedListener { text ->
            if (!inputFromUser) {
                return@addTextChangedListener
            }
            val input = text?.toString()?.toIntOrNull() ?: return@addTextChangedListener
            Log.d(TAG, "Dec: $text")

            inputFromUser = false
            binInputText.setText(Integer.toBinaryString(input))
            hexInputText.setText(Integer.toHexString(input).uppercase(Locale.US))
            inputFromUser = true
        }

        hexInputText.addTextChangedListener { text ->
            if (!inputFromUser) {
                return@addTextChangedListener
            }
            val input = text?.toString()?.toIntOrNull(16) ?: return@addTextChangedListener
            Log.d(TAG, "Hex: $text")

            inputFromUser = false
            decInputText.setText(input.toString())
            binInputText.setText(Integer.toBinaryString(input))
            inputFromUser = true
        }

        numberConverter.keepScreenOn = prefs.keepScreenOn

        addKeepScreenOnMenu(binding.numberConverterScrollView)

        return binding.root
    }

    companion object {
        private var TAG = NumberConverterFragment::class.java.simpleName
    }
}