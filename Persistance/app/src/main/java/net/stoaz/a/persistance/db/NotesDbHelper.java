package net.stoaz.a.persistance.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.stoaz.a.persistance.NotesActivity;

public final class NotesDbHelper extends SQLiteOpenHelper implements NoteColumns {

    private static final String DB_NAME = "appdata.db";
    private static final int DB_VERSION = 1;

    public NotesDbHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_TEXT + " TEXT NOT NULL, " +
                        COL_CREATED_AT + " INTEGER NOT NULL, " +
                        COL_COMPLETED + " INTEGER NOT NULL" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}