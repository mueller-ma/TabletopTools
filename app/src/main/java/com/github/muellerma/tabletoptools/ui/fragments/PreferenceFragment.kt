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
import androidx.preference.PreferenceFragmentCompat
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.positionInAlphabet
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import kotlinx.parcelize.Parcelize
import org.w3c.dom.Text

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_main)
    }

    companion object {
        private var TAG = PreferenceFragment::class.java.simpleName
    }
}
