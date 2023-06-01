package com.github.muellerma.tabletoptools.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.Prefs
import com.github.muellerma.tabletoptools.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class AbstractBaseFragment : Fragment(), CoroutineScope {
    protected lateinit var prefs: Prefs
        private set
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    var savedData: SavedData? = null
    protected open val forceAlwaysScreenOn = false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Prefs(this.requireContext())
    }

    protected fun setupScreenOn(view: View) {
        if (forceAlwaysScreenOn) {
            Log.d(TAG, "setupScreenOn(): forceAlwaysScreenOn is true")
            view.keepScreenOn = true
            return
        }

        fun getMenuItemConfig(isTurnedOn: Boolean): Pair<Drawable?, String> {
            return if (isTurnedOn) {
                Pair(
                    getDrawable(this.requireContext(), R.drawable.ic_brightness_high),
                    getString(R.string.keep_screen_on_enabled)
                )
            } else {
                Pair(
                    getDrawable(this.requireContext(), R.drawable.ic_brightness_low),
                    getString(R.string.keep_screen_on_disabled)
                )
            }
        }

        fun updateViewAndIconState(keepOn: Boolean, item: MenuItem) {
            view.keepScreenOn = keepOn
            val menuItemConfig = getMenuItemConfig(keepOn)
            item.icon = menuItemConfig.first
            item.title = menuItemConfig.second
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                Log.d(TAG, "onCreateMenu()")
                menuInflater.inflate(R.menu.menu_keep_screen_on, menu)
                updateViewAndIconState(prefs.keepScreenOn, menu.findItem(R.id.keep_screen_on))
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                Log.d(TAG, "onMenuItemSelected(${item.itemId})")
                return when (item.itemId) {
                    R.id.keep_screen_on -> {
                        val nowKeepScreenOn = prefs.keepScreenOn.not()
                        Log.d(TAG, "Keep screen on changed to $nowKeepScreenOn")
                        prefs.keepScreenOn = nowKeepScreenOn
                        updateViewAndIconState(nowKeepScreenOn, item)
                        context?.showToast(item.title.toString())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    companion object {
        private val TAG = AbstractBaseFragment::class.java.simpleName
    }
}

interface SavedData : Parcelable