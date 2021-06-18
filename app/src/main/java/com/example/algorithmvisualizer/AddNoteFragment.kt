package com.example.algorithmvisualizer


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.algorithmvisualizer.databinding.FragmentAddNoteBinding
import com.example.algorithmvisualizer.db.Note
import com.example.algorithmvisualizer.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_notes_home.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note:Note?=null  //checking if note is not null so that we cannot delete null note(possibly delete while adding new one)

    private lateinit var binding:FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        binding = FragmentAddNoteBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            //Setting the text (user)
        arguments?.let {
            note=AddNoteFragmentArgs.fromBundle(it).note
            edit_text_title.setText(note?.title)
            edit_text_note.setText(note?.note)
        }
    //to save action
      binding.SaveButton.setOnClickListener{ view ->
          val  noteTitle=edit_text_title.text.toString().trim()
          val noteBody=edit_text_note.text.toString().trim()
            if(noteTitle.isEmpty()){
                edit_text_title.error="Title is required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }
          if(noteBody.isEmpty()) {


              edit_text_note.error = "Note required"
              edit_text_note.requestFocus()
              return@setOnClickListener
              }
            //coroutines part
          launch {

              context?.let {
                  val newNote=Note(noteTitle,noteBody)
                  if(note==null){
                      NoteDatabase(it).getNoteDao().addNote(newNote)
                      it.toast("Note Saved")
                  }else{
                      newNote.id=note!!.id
                      NoteDatabase(it).getNoteDao().updateNote(newNote)
                      it.toast("Note Updated")

                  }

                  val action=AddNoteFragmentDirections.actionSaveNote()
                  Navigation.findNavController(view).navigate(action)
              }
              }
          }

        //delete note action part
        binding.deleteButton.setOnClickListener{
            launch {
                NoteDatabase(context!!).getNoteDao().deleteNote(note!!)
                val action=AddNoteFragmentDirections.actionSaveNote()
                Navigation.findNavController(view!!).navigate(action)
                context?.let{
                    it.toast("Note Deleted")
                }

            }
        }


      }




    }

//another way with menu xml file alertdialog !!Issue with the toolbar thing not showing up
/*private fun deleteNote(){
        AlertDialog.Builder(context!!).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes"){ _,_->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action=AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("No"){_,_->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> if(note!= null) deleteNote() else context?.toast("Cannot delete")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)
    }*/


