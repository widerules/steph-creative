package com.creativedroid.service;

import com.creativedroid.ui.Logger;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/** Location listener 
 * Here we receive location updates with certain accuracy 
 * Default provider is GPS */
public abstract class LocationReceiver implements LocationListener, ILocationReceiver, IStartable{
	
	private LocationManager lManager;
	private String provider;
	private long updateInterval;
	private boolean listening;
	private Location currentLocation;
		
	public LocationReceiver(Context context){
		this.lManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		this.provider = LocationManager.GPS_PROVIDER;
	}
	
	public Location getLocation(){
		return this.currentLocation;
	}
	
	public String getProvider(){
		return this.provider;
	}
		
	public boolean isRunning() {
		return this.listening;
	}
		
	/** callback from LocationListner */
	public final void onLocationChanged(Location location) {
		//TODO set accuracy
		Logger.i("LocationReceiver", "onReceiveLocation " 
				+ location.getLatitude() + " "	+ location.getLongitude());
		this.currentLocation = location;
		this.onLocationReceived(location);
	}
	
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Logger.i("LocationReceiver", "provider disabled " + provider);
		this.provider = null;	
		if(isRunning()){
			stop();			
		}		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Logger.i("LocationReceiver", "provider enabled " + provider);
		this.provider = provider;
	}

	public void onStatusChanged(String provider, int status, Bundle extras){
	}
		
	public void setUpdateInterval(long interval) {
		this.updateInterval = interval;
	}
	
	public void start(){
		if(!this.listening){
			if(this.lManager.isProviderEnabled(this.provider)){
				this.lManager.requestLocationUpdates(this.provider, this.updateInterval, 0, this);
				Logger.i("LocationReceiver", "requestLocationUpdates "
						+ this.provider + " " + this.updateInterval);
				this.currentLocation = lManager.getLastKnownLocation(this.provider);
				this.listening = true;
			}
			else{
				Logger.i("LocationReceiver", "unable to listen location; provider " 
						+ this.provider	+ " disabled");
			}			
		}
	}
	
	public void stop() {
		if(this.listening){
			this.lManager.removeUpdates(this);
			this.listening = false;
			Logger.i("LocationReceiver", "stop listening location updates");
		}		
	}
}

interface ILocationReceiver {

	/** get the current location */
	Location getLocation();

	/** get the current provider */
	String getProvider();
	
	/** callback receive new location */
	void onLocationReceived(Location location);
	
	/** set the update interval in milliseconds */
	void setUpdateInterval(long interval);
}
