package com.safedriver;

import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describe parameters and enabled functionalities of MyService
 * 
 * @author Steph
 * 
 */
public class ServiceConfig implements Parcelable {

	public String provider;
	/** Current speed in km/h */
	public float speed;
	/** Max speed in km/h*/
	public float maxSpeed;
	public Location currentLocation;
	/** minimum location update interval in milliseconds */
	public long updateMinTime;
	public String pseudo;

	public boolean enableSpeedLimit;
	public boolean enableSMSToSpeech;

	/** Coordinates of FST de Tanger Departement Informatique */
	private final double FST_LATITUDE = 35.736615;
	private final double FST_LONGITUDE = -5.89593;

	public ServiceConfig() {
		provider = LocationManager.GPS_PROVIDER;
		updateMinTime = 1000;
		maxSpeed = 60;
		pseudo = "User";
		currentLocation = new Location(provider);
		currentLocation.setLatitude(FST_LATITUDE);
		currentLocation.setLongitude(FST_LONGITUDE);
	}

	public int describeContents() {
		return 0;
	}

	public ServiceConfig(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<ServiceConfig> CREATOR = new Parcelable.Creator<ServiceConfig>() {
		public ServiceConfig createFromParcel(Parcel in) {
			return new ServiceConfig(in);
		}

		public ServiceConfig[] newArray(int size) {
			return new ServiceConfig[size];
		}
	};

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(provider);
		dest.writeFloat(speed);
		dest.writeFloat(maxSpeed);
		dest.writeString(pseudo);
		dest.writeLong(updateMinTime);
		dest.writeDouble(currentLocation.getLatitude());
		dest.writeDouble(currentLocation.getLongitude());
		dest.writeInt(enableSpeedLimit ? 1 : 0);
		dest.writeInt(enableSMSToSpeech ? 1 : 0);
	}

	public void readFromParcel(Parcel in) {
		provider = in.readString();
		speed = in.readFloat();
		maxSpeed = in.readFloat();
		pseudo = in.readString();
		updateMinTime = in.readLong();
		currentLocation = new Location(provider);
		currentLocation.setLatitude(in.readDouble());
		currentLocation.setLongitude(in.readDouble());
		enableSpeedLimit = in.readInt() == 1;
		enableSMSToSpeech = in.readInt() == 1;
	}

}
