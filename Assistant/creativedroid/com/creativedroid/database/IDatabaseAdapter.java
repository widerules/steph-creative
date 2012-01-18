package com.creativedroid.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * CRUD operation of a relational Database
 * @author Steph
 *
 * @param <T>
 */
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