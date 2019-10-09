package ru.nexterot.homework4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent parentIntent = getIntent();
        if (parentIntent.hasExtra("data")) {
            String parentText = parentIntent.getStringExtra("data");
            ((TextView)findViewById(R.id.textView)).setText(parentText);
        }

        findViewById(R.id.buttonFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", ((TextView)findViewById(R.id.textView)).getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
