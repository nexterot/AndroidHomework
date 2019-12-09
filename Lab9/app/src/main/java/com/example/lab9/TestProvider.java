package com.example.lab9;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TestProvider extends ContentProvider {
    private static final int MULTIPLE_ROWS = 1;
    private static final int SINGLE_ROW = 2;

    private SQLiteDatabase mDb;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI("com.example.lab9.provider", "table", MULTIPLE_ROWS);
        sUriMatcher.addURI("com.example.lab9.provider", "table/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        TextDatabaseHelper helper = new TextDatabaseHelper(getContext());
        mDb = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        String sort = sortOrder;
        String select = selection;
        switch (sUriMatcher.match(uri)) {
            case 1: {
                if (TextUtils.isEmpty(sortOrder))
                    sort = TextDatabase.TextDBEntry.TEXT_COLUMN + " ASC;";
                break;
            }
            case 2: {
                select = TextDatabase.TextDBEntry._ID + " = " + uri.getLastPathSegment() + ";";
                break;
            }
        }

        return mDb.query(
                TextDatabase.TextDBEntry.TABLE_NAME,
                new String[]{
                        TextDatabase.TextDBEntry._ID,
                        TextDatabase.TextDBEntry.TEXT_COLUMN},
                select,
                selectionArgs,
                null,
                null,
                sort);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_ROWS: {
                Context context = getContext();
                if (context == null || values == null) {
                    return null;
                }

                long id = mDb.insert(
                        TextDatabase.TextDBEntry.TABLE_NAME,
                        null,
                        values);

                context.getContentResolver().notifyChange(uri, null);

                Log.d(TestProvider.class.getName() + " insert", uri.toString());
                return ContentUris.withAppendedId(uri, id);
            }
            // Не используем остальные запросы
            case SINGLE_ROW: {
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() == null)
            return 0;
        Context context = getContext();

        int count;

        switch (sUriMatcher.match(uri)) {
            case MULTIPLE_ROWS: {
                count = mDb.delete(
                        TextDatabase.TextDBEntry.TABLE_NAME,
                        null,
                        null);
                break;
            }
            case SINGLE_ROW: {
                count = mDb.delete(
                        TextDatabase.TextDBEntry.TABLE_NAME,
                        TextDatabase.TextDBEntry._ID + "=" + ContentUris.parseId(uri),
                        null);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        context.getContentResolver().notifyChange(uri, null);

        Log.d(TestProvider.class.getName() + " delete", uri.toString() + " - " + count);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
