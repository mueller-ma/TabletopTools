package com.github.muellerma.tabletoptools.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.muellerma.tabletoptools.R
import com.github.muellerma.tabletoptools.utils.showToast

class SecondInstance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showToast(R.string.second_instance_hint)
        startActivity(Intent(this, MainActivity::class.java))
    }
}