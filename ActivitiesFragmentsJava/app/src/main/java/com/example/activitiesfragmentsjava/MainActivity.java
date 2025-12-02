package com.example.activitiesfragmentsjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitiesfragmentsjava.data.DeviceData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dummy data for testing
        if (deviceDataList.isEmpty()) {
            deviceDataList.add(new DeviceData("Laptop", "Dell", "XPS15-123", "A powerful laptop"));
            deviceDataList.add(new DeviceData("Smartphone", "Samsung", "S21-456", "A modern smartphone"));
        }

        Button createItemButton = findViewById(R.id.createItemButton);
        Button viewItemsButton = findViewById(R.id.viewItemsButton);

        createItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateDeviceActivity.class);
            intent.putParcelableArrayListExtra("deviceDataList", deviceDataList);
            startActivity(intent);
        });

        viewItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeviceOverviewActivity.class);
            intent.putParcelableArrayListExtra("deviceDataList", deviceDataList);
            startActivity(intent);
        });
    }
}
