package com.github.muellerma.tabletoptools.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.AlphabetTableRowBinding
import com.github.muellerma.tabletoptools.databinding.FragmentAlphabetBinding
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.positionInAlphabet

class AlphabetFragment : AbstractBaseFragment() {
    private lateinit var prefs: Prefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAlphabetBinding.inflate(inflater, container, false)
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

        alphabet.keepScreenOn = prefs.alphabetKeepScreenOn

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_alphabet, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.keep_screen_on -> {
                        val nowKeepScreenOn = alphabet.keepScreenOn.not()
                        prefs.alphabetKeepScreenOn = nowKeepScreenOn
                        alphabet.keepScreenOn = nowKeepScreenOn
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    companion object {
        private var TAG = AlphabetFragment::class.java.simpleName
    }
}
