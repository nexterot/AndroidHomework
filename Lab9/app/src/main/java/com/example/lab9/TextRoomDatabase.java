package com.example.lab9;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {TableEntity.class}, version = 2, exportSchema = false)
public abstract class TextRoomDatabase extends RoomDatabase {
    public abstract TableDao tableDao();
}
