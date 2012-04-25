package com.creativedroid.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

interface IDatabaseHelper{
	
	/** define here your database: name, tables and sql create requests */
	void setup();
	
	/** Add a new table in the database */
	void addTable(String tableName, String sqlCreateTable);
	
	/** get SQLiteDatabase*/
	SQLiteDatabase getDatabase();
	
	/** close database*/
	void close();
	
}

public abstract class DataBaseHelper extends SQLiteOpenHelper  implements IDatabaseHelper{

	private ArrayList<String> tableNames = new ArrayList<String>();
	private ArrayList<String> sqlCreateTables = new ArrayList<String>();
	protected static String databaseName = "default.db";
	protected static int version = 1;
	private SQLiteDatabase sqliteDB;

	public DataBaseHelper(Context context) {
		super(context, databaseName, null, version);
		setup();
	}

	
	public void addTable(String tableName, String sqlCreateTable) {
		tableNames.add(tableName);
		sqlCreateTables.add(sqlCreateTable);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : sqlCreateTables) {
			db.execSQL(sql);
			Log.i("***DatabaseHelper", sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql;
		Log.i("***DatabaseHelper", "upgrade from " + oldVersion + " to "
				+ newVersion);
		for (String table : tableNames) {
			sql = "DROP TABLE IF EXISTS " + table + ";";
			db.execSQL(sql);
			Log.i("***DatabaseHelper", sql);
		}
		onCreate(db);
	}

	public SQLiteDatabase getDatabase() {
		if (sqliteDB == null) {
			sqliteDB = getWritableDatabase();
		}
		return sqliteDB;
	}

	@Override
	public void close() {
		sqliteDB.close();
	}

}
