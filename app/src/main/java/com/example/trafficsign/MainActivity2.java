package com.example.trafficsign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private Button upl, pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button upload = (Button)findViewById(R.id.upl);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strint = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(strint);
            }
        });
        Button cam=(Button)findViewById(R.id.pic);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent strint = new Intent(getApplicationContext(), MainActivity3.class);
                startActivity(strint);
            }
        });
    }

}