package com.example.activitiesfragmentsjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activitiesfragmentsjava.data.ItemData;

public class CreateItemActivity extends AppCompatActivity {

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

        EditText deviceName = this.findViewById(R.id.editTextDeviceName);
        EditText manufacturer = this.findViewById(R.id.editTextManufacturer);
        EditText serialNumber = this.findViewById(R.id.editTextSerialNumber);
        EditText description = this.findViewById(R.id.editTextDescription);

        Button createButton = this.findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemData itemData = new ItemData(
                        deviceName.getText().toString(),
                        manufacturer.getText().toString(),
                        serialNumber.getText().toString(),
                        description.getText().toString()
                );

                Intent intent = new Intent(CreateItemActivity.this, ViewItemsActivity.class);
                intent.putExtra("itemData", itemData);
                startActivity(intent);
            }
        });
    }
}
