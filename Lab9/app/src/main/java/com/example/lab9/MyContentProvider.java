package com.example.lab9;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {

    private static final int MULTIPLE_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI("com.example.lab9.provider", "table3", MULTIPLE_ROWS);
        sUriMatcher.addURI("com.example.lab9.provider", "table3/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                if (TextUtils.isEmpty(s1)) s1 = "_ID ASC";
                break;
            case 2:
                s = s + "_ID = " + uri.getLastPathSegment();
                break;
        }
        Cursor cursor = null;
        // do the query
        //cursor = getContentResolver()
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}