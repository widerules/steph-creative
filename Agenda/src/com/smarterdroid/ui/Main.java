package com.smarterdroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.creativedroid.database.DataBaseHelper;
import com.creativedroid.ui.ListViewItem;
import com.creativedroid.ui.ListViewManager;
import com.creativedroid.ui.ServiceConnector;
import com.smarterdroid.R;
import com.smarterdroid.dataaccess.EventAdapter;
import com.smarterdroid.dataaccess.PlaceAdapter;
import com.smarterdroid.dataaccess.TaskAdapter;
import com.smarterdroid.object.Place;
import com.smarterdroid.service.IMyService;
import com.smarterdroid.service.MyService;
import com.smarterdroid.service.MyServiceReceiver;

public class Main extends Activity implements Vocabulary {

	private final int ADD_PLACE_REQUEST_CODE = 1;
	private ListViewManager listViewManager;
	private ServiceConnector<MyService> srvConnector;
	private MyServiceReceiver srvReceiver;
	private DataBaseHelper dBHelper;
	
	
	private MyServiceReceiver getMyServiceReceiver(){
		return new MyServiceReceiver(this) {
			
			@Override
			public void onFlagReceived(int flag) {
				Log.i("***Main", "receive message from service");
				switch (flag) {
				case IMyService.FLAG_SERVICE_STARTED:
					srvConnector.connectToService();
					break;

				default:
					if (srvConnector.isBound()){
						Pool.config = Pool.service.getConfig();
						listViewManager.refresh();
					}
					break;
				}
			}			
		};
	}	
	
	private DataBaseHelper getDatabaseHelper(){
		return new DataBaseHelper(this) {
			
			public void setup() {
				addTable(PlaceAdapter.TABLE_NAME, PlaceAdapter.SQL_CREATE_TABLE);
				addTable(TaskAdapter.TABLE_NAME, TaskAdapter.SQL_CREATE_TABLE);
				addTable(EventAdapter.TABLE_NAME, EventAdapter.SQL_CREATE_TABLE);
			}
		};
	}

	/** initialize listviewManager */
	private ListViewManager getListViewManager(){
		ListView list = (ListView) findViewById(android.R.id.list);
		ListViewManager l = new ListViewManager(this, list, R.layout.listview_item, R.layout.listview_item_disabled);
		l.addItem(new ItemStatus());
		l.addItem(new ItemEvent());
		l.addItem(new ItemTask());
		l.addItem(new ItemPlace());
		l.addItem(new ItemPreference());
		l.disable();
		return l;
	}
	
