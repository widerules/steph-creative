package creativedroid.ui;

import java.util.ArrayList;

import com.smarterdroid.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapts the ListViewManager data to the ListView
 */
public class ListViewAdapter extends BaseAdapter {

	private LayoutInflater myInflater;

	/** Data to display */
	private ArrayList<ListViewItem> listViewItems;

	public ListViewAdapter(Context context,
			ArrayList<ListViewItem> listViewItems) {
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

		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.listview_item, null);
			holder = new ItemLayoutHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ItemLayoutHolder) convertView.getTag();
		}

		ListViewItem item = getItem(position);

		holder.textName.setText(item.name);
		holder.textDescription.setText(item.description);

		if (item.imageLeft != 0) {
			holder.imageLeft.setImageResource(item.imageLeft);
			holder.imageLeft.setVisibility(View.VISIBLE);
			holder.imageLeft.setOnTouchListener(item);

			// artifice rotate image to -90
//			if (item.imageLeft == android.R.drawable.ic_menu_more) {
//				turn(holder.imageLeft, -90);
//			}

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

	public void turn(ImageView image, int angle) {
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

		public ItemLayoutHolder(View convertView) {
			textName = (TextView) convertView.findViewById(R.id.textName);
			textDescription = (TextView) convertView
					.findViewById(R.id.textDescription);
			imageLeft = (ImageView) convertView.findViewById(R.id.imageLeft);
			imageRight = (ImageView) convertView.findViewById(R.id.imageRight);
			checkBox = (CheckBox) convertView
					.findViewById(android.R.id.checkbox);
		}
	}
}
