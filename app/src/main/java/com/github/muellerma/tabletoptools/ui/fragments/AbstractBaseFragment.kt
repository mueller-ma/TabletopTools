package com.github.muellerma.tabletoptools.ui.fragments

import android.os.Parcelable
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope

abstract class AbstractBaseFragment : Fragment() {
    var savedData: SavedData? = null
}

interface SavedData : Parcelable