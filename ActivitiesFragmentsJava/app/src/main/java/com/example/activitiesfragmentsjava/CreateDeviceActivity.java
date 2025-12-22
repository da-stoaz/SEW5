package com.example.activitiesfragmentsjava;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activitiesfragmentsjava.data.DeviceData;
import com.example.activitiesfragmentsjava.network.DeviceApiService;

public class CreateDeviceActivity extends AppCompatActivity {

    private DeviceApiService apiService;

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

        apiService = new DeviceApiService(this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        EditText deviceName = this.findViewById(R.id.editTextDeviceName);
        EditText manufacturer = this.findViewById(R.id.editTextManufacturer);
        EditText serialNumber = this.findViewById(R.id.editTextSerialNumber);
        EditText description = this.findViewById(R.id.editTextDescription);

        Button createButton = this.findViewById(R.id.createButton);

        createButton.setOnClickListener(v -> {
            String name = deviceName.getText().toString();
            String manuf = manufacturer.getText().toString();
            String serial = serialNumber.getText().toString();
            String desc = description.getText().toString();

            if (name.isEmpty() || manuf.isEmpty()) {
                Toast.makeText(this, "Name and Manufacturer are required", Toast.LENGTH_SHORT).show();
                return;
            }

            DeviceData newDeviceData = new DeviceData(name, manuf, serial, desc);

            apiService.createDevice(newDeviceData, new DeviceApiService.Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(CreateDeviceActivity.this, "Device created", Toast.LENGTH_SHORT).show();
                    // Navigate back to Overview. If Overview is singleTop or we just finish, it should resume.
                    // But we want to ensure it refreshes.
                    // Ideally, just finish() and let Overview refresh in onResume.
                    finish();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(CreateDeviceActivity.this, "Error creating device: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
