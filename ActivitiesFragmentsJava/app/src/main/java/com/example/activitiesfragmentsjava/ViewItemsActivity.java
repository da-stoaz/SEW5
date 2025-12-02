package com.example.activitiesfragmentsjava;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.activitiesfragmentsjava.data.ItemData;
import com.example.activitiesfragmentsjava.fragments.ItemDataFragment;

public class ViewItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            ItemData itemData = bundle.getParcelable("itemData", ItemData.class);

            Log.d(ViewItemsActivity.class.getSimpleName(), itemData.toString());

            if (itemData != null) {
                ItemDataFragment fragment = ItemDataFragment.newInstance(itemData);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_view, fragment)
                        .commit();
            } else {
                Toast.makeText(this, "Error: Item data not found in Intent.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
