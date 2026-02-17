package net.stoaz.a.persistance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.stoaz.a.persistance.db.ColumConstants;
import net.stoaz.a.persistance.db.EventDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnSettings;
    Button btnNotes;
    private static final String TAG = "EVENT_DB";

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

        btnSettings = this.findViewById(R.id.settingsBtn);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnNotes = this.findViewById(R.id.notesButton);
        btnNotes.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, NotesActivity.class);
            startActivity(intent);
        });


        writeDatabase();
        readDatabase();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_display_container, new FragmentSettingsDisplay())
                    .commit();
        }
    }

    private void readDatabase(){
        EventDatabase eventDatabase = new EventDatabase(this);
        SQLiteDatabase db = eventDatabase.getReadableDatabase();

        Cursor cur = db.rawQuery(
                "SELECT _id, time, event FROM " + ColumConstants.TABLE_NAME + " ORDER BY _id DESC",
                null
        );

        try {
            int idIdx = cur.getColumnIndex(ColumConstants._ID);
            int timeIdx = cur.getColumnIndex(ColumConstants.TIME);
            int eventIdx = cur.getColumnIndex(ColumConstants.EVENT);

            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    long id = cur.getLong(idIdx);
                    long time = cur.getLong(timeIdx);
                    String event = cur.getString(eventIdx);

                    Log.v(TAG, "id=" + id + " time=" + time + " event=" + event);
                    cur.moveToNext();
                }
            } else {
                Log.v(TAG, "No rows in table " + ColumConstants.TABLE_NAME);
            }
        } finally {
            cur.close();
        }
    }

    private void writeDatabase(){
        EventDatabase eventDatabase = new EventDatabase(this);

        SQLiteDatabase db = eventDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ColumConstants.TIME, System.currentTimeMillis());
        values.put(ColumConstants.EVENT, "Mitarbeitskontrolle");
        db.insert(ColumConstants.TABLE_NAME, null, values);
        Log.v(TAG, "Inserted one row into " + ColumConstants.TABLE_NAME);
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
