package ru.nexterot.lab8;


import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MyFileManager extends ListActivity {

    TextView textView;
    ArrayAdapter adapter;
    File currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file_manager);

        textView = findViewById(R.id.text);

        findViewById(R.id.button_root).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent kek = new Intent(MyFileManager.this, MyFileManager.class);
                        kek.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(kek);
                    }
                }
        );

        findViewById(R.id.button_new_file).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fileName = ((EditText)findViewById(R.id.edit)).getEditableText().toString();
                        if (currentFile.isDirectory() && !fileName.isEmpty()) {
                            File newFile = new File(currentFile, fileName);
                            try {
                                boolean res = newFile.createNewFile();
                                Log.d("TAG", "create new file: " + res);
                            } catch (IOException e) {
                                Log.d("TAG", "create new file failed with error=" + e.toString());
                            }
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }
                }
        );



        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            ArrayList<String> arr = new ArrayList<>();
            if (path != null) {
                Log.d("TAG", "path is: " + path);
                File rootFile = new File(path);
                currentFile = rootFile;
                if (! rootFile.exists()) {
                    Toast.makeText(this, "Error: path " + path + " not found", Toast.LENGTH_LONG).show();
                    Log.d("TAG",  "Error: path " + path + " not found");
                }
                textView.setText(path);
                if (rootFile.isDirectory()) {
                    File[] files = rootFile.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            arr.add(file.getName());
                        }
                    } else {
                        Log.d("TAG", "listFiles returned null");
                    }
                } else {
                    Toast.makeText(this, path + " is a file", Toast.LENGTH_LONG).show();
                }
            } else {
                File rootDir = Environment.getExternalStorageDirectory();
                currentFile = rootDir;
                textView.setText(rootDir.getPath());
                File[] files = rootDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        arr.add(file.getName());
                    }
                } else {
                    Log.d("TAG", "listFiles returned null");
                }
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arr);
            setListAdapter(adapter);
        }

    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (requestCode == 228 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String content = data.getStringExtra("content");

        }
    }

 */

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent kek = new Intent(MyFileManager.this, MyFileManager.class);
        if (v instanceof TextView) {
            kek.putExtra("path", textView.getText() + "/" + ((TextView)v).getText());
        }
        startActivity(kek);
    }

    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
