package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.FragmentRandomListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RandomListFragment : AbstractBaseFragment() {
    private lateinit var binding: FragmentRandomListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomListBinding.inflate(inflater, container, false)
        val randomList = binding.randomList

        binding.randomSelect.apply {
            setOnClickListener {
                randomList.setText(
                    randomizeList(randomList.text.toString())
                )
            }
        }

        randomList.setText(prefs.lastRandomList)

        randomList.addTextChangedListener {
            prefs.lastRandomList = it.toString()
        }

        binding.randomScrollView.keepScreenOn = prefs.keepScreenOn

        addKeepScreenOnMenu(binding.randomScrollView)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_random_list, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.clear_list -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setMessage(R.string.random_list_clear_list_confirm)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                randomList.setText("")
                            }
                            .setNegativeButton(android.R.string.cancel, null)
                            .show()
                        true
                    }
                    R.id.sort_list -> {
                        randomList.setText(
                            sortList(randomList.text.toString())
                        )
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    companion object {
        @VisibleForTesting
        fun randomizeList(list: String): String {
            val orig = list
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val shuffled = orig.shuffled()

            if (shuffled == orig && orig.size > 1) {
                return randomizeList(list)
            }

            return shuffled.joinToString("\n")
        }

        @VisibleForTesting
        fun sortList(list: String): String {
            return list
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .sorted()
                .joinToString("\n")
        }
    }
}