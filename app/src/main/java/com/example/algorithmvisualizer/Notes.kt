package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.algorithmvisualizer.databinding.ActivityNotesBinding

class Notes : AppCompatActivity() {
    private lateinit var binding: ActivityNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)

        setContentView(binding.root)



        //fragment placing
        val notesFragment=NotesFragment()
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frame_layout,notesFragment)

            commit()
        }

    }
}