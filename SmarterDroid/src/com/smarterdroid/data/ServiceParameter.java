package com.smarterdroid.data;

import android.location.Location;
import android.location.LocationManager;

/**
 * DEscribe enabled functionalities and current data
 * 
 * @author Steph
 * 
 */
public class ServiceParameter {

	public String provider;
	public float speed;
	public Location currentLocation;
	public boolean enableSpeedLimit;
	public boolean enableEventNotification;
	public boolean enableQuietPlace;
	/** minimum location update interval in milliseconds */
	public long updateMinTime;
	
	/** Coordinates of FST de Tanger Departement Informatique*/
	private final double FST_LATITUDE		= 35.736615;
	private final double FST_LONGITUDE 		= -5.89593;
	
	public ServiceParameter(){
		provider = LocationManager.GPS_PROVIDER;
		speed = 0;
		currentLocation = new Location(provider);
		currentLocation.setLatitude(FST_LATITUDE);
		currentLocation.setLongitude(FST_LONGITUDE);
	}
}
