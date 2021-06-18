package com.example.algorithmvisualizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.algorithmvisualizer.db.NoteDatabase

import kotlinx.android.synthetic.main.fragment_notes_home.*
import kotlinx.coroutines.launch

//inheriting the base fragment(our custom fragment) which is in turn inheriting from fragment class
class NotesHomeFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //displaying the notes
        recyclerViewNotes.setHasFixedSize(true)
        //grid of 3
        recyclerViewNotes.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                val notes=NoteDatabase(it).getNoteDao().getAllNotes()
                recyclerViewNotes.adapter=NotesAdapter(notes)

            }
        }


        AddButton.setOnClickListener{
            val action=NotesHomeFragmentDirections.actionAddNote()
            Navigation.findNavController(it).navigate(action)
        }
    }
    }
