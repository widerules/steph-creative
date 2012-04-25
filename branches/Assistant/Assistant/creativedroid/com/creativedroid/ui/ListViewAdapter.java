package com.creativedroid.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
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
public class ListViewAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private int listViewItemLayout;
	/** Data to display */
	private ArrayList<ListViewItem> listViewItems;
	
	public void setItemLayout(int l){
		this.listViewItemLayout = l;
		Log.i("***Adapter", "set layout "+l);
	}

	public ListViewAdapter(Context context,
			ArrayList<ListViewItem> listViewItems, int l) {
		setItemLayout(l);
		this.myInflater = LayoutInflater.from(context);
		this.listViewItems = listViewItems;
	}

	public int getCount() {
		return this.listViewItems.size();
	}

	public ListViewItem getItem(int position) {
		return this.listViewItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/** set the value of each component of the item layout */
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemLayoutHolder holder;
		
		ListViewItem item = getItem(position);
		
		int layout = item.specificItemLayout != 0 ? item.specificItemLayout
				: listViewItemLayout;

		if (convertView == null) {
			convertView = myInflater.inflate(layout, null);
			holder = new ItemLayoutHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ItemLayoutHolder) convertView.getTag();
		}

		holder.textName.setText(item.name);
		holder.textDescription.setText(item.description);

		if (item.imageLeft != 0) {
			holder.imageLeft.setImageResource(item.imageLeft);
			holder.imageLeft.setVisibility(View.VISIBLE);
			holder.imageLeft.setOnTouchListener(item);			
		} else {
			holder.imageLeft.setVisibility(View.INVISIBLE);
		}

		if (item.isCheckable) {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(item.isChecked);
			holder.checkBox.setOnCheckedChangeListener(item);
		} else {
			holder.checkBox.setVisibility(View.INVISIBLE);
		}

		if (item.imageRight != 0) {
			holder.imageRight.setImageResource(item.imageRight);
			holder.imageRight.setVisibility(View.VISIBLE);
			holder.imageRight.setOnTouchListener(item);

			// artifice rotate image to -90
//			if (item.imageRight == android.R.drawable.ic_menu_more) {
//				turn(holder.imageRight, -90);
//			}

		} else {
			holder.imageRight.setVisibility(View.INVISIBLE);
		}
			
		return convertView;
	}

	public static void turnImage(ImageView image, int angle) {
		RotateAnimation anim = new RotateAnimation(0, angle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(1000);
		anim.setFillEnabled(true);
		anim.setFillAfter(true);
		image.startAnimation(anim);
	}

	/**
	 * The handler of the layout item Here we handle the pointer to the
	 * graphical components of the item layout
	 */
	public class ItemLayoutHolder {
		TextView textName;
		TextView textDescription;
		ImageView imageLeft;
		ImageView imageRight;
		CheckBox checkBox;
		EditText editText;

		public ItemLayoutHolder(View convertView) {
			textName = (TextView) convertView.findViewById(android.R.id.text1);
			textDescription = (TextView) convertView
					.findViewById(android.R.id.text2);
			imageLeft = (ImageView) convertView.findViewById(android.R.id.icon1);
			imageRight = (ImageView) convertView.findViewById(android.R.id.icon2);
			checkBox = (CheckBox) convertView
					.findViewById(android.R.id.checkbox);
			editText = (EditText) convertView.findViewById(android.R.id.edit);
		}
		
	}
}
