package com.example.algorithmvisualizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.algorithmvisualizer.db.Note
import kotlinx.android.synthetic.main.note_layout.view.*
//using recycler view for getting all notes in staggered layout || check NotesHomeFragment for more
class NotesAdapter(private val notes:List<Note>):RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(val view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.text_view_title.text=notes[position].title
        holder.view.text_view_note.text=notes[position].note

        holder.view.setOnClickListener{
            val action=NotesHomeFragmentDirections.actionAddNote()
            action.note=notes[position]
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount()=notes.size
}