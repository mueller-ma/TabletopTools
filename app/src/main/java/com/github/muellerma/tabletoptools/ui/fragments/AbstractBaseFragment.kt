package com.github.muellerma.tabletoptools.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class AbstractBaseFragment : Fragment(), CoroutineScope {
    abstract var prefs: Prefs
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    var savedData: SavedData? = null
    abstract fun getViewForKeepScreenOn(): View
    fun Boolean.getMenuItemKeepScreenOnIcon(): Drawable? {
        val context = this@AbstractBaseFragment.context ?: return null
        return when (this) {
            false -> getDrawable(context, R.drawable.ic_flashlight_on_48px)
            else -> getDrawable(context, R.drawable.ic_flashlight_off_48px)
        }
    }
    fun FragmentActivity.addKeepScreenOnMenu() = addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_keep_screen_on, menu)
            menu.findItem(R.id.keep_screen_on).icon = prefs.keepScreenOn.getMenuItemKeepScreenOnIcon()
        }

        override fun onMenuItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.keep_screen_on -> {
                    val nowKeepScreenOn = getViewForKeepScreenOn().keepScreenOn.not()
                    prefs.keepScreenOn = nowKeepScreenOn
                    getViewForKeepScreenOn().keepScreenOn = nowKeepScreenOn
                    item.icon = nowKeepScreenOn.getMenuItemKeepScreenOnIcon()
                    true
                }
                else -> false
            }
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}

interface SavedData : Parcelable