package com.creativedroid.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

interface IDatabaseAdapter<T> {

	/** insert a new object */
	void insert(T obj);

	/** update an object identified with its id */
	int update(T obj, long id);

	/** delete an object with id */
	boolean deleteWithID(long id);

	/** delete all stored objects */
	void deleteAll();

	/** select a single element with id */
	T selectWithID(long id);

	/** select all to cursor */
	Cursor selectAllToCursor();

	/** get a list of all stored objects */
	ArrayList<T> selectAll();

	/** get next id */
	int getNextId();

	/** get the number of stored objects */
	int getCount();
	
	/** convert object to ContentValues */
	ContentValues objectToContentValues(T obj);

	/** get object from cursor supposed not null */
	T cursorToObject(Cursor cur);

}

/**
 * Interface to be implemented for each database to object adapter
 * 
 * @author Steph
 * 
 */
public abstract class DatabaseAdapter<T> implements IDatabaseAdapter<T> {

	protected SQLiteDatabase sqliteDB;
	protected Context context;
	protected String tableName;

	protected static final String COL_ID = "_id";
	protected static final int NUM_COL_ID = 0;

	public DatabaseAdapter(Context context, SQLiteDatabase sqliteDB,
			String tableName) {
		this.sqliteDB = sqliteDB;
		this.context = context;
		this.tableName = tableName;
	}

	public Cursor selectAllToCursor() {
		return sqliteDB.query(tableName, null, null, null, null, null, null);
	}

	public void insert(T obj) {
		sqliteDB.insert(tableName, null, objectToContentValues(obj));
	}

	public int update(T obj, long id) {
		return sqliteDB.update(tableName, objectToContentValues(obj), COL_ID
				+ " = " + id, null);
	}

	public boolean deleteWithID(long id) {
		return sqliteDB.delete(tableName, COL_ID + "=" + id, null) > 0;
	}

	public void deleteAll() {
		sqliteDB.execSQL("DELETE FROM " + tableName);
	}

	public T selectWithID(long id) {
		Cursor cur = sqliteDB.query(tableName, null, COL_ID + " = " + id, null,
				null, null, null);

		if (cur.getCount() == 0)
			return null;

		cur.moveToFirst();
		T obj = cursorToObject(cur);
		cur.close();

		return obj;
	}

	public int getNextId() {
		Cursor dataCount = sqliteDB.rawQuery("SELECT MAX(" + COL_ID + ") FROM "
				+ tableName + ";", null);
		dataCount.moveToFirst();
		int id = dataCount.getInt(0);
		return id + 1;
	}

	public int getCount() {
		Cursor dataCount = sqliteDB.rawQuery("SELECT COUNT(*) FROM "
				+ tableName + ";", null);
		dataCount.moveToFirst();
		int count = dataCount.getInt(0);
		return count;
	}

	/** get a list of all stored objects */
	public ArrayList<T> selectAll() {
		ArrayList<T> list = new ArrayList<T>();
		Cursor cur = selectAllToCursor();

		if (cur.getCount() != 0) {

			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				list.add(cursorToObject(cur));
				cur.moveToNext();
			}
		}
		cur.close();

		return list;
	}

}