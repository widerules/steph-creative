package com.creativedroid.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

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
	protected int specificLayout;
	private ListViewManager parent;

	public ListViewItem() {
		this.name = "";
		this.description = "";
		this.imageLeft = 0;
		this.imageRight = 0;
		this.isCheckable = false;
		this.isChecked = false;
		this.specificLayout = 0;
		this.setup();
	}

	public void onCheckedChanged(boolean isChecked) {
	}

	public final void onCheckedChanged(CompoundButton view, boolean checked) {
		this.isChecked = checked;
		this.onCheckedChanged(this.isChecked);
	}

	public void onImageLeftClick() {
	}

	public void onImageRightClick() {
	}

	public void onItemClicked() {
	}

	public void onRefresh() {
	}

	public final boolean onTouch(View view, MotionEvent event) {
		if (view instanceof ImageView && this.parent.isEnabled()) {
			switch (view.getId()) {
			case android.R.id.icon1:
				this.onImageLeftClick();
				break;
			case android.R.id.icon2:
				this.onImageRightClick();
				break;
			}
		}
		return false;
	}

	public void setParent(ListViewManager parent) {
		this.parent = parent;
	}

}

/**
 * all methods of a listviewItem
 * 
 * @author Steph
 * 
 */
interface IListViewItem {

	/** callback on item checked */
	void onCheckedChanged(boolean isChecked);

	/** callback on left image if exists */
	void onImageLeftClick();

	/** callback on right image if exists */
	void onImageRightClick();

	/** callback on item click */
	void onItemClicked();

	/** refresh the item */
	void onRefresh();

	/**
	 * Method that is launched on the item creation 
	 * set the initial value of components : name, image, isCheckable, specificLayout
	 */
	void setup();

}
