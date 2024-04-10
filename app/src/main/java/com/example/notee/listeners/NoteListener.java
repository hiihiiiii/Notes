package com.example.notee.listeners;

import com.example.notee.entities.Note;

public interface NoteListener  {
    void onNoteClicked(Note note, int position);
}
