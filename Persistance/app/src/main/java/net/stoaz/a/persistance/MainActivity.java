package net.stoaz.a.persistance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.stoaz.a.persistance.db.ColumConstants;
import net.stoaz.a.persistance.db.EventDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnNext;

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

        btnNext = this.findViewById(R.id.button);
        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void readDatabase(){
        EventDatabase eventDatabase = new EventDatabase(this);
        SQLiteDatabase db = eventDatabase.getReadableDatabase();

    }

    private void writeDatabase(){
        EventDatabase eventDatabase = new EventDatabase(this);

        SQLiteDatabase db = eventDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ColumConstants.TIME, System.currentTimeMillis());
        values.put(ColumConstants.EVENT, "Mitarbeitskontrolle");
        db.insert(ColumConstants.TABLE_NAME, null, values);
    }

    /*
    @Override
    public void onClick(View view){
        Log.w(MainActivity.class.getSimpleName(), "test");

        if (view == btnNext){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (view == btnSettings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
    }*/
}