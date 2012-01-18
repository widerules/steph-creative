package com.creativedroid.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Manage the listView by handling several ListViewItems
 */
public final class ListViewManager implements OnItemClickListener {

	private Context context;
	private ListView listView;
	private ListViewAdapter adapter;
	private ArrayList<ListViewItem> listViewItems;
	private int enabledItemLayout;
	private int disabledItemLayout;
	/** flag that specify whether items can be clicked or not */
	private boolean clickEnabled;

	public ListViewManager(Context context, ListView listView, int enabledItemLayout, int disabledItemLayout) {
		this.context = context;
		this.listView = listView;
		this.enabledItemLayout = enabledItemLayout;
		this.disabledItemLayout = disabledItemLayout;
		this.listViewItems = new ArrayList<ListViewItem>();
		this.listView.setOnItemClickListener(this);
		this.clickEnabled = true;
	}

	public void addItem(ListViewItem item) {
		this.listViewItems.add(item);
		item.setParent(this);
	}

	/** Update each item and redisplay */
	public void refresh() {
		for (ListViewItem item : listViewItems) {
			item.onRefresh();
		}

		int layout = this.clickEnabled? this.enabledItemLayout : this.disabledItemLayout;
		this.adapter = new ListViewAdapter(this.context, this.listViewItems, layout);
		this.listView.setAdapter(this.adapter);
		
//		if (adapter == null) {
//			int layout = clickEnabled? listViewItemLayoutId : listViewItemLayoutDisabledId;
//			adapter = new ListViewAdapter(context, listViewItems, layout);
//			listView.setAdapter(adapter);
//		} else {
//			adapter.notifyDataSetChanged();
//		}
		Logger.i("ListViewManager", "refreshed");
	}

	public void enable() {
		this.clickEnabled = true;
	}

	public void disable() {
		this.clickEnabled = false;
	}

	public boolean isEnabled() {
		return clickEnabled;
	}

	/** Click on item in the ListView Here only process click if item is clickEnabled */
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		if (this.clickEnabled) {
			//ListViewItem item = (ListViewItem) list.getAdapter().getItem(position);
			ListViewItem item = this.adapter.getItem(position);
			item.onItemClicked();
		}
	}
}
