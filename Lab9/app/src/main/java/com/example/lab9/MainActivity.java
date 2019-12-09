package com.example.lab9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mItemsList;
    private ListAdapter mListAdapter;
    private List<TableEntity> elemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        elemsList = new ArrayList<>();

        mItemsList = (RecyclerView) findViewById(R.id.rv_items);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mItemsList.setLayoutManager(linearLayoutManager);

        mListAdapter = new ListAdapter(elemsList);
        mItemsList.setAdapter(mListAdapter);

        mItemsList.setHasFixedSize(true);

        readAndUpdateEntries();

        // Кнопка для добавления элементов в БД
        Button addElemButton = (Button) findViewById(R.id.button_add);
        final EditText editTextElem = (EditText) findViewById(R.id.edit_elem);
        addElemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Добавляем в БД элемент из EditText
                String text = editTextElem.getText().toString();
                Uri uri = insertEntry(text);
                readAndUpdateEntries();
            }
        });

        // Кнопка для смены отображаемых данных
        final Button changeDataButton = (Button) findViewById(R.id.button_change_data);
        changeDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Если отображаются данные из БД, показываем контакты
                if (changeDataButton.getText().toString().equals("Show contacts")) {
                    readContacts();
                    changeDataButton.setText("Show database");
                } else {
                    // Отображаем данные из БД
                    readAndUpdateEntries();
                    changeDataButton.setText("Show contacts");
                }
            }
        });


        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (changeDataButton.getText().toString().equals("Show database")) {
                            mListAdapter.deleteElem(((ElemViewHolder) viewHolder).id);
                        } else {
                            long elemId = ((ElemViewHolder) viewHolder).getId();
                            Uri uri = ContentUris.withAppendedId(
                                    Uri.parse("content://com.example.lab9.provider/table")
                                            .buildUpon().build(), elemId);
                            getContentResolver().delete(uri, null, null);
                            readAndUpdateEntries();
                        }
                    }
                }
        ).attachToRecyclerView(mItemsList);
    }

    // Получение данных из БД и обновление адаптера
    private void readAndUpdateEntries() {
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://com.example.lab9.provider/table")
                .buildUpon().build(),
                null,
                null,
                null,
                null);

        if (cursor != null) {
            ArrayList<TableEntity> list = new ArrayList<>();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.moveToPosition(i)) {
                    long id = cursor.getLong(
                            cursor.getColumnIndex(TextDatabase.TextDBEntry._ID));
                    String text = cursor.getString(
                            cursor.getColumnIndex(TextDatabase.TextDBEntry.TEXT_COLUMN));
                    list.add(new TableEntity(id, text));
                }
            }
            mListAdapter.setData(list);
        }
    }

    // Добавление записи в БД
    private Uri insertEntry(String text) {
        ContentValues values = new ContentValues();
        values.put(TextDatabase.TextDBEntry.TEXT_COLUMN, text);

        Uri uri = getContentResolver().insert(
                Uri.parse("content://com.example.lab9.provider/table")
                .buildUpon().build(),
                values);
        if (uri == null) {
            throw new IllegalArgumentException("URI in insertEntry is null");
        }

        return uri;
    }

    private void readContacts() {
        // Получаем разрешение на запрос контактов
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }

        Cursor contactsCursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null
        );

        if (contactsCursor != null) {
            ArrayList<TableEntity> list = new ArrayList<>();
            for (int i = 0; i < contactsCursor.getCount(); i++) {
                if (contactsCursor.moveToPosition(i)) {
                    long id = contactsCursor.getLong(
                            contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String text = contactsCursor.getString(
                            contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    list.add(new TableEntity(id, text));
                }
            }
            mListAdapter.setData(list);
        }
    }
}
