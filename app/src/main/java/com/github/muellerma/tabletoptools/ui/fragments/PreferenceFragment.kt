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
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.openInBrowser
import com.github.muellerma.tabletoptools.utils.positionInAlphabet
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsConfiguration
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.SpecialButton
import kotlinx.parcelize.Parcelize
import org.w3c.dom.Text

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_main)

        getPreference("about").apply {
            setOnPreferenceClickListener {
                val fragment = LibsBuilder()
                    .withActivityTitle(getString(R.string.about))
                    .withAboutIconShown(true)
                    .withAboutVersionShownName(true)
                    .withSortEnabled(true)
                    .withListener(AboutButtonsListener())
                    .supportFragment()

                parentFragmentManager.commit {
                    addToBackStack("about")
                    setReorderingAllowed(true)
                    replace(R.id.nav_host_fragment, fragment, "about")
                }
                true
            }
        }
    }

    companion object {
        private var TAG = PreferenceFragment::class.java.simpleName
    }
}

fun PreferenceFragmentCompat.getPreference(key: String) =
    preferenceManager.findPreference<Preference>(key)!!


class AboutButtonsListener : LibsConfiguration.LibsListener {
    override fun onExtraClicked(v: View, specialButton: SpecialButton): Boolean {
        val link = when (specialButton) {
            SpecialButton.SPECIAL1 -> "https://github.com/mueller-ma/TabletopTools/"
            SpecialButton.SPECIAL2 -> "https://f-droid.org/packages/com.github.muellerma.tabletoptools/"
            SpecialButton.SPECIAL3 -> "https://crowdin.com/project/tabletop-tools"
        }
        link.openInBrowser(v.context)
        return true
    }

    override fun onIconClicked(v: View) {
        // no-op
    }

    override fun onIconLongClicked(v: View): Boolean {
        return false
    }

    override fun onLibraryAuthorClicked(v: View, library: Library): Boolean {
        return false
    }

    override fun onLibraryAuthorLongClicked(v: View, library: Library): Boolean {
        return false
    }

    override fun onLibraryBottomClicked(v: View, library: Library): Boolean {
        return false
    }

    override fun onLibraryBottomLongClicked(v: View, library: Library): Boolean {
        return false
    }

    override fun onLibraryContentClicked(v: View, library: Library): Boolean {
        return false
    }

    override fun onLibraryContentLongClicked(v: View, library: Library): Boolean {
        return false
    }
}