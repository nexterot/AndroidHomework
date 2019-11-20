package ru.nexterot.lab8;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MyFileManager extends ListActivity {

    TextView textView;
    ArrayAdapter adapter;

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
                        Intent kek = new Intent(MyFileManager.this, MyFileManager.class);
                        startActivityForResult(kek, 228);
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

    private static final int WRITE_REQUEST_CODE = 43;
    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if (requestCode == 228 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String content = data.getStringExtra("content");
            File newFile = new File(textView.getText() + "/" + name);
            newFile.

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

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
