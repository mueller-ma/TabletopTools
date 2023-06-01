package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.widget.addTextChangedListener
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentRot13Binding
import com.github.muellerma.tabletoptools.utils.isLatinLetter
import com.github.muellerma.tabletoptools.utils.showToast
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
        binding.rot13ResolveButton.setOnClickListener {
            tryResolve()
        }

        setupScreenOn(binding.root)

        return binding.root
    }

    private fun updateResult() {
        val rotateBy = slider.value.toInt()
        inputText.hint = resources.getQuantityString(R.plurals.rot13_hint, rotateBy, rotateBy)
        result.text = rotateString(inputText.text.toString(), rotateBy)
    }

    private fun tryResolve() {
        val input = inputText.text.toString()
        val rotated = (0..25).associateWith { rotateBy ->
            rotateString(input, rotateBy)
        }
        val commonWords = resources.getStringArray(R.array.rot13_common_words).toSet()
        var highestIndex = 0
        var highestMatch = 0
        rotated.forEach {
            val splitText = it.value.split(" ").toList()
            val matches = splitText.intersect(commonWords).size
            if (matches > highestMatch) {
                highestMatch = matches
                highestIndex = it.key
            }
            Log.d(TAG, "Rotate by ${it.key} has $matches matches")
        }

        if (highestMatch > 0) {
            slider.value = highestIndex.toFloat()
            updateResult()
        } else {
            context?.showToast(R.string.rot13_could_not_rotate)
        }
    }

    companion object {
        private val TAG = Rot13Fragment::class.java.simpleName

        @VisibleForTesting
        fun rotateChar(c: Char, rotateBy: Int): Char {
            if (!c.isLatinLetter()) {
                return c
            }

            val start = if (c.isUpperCase()) 65 else 97
            val newChar = (c.code - start + rotateBy) % 26
            return (newChar + start).toChar()
        }

        @VisibleForTesting
        fun rotateString(s: String, rotateBy: Int): String {
            return s.map { rotateChar(it, rotateBy) }.joinToString("")
        }
    }
}