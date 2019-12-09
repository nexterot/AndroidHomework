package com.example.lab9;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TextDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "text.db";
    private final static int DATABASE_VER = 1;

    public TextDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = String.format(
                "CREATE TABLE %s ( " +
                "%s INTEGER PRIMARY KEY, " +
                "%s TEXT NOT NULL);",
                TextDatabase.TextDBEntry.TABLE_NAME,
                TextDatabase.TextDBEntry._ID,
                TextDatabase.TextDBEntry.TEXT_COLUMN);
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format
                ("DROP TABLE %s ;",
                TextDatabase.TextDBEntry.TABLE_NAME));
        onCreate(db);
    }
}
