package com.safedriver;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.creativedroid.service.IServiceBinder;
import com.creativedroid.service.LocationReceiver;
import com.creativedroid.service.Reader;
import com.creativedroid.service.SMSReceiver;
import com.creativedroid.service.SpeedMeter;
import com.creativedroid.ui.Logger;

/**
 * implementation of service operations
 * 
 * @author Steph
 * 
 */
public class MyService extends Service implements Vocabulary {
	
	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class MyBinder extends Binder implements IServiceBinder {

		public MyService getService() {
			return MyService.this;
		}
	}
	public static final String SERVICE_ACTION = "MyService.Action";
	public static final int FLAG_SERVICE_STARTED = 0; // empty intent
	public static final int FLAG_CONFIG_CHANGED = 1; // empty intent
	public static final int FLAG_CONFIG_CHANGED_INSIDE = 2; // intent with the whole serviceConfig
										// data
	
	private ServiceConfig config;
	private LocationReceiver locationReceiver;
	private Reader voice;
	private SMSReceiver smsReceiver;
	private SpeedMeter speedLimiter;

	public ServiceConfig getConfig() {
		return this.config;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		this.locationReceiver = this.createLocationReceiver();
		this.voice = new Reader(this);
		this.smsReceiver = this.createSMSReceiver();
		this.speedLimiter = this.createSpeedLimiter();
		super.onCreate();
	}

	/**
	 * On destroying the service stop all functions
	 */
	@Override
	public void onDestroy() {		
		this.stopSMSToSpeech();
		this.stopSpeedLimit();
		this.voice.delete();
		this.stopRequestLocation();
		Logger.i("MyService", "stopped");
		super.onDestroy();
	}

	/**
	 * Start the service We want this service to continue running until it is
	 * explicitly stopped, so return sticky.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// extract serviceConfig in the intent
		Bundle bundle = intent.getExtras();
		this.config = bundle.getParcelable("config");		
		this.startRequestLocation();
		this.startSMSToSpeech();
		this.startSpeedLimit();	
		this.sendEmptyBroadcast(MyService.FLAG_SERVICE_STARTED);
		Logger.i("MyService", "started");
		return Service.START_STICKY;
	}

	/**
	 * send intent with whole serviceConfig data (never used)
	 * */
	public void sendBroadcastConfig() {
		Intent intent = new Intent(MyService.SERVICE_ACTION);
		intent.setFlags(MyService.FLAG_CONFIG_CHANGED_INSIDE);
		Bundle bundle = new Bundle();
		bundle.putParcelable("param", this.config);
		intent.putExtras(bundle);
		this.sendBroadcast(intent);
	}

	public void sendEmptyBroadcast(int flag) {
		Intent intent = new Intent(MyService.SERVICE_ACTION);
		intent.setFlags(flag);
		this.sendBroadcast(intent);
	}

	public void setMaxSpeed(int speed) {
		this.config.maxSpeed = speed;
		speedLimiter.setMaxSpeed(config.maxSpeed);
		sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
	}

	public void setPseudo(String pseudo) {
		config.pseudo = pseudo;
		voice.read(Vocabulary.HELLO + " " + config.pseudo);
		sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
	}

	public void startRequestLocation() {
		locationReceiver.setUpdateInterval(config.updateMinTime);
		locationReceiver.start();
	}

	// //////////////////SET GET service configurations ////////////////////////

	public void startSMSToSpeech() {
		smsReceiver.start();
	}

	public void startSpeedLimit() {
		speedLimiter.setMaxSpeed((int) config.maxSpeed);
		speedLimiter.start();
	}

	public void stopRequestLocation() {
		locationReceiver.stop();
	}

	public void stopSMSToSpeech() {
		smsReceiver.stop();
	}

	public void stopSpeedLimit() {
		speedLimiter.stop();
	}

	private LocationReceiver createLocationReceiver() {
		return new LocationReceiver(this) {

			public void onLocationReceived(Location location) {
				config.currentLocation = location;
				if (speedLimiter.isRunning())
					speedLimiter.pushLocation(location);
				sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
			}
		};
	}

	private SMSReceiver createSMSReceiver() {
		return new SMSReceiver(this) {

			public void onReceiveSMS(String sender, String content) {
				voice.read(config.pseudo + ", " + YOU_GOT_SMS_FROM + ". "
						+ sender + ". " + content);
			}
		};
	}

	private SpeedMeter createSpeedLimiter() {
		return new SpeedMeter() {

			public void onSpeedChanged(int speed) {
				config.speed = speed;
				sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
			}

			public void onSpeedExceed(int speed) {
				voice.read(WARNING_SPEED_LIMIT);
			}
		};
	}

}
