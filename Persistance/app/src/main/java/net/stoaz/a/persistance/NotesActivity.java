package net.stoaz.a.persistance;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.stoaz.a.persistance.db.NoteColumns;
import net.stoaz.a.persistance.db.NotesDbHelper;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "DB";
    private NotesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        dbHelper = new NotesDbHelper(this);

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etText = findViewById(R.id.etText);
        CheckBox cbCompleted = findViewById(R.id.cbCompleted);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnRead = findViewById(R.id.btnRead);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String text = etText.getText().toString().trim();
            boolean completed = cbCompleted.isChecked();
            if (title.isEmpty() || text.isEmpty()) return;

            long id = insertNote(title, text, System.currentTimeMillis(), completed);
            Log.d(TAG, "Inserted id=" + id);
        });

        btnRead.setOnClickListener(v -> logAll());
    }

    private void logAll() {
        try (Cursor c = readAllNotes()) {
            int idIdx = c.getColumnIndexOrThrow(NoteColumns._ID);
            int tIdx = c.getColumnIndexOrThrow(NoteColumns.COL_TITLE);
            int xIdx = c.getColumnIndexOrThrow(NoteColumns.COL_TEXT);
            int crIdx = c.getColumnIndexOrThrow(NoteColumns.COL_CREATED_AT);
            int coIdx = c.getColumnIndexOrThrow(NoteColumns.COL_COMPLETED);

            if (!c.moveToFirst()) {
                Log.d(TAG, "No rows.");
                return;
            }

            do {
                long id = c.getLong(idIdx);
                String title = c.getString(tIdx);
                String text = c.getString(xIdx);
                long createdAt = c.getLong(crIdx);          // epoch millis
                boolean completed = c.getInt(coIdx) == 1;   // 0/1

                Log.d(TAG, id + " | " + title + " | " + text
                        + " | createdAt=" + createdAt + " | completed=" + completed);
            } while (c.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    private long insertNote(String title, String text, long createdAtMillis, boolean completed) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteColumns.COL_TITLE, title);
        values.put(NoteColumns.COL_TEXT, text);
        values.put(NoteColumns.COL_CREATED_AT, createdAtMillis);
        values.put(NoteColumns.COL_COMPLETED, completed ? 1 : 0);

        return db.insert(NoteColumns.TABLE_NAME, null, values);
    }

    private Cursor readAllNotes() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                NoteColumns.TABLE_NAME,
                new String[]{
                        NoteColumns._ID,
                        NoteColumns.COL_TITLE,
                        NoteColumns.COL_TEXT,
                        NoteColumns.COL_CREATED_AT,
                        NoteColumns.COL_COMPLETED
                },
                null,
                null,
                null,
                null,
                NoteColumns._ID + " DESC"
        );
    }


}