package com.github.muellerma.tabletoptools.ui.rot13

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.isLatinLetter
import com.google.android.material.slider.Slider

class Rot13Fragment : Fragment() {
    private lateinit var slider: Slider
    private lateinit var inputText: EditText
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rot13, container, false)
        slider = root.findViewById<Slider>(R.id.rot13_slider).apply {
            addOnChangeListener { _, _, _ -> updateResult() }
        }
        inputText = root.findViewById<EditText>(R.id.rot13_input_text).apply {
            addTextChangedListener {
                updateResult()
            }
        }
        result = root.findViewById(R.id.rot13_result_text)
        updateResult()
        return root
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
            val newChar = (c.toInt() - start + rotateBy) % 26
            return (newChar + start).toChar()
        }
    }
}