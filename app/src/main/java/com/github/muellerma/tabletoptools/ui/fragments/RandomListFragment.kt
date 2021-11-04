package com.github.muellerma.tabletoptools.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.preferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RandomListFragment : AbstractBaseFragment() {
    private lateinit var randomList: EditText
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_random_list, container, false)
        randomList = root.findViewById(R.id.random_list)

        root.findViewById<Button>(R.id.random_select).apply {
            setOnClickListener {
                randomList.setText(
                    randomizeList(randomList.text.toString())
                )
            }
        }

        prefs = randomList.context.preferences()

        randomList.setText(prefs.getString("random_list_last_list", ""))

        randomList.addTextChangedListener {
            prefs.edit {
                putString("random_list_last_list", it.toString())
            }
        }

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_random_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @VisibleForTesting
        fun randomizeList(list: String): String {
            return list
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .shuffled()
                .joinToString("\n")
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