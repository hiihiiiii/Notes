package com.example.notee.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notee.R;
import com.example.notee.entities.Note;
import com.example.notee.listeners.NoteListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes;
    private NoteListener noteListener;
    public NotesAdapter(List<Note> notes, NoteListener noteListener) {
        this.notes = notes;
        this.noteListener=noteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListener.onNoteClicked(notes.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static  class  NoteViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtSubtitle, txtDatetime;

        LinearLayout layoutNote;
        RoundedImageView imageNote;


       NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle= itemView.findViewById(R.id.textTitle);
            txtSubtitle=itemView.findViewById(R.id.textSubtitle);
            txtDatetime= itemView.findViewById(R.id.textDatetime);
            layoutNote= itemView.findViewById(R.id.layoutNote);
            imageNote= itemView.findViewById(R.id.imgNote);
        }

        void setNote(Note note){
           txtTitle.setText(note.getTitle());
           if(note.getSubtitle().trim().isEmpty()){
               txtTitle.setVisibility(View.GONE);
           }
           else {
               txtSubtitle.setText(note.getSubtitle());
           }
           txtDatetime.setText(note.getDatetime());

            GradientDrawable gradientDrawable= (GradientDrawable) layoutNote.getBackground();
              if(note.getColor()!= null){
                  gradientDrawable.setColor(Color.parseColor(note.getColor()));
              }else {
                  gradientDrawable.setColor(Color.parseColor(("#333333")));
              }

            if(note.getImagePath() != null){
                Log.e(note.getImagePath(),"path");
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else {
                imageNote.setVisibility(View.GONE);
            }
          }
    }
}