	private ServiceConnector<MyService> getServiceConnector() {
	
		return new ServiceConnector<MyService>(this, MyService.class,
				"com.smarterdroid.service.MyService") {
	
			public void onServiceConnected() {
				Pool.service = getService();
				Pool.config = Pool.service.getConfig();
				listViewManager.enable();
				listViewManager.refresh();
			}
	
			public void onServiceDisconnected() {
				listViewManager.disable();
				listViewManager.refresh();
			}
		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Pool.context = this;

		listViewManager = getListViewManager();
		dBHelper = getDatabaseHelper();
		Pool.eventAdapter = new EventAdapter(this, dBHelper.getDatabase());
		Pool.taskAdapter = new TaskAdapter(this, dBHelper.getDatabase());
		Pool.placeAdapter = new PlaceAdapter(this, dBHelper.getDatabase());

		srvConnector = getServiceConnector();
		if (srvConnector.isServiceRunning())
			srvConnector.connectToService();

		srvReceiver = getMyServiceReceiver();

		listViewManager.refresh();
	}

	/** go foreground */
	@Override
	protected void onResume() {
		srvReceiver.start();
		Log.i("***Main", "onresume");
		super.onResume();
	}

	/** go background */
	@Override
	protected void onStop() {
		srvReceiver.stop();
		Log.i("***Main", "onstop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		srvConnector.disconnectFromService();
		dBHelper.close();
		super.onDestroy();
	}

	class ItemStatus extends ListViewItem {

		public void setup() {
			name = SERVICE_STATUS;
			description = RUNNING;
			isCheckable = true;
			imageLeft = android.R.drawable.ic_menu_info_details;
		}

		@Override
		public void onCheckedChanged(boolean isChecked) {
			if (isChecked)
				srvConnector.startService();
			else
				srvConnector.stopService();
		}

		@Override
		public void onImageLeftClick() {
			showDialogFunctions();
		}

		@Override
		public void onRefresh() {
			if (srvConnector.isBound()) {
				int runningFunctions = 0;
				if(Pool.config.enableEventAlarm) runningFunctions++;
				if(Pool.config.enableQuietPlace) runningFunctions++;
				if(Pool.config.enableSMSToSpeech) runningFunctions++;
				if(Pool.config.enableSpeedLimit) runningFunctions++;				
				description = runningFunctions + " " + RUNNING;
				isChecked = true;
			}
			else{
				description = STOPPED;
				isChecked = false;
			}
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

	/** A dialog that allow us to choose the functionalities */
	class SelectFunctionDialog {
	
		/** boolean array that describe current enabled functionalities */
		private boolean[] selections;
	
		/** get array of options */
		private CharSequence[] getFunctionsOptions() {
			return new CharSequence[] { 
					FCT_EVENT_ALARM, 
					FCT_QUIET_PLACE,
					FCT_SMS_TO_SPEECH, 
					FCT_SPEED_LIMIT };
		}
	
		/** get boolean array of functionalities */
		private boolean[] getFunctions() {
			boolean[] selections = new boolean[4];
			selections[0] = Pool.config.enableEventAlarm;
			selections[1] = Pool.config.enableQuietPlace;
			selections[2] = Pool.config.enableSMSToSpeech;
			selections[3] = Pool.config.enableSpeedLimit;
			return selections;
		}
	
		/** set functionalities */
		private void setFunctions(boolean[] selections) {
			if (selections[0])
				Pool.service.startEventAlarm();
			else
				Pool.service.stopEventAlarm();
			if (selections[1])
				Pool.service.startQuietPlace();
			else
				Pool.service.stopQuietPlace();
			if(selections[2])
				Pool.service.startSMSToSpeech();
			else
				Pool.service.stopSMSToSpeech();
			if(selections[3])
				Pool.service.startSpeedLimit();
			else
				Pool.service.stopSpeedLimit();
		}
	
		public void show() {
			selections = getFunctions();
			AlertDialog.Builder adb = new AlertDialog.Builder(Main.this);
			adb.setTitle(SELECT_FUNCTIONS);
			adb.setPositiveButton(OK, new OnClickListener() {
	
				public void onClick(DialogInterface dialog, int which) {
					setFunctions(selections);
				}
			});
	
			adb.setIcon(android.R.drawable.ic_menu_info_details);
			adb.setNegativeButton(CANCEL, null);
			adb.setMultiChoiceItems(getFunctionsOptions(), selections,
					new OnMultiChoiceClickListener() {
	
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO on item selected
	
						}
					});
			adb.show();
		}
	}

	public void showDialogFunctions() {
		SelectFunctionDialog dialog = new SelectFunctionDialog();
		dialog.show();
	}

	public void goToAddNewEvent() {
		// TODO Auto-generated method stub
		
		Toast.makeText(Main.this,
				"Under construction add new event ", Toast.LENGTH_SHORT)
				.show();

	}

	public void goToViewEvents() {
		// TODO Auto-generated method stub
		
		Toast.makeText(Main.this,
				"Under construction view events ", Toast.LENGTH_SHORT)
				.show();

	}

	public void goToAddNewTask() {
		// TODO Auto-generated method stub
		
		Toast.makeText(Main.this,
				"Under construction add new task ", Toast.LENGTH_SHORT)
				.show();

	}

	public void goToViewTasks() {
		// TODO Auto-generated method stub
		
		Toast.makeText(Main.this,
				"Under construction view tasks ", Toast.LENGTH_SHORT)
				.show();

	}
	
	public void goToAddNewPlace() {
		Intent intent = new Intent(this, PlaceEditor.class);
		startActivityForResult(intent, ADD_PLACE_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_PLACE_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {

				Bundle bundle = data.getExtras();
				Place place = bundle.getParcelable("place");
				Pool.placeAdapter.insert(place);
				Toast.makeText(this, place.description + " successfully added",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void goToViewPlaces() {
		Intent intent = new Intent(this, Places.class);
		startActivity(intent);
	}

	public void goToPreferences() {
		Intent intent = new Intent(this, Preferences.class);
		startActivity(intent);
	}

}