package com.example.notee.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notee.R;
import com.example.notee.activities.CreateNoteActivity;
import com.example.notee.adapters.NotesAdapter;
import com.example.notee.database.NotesDatabase;
import com.example.notee.entities.Note;
import com.example.notee.listeners.NoteListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NoteListener {
    public static  final int REQUEST_CODE_ADD_NOTE=1;
    public static final int REQUEST_CODE_UPDATE_NOTE=2;
    public static final int REQUEST_CODE_SHOW_NOTES=3;
    private RecyclerView notesRecylerView;
    private List<Note> noteList;
    private int noteClickedPosition=-1;
   NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageAddNoteMain= findViewById(R.id.imgAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
        notesRecylerView= findViewById(R.id.noteRecyclerview);
        notesRecylerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        noteList= new ArrayList<>();
        notesAdapter= new NotesAdapter(noteList,this);
        notesRecylerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES);

    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition=position;
        Intent intent= new Intent(getApplicationContext(),CreateNoteActivity.class);
        intent.putExtra("isVieworUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);
    }

    private  void getNotes(final int requestCode ){
        @SuppressLint("StaticFieldLeak")
        class  GetNotesTask extends AsyncTask<Void,Void, List<Note>>{
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if(requestCode==REQUEST_CODE_SHOW_NOTES){
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode== REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecylerView.smoothScrollToPosition(0);
                } else if (requestCode==REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    noteList.add(noteClickedPosition,notes.get(noteClickedPosition));
                    notesAdapter.notifyItemChanged(noteClickedPosition);
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD_NOTE && resultCode==RESULT_OK){
                getNotes(REQUEST_CODE_ADD_NOTE);
        }else if (requestCode==REQUEST_CODE_UPDATE_NOTE && resultCode==RESULT_OK){
            if(data!=null){
                getNotes(REQUEST_CODE_UPDATE_NOTE);
            }
        }
    }
}