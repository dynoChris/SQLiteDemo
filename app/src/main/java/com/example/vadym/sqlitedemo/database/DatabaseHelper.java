package com.example.vadym.sqlitedemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vadym.sqlitedemo.database.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes_db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Note.TABLE_NAME + "("
                + Note.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Note.COLUMN_NOTE + " TEXT,"
                + Note.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")");

    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + Note.TABLE_NAME + " ORDER BY " + Note.COLUMN_ID + " DESC",
                null);

        if (cursor.moveToFirst()) {
            do {
                Note n = new Note();
                n.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                n.setText(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                n.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(n);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    public long insertNote(String note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note);

        long id = db.insert(Note.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME, new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP}, Note.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
        );
        cursor.close();

        return note;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getText());

        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}