package com.creativedroid.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Basic database adapter
 * 
 * @author Steph
 * 
 * @param <T>
 */
public abstract class DatabaseAdapter<T> implements IDatabaseAdapter<T> {

	protected SQLiteDatabase sqliteDB;
	protected Context context;
	protected String tableName;

	protected static final String COL_ID = "_id";
	protected static final int NUM_COL_ID = 0;

	public DatabaseAdapter(Context context, SQLiteDatabase sqliteDB, String tableName) {
		this.sqliteDB = sqliteDB;
		this.context = context;
		this.tableName = tableName;
	}

	public void deleteAll() {
		this.sqliteDB.execSQL("DELETE FROM " + this.tableName);
	}

	public boolean deleteWithID(long id) {
		return this.sqliteDB.delete(this.tableName, DatabaseAdapter.COL_ID + "=" + id, null) > 0;
	}

	public int getCount() {
		Cursor c = sqliteDB.rawQuery("SELECT COUNT(*) FROM " + this.tableName + ";", null);
		c.moveToFirst();
		int count = c.getInt(0);
		return count;
	}

	public int getNextId() {
		Cursor c = this.sqliteDB.rawQuery("SELECT MAX(" + DatabaseAdapter.COL_ID + ") FROM " + this.tableName + ";", null);
		c.moveToFirst();
		int id = c.getInt(0);
		return id + 1;
	}

	public void insert(T obj) {
		this.sqliteDB.insert(this.tableName, null, objectToContentValues(obj));
	}

	public ArrayList<T> selectAll() {
		ArrayList<T> list = new ArrayList<T>();
		Cursor c = selectAllToCursor();		
		if (c.getCount() != 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				list.add(cursorToObject(c));
				c.moveToNext();
			}
		}
		c.close();
		return list;
	}

	public Cursor selectAllToCursor() {
		return this.sqliteDB.query(this.tableName, null, null, null, null, null, null);
	}

	public T selectWithID(long id) {
		Cursor c = this.sqliteDB.query(this.tableName, null, DatabaseAdapter.COL_ID + " = " + id, null,
				null, null, null);
		if (c.getCount() == 0){
			return null;
		}
		c.moveToFirst();
		T obj = cursorToObject(c);
		c.close();
		return obj;
	}

	public int update(T obj, long id) {
		return this.sqliteDB.update(this.tableName,
				this.objectToContentValues(obj), DatabaseAdapter.COL_ID + " = " + id, null);
	}

}