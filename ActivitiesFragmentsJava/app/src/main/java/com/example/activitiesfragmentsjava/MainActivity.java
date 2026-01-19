package com.example.activitiesfragmentsjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createItemButton = findViewById(R.id.createItemButton);
        Button viewItemsButton = findViewById(R.id.viewItemsButton);

        createItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateDeviceActivity.class);
            startActivity(intent);
        });

        viewItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeviceOverviewActivity.class);
            startActivity(intent);
        });
    }
}
