package net.stoaz.firstandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.stoaz.firstandroidproject.data.DataClass;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNext = this.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        Log.w(MainActivity.class.getSimpleName(), "Hahaha - We are in Software engineering class");
    }

    @Override
    public void onClick(View v) {
        Log.w(MainActivity.class.getSimpleName(), "next click");

        DataClass data = new DataClass(10);


        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra("data", data);
        intent.putExtra("name", "John");
        startActivity(intent);
    }
}