package com.risingstar.todolist

import androidx.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notesEntity: NotesEntity)

    @Delete
    fun delete(notesEntity: NotesEntity)

    @Query("DELETE FROM notes")
    fun reset(/*notesEntityList: List<NotesEntity>*/)

    @Query("UPDATE notes SET title =:newTitle,description =:newDesc WHERE id =:nId" )
    fun update(nId: Int , newTitle : String , newDesc : String?)

    @Query("SELECT * FROM notes")
    fun getAllNotes() : List<NotesEntity>
}