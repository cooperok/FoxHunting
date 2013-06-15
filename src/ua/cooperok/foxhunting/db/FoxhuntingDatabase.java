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
	
    private DatabaseHelper databaseHelper;
    
    public FoxhuntingDatabase(Context context) {
    	
    	databaseHelper = new DatabaseHelper(context);
    	
    }
    
    public Cursor getRecords() {
    	
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(RECORDS_TABLE_NAME);
        
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        
        return qb.query(db, null, null, null, null, null, "created ASC");

    }

    public Record insertRecord(int steps, String username) {

    	long rowId;
        
        String created = "";
    	
        ContentValues values = new ContentValues();
        
        values.put(RecordsColumns.STEPS, steps);
        
        values.put(RecordsColumns.USERNAME, username);
        
        Date now = new Date(System.currentTimeMillis());
        
        values.put(RecordsColumns.CREATED, now.toString());
        
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        rowId = db.insert(RECORDS_TABLE_NAME, RecordsColumns.ID, values);

        Record record;
        
        if (rowId > 0) {
        	
            record = new Record(rowId, steps, created);
        	
        } else {
        	
        	throw new SQLException("Failed to insert record");
        	
        }
        
        return record;
        
    }
    
    public void deleteFolder(long recordID) {
    	
    	SQLiteDatabase db = databaseHelper.getWritableDatabase();
    	
    	// delete the record
    	db.delete(RECORDS_TABLE_NAME, RecordsColumns.ID + "=" + recordID, null);
    }
    
}
