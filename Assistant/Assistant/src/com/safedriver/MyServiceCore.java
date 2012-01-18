package com.safedriver;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
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
				voice.read(config.pseudo+", "+YOU_GOT_SMS_FROM + ". " + sender + ". " + THE_CONTENT_IS
						+ ". " + content);
			}
		};
	}
	
	protected SpeedLimiter getSpeedLimiter(){		
		return new SpeedLimiter() {
			
			public void onSpeedExceed(float speed) {
				voice.read(config.pseudo+", "+ WARNING_SPEED_LIMIT);
				Log.i("***MyService", "Speed exceed : "+speed+" !!");
			}
			
			public void onSpeedChanged(float speed) {
				config.speed = speed;
				sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
				Log.i("***MyService", "Speed : "+speed);
			}
		};
	}
	
	
	
}
