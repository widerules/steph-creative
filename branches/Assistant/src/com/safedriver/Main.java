package com.safedriver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.creativedroid.service.BasicBroadcastReceiver;
import com.creativedroid.service.ServiceConnector;
import com.creativedroid.ui.ListViewItem;
import com.creativedroid.ui.ListViewManager;
import com.creativedroid.ui.Logger;
import com.creativedroid.ui.Util;
import com.smarterdroid.R;

public class Main extends Activity implements Vocabulary {
	
	public static Context context;
	private ListViewManager listViewManager;
	private ServiceConnector<MyService> srvConnector;	
	private BasicBroadcastReceiver  srvReceiver;
	private MyService service;	
	private SharedPreferences preferences;	
	private Editor editor;	
	private ServiceConfig config;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//set static context
		Main.context = this;
		//initialize objects
		this.listViewManager = this.createListViewManager();
		this.srvReceiver = this.createServiceReceiver();
		this.config = new ServiceConfig();		
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.editor = this.preferences.edit();		
		//connect to service
		this.srvConnector = createServiceConnector();
		if (this.srvConnector.isServiceRunning()){
			this.srvConnector.connectToService();
		}			
		else{
			this.loadConfigFromPreferences();
			Toast.makeText(this, PLEASE_START, Toast.LENGTH_LONG).show();
		}
		//display content
		this.listViewManager.refresh();
	}

	@Override
	protected void onDestroy() {
		this.srvConnector.disconnectFromService();
		this.saveConfigToPreference();
		super.onDestroy();
	}

	/** go foreground */
	@Override
	protected void onResume() {
		this.srvReceiver.start();
		Logger.i("Main", "resume");
		super.onResume();
	}

	/** go background */
	@Override
	protected void onStop() {
		this.srvReceiver.stop();
		Logger.i("Main", "stop");
		super.onStop();
	}

	public void showDialogMaxSpeed() {		
		final int[] maxSpeedValues = new int[] { 60, 80, 120 };
		CharSequence[] TIME_OPTIONS = new CharSequence[maxSpeedValues.length];
		for (int i = 0; i < TIME_OPTIONS.length; i++) {
			TIME_OPTIONS[i] = maxSpeedValues[i] + " km/h";
		}
		int index = Util.indexOf(this.config.maxSpeed, maxSpeedValues);

		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(SELECT_MAX_SPEED);
		adb.setIcon(android.R.drawable.ic_menu_sort_by_size);
		adb.setSingleChoiceItems(TIME_OPTIONS, index, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Main.this.config.maxSpeed = maxSpeedValues[which];
				if (Main.this.srvConnector.isBound()){
					Main.this.service.setMaxSpeed(Main.this.config.maxSpeed);
				}
				dialog.cancel();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	public void showDialogName() {
		final EditText input = new EditText(this);
		input.setText(this.config.pseudo);
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(PROMPT_PSEUDO);
		adb.setIcon(android.R.drawable.ic_menu_info_details);
		adb.setView(input);
		adb.setPositiveButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Main.this.config.pseudo = input.getText().toString();
				if (Main.this.srvConnector.isBound()){
					Main.this.service.setPseudo(Main.this.config.pseudo);
				}
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	/** initialize listviewManager */
	private ListViewManager createListViewManager() {
		ListView list = (ListView) findViewById(android.R.id.list);
		ListViewManager lm = new ListViewManager(this, list,
				R.layout.listview_item, R.layout.listview_item_disabled);
		lm.addItem(new ItemStatus());
		lm.addItem(new ItemMaxSpeed());
		lm.disable();
		return lm;
	}

	private ServiceConnector<MyService> createServiceConnector() {

		return new ServiceConnector<MyService>(this, MyService.class,
				"com.safedriver.MyService") {

			public void onServiceConnected() {
				Main.this.service = this.getService();
				Main.this.config = Main.this.service.getConfig();
				Main.this.listViewManager.enable();
				Main.this.listViewManager.refresh();
			}

			public void onServiceDisconnected() {
				Main.this.listViewManager.disable();
				Main.this.listViewManager.refresh();
			}
		};
	}

	private BasicBroadcastReceiver createServiceReceiver() {
		return new BasicBroadcastReceiver(this, MyService.SERVICE_ACTION) {
			
			@Override
			public void onReceive(Context context, Intent intent) {				
				switch (intent.getFlags()) {
				case MyService.FLAG_CONFIG_CHANGED_INSIDE:
					Bundle bundle = intent.getExtras();
					ServiceConfig config = bundle.getParcelable("param");
					this.onConfigReceived(config);
					break;

				default: 
					this.onFlagReceived(intent.getFlags());
					break;
				}		
			}
			
			private void onConfigReceived(ServiceConfig config){
				//TODO process serviceConfig
			}
			
			private void onFlagReceived(int flag) {
				Logger.i("Main", "receive message with flag "+ flag);
				switch (flag) {
				case MyService.FLAG_SERVICE_STARTED:
					Main.this.srvConnector.connectToService();
					break;

				case MyService.FLAG_CONFIG_CHANGED:
					if (Main.this.srvConnector.isBound()) {
						Main.this.config = Main.this.service.getConfig();
						Main.this.listViewManager.refresh();
					}
					break;
				}
			}
			
		};
	}

	private void loadConfigFromPreferences(){		
		//fisrt use
		if(!this.preferences.contains("x")){
			 this.editor.putString("x", "1");			 
			 this.editor.commit();
			 this.showDialogName();
		}
		else{
			this.config.pseudo = this.preferences.getString("pseudo", "");
			this.config.maxSpeed = this.preferences.getInt("maxSpeed", 0);
		}
	}

	private void saveConfigToPreference(){
		this.editor.putInt("maxSpeed", this.config.maxSpeed);
		this.editor.putString("pseudo", this.config.pseudo);
		editor.commit();
	}

	class ItemStatus extends ListViewItem {
	
		@Override
		public void onCheckedChanged(boolean isChecked) {
			if (isChecked){
				Bundle bundle = new Bundle();
				bundle.putParcelable("config", config);
				Main.this.srvConnector.startService(bundle);
			}				
			else
				srvConnector.stopService();
		}
	
		@Override
		public void onImageLeftClick() {
			Main.this.showDialogName();
		}
	
		@Override
		public void onRefresh() {
			if (srvConnector.isBound()) {
				this.description = RUNNING;
				this.isChecked = true;
			} 
			else {
				this.description = STOPPED;
				this.isChecked = false;
			}
		}
		
		public void setup() {
			this.name = SERVICE_STATUS;
			this.isCheckable = true;
			this.imageLeft = android.R.drawable.ic_menu_info_details;
		}
	}

	class ItemMaxSpeed extends ListViewItem {
	
		@Override
		public void onItemClicked() {
			Main.this.showDialogMaxSpeed();
		}
	
		@Override
		public void onRefresh() {
			if(Main.this.srvConnector.isBound()){
				this.description = Main.this.config.maxSpeed + " km/h\n"
					+ CURRENT_SPEED + " " + Main.this.config.speed + " km/h";
			}
		}
	
		public void setup() {
			this.name = MAXSPEED;
			this.imageLeft = android.R.drawable.ic_menu_sort_by_size;
		}
	}

}