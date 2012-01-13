package com.smarterdroid.ui.basic;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This class describes the item in a listView 
 * managed by a ListViewManager
 * Implemented methods are:
 * - onClick()
 * - onCheckedChanged()
 * - setup()
 * - update()
 */
public abstract class ListViewItem implements OnCheckedChangeListener{
	
	protected String name;
	protected String value = "";
	protected int image = 0;
	protected boolean checkable = false;
	protected boolean checked = false;
	
	public ListViewItem(){
		setup(); //here set name 
	}
	
	/**
	 * Method  that is launched on the item creation
	 * especially set the name by the method setName()
	 */
	protected abstract void setup();
	
	/**
	 * Method  that is launched on the item update
	 * especially set the value by the method setValue()
	 * and the checked by the method setChecked()
	 */
	public abstract void update();
	
	/**
	 * Callback when item is clicked
	 */
	public void onClick(){}
		
	/**
	 * Callback of the checkBox element of the item
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {}
}
