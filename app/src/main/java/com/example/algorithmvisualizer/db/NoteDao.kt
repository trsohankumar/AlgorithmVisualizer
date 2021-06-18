package com.example.algorithmvisualizer.db

import androidx.room.*

//dao->data access object(contains all the methods used for accessing the database
@Dao
interface NoteDao {

    //all crud stuff

    @Insert
    suspend fun addNote(note:Note)

    @Query(value="SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes():List<Note>

   // @Insert
    //suspend fun addMultipleNotes(vararg note:Note)

    @Update
    suspend fun updateNote(note:Note)

    @Delete
    suspend fun deleteNote(note:Note)

}