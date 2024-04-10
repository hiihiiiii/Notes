package com.example.notee.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notee.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insertNote(Note note);
    @Delete
    void deleteNote(Note note);

}
