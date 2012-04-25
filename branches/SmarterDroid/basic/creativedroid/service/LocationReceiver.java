package creativedroid.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

interface ILocationReceiver {

	void onNewLocation(Location location);

	void setUpdateInterval(long interval);

	void setProvider(String provider);
}

/** Location listener 
 * Here we receive location updates with certain accuracy */
public abstract class LocationReceiver implements LocationListener, ILocationReceiver, IStartable{
	
	private LocationManager lManager;
	private String provider;
	private long updateInterval;
	private boolean running;
		
	public LocationReceiver(Context context){
		lManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);	
	}
	
	public void start(){
		if(!running){
			lManager.requestLocationUpdates(provider, updateInterval, 0, this);
			Log.i("***LocationReceiver", "requestLocationUpdates "+ provider+ " "+updateInterval);
			Location lastLocation = lManager.getLastKnownLocation(provider);
			if(lastLocation!=null){
				onNewLocation(lastLocation);
			}
			running = true;
		}		
	}
	
	public void stop() {
		if(running){
			lManager.removeUpdates(this);
			running = false;
		}		
	}
	
	public boolean isRunning() {
		return running;
	}

	public final void onLocationChanged(Location location) {
		//TODO set accuracy
		Log.i("***LocationReceiver", "onLocationChanged "+location.getLatitude()+" "+location.getLongitude());
		onNewLocation(location);
	}

	public void setUpdateInterval(long interval) {
		updateInterval = interval;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
		
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
