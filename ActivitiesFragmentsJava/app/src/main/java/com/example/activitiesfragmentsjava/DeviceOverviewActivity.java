package com.example.activitiesfragmentsjava;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activitiesfragmentsjava.data.DeviceData;
import com.example.activitiesfragmentsjava.fragments.DeviceListFragment;

import java.util.ArrayList;

public class DeviceOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<DeviceData> deviceDataList = bundle.getParcelableArrayList("deviceDataList", DeviceData.class);

            if (deviceDataList != null && savedInstanceState == null) {
                DeviceListFragment fragment = DeviceListFragment.newInstance(deviceDataList);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .commit();
            }
        }
    }
}
