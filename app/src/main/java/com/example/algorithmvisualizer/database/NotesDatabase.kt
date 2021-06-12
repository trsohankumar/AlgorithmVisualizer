package com.example.algorithmvisualizer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.algorithmvisualizer.dao.NotesDao

import com.example.algorithmvisualizer.entities.Notes

@Database(entities = [Notes::class],version = 1,exportSchema = false)
abstract class NotesDatabase :RoomDatabase(){

    companion object{
        var notesDatabase:NotesDatabase?=null

        @Synchronized
        fun getDatabase(context:Context):NotesDatabase{
            if(notesDatabase!=null){
                notesDatabase=Room.databaseBuilder(context,
                NotesDatabase::class.java,
                "notes.db").build()
            }
            return notesDatabase!!
        }
    }
    abstract fun notesDao():NotesDao
}