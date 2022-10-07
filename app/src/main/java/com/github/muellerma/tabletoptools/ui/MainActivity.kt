package com.github.muellerma.tabletoptools.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.databinding.ActivityMainBinding
import com.github.muellerma.tabletoptools.ui.fragments.AbstractBaseFragment
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.parcelable

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBar.toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        navGraph.setStartDestination(getDefaultStartDestinationId())
        navController.graph = navGraph

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
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun getDefaultStartDestinationId(): Int {
        return when (Prefs(this).startPage) {
            getString(R.string.menu_dices_value) -> R.id.nav_dices
            getString(R.string.menu_random_list_value) -> R.id.nav_random_list
            getString(R.string.menu_rot13_value) -> R.id.nav_rot13
            getString(R.string.menu_alphabet_value) -> R.id.nav_alphabet
            getString(R.string.menu_number_converter_value) -> R.id.nav_number_converter
            getString(R.string.menu_timer_value) -> R.id.nav_timer
            getString(R.string.menu_buzzers_value) -> R.id.nav_buzzers
            else -> R.id.nav_dices
        }
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
                fragment.savedData = savedInstanceState.parcelable(fragment::class.java.simpleName)
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
}