package net.stoaz.a.persistance.db;

import android.provider.BaseColumns;

public interface NoteColumns extends BaseColumns {
    String TABLE_NAME = "notes";
    String COL_TITLE = "title";
    String COL_TEXT = "text";
    String COL_CREATED_AT = "createdAt"; // stored as INTEGER (epoch millis)
    String COL_COMPLETED = "completed";  // stored as INTEGER (0/1)
}