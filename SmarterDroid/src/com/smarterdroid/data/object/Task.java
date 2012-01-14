package com.smarterdroid.data.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple task to do when user enter or exit a place
 * 
 * @author Steph
 * 
 */
public class Task implements Parcelable {

	public int id;
	/** One line to describe the task */
	public String description;
	/** Place where task should be done */
	public Place place;
	/** remind task when enter the place. If false remind on exit */
	public boolean activeWhenEnterPlace;

	public Task(int id, String description, Place place,
			boolean activeWhenEnterPlace) {
		this.id = id;
		this.description = description;
		this.place = place;
		this.activeWhenEnterPlace = activeWhenEnterPlace;
	}
	
	public Task(){
		this(-1, "", null, false);
	}

	public int describeContents() {
		return 0;
	}

	public Task(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
		public Task createFromParcel(Parcel in) {
			return new Task(in);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(description);
		dest.writeInt(activeWhenEnterPlace ? 1 : 0);
		
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
		activeWhenEnterPlace = in.readInt() == 1;
		
		if (in.readInt() == 1) {
			place = new Place(in);
		}
	}

}
