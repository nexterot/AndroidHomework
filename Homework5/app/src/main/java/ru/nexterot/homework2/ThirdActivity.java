package ru.nexterot.homework2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent i = getIntent();
        if (i != null && i.getExtras() != null) {
            String info = i.getExtras().getString("info");
            ((TextView)findViewById(R.id.text)).setText(info);
        }
    }
}
