package com.risingstar.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotesEntity :: class],version = 1,exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao() : NotesDao
}