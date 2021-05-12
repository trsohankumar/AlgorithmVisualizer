package com.example.algorithmvisualizer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoActionBar)
        setContentView(R.layout.activity_main)
        
        val appSettingPrefs:SharedPreferences=getSharedPreferences("AppSettingPrefs",0)
        val sharedPrefsEdit:SharedPreferences.Editor=appSettingPrefs.edit()
        val isNightModeOn:Boolean=appSettingPrefs.getBoolean("NightMode",false)


        if(isNightModeOn)
        {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate
                .setDefaultNightMode(
                    AppCompatDelegate
                        .MODE_NIGHT_NO);
        }
        modeImage.setOnClickListener(View.OnClickListener {
            if(isNightModeOn)
            {
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_NO);
                sharedPrefsEdit.putBoolean("NightMode",false)
                sharedPrefsEdit.apply()

            }
            else
            {
                AppCompatDelegate
                    .setDefaultNightMode(
                        AppCompatDelegate
                            .MODE_NIGHT_YES);
                sharedPrefsEdit.putBoolean("NightMode",true)
                sharedPrefsEdit.apply()

            }
        })


    }
}