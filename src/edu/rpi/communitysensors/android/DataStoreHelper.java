package edu.rpi.communitysensors.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DataStoreHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "sensorCaching";
	private static final String DATA_TABLE_NAME = "sensorDataStore";
	public static final String DATE_TIME = "dateTime";
	public static final String TYPE = "type";
	public static final String SENSOR_ID = "sensorId";
	public static final String VALUE = "value";
	
	private static final String DATA_TABLE_CREATE = 
			"CREATE TABLE " + DATA_TABLE_NAME + " (" + 
					SENSOR_ID + " INTEGER, " + 
					DATE_TIME + " TEXT, " +
	                TYPE + " TEXT, " +
					VALUE + " REAL " + 
	                "PRIMARY KEY (" + SENSOR_ID + ", " + DATE_TIME + "));";
	
	private static final String DATA_TABLE_UPDATE = "";

	public DataStoreHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(DATA_TABLE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL(DATA_TABLE_UPDATE);

	}

}
