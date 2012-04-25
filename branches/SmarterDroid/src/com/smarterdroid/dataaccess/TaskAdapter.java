package com.smarterdroid.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smarterdroid.object.Task;

import creativedroid.database.DatabaseAdapter;

public class TaskAdapter extends DatabaseAdapter<Task>{
	
	public static final String TABLE_NAME = "tasks";
	
	private static final String COL_DESCRIPTION = "description";
	private static final int NUM_COL_DESCRIPTION = 1;
	private static final String COL_PLACE_ID = "place_id";
	private static final int NUM_COL_PLACE_ID = 2;
	private static final String COL_ACTIVE_ENTER = "active_enter";
	private static final int NUM_COL_ACTIVE_ENTER = 3;
	
	public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME 
			+ " (" 
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_DESCRIPTION + " TEXT NOT NULL, " 
			+ COL_PLACE_ID + " INTEGER NOT NULL, "
			+ COL_ACTIVE_ENTER + " INTEGER NOT NULL" 
			+ ");";
	
	public TaskAdapter(Context context, SQLiteDatabase sqliteDB) {
		super(context, sqliteDB, TABLE_NAME);
	}

	public ContentValues objectToContentValues(Task task) {
		ContentValues values = new ContentValues();
		values.put(COL_DESCRIPTION, task.description);
		values.put(COL_PLACE_ID, task.place!=null? task.place.id : -1);
		values.put(COL_ACTIVE_ENTER, task.activeWhenEnterPlace? 1 : 0);
		return values;
	}

	public Task cursorToObject(Cursor cur) {
		Task task = new Task();
		task.id = cur.getInt(NUM_COL_ID);
		task.description = cur.getString(NUM_COL_DESCRIPTION);
		task.activeWhenEnterPlace = cur.getInt(NUM_COL_ACTIVE_ENTER)==1;
		int placeID = cur.getInt(NUM_COL_PLACE_ID);
		if(placeID!=-1){
			PlaceAdapter pad = new PlaceAdapter(context, sqliteDB);
			task.place = pad.selectWithID(placeID);
		}
		return task;
	}

}
