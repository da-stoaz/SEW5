package com.example.activitiesfragmentsjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activitiesfragmentsjava.data.DeviceData;

import java.util.ArrayList;

public class CreateDeviceActivity extends AppCompatActivity {

    private ArrayList<DeviceData> deviceDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        deviceDataList = getIntent().getParcelableArrayListExtra("deviceDataList");
        if (deviceDataList == null) {
            deviceDataList = new ArrayList<>();
        }

        EditText deviceName = this.findViewById(R.id.editTextDeviceName);
        EditText manufacturer = this.findViewById(R.id.editTextManufacturer);
        EditText serialNumber = this.findViewById(R.id.editTextSerialNumber);
        EditText description = this.findViewById(R.id.editTextDescription);

        Button createButton = this.findViewById(R.id.createButton);

        createButton.setOnClickListener(v -> {
            DeviceData newDeviceData = new DeviceData(
                    deviceName.getText().toString(),
                    manufacturer.getText().toString(),
                    serialNumber.getText().toString(),
                    description.getText().toString()
            );

            deviceDataList.add(newDeviceData);

            Intent intent = new Intent(CreateDeviceActivity.this, DeviceOverviewActivity.class);
            intent.putParcelableArrayListExtra("deviceDataList", deviceDataList);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
