package com.smarterdroid;

import com.smarterdroid.data.Event;
import com.smarterdroid.data.Place;
import com.smarterdroid.data.Task;
import com.smarterdroid.database.DataBaseHelper;
import com.smarterdroid.database.EventAdapter;
import com.smarterdroid.database.PlaceAdapter;
import com.smarterdroid.database.TaskAdapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		test();
		Toast.makeText(this, "Test done", Toast.LENGTH_LONG).show();
	}

	private void test() {

		String[] tableNames = new String[] { PlaceAdapter.TABLE_NAME, TaskAdapter.TABLE_NAME, EventAdapter.TABLE_NAME };
		String[] sqlCreateTables = new String[] { PlaceAdapter.SQL_CREATE_TABLE, TaskAdapter.SQL_CREATE_TABLE, EventAdapter.SQL_CREATE_TABLE };
		DataBaseHelper dbh = new DataBaseHelper(this, null, 4, tableNames,
				sqlCreateTables);
		SQLiteDatabase db = dbh.open();
		PlaceAdapter pad = new PlaceAdapter(this, db);
		EventAdapter ead = new EventAdapter(this, db);
		TaskAdapter tad = new TaskAdapter(this, db);

		tad.deleteAll();
		tad.insert(new Task());
		tad.selectAll();
		tad.deleteWithID(-1);
		int x= tad.getCount();
		tad.selectWithID(0);
		tad.getNextId();
		Log.i("Main", ""+x);
		
		dbh.close();
	}
	
}