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
import java.util.*

class NumberConverterFragment : AbstractBaseFragment() {
    private lateinit var binInputText: EditText
    private lateinit var decInputText: EditText
    private lateinit var hexInputText: EditText
    private var inputFromUser = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_number_converter, container, false)
        binInputText = root.findViewById(R.id.number_converter_input_bin)
        decInputText = root.findViewById(R.id.number_converter_input_dec)
        hexInputText = root.findViewById(R.id.number_converter_input_hex)

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
        return root
    }

    companion object {
        private var TAG = NumberConverterFragment::class.java.simpleName
    }
}