package com.creativedroid.ui;

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
	private int listViewItemLayoutId;
	private int listViewItemLayoutDisabledId;

	/**
	 * flag that specify whether items can be clicked or not By default set to
	 * FALSE
	 * */
	private boolean clickEnabled;

	public ListViewManager(Context context, ListView listView, int listViewItemLayoutId, int listViewItemLayoutDisabledId) {
		this.context = context;
		this.listView = listView;
		this.listViewItemLayoutId = listViewItemLayoutId;
		this.listViewItemLayoutDisabledId = listViewItemLayoutDisabledId;
		listViewItems = new ArrayList<ListViewItem>();
		this.listView.setOnItemClickListener(this);
		clickEnabled = true;
	}

	public void addItem(ListViewItem item) {
		listViewItems.add(item);
		item.setParent(this);
	}

	/** Update each item and redisplay */
	public void refresh() {
		for (ListViewItem item : listViewItems) {
			item.onRefresh();
		}

		int layout = clickEnabled? listViewItemLayoutId : listViewItemLayoutDisabledId;
		adapter = new ListViewAdapter(context, listViewItems, layout);
		listView.setAdapter(adapter);
		
//		if (adapter == null) {
//			int layout = clickEnabled? listViewItemLayoutId : listViewItemLayoutDisabledId;
//			adapter = new ListViewAdapter(context, listViewItems, layout);
//			listView.setAdapter(adapter);
//		} else {
//			adapter.notifyDataSetChanged();
//		}
		Log.i("***ListViewManager", "refreshed");
	}

	public void enable() {
		clickEnabled = true;
//		if (adapter != null)
//			adapter.setItemLayout(listViewItemLayoutId);
	}

	public void disable() {
		clickEnabled = false;
//		if (adapter != null)
//			adapter.setItemLayout(listViewItemLayoutDisabledId);
	}

	public boolean isEnabled() {
		return clickEnabled;
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
