package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class AbstractBaseFragment : Fragment(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    var savedData: SavedData? = null
}

interface SavedData : Parcelable