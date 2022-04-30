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
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alphabet, container, false)

        val table = root.findViewById<TableLayout>(R.id.alphabet_table)

        ('A'..'Z').forEach { letter ->
            @SuppressLint("InflateParams") // View is set as child below
            val row = inflater.inflate(R.layout.alphabet_table_row, null)
            val letterLabel = row.findViewById<TextView>(R.id.alphabet_table_row_letter)
            val numberLabel = row.findViewById<TextView>(R.id.alphabet_table_row_number)
            letterLabel.text = letter.toString()
            numberLabel.text = letter.positionInAlphabet().toString()

            table.addView(row)
        }

        return root
    }

    companion object {
        private var TAG = AlphabetFragment::class.java.simpleName
    }
}
