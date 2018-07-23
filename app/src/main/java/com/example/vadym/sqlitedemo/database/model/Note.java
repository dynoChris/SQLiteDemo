package com.example.vadym.sqlitedemo.database.model;

public class Note {

    public static final String TABLE_NAME = "table_notes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "Note";
    public static final String COLUMN_TIMESTAMP = "Timestamp";

    private int id;
    private String text;
    private String timestamp;

    public Note() {

    }

    public Note(int id, String text, String timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}