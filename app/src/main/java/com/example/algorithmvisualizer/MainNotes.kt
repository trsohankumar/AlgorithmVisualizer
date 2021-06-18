package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.algorithmvisualizer.databinding.ActivityMainNotesBinding
import kotlinx.android.synthetic.main.activity_main_notes.*
//main notes class in which You have a notes home fragment implemented which has a navigation architecture to AddNoteFragment
class MainNotes : AppCompatActivity() {
    private lateinit var binding:ActivityMainNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityMainNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbMyNotes)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            //actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.tbMyNotes.setNavigationOnClickListener {
            onBackPressed()

        }



        val navController= Navigation.findNavController(this,R.id.fragment)




    }
//the navigation architecture implementation using navigation fragment
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this,R.id.fragment),
            null
        )
    }
}