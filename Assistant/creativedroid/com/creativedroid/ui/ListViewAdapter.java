package com.creativedroid.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapts the ListViewManager data to the ListView
 */
class ListViewAdapter extends BaseAdapter {

	/**
	 * The handler of the layout item Here we handle the pointer to the
	 * graphical components of the item layout
	 */
	class ItemHolder {
		TextView textName;
		TextView textDescription;
		ImageView imageLeft;
		ImageView imageRight;
		CheckBox checkBox;
		EditText editText;

		public ItemHolder(View convertView) {
			textName = (TextView) convertView.findViewById(android.R.id.text1);
			textDescription = (TextView) convertView.findViewById(android.R.id.text2);
			imageLeft = (ImageView) convertView.findViewById(android.R.id.icon1);
			imageRight = (ImageView) convertView.findViewById(android.R.id.icon2);
			checkBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
			editText = (EditText) convertView.findViewById(android.R.id.edit);
		}
		
	}
	
	/** animation to rotate image */
	public static void turnImage(ImageView image, int angle) {
		RotateAnimation anim = new RotateAnimation(0, angle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(1000);
		anim.setFillEnabled(true);
		anim.setFillAfter(true);
		image.startAnimation(anim);
	}
	
	private LayoutInflater myInflater;
	private int itemLayout;
	private ArrayList<ListViewItem> items;

	public ListViewAdapter(Context context,	ArrayList<ListViewItem> items, int layout) {
		this.setItemLayout(layout);
		this.myInflater = LayoutInflater.from(context);
		this.items = items;
	}

	public int getCount() {
		return this.items.size();
	}

	public ListViewItem getItem(int position) {
		return this.items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/** set the value of each component of the item layout */
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder holder;
		
		ListViewItem item = getItem(position);
		
		int layout = item.specificLayout != 0 ? item.specificLayout	: this.itemLayout;

		if (convertView == null) {
			convertView = this.myInflater.inflate(layout, null);
			holder = new ItemHolder(convertView);
			convertView.setTag(holder);
		} 
		else {
			holder = (ItemHolder) convertView.getTag();
		}

		holder.textName.setText(item.name);
		holder.textDescription.setText(item.description);

		if (item.imageLeft != 0) {
			holder.imageLeft.setImageResource(item.imageLeft);
			holder.imageLeft.setVisibility(View.VISIBLE);
			holder.imageLeft.setOnTouchListener(item);			
		} 
		else {
			holder.imageLeft.setVisibility(View.INVISIBLE);
		}

		if (item.isCheckable) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(item.isChecked);
			holder.checkBox.setOnCheckedChangeListener(item);
		} 
		else {
			holder.checkBox.setVisibility(View.INVISIBLE);
		}

		if (item.imageRight != 0) {
			holder.imageRight.setImageResource(item.imageRight);
			holder.imageRight.setVisibility(View.VISIBLE);
			holder.imageRight.setOnTouchListener(item);
		} 
		else {
			holder.imageRight.setVisibility(View.INVISIBLE);
		}
			
		return convertView;
	}

	public void setItemLayout(int layout) {
		this.itemLayout = layout;
		Logger.i("ListViewAdapter", "set layout " + layout);
	}
}
