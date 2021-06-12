package com.example.algorithmvisualizer.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//Entity represents a table within Database
@Entity(tableName="Notes") //creates a table for each class with use of @Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) //room will take care of generation
    var id:Int,

    @ColumnInfo(name="title") //naming columns
    var title:String,

    @ColumnInfo(name="sub_title")
    var subTitle:String,

    @ColumnInfo(name="date_time")
    var dateTime:String,

    @ColumnInfo(name="notes_text")
    var notesText:String,

    @ColumnInfo(name="image")
    var image:String,
):Serializable{
    override fun toString(): String {
        return "$title:$dateTime"
    }
}