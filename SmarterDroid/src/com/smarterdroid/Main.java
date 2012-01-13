package com.smarterdroid;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.smarterdroid.data.Event;
import com.smarterdroid.data.Place;
import com.smarterdroid.data.Task;
import com.smarterdroid.database.DataBaseHelper;
import com.smarterdroid.database.EventAdapter;
import com.smarterdroid.database.PlaceAdapter;
import com.smarterdroid.database.TaskAdapter;

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
		DataBaseHelper dbh = new DataBaseHelper(this, "smarter.db", 1, tableNames,
				sqlCreateTables);
		SQLiteDatabase db = dbh.open();
		PlaceAdapter pad = new PlaceAdapter(this, db);
		EventAdapter ead = new EventAdapter(this, db);
		TaskAdapter tad = new TaskAdapter(this, db);

		ead.deleteAll();
		ead.insert(new Event());
		ead.selectAll();
		ead.deleteWithID(-1);
		int x= ead.getCount();
		ead.selectWithID(0);
		ead.getNextId();
		Log.i("Main", "EventAdapter.getCount()="+x);
		
		Intent intent;
		Bundle bundle;
		Place place;
		Event event;
		Task task;
		
		//send objects via intent
		intent = new Intent();	
		bundle = new Bundle();
	    bundle.putParcelable("place", new Place());
	    bundle.putParcelable("event", new Event());
	    bundle.putParcelable("task", new Task());	    
	    intent.putExtras(bundle);	
		
	    //get objects from intent
		bundle = intent.getExtras();
		place = bundle.getParcelable("place");
		task = bundle.getParcelable("task");
		event = bundle.getParcelable("event");
		
		Log.i("***Main", ""+(place!=null) + (task!=null)+ (event!=null));
		
		dbh.close();
	}
	
}