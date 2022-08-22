package com.github.muellerma.tabletoptools.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.ui.fragments.AbstractBaseFragment
import com.github.muellerma.tabletoptools.utils.Prefs

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var prefs: Prefs;

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        prefs = Prefs(this)
        setTheme()
        prefs.sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dices,
                R.id.nav_random_list,
                R.id.nav_rot13,
                R.id.nav_alphabet,
                R.id.nav_number_converter,
                R.id.nav_timer,
                R.id.nav_buzzers,
                R.id.nav_prefs
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp()")
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState()")
        getFragments()?.forEach { fragment ->
            if (fragment is AbstractBaseFragment) {
                fragment.savedData = savedInstanceState.getParcelable(fragment::class.java.simpleName)
            }
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState()")
        getFragments()?.forEach { fragment ->
            if (fragment is AbstractBaseFragment) {
                outState.putParcelable(fragment::class.java.simpleName, fragment.savedData)
            }
        }

        super.onSaveInstanceState(outState)
    }

    private fun getFragments(): MutableList<Fragment>? {
        return supportFragmentManager
            .fragments
            .firstOrNull { it is NavHostFragment }
            ?.childFragmentManager
            ?.fragments
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var sharedPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
        if (key == "theme_color") {
            setTheme()
            recreate()
        }
    }

    private fun setTheme() {
        val theme = when(prefs.themeColor) {
            "Green" -> R.style.Theme_TabletopTools_Green
            "Red" -> R.style.Theme_TabletopTools_Red
            "Blue" -> R.style.Theme_TabletopTools_Blue
            "Pink" -> R.style.Theme_TabletopTools_Pink
            "Lime" -> R.style.Theme_TabletopTools_Lime
            "Orange" -> R.style.Theme_TabletopTools_Orange
            else -> R.style.Theme_TabletopTools
        }
        setTheme(theme)
    }
}