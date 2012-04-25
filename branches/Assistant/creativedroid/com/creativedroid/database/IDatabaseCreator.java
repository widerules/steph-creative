package com.creativedroid.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Operation to setup a SQLiteDatabase
 * @author Steph
 *
 */
interface IDatabaseCreator {

	/** Add a new table in the database */
	void addTable(String tableName, String sqlCreateTable);
	
	/** get SQLiteDatabase*/
	SQLiteDatabase getDatabase();
	
	/** close database*/
	void close();

}
