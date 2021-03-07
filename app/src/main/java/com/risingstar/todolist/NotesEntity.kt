package com.risingstar.todolist


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true) var id : Int,
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "description")var desc : String?
)