package creativedroid.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Manage the listView by handling several ListViewItems
 */
public class ListViewManager implements OnItemClickListener {

	private Context context;
	private ListView listView;
	private ListViewAdapter adapter;
	private ArrayList<ListViewItem> listViewItems;

	/**
	 * flag that specify whether items can be clicked or not By default set to
	 * FALSE
	 * */
	private boolean clickEnabled;

	public ListViewManager(Context context, ListView listView) {
		this.context = context;
		this.listView = listView;
		listViewItems = new ArrayList<ListViewItem>();
		this.listView.setOnItemClickListener(this);
		clickEnabled = false;
	}

	public void addItem(ListViewItem item) {
		listViewItems.add(item);
	}

	/** Update each item and redisplay */
	public void refresh() {
		for (ListViewItem item : listViewItems) {
			item.onRefresh();
		}

		if (adapter == null) {
			adapter = new ListViewAdapter(context, listViewItems);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		Log.i("***ListViewManager", "refreshed");
	}

	public void enable() {
		clickEnabled = true;
	}

	public void disable() {
		clickEnabled = false;
	}

	/**
	 * Click on item in the ListView Here only process click if item is
	 * clickEnabled
	 */
	public void onItemClick(AdapterView<?> list, View view, int position,
			long id) {
		if (clickEnabled) {
			ListViewItem item = (ListViewItem) list.getAdapter().getItem(position);
			item.onItemClicked();
		}
	}
}
