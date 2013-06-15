package ua.cooperok.foxhunting.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;

	private String TAG = "DatabaseHelper";

	private Context _context;
	
	DatabaseHelper(Context context) {

		super(context, FoxhuntingDatabase.DATABASE_NAME, null, DATABASE_VERSION);
		
		this._context = context;

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + FoxhuntingDatabase.RECORDS_TABLE_NAME + " ("
				+ RecordsColumns.ID + " INTEGER PRIMARY KEY,"
				+ RecordsColumns.STEPS + " INTEGER,"
				+ RecordsColumns.USERNAME + " TEXT,"
				+ RecordsColumns.CREATED + " DATE"
				+ ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int _oldVersion, int _newVersion) {

		//Logging database update
		Log.w(TAG, "Upgrading from version " + _oldVersion + " to " +
				_newVersion + ", which will destroy all old data");
		
		//The simplest way - drop database and create new
		db.execSQL("DROP TABLE IF EXISTS " + FoxhuntingDatabase.RECORDS_TABLE_NAME);

		// Create a new one.
		onCreate(db);

	}
	
	

}
