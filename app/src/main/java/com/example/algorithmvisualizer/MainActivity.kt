package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoActionBar)
        setContentView(R.layout.activity_main)
        AppCompatDelegate
            .setDefaultNightMode(
                AppCompatDelegate
                    .MODE_NIGHT_YES);
    }
}