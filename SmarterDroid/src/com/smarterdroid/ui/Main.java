package com.smarterdroid.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ListView;
import com.smarterdroid.R;
import com.smarterdroid.data.access.EventAdapter;
import com.smarterdroid.data.access.PlaceAdapter;
import com.smarterdroid.data.access.TaskAdapter;
import com.smarterdroid.data.object.Event;
import com.smarterdroid.data.object.Place;
import com.smarterdroid.data.object.ServiceConfig;
import com.smarterdroid.data.object.Task;
import com.smarterdroid.service.MyService;

import creativedroid.database.DataBaseHelper;
import creativedroid.ui.ListViewItem;
import creativedroid.ui.ListViewManager;

public class Main extends Activity implements Vocabulary {

	final String SERVICE_NAME = "com.smarterdroid.service.MyService";	
	private ListView list;
	private ListViewManager listViewManager;
	private ServiceConfig srvConfig;
	private MyService boundService;
	private boolean isBound;
	private SrvConnection srvConnection = new SrvConnection();
	private SrvReceiver srvReceiver = new SrvReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initListView();
		
		registerReceiver(srvReceiver, new IntentFilter(MyService.SERVICE_ACTION));

	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(srvReceiver);
		super.onDestroy();
	}

	/** initialize listviewManager */
	private void initListView() {
		list = (ListView) findViewById(android.R.id.list);
		listViewManager = new ListViewManager(this, list);
		listViewManager.addItem(new ItemServiceStatus());
		listViewManager.addItem(new ItemEvent());
		listViewManager.addItem(new ItemTask());
		listViewManager.addItem(new ItemPlace());
		listViewManager.addItem(new ItemPreference());
		listViewManager.refresh();
	}

	private void testDB() {

		String[] tableNames = new String[] { PlaceAdapter.TABLE_NAME,
				TaskAdapter.TABLE_NAME, EventAdapter.TABLE_NAME };
		String[] sqlCreateTables = new String[] {
				PlaceAdapter.SQL_CREATE_TABLE, TaskAdapter.SQL_CREATE_TABLE,
				EventAdapter.SQL_CREATE_TABLE };
		DataBaseHelper dbh = new DataBaseHelper(this, "smarter.db", 1,
				tableNames, sqlCreateTables);
		SQLiteDatabase db = dbh.open();
		PlaceAdapter pad = new PlaceAdapter(this, db);
		EventAdapter ead = new EventAdapter(this, db);
		TaskAdapter tad = new TaskAdapter(this, db);

		ead.deleteAll();
		ead.insert(new Event());
		ead.selectAll();
		ead.deleteWithID(-1);
		int x = ead.getCount();
		ead.selectWithID(0);
		ead.getNextId();
		Log.i("Main", "EventAdapter.getCount()=" + x);

		Intent intent;
		Bundle bundle;
		Place place;
		Event event;
		Task task;

		// send objects via intent
		intent = new Intent();
		bundle = new Bundle();
		bundle.putParcelable("place", new Place());
		bundle.putParcelable("event", new Event());
		bundle.putParcelable("task", new Task());
		intent.putExtras(bundle);

		// get objects from intent
		bundle = intent.getExtras();
		place = bundle.getParcelable("place");
		task = bundle.getParcelable("task");
		event = bundle.getParcelable("event");

		Log.i("***Main", "" + (place != null) + (task != null)
				+ (event != null));

		dbh.close();

		Log.i("***Main", "Test done");
	}

	class ItemServiceStatus extends ListViewItem {

		public void setup() {
			name = SERVICE_STATUS;
			description = RUNNING;
			isCheckable = true;
			imageLeft = android.R.drawable.ic_menu_info_details;
		}

		@Override
		public void onCheckedChanged(boolean isChecked) {
			if (isChecked)
				startMyService();
			else
				stopMyService();
		}

		@Override
		public void onImageLeftClick() {
			showDialogFunctions();
		}

		@Override
		public void onRefresh() {
			description = isBound ? RUNNING : STOPPED;
			isChecked = isBound;
		}

	}

	class ItemEvent extends ListViewItem {

		public void setup() {
			name = EVENT;
			description = EVENT_DESC;
			imageLeft = android.R.drawable.ic_menu_add;
		}

		@Override
		public void onImageLeftClick() {
			goToAddNewEvent();
		}

		@Override
		public void onItemClicked() {
			goToViewEvents();
		}

	}

	class ItemTask extends ListViewItem {

		public void setup() {
			name = TASK;
			description = TASK_DESC;
			imageLeft = android.R.drawable.ic_menu_add;
		}

		@Override
		public void onImageLeftClick() {
			goToAddNewTask();
		}

		@Override
		public void onItemClicked() {
			goToViewTasks();
		}

	}

	class ItemPlace extends ListViewItem {

		public void setup() {
			name = PLACE;
			description = PLACE_DESC;
			imageLeft = android.R.drawable.ic_menu_add;
		}

		@Override
		public void onImageLeftClick() {
			goToAddNewPlace();
		}

		@Override
		public void onItemClicked() {
			goToViewPlaces();
		}

	}

	class ItemPreference extends ListViewItem {

		public void setup() {
			name = PREFERENCES;
			description = PREFERENCES_DESC;
			imageLeft = android.R.drawable.ic_menu_preferences;
		}

		@Override
		public void onItemClicked() {
			goToPreferences();
		}
	}
	
	/**
	 * Receive broadcast messages from MyService 
	 * Here get the service information in a (ServiceParameter)bundle 
	 * 1. read ServiceParameter
	 * 2. update listView
	 */
	class SrvReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("***SrvReceiver", "receive message ");
			Bundle bundle = intent.getExtras();
			srvConfig = bundle.getParcelable("param");
			listViewManager.refresh();
			if (!isBound)
				connectToService();
		}
	}
	
	/**
	 * class used to bind to the service
	 */
	class SrvConnection implements ServiceConnection {
		/**
		 * This is called when the connection with the service has been
		 * established, giving us the service object we can use to 
		 * interact with the service. Because we have bound to a explicit 
		 * service that we know is running in our own process, we can 
		 * cast its IBinder to a concrete class and directly access it.
		 */
		public void onServiceConnected(ComponentName className, IBinder service) {
			boundService = ((MyService.MyBinder) service).getService();
			isBound = true;
			srvConfig = boundService.getConfig();		
			listViewManager.enable();
			listViewManager.refresh();			
			Log.i("**Main", "onServiceConnected");
		}
	
		/**
		 * This is called when the connection with the service has been 
		 * unexpectedly disconnected -- that is, its process crashed. 
		 * Because it is running in our same process, we should never 
		 * see this happen.
		 */
		public void onServiceDisconnected(ComponentName className) {			
			boundService = null;
		}
	}

	
	/**
	 * Establish a connection with the service. We use an explicit
	 *  class name because we want a specific service implementation that
	 *  we know will be running in our own process (and thus won't be
	 *  supporting component replacement by other applications).
	 */
	public void connectToService() {
		bindService(new Intent(this, MyService.class), srvConnection,
				Context.BIND_AUTO_CREATE);
	}

	/** Detach our existing connection */
	public void disconnectFromService() {
		if (isBound) {
			unbindService(srvConnection);
			isBound = false;
			listViewManager.disable();
			Log.i("***Main", "disconnected from service");
		}
	}

	/**
	 * Start service : the service obviously start in asynchronous call so wait
	 * for the broadcast message SERVICE_STARTED to update the view again !!
	 */
	public void startMyService() {
		Intent intent = new Intent(this, MyService.class);
		startService(intent);
	}

	public void stopMyService() {
		disconnectFromService();
		Intent intent = new Intent(this, MyService.class);
		stopService(intent);
		listViewManager.refresh();
	}

	public void showDialogFunctions() {
		// TODO Auto-generated method stub

	}

	public void goToAddNewEvent() {
		// TODO Auto-generated method stub

	}

	public void goToViewEvents() {
		// TODO Auto-generated method stub

	}

	public void goToAddNewTask() {
		// TODO Auto-generated method stub

	}

	public void goToViewTasks() {
		// TODO Auto-generated method stub

	}

	public void goToAddNewPlace() {
		// TODO Auto-generated method stub

	}

	public void goToViewPlaces() {
		// TODO Auto-generated method stub

	}

	public void goToPreferences() {
		// TODO Auto-generated method stub

	}
	
	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (SERVICE_NAME.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
}