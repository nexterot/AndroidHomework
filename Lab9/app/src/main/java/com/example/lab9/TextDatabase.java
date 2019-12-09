package com.example.lab9;

import android.provider.BaseColumns;

public class TextDatabase {
    // Информация о базе данных
    public static class TextDBEntry implements BaseColumns {
        public static final String TABLE_NAME = "Text_Table";
        public static final String TEXT_COLUMN = "text_column";
    }
}
