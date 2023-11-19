package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import com.github.muellerma.tabletoptools.databinding.FragmentPrimeFacBinding
import kotlin.math.sqrt


class PrimeFacFragment : AbstractBaseFragment(), TextWatcher {
    private lateinit var binding: FragmentPrimeFacBinding
    private lateinit var input: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")
        binding = FragmentPrimeFacBinding.inflate(inflater, container, false)

        input = binding.input

        input.addTextChangedListener(this)

        val savedInput = savedInstanceState?.getCharSequence("input")?.toString().orEmpty()
        input.setText(savedInput)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence("input", input.text)
        super.onSaveInstanceState(outState)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // no-op
    }

    override fun afterTextChanged(s: Editable?) {
        val input = input.text.toString().toIntOrNull()
        val output = if (input == null) {
            ""
        } else {
            factor(input).joinToString(", ")
        }
        binding.result.text = output
    }

    companion object {
        private var TAG = PrimeFacFragment::class.java.simpleName

        @VisibleForTesting
        fun factor(num: Int): ArrayList<Int> {
            val array = ArrayList<Int>()
            var x = num
            var i = 2
            while (i <= sqrt(x.toDouble())) {
                while (x % i == 0) {
                    array.add(i)
                    x /= i
                }
                i++
            }
            if (x != 1 || num == 1) {
                array.add(x)
            }
            return array
        }
    }
}