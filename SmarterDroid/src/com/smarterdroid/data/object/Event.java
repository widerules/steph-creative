package com.smarterdroid.data.object;

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An event specified in time and that you want to be prevented before it
 * happens Can be shared to another friends Date is supposed not null
 * 
 * @author Steph
 * 
 */
public class Event implements Parcelable {

	public int id;
	public String description;
	/** date of the event */
	public Date date;
	/** prevent time in minutes before starting the event */
	public int preventTime;
	/** place where the event will happen */
	public Place place;
	/** flag event shared with friend */
	public boolean shared;
	/** friend from who you got the event */
	public String source;

	public Event(int id, String description, Date date, int preventTime,
			Place place, boolean shared, String source) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.preventTime = preventTime;
		this.place = place;
		this.shared = shared;
		this.source = source;
	}

	public Event() {
		this(-1, "", null, 0, null, false, "");
	}

	public int describeContents() {
		return 0;
	}

	public Event(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(description);
		dest.writeInt(preventTime);
		dest.writeInt(shared ? 1 : 0);
		dest.writeString(source);

		if (date != null) {
			dest.writeInt(1);
			writeDateToParcel(dest);
		} else {
			dest.writeInt(0);
		}

		if (place != null) {
			dest.writeInt(1);
			place.writeToParcel(dest, flags);
		} else {
			dest.writeInt(0);
		}

	}

	public void readFromParcel(Parcel in) {
		id = in.readInt();
		description = in.readString();
		preventTime = in.readInt();
		shared = in.readInt() == 1;
		source = in.readString();

		if (in.readInt() == 1) {
			readDateFromParcel(in);
		}

		if (in.readInt() == 1) {
			place = new Place(in);
		}
	}

	private void writeDateToParcel(Parcel dest) {
		dest.writeInt(date.getDay());
		dest.writeInt(date.getMonth());
		dest.writeInt(date.getYear());
		dest.writeInt(date.getHours());
		dest.writeInt(date.getMinutes());
	}

	private void readDateFromParcel(Parcel in) {
		date = new Date();
		date.setDate(in.readInt());
		date.setMonth(in.readInt());
		date.setYear(in.readInt());
		date.setHours(in.readInt());
		date.setMinutes(in.readInt());
	}

}
