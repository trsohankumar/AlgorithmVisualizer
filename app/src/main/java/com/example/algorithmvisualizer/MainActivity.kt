package com.example.algorithmvisualizer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.algorithmvisualizer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        
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
        binding.modeImage.setOnClickListener(View.OnClickListener {
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