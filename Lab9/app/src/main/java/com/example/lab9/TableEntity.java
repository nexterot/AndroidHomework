package com.example.lab9;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "textTable")
public class TableEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @NonNull
    @ColumnInfo(name = "text_column")
    public String text;

    public TableEntity(String text) {
        this.text = text;
    }

    @Ignore
    public TableEntity(long id, String text) {
        this.id = id;
        this.text = text;
    }
}
