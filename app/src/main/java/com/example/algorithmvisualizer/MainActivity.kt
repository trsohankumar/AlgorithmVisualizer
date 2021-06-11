package com.example.algorithmvisualizer

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.algorithmvisualizer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        binding.cvAlgoInformation.setOnClickListener{
            Intent(this,InformationMainActivity::class.java).also{
                startActivity(it)
            }
        }
        binding.cvSortingVisualizer.setOnClickListener {
            val intent = Intent(this,SortingVisualizer::class.java)
            startActivity(intent)
        }
        binding.cvNotesSection.setOnClickListener{
            val intent=Intent(this,Notes::class.java)
            startActivity(intent)
        }
    }
}
