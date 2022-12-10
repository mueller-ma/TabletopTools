package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentRot13Binding
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.isLatinLetter
import com.google.android.material.slider.Slider

class Rot13Fragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentRot13Binding
    private lateinit var slider: Slider
    private lateinit var inputText: EditText
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRot13Binding.inflate(inflater, container, false)
        val rot13 = binding.rot13ScrollView
        slider = binding.rot13Slider.apply {
            addOnChangeListener { _, _, _ -> updateResult() }
        }
        inputText = binding.rot13InputText.apply {
            addTextChangedListener {
                updateResult()
            }
        }
        result = binding.rot13ResultText
        updateResult()

        rot13.keepScreenOn = prefs.keepScreenOn

        addKeepScreenOnMenu(binding.rot13ScrollView)

        return binding.root
    }

    private fun updateResult() {
        val rotateBy = slider.value.toInt()
        var resultText = ""
        inputText.text.forEach { resultText += rotateChar(it, rotateBy) }
        inputText.hint = resources.getQuantityString(R.plurals.rot13_hint, rotateBy, rotateBy)
        result.text = resultText
    }

    companion object {
        @VisibleForTesting
        fun rotateChar(c: Char, rotateBy: Int): Char {
            if (!c.isLatinLetter()) {
                return c
            }

            val start = if (c.isUpperCase()) 65 else 97
            val newChar = (c.code - start + rotateBy) % 26
            return (newChar + start).toChar()
        }
    }
}