package com.smarterdroid.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.smarterdroid.object.Place;
import com.smarterdroid.object.ServiceConfig;
import com.smarterdroid.ui.Vocabulary;

import creativedroid.service.LocationReceiver;
import creativedroid.service.SMSReceiver;
import creativedroid.service.SpeedLimiter;
import creativedroid.service.Voice;

/**
 * Implementation of service functions
 * @author Steph
 *
 */
public abstract class MyServiceCore extends Service implements IMyService, Vocabulary {
		
	
	
	protected ServiceConfig config;
	protected LocationReceiver locationReceiver;
	protected Voice voice;
	protected SMSReceiver smsReceiver;
	protected SpeedLimiter speedLimiter;
	
	protected PlaceDetector placeDetector = new PlaceDetector();
	protected AudioManager audioManager;
	
	public void sendEmptyBroadcast(int flag){
		Intent intent = new Intent(SERVICE_ACTION);
		intent.setFlags(flag);
		sendBroadcast(intent);
	}
	
	/** send intent with whole serviceConfig data
	 * (never used)
	 * */
	public void sendBroadcastConfig(){
		Intent intent = new Intent(SERVICE_ACTION);
		intent.setFlags(FLAG_CONFIG_CHANGED_INSIDE);
		Bundle bundle = new Bundle();
		bundle.putParcelable("param", config);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}
	
	protected LocationReceiver getLocationReceiver(){
		return new LocationReceiver(this) {
			
			public void onNewLocation(Location location) {				
				config.currentLocation = location;
				if(speedLimiter.isRunning()) speedLimiter.setNewLocation(location);
				if(placeDetector.isRunning()) placeDetector.processLocation(location);
				sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
			}

			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onProviderEnabled(String provider) {
				config.provider = provider;
				//TODO Auto-generated method stub
			}

		};
	}
	
	protected SMSReceiver getSMSReceiver(){
		return new SMSReceiver(this) {
			
			public void onReceiveSMS(String sender, String content) {
				voice.read(YOU_GOT_SMS_FROM + ". " + sender + ". " + THE_CONTENT_IS
						+ ". " + content);
			}
		};
	}
	
	protected SpeedLimiter getSpeedLimiter(){		
		return new SpeedLimiter() {
			
			public void onSpeedExceed(float speed) {
				voice.read(WARNING_SPEED_LIMIT);
				Log.i("***MyService", "Speed exceed : "+speed+" !!");
			}
			
			public void onSpeedChanged(float speed) {
				config.speed = speed;
				sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
				Log.i("***MyService", "Speed : "+speed);
			}
		};
	}
	
	
	class PlaceDetector {
		
		private boolean running;
		private ArrayList<Place> placesList;
		
		public void setPlacesList(ArrayList<Place> placesList){
			this.placesList = placesList;
		}
		
		public boolean isRunning(){
			return running;
		}
		
		public void enable(){
			running = true;
		}
		
		public void disable(){
			running = false;
		}
		
		/** Process new location*/	
		public void processLocation(Location currentLocation){
			Place nearestPlace = null;
			Location placeLocation =  new Location(config.provider);
			float nearestDistance = 0;
			float distance;
			
			for (Place place : placesList) {
				placeLocation.setLatitude(place.latitude);
				placeLocation.setLongitude(place.longitude);			
				distance = placeLocation.distanceTo(currentLocation);
				Log.i("***PlaceDetector", "distance to "+place.description+" "+distance);
				if(distance<place.radius){
					if(nearestPlace==null || distance<nearestDistance){
						nearestPlace = place;
						nearestDistance = distance;
					}
				}
			}
			
			if(nearestPlace!=null){
				onEnterPlace(nearestPlace);
			}
			else{
				onOutOfAnyPlace();
			}
		}
		
		public void onEnterPlace(Place place){
			Log.i("***PlaceDetector", "Enter place "+place.description);
			if(place.quiet)
				setQuietMode();
		}
		
		private void setQuietMode() {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		}

		public void onOutOfAnyPlace(){
			Log.i("***PlaceDetector", "Out of any place");
			setNormalMode();
		}

		private void setNormalMode() {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}

	}
	
	
	
}
