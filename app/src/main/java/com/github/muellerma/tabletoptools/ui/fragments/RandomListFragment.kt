package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import com.github.muellerma.tabletoptools.R

class RandomListFragment : AbstractBaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_random_list, container, false)
        val randomList = root.findViewById<EditText>(R.id.random_list)

        root.findViewById<Button>(R.id.random_select).apply {
            setOnClickListener {
                randomList.setText(randomizeList(randomList.text.toString()))
            }
        }

        return root
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
    }
}