package ru.nexterot.homework4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                Class destinationActivity = SecondActivity.class;
                Intent childActivityIntent = new Intent(context, destinationActivity);
                String text = ((EditText)findViewById(R.id.edit_text)).getText().toString();
                childActivityIntent.putExtra("data", text);
                startActivityForResult(childActivityIntent, REQUEST_CODE);
            }
        });
        findViewById(R.id.buttonWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri parsedUrl = Uri.parse("https://developer.android.com");
                Intent newIntent = new Intent(Intent.ACTION_VIEW, parsedUrl);
                if (newIntent.resolveActivity(getPackageManager())!=null) {
                    startActivity(newIntent);
                }
            }
        });
        findViewById(R.id.buttonMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri addressUri = Uri.parse("geo:55.7443629,37.5665334");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(addressUri);
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(v.getContext(), "No map app found", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.buttonShareText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mimeType = "text/plain";
                String title = "SharingString";
                String text = ((EditText)findViewById(R.id.edit_text)).getText().toString();
                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(text)
                        .startChooser();
            }
        });
        findViewById(R.id.buttonPhoneCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:88005553535"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String text = data.getStringExtra("data");
        ((TextView) findViewById(R.id.main_text)).setText(text);
    }
}
