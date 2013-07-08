//package ua.cooperok.foxhunting.db;
//
//import android.content.Context;
//import android.database.*;
//import android.database.sqlite.*;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.util.Log;
//
//public class RecordsAdapter {
//
//	private static final String DATABASE_NAME = "foxhunting";
//	private static final String DATABASE_TABLE = "records";
//	private static final int DATABASE_VERSION = 1;
//
//	// Индекс (ключ) столбца, с которым мы будем работать.
//	public static final String ID="id";
//	
//	// Имя и индекс каждого столбца в вашей базе данных.
//	public static final String KEY_NAME="name";
//	public static final int NAME_COLUMN = 1;
//
//	// Инструкция на языке SQL для создания новой базы данных.
//	private static final String DATABASE_CREATE = "create table " +
//	DATABASE_TABLE + " (" + ID +
//	" integer primary key autoincrement, " +
//	KEY_NAME + " text not null);";
//
//	// Поле, хранящее экземпляр базы данных.
//	private SQLiteDatabase db;
//
//	// Объект Context приложения, которое использует базу данных.
//	private final Context context;
//	
//	// Вспомогательный класс для открытия/обновления базы данных.
//	private myDbHelper dbHelper;
//
//	
//	public RecordsAdapter(Context context) {
//		
//		this.context = context;
//		
//		dbHelper = new myDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
//		
//		SQLiteDatabase db;
//		
//		/* Вызов метода getWritableDatabase может завершиться неудачно из-за проблем
//		 * с полномочиями или нехваткой места на диске, поэтому лучше предусмотреть
//		 * откат к методу getReadableDatabase
//		 */
//		try {
//		
//			db = dbHelper.getWritableDatabase();
//			
//		} catch (SQLiteException ex){
//			
//			db = dbHelper.getReadableDatabase();
//			
//		}
//		
//	}
//	
//	public RecordsAdapter open() throws SQLException {
//	
//		db = dbHelper.getWritableDatabase();
//		
//		return this;
//		
//	}
//	
//	public void close() {
//	
//		db.close();
//		
//	}
//
//	public int insertEntry(MyObject _myObject) {
//
//		// TODO: Создать новый объект ContentValues для представления нашей
//		// строки и вставить его в базу данных.
//		return index;
//		
//	}
//	
//	public boolean removeEntry(long _rowIndex) {
//	
//		return db.delete(DATABASE_TABLE, ID + "=" + _rowIndex, null) > 0;
//
//	}
//	
//	public Cursor getAllEntries () {
//	
//		return db.query(DATABASE_TABLE, new String[] {ID, KEY_NAME}, null, null, null, null, null);
//
//	}
//	
//	public MyObject getEntry(long _rowIndex) {
//		
//		// TODO: Возвратить курсор, ссылающийся на строку из базы данных,
//		// и использовать значения для заполнения данными экземпляра MyObject.
//		
//		return objectInstance;
//		
//	}
//
//	public boolean updateEntry(long _rowIndex, MyObject _myObject) {
//		
//		// TODO: Создать экземпляр класса ContentValues, основанный на новом
//		// объекте, и использовать его для обновления строки в базе данных.
//	
//		return true;
//		
//	}
//
//	private static class myDbHelper extends SQLiteOpenHelper {
//		
//		public myDbHelper(Context context, String name, CursorFactory factory, int version) {
//		
//			super(context, name, factory, version);
//			
//		}
//
//		// Вызывается, когда база данных не найдена на носителе,
//		// и вспомогательный класс должен создать новую.
//		@Override
//		public void onCreate(SQLiteDatabase _db) {
//			
//			_db.execSQL(DATABASE_CREATE);
//			
//		}
//		
//		// Вызывается при несоответствии версии базы данных, в следствие чего
//		// версия базы данных на носителе нуждается в обновлении до текущей.
//		@Override
//		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
//
//			// Логируйте обновление версии.
//			Log.w("TaskDBAdapter", "Upgrading from version " +
//			_oldVersion + " to " +
//			_newVersion + ", which will destroy all old data");
//			
//			// Приведите существующую базу данных в соответствие с новой
//			// версией. Все предыдущие версии могут быть обработаны путем
//			// сравнения значений _oldVersion и _newVersion.
//			// Самый простой способ - удалить старую таблицу и создать новую.
//			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
//	
//			// Create a new one.
//			onCreate(_db);
//			
//		}
//		
//	}
//	
//}
