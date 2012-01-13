package com.smarterdroid.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A location tagged by the user or got from external services Its is obviously
 * a circle area
 * 
 * @author Steph
 * 
 */
public class Place implements Parcelable {

	public int id;
	/** Common name of the place or simply its address */
	public String description;
	public double latitude;
	public double longitude;
	/** Radius in meter */
	public float radius;
	/** Go on vibrate when in this place */
	public boolean quiet;

	public Place(int id, String description, double latitude, double longitude,
			float radius, boolean quiet) {
		this.id = id;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
		this.quiet = quiet;
	}

	public Place() {
		this(-1, "", 0, 0, 0, false);
	}

	public int describeContents() {
		return 0;
	}

	public Place(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		public Place[] newArray(int size) {
			return new Place[size];
		}
	};

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(description);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeFloat(radius);
		dest.writeInt(quiet ? 1 : 0);
	}

	public void readFromParcel(Parcel in) {
		id = in.readInt();
		description = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
		radius = in.readFloat();
		quiet = in.readInt() == 1;
	}

}
