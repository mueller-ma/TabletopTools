package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentNumberConverterBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import java.util.Locale

class NumberConverterFragment : AbstractBaseFragment() {
    private lateinit var prefs: Prefs
    private var inputFromUser = true

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNumberConverterBinding.inflate(inflater, container, false)
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

        prefs = Prefs(numberConverter.context)

        numberConverter.keepScreenOn = prefs.numberConverterKeepScreenOn

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_number_converter, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.keep_screen_on -> {
                        val nowKeepScreenOn = numberConverter.keepScreenOn.not()
                        prefs.numberConverterKeepScreenOn = nowKeepScreenOn
                        numberConverter.keepScreenOn = nowKeepScreenOn
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    companion object {
        private var TAG = NumberConverterFragment::class.java.simpleName
    }
}