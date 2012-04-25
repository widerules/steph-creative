package com.smarterdroid.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.smarterdroid.R;
import com.smarterdroid.object.Place;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class Places extends ListActivity implements Vocabulary,
		OnItemClickListener {

	private static final String COL_ID = "_id";
	private static final String COL_NAME = "name";
	private static final String COL_QUIET = "quiet";

	private static ArrayList<HashMap<String, String>> PlacesToHashMaps(
			ArrayList<Place> places) {
		ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		for (Place place : places) {
			map = new HashMap<String, String>();
			map.put(COL_ID, "" + place.id);
			map.put(COL_NAME, place.description);
			map.put(COL_QUIET, place.quiet ? SILENT : NORMAL);
			array.add(map);
		}
		return array;
	}

	private final int EDIT_PLACE_REQUEST_CODE = 200;
	private final int ADD_PLACE_REQUEST_CODE = 201;
	private ArrayList<HashMap<String, String>> listItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getListView().setOnCreateContextMenuListener(this);
		getListView().setOnItemClickListener(this);

		dataBind();
	}

	private void dataBind() {
		ArrayList<Place> places = Pool.placeAdapter.selectAll();
		listItem = PlacesToHashMaps(places);

		SimpleAdapter adapter = new SimpleAdapter(this, listItem,
				R.layout.place_item, new String[] { COL_NAME, COL_QUIET },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuAdd = menu.add(0, 200, 0, ADD);
		menuAdd.setIcon(android.R.drawable.ic_menu_add);
		MenuItem menuDeleteAll = menu.add(0, 201, 0, DELETE_ALL);
		menuDeleteAll.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 200:
			goToAddNewPlace();
			break;
		case 201:
			Pool.placeAdapter.deleteAll();
			dataBind();
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(ACTION);
		menu.setHeaderIcon(android.R.drawable.ic_menu_edit);
		menu.add(0, 102, 0, EDIT);
		menu.add(0, 101, 1, DELETE);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		HashMap<String, String> map = listItem.get(info.position);
		int id = Integer.parseInt(map.get(COL_ID));

		switch (item.getItemId()) {
		case 101:
			Pool.placeAdapter.deleteWithID(id);
			dataBind();
			break;

		case 102:
			// go to place editor
			Place place = Pool.placeAdapter.selectWithID(id);
			goToEditPlace(place);
			break;
		}

		return true;
	}

	private void goToAddNewPlace() {
		Intent intent = new Intent(this, PlaceEditor.class);
		startActivityForResult(intent, ADD_PLACE_REQUEST_CODE);
	}

	/**
	 * Show PlaceEditor to edit a place
	 * 
	 * @param zone
	 */
	private void goToEditPlace(Place place) {
		Intent intent = new Intent(this, PlaceEditor.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("place", place);
		intent.putExtras(bundle);
		startActivityForResult(intent, EDIT_PLACE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			Place place = bundle.getParcelable("place");
			switch (requestCode) {
			case EDIT_PLACE_REQUEST_CODE:
				Pool.placeAdapter.update(place, place.id);
				break;

			case ADD_PLACE_REQUEST_CODE:
				Pool.placeAdapter.insert(place);
				break;
			}
			dataBind();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// Cursor cursor = (Cursor)l.getAdapter().getItem(position);
		// Place place = Pool.placeAdapter.selectWithID(id);
		// goToEditPlace(place);
	}

}
