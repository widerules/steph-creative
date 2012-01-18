package com.smarterdroid.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.creativedroid.database.DatabaseAdapter;
import com.smarterdroid.object.Place;

public class PlaceAdapter extends DatabaseAdapter<Place> {

	public static final String TABLE_NAME = "places";
	
	private static final String COL_DESCRIPTION = "description";
	private static final int NUM_COL_DESCRIPTION = 1;
	private static final String COL_LATITUDE = "latitude";
	private static final int NUM_COL_LATITUDE = 2;
	private static final String COL_LONGITUDE = "longitude";
	private static final int NUM_COL_LONGITUDE = 3;
	private static final String COL_RADIUS = "radius";
	private static final int NUM_COL_RADIUS = 4;
	private static final String COL_QUIET = "quiet";
	private static final int NUM_COL_QUIET = 5;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME 
			+ " (" 
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_DESCRIPTION + " TEXT NOT NULL, " 
			+ COL_LATITUDE + " REAL NOT NULL, "
			+ COL_LONGITUDE + " REAL NOT NULL, " 
			+ COL_RADIUS + " INTEGER NOT NULL, "
			+ COL_QUIET + " INTEGER NOT NULL" 
			+ ");";
	
	public PlaceAdapter(Context context, SQLiteDatabase sqliteDB) {
		super(context, sqliteDB, TABLE_NAME);
	}

	public ContentValues objectToContentValues(Place place) {
		ContentValues values = new ContentValues();
		values.put(COL_DESCRIPTION, place.description);
		values.put(COL_LATITUDE, place.latitude);
		values.put(COL_LONGITUDE, place.longitude);
		values.put(COL_RADIUS, place.radius);
		values.put(COL_QUIET, place.quiet ? 1 : 0);
		return values;
	}

	public Place cursorToObject(Cursor cur) {
		Place place = new Place();
		place.id = cur.getInt(NUM_COL_ID);
		place.description = cur.getString(NUM_COL_DESCRIPTION);
		place.latitude = cur.getDouble(NUM_COL_LATITUDE);
		place.longitude = cur.getDouble(NUM_COL_LONGITUDE);
		place.radius = cur.getInt(NUM_COL_RADIUS);
		place.quiet = cur.getInt(NUM_COL_QUIET) == 1;
		return place;
	}

}
