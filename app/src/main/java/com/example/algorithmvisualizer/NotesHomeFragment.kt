package com.example.algorithmvisualizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.algorithmvisualizer.databinding.FragmentNotesHomeBinding
import com.example.algorithmvisualizer.db.NoteDatabase

import kotlinx.android.synthetic.main.fragment_notes_home.*
import kotlinx.coroutines.launch

//inheriting the base fragment(our custom fragment) which is in turn inheriting from fragment class
class NotesHomeFragment : BaseFragment() {


    private lateinit var binding:FragmentNotesHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesHomeBinding.inflate(layoutInflater,container,false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //displaying the notes
        binding.recyclerViewNotes.setHasFixedSize(true)
        //grid of 3
        binding.recyclerViewNotes.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                val notes=NoteDatabase(it).getNoteDao().getAllNotes()
                recyclerViewNotes.adapter=NotesAdapter(notes)

            }
        }


        binding.AddButton.setOnClickListener{
            val action=NotesHomeFragmentDirections.actionAddNote()
            Navigation.findNavController(it).navigate(action)
        }
    }
    }
