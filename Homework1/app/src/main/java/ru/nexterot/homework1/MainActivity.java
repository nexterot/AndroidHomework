package ru.nexterot.homework1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    ImageView imageView;
    Button button;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        editText = findViewById(R.id.edit_text);
        imageView = findViewById(R.id.image_view);
        button = findViewById(R.id.button);
        checkBox = findViewById(R.id.checkbox);

        textView.setText("text set from Java");
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        imageView.setImageAlpha(100);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        checkBox.setChecked(true);
    }
}
