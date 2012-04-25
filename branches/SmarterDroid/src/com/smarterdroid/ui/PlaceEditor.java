package com.smarterdroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.smarterdroid.R;
import com.smarterdroid.object.Place;
import creativedroid.ui.ListViewItem;
import creativedroid.ui.ListViewManager;
import creativedroid.ui.Util;

/**
 * Activity to edit a place
 * 
 * @author Steph
 * 
 */
public class PlaceEditor extends Activity implements Vocabulary {

	private final CharSequence[] dimOptions = new CharSequence[] { ROOM, HOUSE,
			PARK };
	private final int[] dimValues = new int[] { 10, 50, 500 };

	private Place place;
	private ListViewManager listViewManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);

		findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				closeWithPlaceAsResult();
			}
		});

		findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View arg0) {
						finish();
					}
				});
		
		place = getPlaceFromLauncherIntent();
		
		listViewManager = getListViewManager();
		listViewManager.refresh();
	}

	/**
	 * get place from launcher intent and set the activity title ( create or
	 * edit a place)
	 */
	private Place getPlaceFromLauncherIntent() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			/**
			 * place received, so edit
			 */
			setTitle(EDIT_PLACE);
			return bundle.getParcelable("place");
		} else {
			/**
			 * no zone received, so create new zone Place the zone at the
			 * current location provided by geoService
			 * */
			setTitle(ADD_PLACE);
			return new Place(-1, "?",
					Pool.config.currentLocation.getLatitude(),
					Pool.config.currentLocation.getLongitude(), dimValues[0],
					false);
		}
	}

	private ListViewManager getListViewManager() {
		ListView list = (ListView) findViewById(android.R.id.list);
		ListViewManager l = new ListViewManager(this, list,
				R.layout.listview_item, 0);
		l.addItem(new ItemName());
		l.addItem(new ItemLocation());
		l.addItem(new ItemDimension());
		l.addItem(new ItemAudioMode());
		return l;
	}

	private void closeWithPlaceAsResult() {
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelable("place", place);
		resultIntent.putExtras(bundle);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	class ItemName extends ListViewItem {

		public void setup() {
			name = NAME;
			description = ENTER_NAME;
			imageLeft = android.R.drawable.ic_menu_edit;
		}

		@Override
		public void onRefresh() {
			description = place.description;
		}

		@Override
		public void onItemClicked() {
			showDialogName();
		}
	}

	class ItemLocation extends ListViewItem {

		public void setup() {
			name = LOCATION;
			imageLeft = android.R.drawable.ic_menu_mapmode;
		}

		@Override
		public void onRefresh() {
			description = place.latitude + " " + place.longitude;
		}

		@Override
		public void onItemClicked() {
			gotoGoogleMap();
		}
	}

	class ItemDimension extends ListViewItem {

		public void setup() {
			name = DIMENSION;
			imageLeft = android.R.drawable.ic_menu_crop;
		}

		@Override
		public void onRefresh() {
			int index = Util.indexOf(place.radius, dimValues);
			if (index != -1) {
				description = "" + dimOptions[index];
			}
			super.onRefresh();
		}

		@Override
		public void onItemClicked() {
			showDialogDimension();
		}

	}

	class ItemAudioMode extends ListViewItem {

		public void setup() {
			name = QUIET;
		}

		@Override
		public void onRefresh() {
			if (place.quiet) {
				imageLeft = android.R.drawable.ic_lock_silent_mode;
				description = SILENT;
			} else {
				imageLeft = android.R.drawable.ic_lock_silent_mode_off;
				description = DISABLED;
			}
		}

		@Override
		public void onItemClicked() {
			place.quiet = !place.quiet;
			listViewManager.refresh();
		}
	}

	public void showDialogName() {
		final EditText input = new EditText(this);
		input.setText(place.description);

		AlertDialog.Builder adb = new AlertDialog.Builder(PlaceEditor.this);
		adb.setTitle(ENTER_NAME);
		adb.setIcon(android.R.drawable.ic_menu_info_details);
		adb.setView(input);
		adb.setPositiveButton(OK, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				place.description = input.getText().toString();
				listViewManager.refresh();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

	public void gotoGoogleMap() {
		// TODO Auto-generated method stub

		Toast.makeText(PlaceEditor.this,
				"Under construction set place on GoogleMap ",
				Toast.LENGTH_SHORT).show();

	}

	public void showDialogDimension() {
		int index = Util.indexOf(place.radius, dimValues);

		AlertDialog.Builder adb = new AlertDialog.Builder(PlaceEditor.this);
		adb.setTitle(SELECT_DIMENSION);
		adb.setIcon(android.R.drawable.ic_menu_crop);
		adb.setSingleChoiceItems(dimOptions, index, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int dim = dimValues[which];
				place.radius = dim;
				listViewManager.refresh();
				dialog.cancel();
			}
		});
		adb.setNegativeButton(CANCEL, null);
		adb.show();
	}

}
