package com.github.muellerma.tabletoptools.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.github.muellerma.tabletoptools.databinding.AlphabetTableRowBinding
import com.github.muellerma.tabletoptools.databinding.FragmentAlphabetBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.positionInAlphabet

class AlphabetFragment : AbstractBaseFragment() {
    override lateinit var prefs: Prefs
    private lateinit var binding: FragmentAlphabetBinding

    override fun getViewForKeepScreenOn(): ScrollView = binding.alphabetScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlphabetBinding.inflate(inflater, container, false)
        val alphabet = binding.alphabetScrollView

        ('A'..'Z').forEach { letter ->
            @SuppressLint("InflateParams") // View is set as child below
            val row = AlphabetTableRowBinding.inflate(inflater)
            val letterLabel = row.alphabetTableRowLetter
            val numberLabel = row.alphabetTableRowNumber
            letterLabel.text = letter.toString()
            numberLabel.text = letter.positionInAlphabet().toString()

            binding.alphabetTable.addView(row.root)
        }

        prefs = Prefs(alphabet.context)

        alphabet.keepScreenOn = prefs.keepScreenOn

        requireActivity().addKeepScreenOnMenu()

        return binding.root
    }

    companion object {
        private var TAG = AlphabetFragment::class.java.simpleName
    }
}
