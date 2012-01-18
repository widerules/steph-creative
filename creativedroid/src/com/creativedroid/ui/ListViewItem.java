package com.creativedroid.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

/**
 * all methods of a listviewItem
 * 
 * @author Steph
 * 
 */
interface IListViewItem {

	/**
	 * Method that is launched on the item creation : set the initial value of
	 * components
	 */
	void setup();

	/** refresh the item */
	void onRefresh();

	/** callback on item click */
	void onItemClicked();

	/** callback on left image if exists */
	void onImageLeftClick();

	/** callback on right image if exists */
	void onImageRightClick();

	/** callback on item checked */
	void onCheckedChanged(boolean isChecked);

}

/**
 * Describes the item in a listView managed by a ListViewManager
 * 
 * @author Steph
 * 
 */
public abstract class ListViewItem implements IListViewItem, OnTouchListener,
		OnCheckedChangeListener {

	protected String name;
	protected String description;
	protected int imageLeft;
	protected int imageRight;
	protected boolean isCheckable;
	protected boolean isChecked;
	protected int specificItemLayout;
	private ListViewManager parent;

	public ListViewItem() {
		this.name = "";
		this.description = "";
		this.imageLeft = 0;
		this.imageRight = 0;
		this.isCheckable = false;
		this.isChecked = false;
		this.specificItemLayout = 0;
		setup();
	}

	public void setParent(ListViewManager parent) {
		this.parent = parent;
	}

	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	public void onItemClicked() {
		// TODO Auto-generated method stub

	}

	public void onImageLeftClick() {
		// TODO Auto-generated method stub

	}

	public void onImageRightClick() {
		// TODO Auto-generated method stub

	}

	public void onCheckedChanged(boolean isChecked) {
		// TODO Auto-generated method stub
	
	}

	public final void onCheckedChanged(CompoundButton view, boolean checked) {
		this.isChecked = checked;
		onCheckedChanged(checked);
	}

	public final boolean onTouch(View view, MotionEvent event) {
		if (view instanceof ImageView && parent.isEnabled()) {
			switch (view.getId()) {
			case android.R.id.icon1:
				onImageLeftClick();
				break;
			case android.R.id.icon2:
				onImageRightClick();
				break;
			}
		}
		return false;
	}

}
