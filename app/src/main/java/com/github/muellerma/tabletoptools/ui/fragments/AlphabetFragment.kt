package com.github.muellerma.tabletoptools.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.AlphabetTableRowBinding
import com.github.muellerma.tabletoptools.databinding.FragmentAlphabetBinding
import com.github.muellerma.tabletoptools.utils.positionInAlphabet
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import kotlinx.parcelize.Parcelize
import org.w3c.dom.Text

class AlphabetFragment : AbstractBaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAlphabetBinding.inflate(inflater, container, false)

        ('A'..'Z').forEach { letter ->
            @SuppressLint("InflateParams") // View is set as child below
            val row = AlphabetTableRowBinding.inflate(inflater)
            val letterLabel = row.alphabetTableRowLetter
            val numberLabel = row.alphabetTableRowNumber
            letterLabel.text = letter.toString()
            numberLabel.text = letter.positionInAlphabet().toString()

            binding.alphabetTable.addView(row.root)
        }

        return binding.root
    }

    companion object {
        private var TAG = AlphabetFragment::class.java.simpleName
    }
}
