package com.example.vadym.sqlitedemo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vadym.sqlitedemo.R;
import com.example.vadym.sqlitedemo.database.model.Note;
import com.example.vadym.sqlitedemo.utils.OnRecyclerLongClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.MyViewHolder> {
    List<Note> notes;
    Context context;

    public AdapterRecycler(List<Note> notes, Context context) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recycler, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewNote.setText(notes.get(position).getText());
        holder.textViewDot.setText(Html.fromHtml("&#8226;"));
        holder.textViewTimestamp.setText(formatDate(notes.get(position).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNote;
        TextView textViewDot;
        TextView textViewTimestamp;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OnRecyclerLongClickListener listener = (OnRecyclerLongClickListener) context;
                    listener.onLongClick(getAdapterPosition());
                    return true;
                }
            });

            textViewNote = (TextView) itemView.findViewById(R.id.text_view_note);
            textViewDot = (TextView) itemView.findViewById(R.id.text_view_dot);
            textViewTimestamp = (TextView) itemView.findViewById(R.id.text_view_timestamp);

        }
    }
}

