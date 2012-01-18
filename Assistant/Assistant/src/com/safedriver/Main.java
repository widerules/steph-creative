package com.safedriver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.creativedroid.ui.ListViewItem;
import com.creativedroid.ui.ListViewManager;
import com.creativedroid.ui.ServiceConnector;
import com.creativedroid.ui.Util;
import com.smarterdroid.R;

public class Main extends Activity implements Vocabulary {

	final float[] maxSpeedValues = new float[] { 60, 80, 120 };
	public static Context context;
	private ListViewManager listViewManager;
	private ServiceConnector<MyService> srvConnector;
	private MyServiceReceiver srvReceiver;
	private SharedPreferences preferences;
	private Editor editor;
	private MyService service;
	private ServiceConfig config;
	
	private void loadPreferences(){
		
		config = new ServiceConfig();
		config.enableSMSToSpeech = true;
		config.enableSpeedLimit = true;
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		
		//fisrt use
		if(!preferences.contains("NotFirstUse")){
			 editor.putString("NotFirstUse", "1");			 
			 editor.commit();
			 showDialogName();
		}
		else{
			config.pseudo = preferences.getString("pseudo", "");
			config.maxSpeed = preferences.getFloat("maxSpeed", 0);
		}
	}

	private MyServiceReceiver getMyServiceReceiver() {
		return new MyServiceReceiver(this) {

			@Override
			public void onFlagReceived(int flag) {
				Log.i("***Main", "receive message from service");
				switch (flag) {
				case IMyService.FLAG_SERVICE_STARTED:
					srvConnector.connectToService();
					break;

				default:
					if (srvConnector.isBound()) {
						config = service.getConfig();
						listViewManager.refresh();
					}
					break;
				}
			}
		};
	}

	/** initialize listviewManager */
	private ListViewManager getListViewManager() {
		ListView list = (ListView) findViewById(android.R.id.list);
		ListViewManager l = new ListViewManager(this, list,
				R.layout.listview_item, R.layout.listview_item_disabled);
		l.addItem(new ItemStatus());
		l.addItem(new ItemMaxSpeed());
		l.disable();
		return l;
	}

	private ServiceConnector<MyService> getServiceConnector() {

		return new ServiceConnector<MyService>(this, MyService.class,
				"com.safedriver.MyService") {

			public void onServiceConnected() {
				service = getService();
				config = service.getConfig();
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

		context = this;

		listViewManager = getListViewManager();
		
		srvConnector = getServiceConnector();
		if (srvConnector.isServiceRunning())
			srvConnector.connectToService();
		else
			loadPreferences();

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
		super.onDestroy();
	}

	class ItemStatus extends ListViewItem {

		public void setup() {
			name = SERVICE_STATUS;
			isCheckable = true;
			imageLeft = android.R.drawable.ic_menu_recent_history;
		}

		@Override
		public void onCheckedChanged(boolean isChecked) {
			if (isChecked){
				Bundle bundle = new Bundle();
				bundle.putParcelable("config", config);
				//srvConnector.startService();
				srvConnector.startService(bundle);
			}				
			else
				srvConnector.stopService();
		}

		@Override
		public void onRefresh() {
			if (srvConnector.isBound()) {
				description = RUNNING;
				isChecked = true;
			} else {
				description = STOPPED;
				isChecked = false;
			}
		}
		
		@Override
		public void onImageLeftClick() {
			showDialogName();
		}

	}

	class ItemMaxSpeed extends ListViewItem {

		public void setup() {
			name = MAXSPEED;
			imageLeft = android.R.drawable.ic_menu_sort_by_size;
		}

		@Override
		public void onRefresh() {
			if(srvConnector.isBound())
			description = (int) config.maxSpeed + " km/h\n"
					+ CURRENT_SPEED + " " + (int)config.speed + " km/h";
		}

		@Override
		public void onItemClicked() {
			showDialogMaxSpeed();
		}
	}

	public void showDialogMaxSpeed() {		
		CharSequence[] TIME_OPTIONS = new CharSequence[maxSpeedValues.length];
		for (int i = 0; i < TIME_OPTIONS.length; i++) {
			TIME_OPTIONS[i] = (int) maxSpeedValues[i] + " km/h";
		}
		int index = Util.indexOf(config.maxSpeed, maxSpeedValues);

		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(SELECT_MAX_SPEED);
		adb.setIcon(android.R.drawable.ic_menu_sort_by_size);
		adb.setSingleChoiceItems(TIME_OPTIONS, index, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				float maxSpeed = maxSpeedValues[which];
				editor.putFloat("maxSpeed", maxSpeed);
				editor.commit();
				config.maxSpeed = maxSpeed;
				if (srvConnector.isBound())
					service.setMaxSpeed(maxSpeed);
				dialog.cancel();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	public void showDialogName() {
		final EditText input = new EditText(this);
		input.setText("");

		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(PROMPT_PSEUDO);
		adb.setIcon(android.R.drawable.ic_menu_info_details);
		adb.setView(input);
		adb.setPositiveButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				editor.putString("pseudo", value);
				editor.commit();
				config.pseudo = value;
				if (srvConnector.isBound())
					service.setPseudo(value);
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

}