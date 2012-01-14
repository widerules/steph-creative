package com.smarterdroid.data.access;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smarterdroid.data.object.Event;

import creativedroid.database.DatabaseAdapter;

public class EventAdapter extends DatabaseAdapter<Event> {

	public static final String TABLE_NAME = "events";

	private static final String COL_DESCRIPTION = "description";
	private static final int NUM_COL_DESCRIPTION = 1;
	private static final String COL_DATE = "event_date";
	private static final int NUM_COL_DATE = 2;
	private static final String COL_PREVENT_TIME = "prevent_time";
	private static final int NUM_COL_PREVENT_TIME = 3;
	private static final String COL_PLACE_ID = "place_id";
	private static final int NUM_COL_PLACE_ID = 4;
	private static final String COL_SHARED = "shared";
	private static final int NUM_COL_SHARED = 5;
	private static final String COL_SOURCE = "source";
	private static final int NUM_COL_SOURCE = 6;

	public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ " (" 
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_DESCRIPTION + " TEXT NOT NULL, " 
			+ COL_DATE + " TEXT NOT NULL, "
			+ COL_PREVENT_TIME + " INTEGER NOT NULL, " 
			+ COL_PLACE_ID + " INTEGER NOT NULL, "
			+ COL_SHARED + " INTEGER NOT NULL, " 
			+ COL_SOURCE + " STRING NOT NULL" 
			+ ");";

	public EventAdapter(Context context, SQLiteDatabase sqliteDB) {
		super(context, sqliteDB, TABLE_NAME);
	}

	@Override
	protected ContentValues objectToContentValues(Event event) {
		ContentValues values = new ContentValues();
		values.put(COL_DESCRIPTION, event.description);
		values.put(COL_DATE, dateToString(event.date));
		values.put(COL_PREVENT_TIME, event.preventTime);
		values.put(COL_PLACE_ID, event.place != null ? event.place.id : -1);
		values.put(COL_SHARED, event.shared ? 1 : 0);
		values.put(COL_SOURCE, event.source);
		return values;
	}

	/** Convert date to string */
	private String dateToString(Date date) {
		if(date==null) return "";
		String str= "";
		str+= date.getDate();
		str+= ":"+date.getMonth();
		str+= ":"+date.getYear();
		str+= ":"+date.getHours();
		str+= ":"+date.getMinutes();
		return str;
	}

	/** convert string to date */
	private Date stringToDate(String str) {
		if(str.equals("")) return null;
		String el[] = str.split(":");
		Date date = new Date();
		date.setDate(Integer.parseInt(el[0]));
		date.setMonth(Integer.parseInt(el[1]));
		date.setYear(Integer.parseInt(el[2]));
		date.setHours(Integer.parseInt(el[3]));
		date.setMinutes(Integer.parseInt(el[4]));
		return date;
	}

	@Override
	protected Event cursorToObject(Cursor cur) {
		Event event = new Event();
		event.id = cur.getInt(NUM_COL_ID);
		event.description = cur.getString(NUM_COL_DESCRIPTION);
		event.date = stringToDate(cur.getString(NUM_COL_DATE));
		event.preventTime = cur.getInt(NUM_COL_PREVENT_TIME);
		event.shared = cur.getInt(NUM_COL_SHARED) == 1;
		event.source = cur.getString(NUM_COL_SOURCE);

		int placeID = cur.getInt(NUM_COL_PLACE_ID);
		if (placeID != -1) {
			PlaceAdapter pad = new PlaceAdapter(context, sqliteDB);
			event.place = pad.selectWithID(placeID);
		}
		return event;
	}

}
