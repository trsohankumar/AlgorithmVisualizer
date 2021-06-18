package com.example.algorithmvisualizer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
//Entity represents a table within Database
@Entity //creates a table for each class with use of @Entity
data class Note(
    val title:String,   //could use @ColumnInfo for naming columsn
    val note:String
):Serializable{
    @PrimaryKey(autoGenerate = true)  //room will take care of generation
    var id:Int=0
}