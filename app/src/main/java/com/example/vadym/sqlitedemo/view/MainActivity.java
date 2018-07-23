package com.example.vadym.sqlitedemo.view;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vadym.sqlitedemo.R;
import com.example.vadym.sqlitedemo.database.DatabaseHelper;
import com.example.vadym.sqlitedemo.database.model.Note;
import com.example.vadym.sqlitedemo.utils.MyDividerItemDecoration;
import com.example.vadym.sqlitedemo.utils.OnRecyclerLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRecyclerLongClickListener {
    private List<Note> notes = new ArrayList<>();

    private RecyclerView rv;
    private AdapterRecycler adapter;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);
        notes.addAll(db.getAllNotes());

        adapter = new AdapterRecycler(notes, this);
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        rv.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create note
                showNoteDialog(false, null, -1);
            }
        });
    }

    @Override
    public void onLongClick(int position) {
        showActionDialog(position);
    }

    private void showActionDialog(final int position) {
        String choice[] = new String[]{getResources().getString(R.string.edit), getResources().getString(R.string.delete)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_option);
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //update note
                        String text = notes.get(position).getText();
                        showNoteDialog(true, text, position);
                        break;
                    case 1: //delete note
                        //deleteNote(position);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean needUpdate, String text, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.dialog, null);

        final EditText editTextNote = (EditText) v.findViewById(R.id.edit_text_note_dialog);
        if (needUpdate)
            editTextNote.setText(text);
        TextView textViewTitleDialog = (TextView) v.findViewById(R.id.text_view_title_dialog);
        textViewTitleDialog.setText(needUpdate ? R.string.update_note : R.string.new_note);

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(MainActivity.this);
        builderDialog.setView(v)
                .setPositiveButton(needUpdate ? R.string.update : R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(editTextNote.getText().toString())) {
                            Toast.makeText(MainActivity.this, R.string.enter_note, Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            if (!needUpdate) {
                                String text = editTextNote.getText().toString();
                                addNote(text);
                            } else {
                                String text = editTextNote.getText().toString();
                                updateNote(text, position);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builderDialog.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    private void updateNote(String text, int position) {
        Note n = notes.get(position);
        n.setText(text);

        db.updateNote(n);

        notes.set(position, n);
        adapter.notifyItemChanged(position);
    }

    private void addNote(String note) {
        //CREATE
        long id = db.insertNote(note);
        //READ
        Note n = db.getNote(id);
        notes.add(0, n);

        adapter.notifyDataSetChanged();
    }
}