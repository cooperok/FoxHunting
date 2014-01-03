package ua.cooperok.foxhunting.db;

import java.util.Date;

import ua.cooperok.foxhunting.game.Record;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class FoxhuntingDatabase {

    public static final String DATABASE_NAME = "foxhunting";

    public static final String RECORDS_TABLE_NAME = "records";

    private DatabaseHelper mDatabaseHelper;

    public FoxhuntingDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public Cursor getRecords() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(RECORDS_TABLE_NAME);
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return qb.query(db, null, null, null, null, null, "created ASC");
    }

    public Record insertRecord(int steps, String username) {
        long rowId;
        long now = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put(RecordsColumns.STEPS, steps);
        values.put(RecordsColumns.USERNAME, username);
        values.put(RecordsColumns.CREATED, now);
        
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        //
        rowId = db.insert(RECORDS_TABLE_NAME, RecordsColumns.ID, values);
        Record record;
        if (rowId > 0) {
            record = new Record(rowId, steps, now);
        } else {
            throw new SQLException("Failed to insert record");
        }
        return record;
    }

    public void deleteFolder(long recordID) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // delete the record
        db.delete(RECORDS_TABLE_NAME, RecordsColumns.ID + "=" + recordID, null);
    }

}