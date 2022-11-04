package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentRot13Binding
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.isLatinLetter
import com.google.android.material.slider.Slider

class Rot13Fragment : AbstractBaseFragment() {
    private lateinit var prefs: Prefs
    private lateinit var slider: Slider
    private lateinit var inputText: EditText
    private lateinit var result: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRot13Binding.inflate(inflater, container, false)
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

        prefs = Prefs(rot13.context)

        rot13.keepScreenOn = prefs.rot13KeepScreenOn

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_rot13, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.keep_screen_on -> {
                        val nowKeepScreenOn = rot13.keepScreenOn.not()
                        prefs.rot13KeepScreenOn = nowKeepScreenOn
                        rot13.keepScreenOn = nowKeepScreenOn
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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