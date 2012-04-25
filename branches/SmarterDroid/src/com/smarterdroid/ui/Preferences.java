package com.smarterdroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.smarterdroid.R;
import com.smarterdroid.service.MyServiceReceiver;

import creativedroid.ui.ListViewItem;
import creativedroid.ui.ListViewManager;
import creativedroid.ui.Util;

/**
 * Activity for setting service configurations
 * 
 * @author Steph
 * 
 */
public class Preferences extends Activity implements Vocabulary{

	private ListViewManager listViewManager;
	private MyServiceReceiver srvReceiver;
	
	private MyServiceReceiver getMyServiceReceiver(){
		return new MyServiceReceiver(this) {
			
			@Override
			public void onFlagReceived(int flag) {
				Log.i("***Preferences", "receive message from service");
				Pool.config = Pool.service.getConfig();
				listViewManager.refresh();
			}
		};
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle(PREFERENCES);
		
		initListView();
		srvReceiver = getMyServiceReceiver();	
		
		listViewManager.refresh();		
	}
	
	@Override
	protected void onResume() {
		srvReceiver.start();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		srvReceiver.stop();
		super.onDestroy();
	}
	
	/** initialize listviewManager */
	private void initListView() {
		ListView list = (ListView) findViewById(android.R.id.list);
		listViewManager = new ListViewManager(this, list,
				R.layout.listview_item, 0);
		listViewManager.addItem(new ItemPseudo());
		listViewManager.addItem(new ItemLocation());
		listViewManager.addItem(new ItemUpdate());
		listViewManager.addItem(new ItemMaxSpeed());
	}
	
	class ItemPseudo extends ListViewItem{

		public void setup() {
			name = PSEUDO;				
			imageLeft = android.R.drawable.ic_menu_info_details;
		}	
		
		@Override
		public void onRefresh() {
			description = YOUR_PSEUDO+" "+Pool.config.pseudo;
		}
		
		@Override
		public void onItemClicked() {
			showDialogSetPseudo();	
		}
	}
	
	class ItemLocation extends ListViewItem{

		public void setup() {
			name = PROVIDER;
			imageLeft = android.R.drawable.ic_menu_mylocation;
		}
		
		@Override
		public void onRefresh() {
			description = Pool.config.provider + "\n"
					+ Pool.config.currentLocation.getLatitude() + "  "
					+ Pool.config.currentLocation.getLongitude();
		}

		@Override
		public void onItemClicked() {
			showAndroidLocationSettings();
		}
	}
	
	class ItemUpdate extends ListViewItem {

		public void setup() {
			name = UPDATE_INETVAL;
			imageLeft = android.R.drawable.ic_menu_recent_history;
		}

		@Override
		public void onRefresh() {
			description = (Pool.config.updateMinTime / 1000) + " sec";
		}

		@Override
		public void onItemClicked() {
			showDialogUpdateInterval();
		}
	}
	
	class ItemMaxSpeed extends ListViewItem{
		
		public void setup() {
			name = MAXSPEED;
			imageLeft = android.R.drawable.ic_menu_sort_by_size;
		}
		
		@Override
		public void onRefresh() {
			description = (int)Pool.config.maxSpeed + " km/h\n"
					+CURRENT_SPEED+" "+Pool.config.speed+" km/h";
		}
		
		@Override
		public void onItemClicked() {
			showDialogMaxSpeed();
		}
	}

	public void showDialogUpdateInterval() {
		final long[] updateTimeValues = new long[] { 1000, 10000, 60000 };
		CharSequence[] timeOptions = new CharSequence[updateTimeValues.length];
		for (int i = 0; i < timeOptions.length; i++) {
			timeOptions[i] = (updateTimeValues[i] / 1000) + " sec";
		}
		int index = Util.indexOf(Pool.config.updateMinTime, updateTimeValues);
		
		AlertDialog.Builder adb = new AlertDialog.Builder(Preferences.this);
		adb.setTitle(SELECT_UPDATE_TIME);		
		adb.setIcon(android.R.drawable.ic_menu_recent_history);
		adb.setSingleChoiceItems(timeOptions, index, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				long interval = updateTimeValues[which];
				Pool.service.setUpdateInterval(interval);
				dialog.cancel();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	public void showAndroidLocationSettings() {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	public void showDialogSetPseudo() {		
		final EditText input = new EditText(this);
		input.setText(Pool.config.pseudo);
		
		AlertDialog.Builder adb = new AlertDialog.Builder(Preferences.this);
		adb.setTitle(PROMPT_PSEUDO);
		adb.setIcon(android.R.drawable.ic_menu_info_details);
		adb.setView(input);
		adb.setPositiveButton(OK, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  Pool.service.setPseudo(value);
		  }
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	public void showDialogMaxSpeed() {
		final float[] maxSpeedValues = new float[] { 60, 80, 120 };
		CharSequence[] TIME_OPTIONS = new CharSequence[maxSpeedValues.length];
		for (int i = 0; i < TIME_OPTIONS.length; i++) {
			TIME_OPTIONS[i] = (int)maxSpeedValues[i] + " km/h";
		}
		int index = Util.indexOf(Pool.config.maxSpeed, maxSpeedValues);
		
		AlertDialog.Builder adb = new AlertDialog.Builder(Preferences.this);
		adb.setTitle(SELECT_MAX_SPEED);	
		adb.setIcon(android.R.drawable.ic_menu_sort_by_size);
		adb.setSingleChoiceItems(TIME_OPTIONS, index, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				float maxSpeed = maxSpeedValues[which];
				Pool.service.setMaxSpeed(maxSpeed);
				dialog.cancel();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}
	
	


}
