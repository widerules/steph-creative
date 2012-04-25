package com.creativedroid.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.creativedroid.ui.Logger;

/**
 * Use this to create the structure of your SQLiteDatabase
 * @author Steph
 *
 */
public final class DatabaseCreator implements IDatabaseCreator{

	private Context context;
	private ArrayList<String> tableNames;
	private ArrayList<String> sqlCreateTables = new ArrayList<String>();
	private String databaseName;
	private int version;
	private SQLiteDatabase sqliteDB;
	private SQLiteOpenHelper dbOpenHelper;
	
	public DatabaseCreator(Context context, String databaseName, int version) {		
		this.context = context;
		this.databaseName = databaseName;
		this.version = version;
		this.tableNames = new ArrayList<String>();
		this.sqlCreateTables = new ArrayList<String>();		
	}
	
	public DatabaseCreator(Context context) {
		this(context, "default.db", 1);
	}
	
	public void addTable(String tableName, String sqlCreateTable) {
		this.tableNames.add(tableName);
		this.sqlCreateTables.add(sqlCreateTable);
	}

	public SQLiteDatabase getDatabase() {
		if (this.sqliteDB == null) {
			this.dbOpenHelper = this.createSQLiteOpenHelper();
			this.sqliteDB = this.dbOpenHelper.getWritableDatabase();
			Logger.i("DatabaseCreator", "open database");
		}
		return this.sqliteDB;
	}
	
	private SQLiteOpenHelper createSQLiteOpenHelper(){
		return new SQLiteOpenHelper(this.context, this.databaseName, null, this.version) {
						
			@Override
			public void onCreate(SQLiteDatabase db) {
				for (String sql : DatabaseCreator.this.sqlCreateTables) {
					db.execSQL(sql);
					Logger.i("DatabaseCreator", sql);
				}
			}
			
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {				
				Logger.i("DatabaseCreator", "upgrade from " + oldVersion + " to " + newVersion);
				String sql;
				for (String table : DatabaseCreator.this.tableNames) {
					sql = "DROP TABLE IF EXISTS " + table + ";";
					db.execSQL(sql);
					Logger.i("DatabaseCreator", sql);
				}
				this.onCreate(db);
			}
			
		};
	}

	public void close() {
		if(this.sqliteDB!=null){
			this.sqliteDB.close();
			Logger.i("DatabaseCreator", "close database");
		}		
	}

}
