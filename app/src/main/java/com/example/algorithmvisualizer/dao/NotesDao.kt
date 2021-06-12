package com.example.algorithmvisualizer.dao

import androidx.room.*
import com.example.algorithmvisualizer.entities.Notes
//dao->data access object(contains all the methods used for accessing the database
@Dao
interface NotesDao {
    @get:Query("SELECT * FROM notes ORDER BY id DESC")
    val allNotes:List<Notes>

    //all crud stuff

    @Insert(onConflict=OnConflictStrategy.REPLACE) //same note will get replaced i.e is the conflict strategy
    fun insertNotes(notes:Notes)

    @Delete
    fun deleteNote(notes: Notes)


}

