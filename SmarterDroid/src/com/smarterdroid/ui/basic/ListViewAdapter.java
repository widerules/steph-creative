package com.smarterdroid.ui.basic;

import java.util.ArrayList;

import spacemode.android.R;
import spacemode.android.ui.MainActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this class adapts the  ListViewManager data to the ListView graphical
 * component of an activity
 */
public class ListViewAdapter extends BaseAdapter {
		
	private LayoutInflater myInflater;
	
	/** Data to display*/
	private ArrayList<ListViewItem> listViewItems;

	public ListViewAdapter(Context context, ArrayList<ListViewItem> listViewItems) {
		this.myInflater = LayoutInflater.from(context);
		this.listViewItems= listViewItems;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ItemLayoutHolder holder;
	
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.listview_item, null);
			holder = new ItemLayoutHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ItemLayoutHolder) convertView.getTag();
		}
	
		//set the value of each component of the item layout
		ListViewItem item = getItem(position);	
				
		holder.txtName.setText(item.name);
		holder.txtValue.setText(item.value);
		
		if(item.image!=0){
			holder.image.setImageResource(item.image);
			holder.image.setVisibility(View.VISIBLE);
			
			holder.image.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View arg0, MotionEvent arg1) {
					Toast.makeText(MainActivity.singleton, "Add", Toast.LENGTH_LONG).show();
					return true;
				}
			});
			
		   turn(holder.image);
			
		}
		else{
			holder.image.setVisibility(View.INVISIBLE);
		}
		
		if (item.checkable) {
			holder.check.setVisibility(View.VISIBLE);
			holder.check.setChecked(item.checked);
			holder.check.setOnCheckedChangeListener(item);
		} else {
			holder.check.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}
	
	
	public void turn(ImageView image)
	{
	    RotateAnimation anim = new RotateAnimation(0, 90,
	    		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);

	    anim.setInterpolator(new LinearInterpolator());
	    anim.setDuration(1000);
	    anim.setFillEnabled(true);

	    anim.setFillAfter(true);
	    image.startAnimation(anim);
	}

	/**
	 * The handler of the layout item 
	 * Here we handle the pointer to the graphical components
	 * of the item layout
	 */
	public class ItemLayoutHolder {
		TextView txtName;
		TextView txtValue;
		CheckBox check;
		ImageView image; 
		
		public ItemLayoutHolder( View convertView){
			txtName = (TextView) convertView.findViewById(R.id.textName);
			txtValue = (TextView) convertView.findViewById(R.id.textValue);
			check = (CheckBox) convertView.findViewById(R.id.check);
			image = (ImageView) convertView.findViewById(R.id.imageView1);
			
		}
	}
}
