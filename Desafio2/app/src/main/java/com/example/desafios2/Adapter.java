package com.example.desafios2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<Note> notesList;
    private ClickListener clickListener;
    private LongClickListener longClickListener;

    public Adapter(ArrayList<Note> notesList) {
        this.notesList = notesList;
    }

    public void filterList(ArrayList<Note> filterlist) {
        this.notesList = filterlist;
        notifyDataSetChanged();
    }

    public void setNotesList(ArrayList<Note> notesList) {
        this.notesList = notesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder view = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));

        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.titleOutput.setText(note.getTitle());
        holder.status = note.getStatus();
        if (note.getStatus()) {
            holder.bodyOutput.setText(note.getBody());
        } else {
            holder.bodyOutput.setText("You must accept note to read its content");
        }

    }

    @Override
    public int getItemCount() {
        if(notesList==null){
            return 0;
        }
        return notesList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.note_layout;
    }

    void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    void setOnItemLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleOutput;
        TextView bodyOutput;
        Boolean status;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleOutput = itemView.findViewById(R.id.Title);
            bodyOutput = itemView.findViewById(R.id.textView_notes);

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
            if (longClickListener != null ) {
                itemView.setOnLongClickListener(this);
            }
        }

        public void bindData(final String data) {
            titleOutput.setText(data);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
            System.out.println("CLICK");
        }

        @Override
        public boolean onLongClick(View view) {
            if (longClickListener != null && status) {
                longClickListener.onItemClick(getAdapterPosition(), view);
            }
            System.out.println("LONG CLICK");
            return true;
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public interface LongClickListener {
        void onItemClick(int position, View v);
    }
}
