package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.algorithmvisualizer.databinding.ActivityInfoBinding
import com.example.algorithmvisualizer.databinding.ActivityNotesBinding

class Notes : AppCompatActivity() {
    private lateinit var binding: ActivityNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.tbNotes)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            //actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.tbNotes.setNavigationOnClickListener {
            onBackPressed()
        }



    }
}