package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentNumberConverterBinding
import java.util.*

class NumberConverterFragment : AbstractBaseFragment() {
    private var inputFromUser = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNumberConverterBinding.inflate(inflater, container, false)
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
        return binding.root
    }

    companion object {
        private var TAG = NumberConverterFragment::class.java.simpleName
    }
}