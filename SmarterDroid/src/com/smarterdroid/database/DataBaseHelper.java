package com.smarterdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private String[] tableNames;
	private String[] sqlCreateTables;
	private SQLiteDatabase sqliteDB;

	public DataBaseHelper(Context context, String name, int version,
			String[] tableNames, String[] sqlCreateTables) {
		super(context, name, null, version);
		this.tableNames = tableNames;
		this.sqlCreateTables = sqlCreateTables;
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

	public SQLiteDatabase open() {
		if (sqliteDB == null) {
			sqliteDB = getWritableDatabase();
		}
		return sqliteDB;
	}

	public void close() {
		sqliteDB.close();
	}

}
