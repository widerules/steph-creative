package com.smarterdroid.service;

import com.smarterdroid.object.ServiceConfig;
import com.smarterdroid.ui.Pool;

import creativedroid.service.IServiceBinder;
import creativedroid.service.Voice;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * implementation of service operations
 * 
 * @author Steph
 * 
 */
public class MyService extends MyServiceCore {

	@Override
	public IBinder onBind(Intent arg0) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		locationReceiver = getLocationReceiver();
		voice = new Voice(this);
		smsReceiver = getSMSReceiver();
		speedLimiter = getSpeedLimiter();
		super.onCreate();
	}

	/**
	 * Start the service We want this service to continue running until it is
	 * explicitly stopped, so return sticky.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		loadConfig();
		startRequestLocation();
		startEnabledFunctions();
		sendEmptyBroadcast(FLAG_SERVICE_STARTED);
		Log.i("***MyService", "started");
		return START_STICKY;
	}

	/**
	 * On destroying the service stop all functions
	 */
	@Override
	public void onDestroy() {
		saveConfig();
		stopAllRunningFunctions();
		voice.delete();
		stopRequestLocation();
		Log.i("***MyService", "stopped");
		super.onDestroy();
	}

	/** save service configuration in application preference */
	private void saveConfig() {
		// TODO Auto-generated method stub

	}

	/** read service configuration from application preferences */
	private void loadConfig() {
		config = new ServiceConfig();
		config.enableQuietPlace = true;
		config.enableSMSToSpeech = true;
		config.enableSpeedLimit = true;

		// TODO Auto-generated method stub

	}

	private void startEnabledFunctions() {
		if (config.enableEventAlarm)
			startEventAlarm();
		if (config.enableQuietPlace)
			startQuietPlace();
		if (config.enableSMSToSpeech)
			startSMSToSpeech();
		if (config.enableSpeedLimit)
			startSpeedLimit();
	}

	private void stopAllRunningFunctions() {
		if (config.enableEventAlarm)
			stopEventAlarm();
		if (config.enableQuietPlace)
			stopQuietPlace();
		if (config.enableSMSToSpeech)
			stopSMSToSpeech();
		if (config.enableSpeedLimit)
			stopSpeedLimit();
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class MyBinder extends Binder implements IServiceBinder {

		public MyService getService() {
			return MyService.this;
		}
	}

	// //////////////////SET GET service configurations ////////////////////////

	public ServiceConfig getConfig() {
		return config;
	}

	public void setMaxSpeed(float speed) {
		config.maxSpeed = speed;
		speedLimiter.setMaxSpeed(config.maxSpeed);
		sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
	}

	public void setUpdateInterval(long interval) {
		config.updateMinTime = interval;
		locationReceiver.stop();
		locationReceiver.start();
		speedLimiter.setDelay(config.updateMinTime);
		sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
	}

	public void setPseudo(String pseudo) {
		config.pseudo = pseudo;
		sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
	}

	// ////////////////// START/STOP FUNCTIONS ////////////////////////////

	public void startRequestLocation() {
		locationReceiver.setProvider(config.provider);
		locationReceiver.setUpdateInterval(config.updateMinTime);
		locationReceiver.start();
	}

	public void stopRequestLocation() {
		locationReceiver.stop();
	}

	public void startSpeedLimit() {
		if (!config.enableSpeedLimit) {
			config.enableSpeedLimit = true;
			speedLimiter.setDelay(config.updateMinTime);
			speedLimiter.setMaxSpeed(config.maxSpeed);
			speedLimiter.start();
			Log.i("***MyService", "startSpeedLimit");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void stopSpeedLimit() {
		if (config.enableSpeedLimit) {
			config.enableSpeedLimit = false;
			speedLimiter.stop();
			Log.i("***MyService", "stopSpeedLimit");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void startEventAlarm() {
		if (!config.enableEventAlarm) {
			config.enableEventAlarm = true;
			// TODO
			Log.i("***MyService", "startEventAlarm");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void stopEventAlarm() {
		if (config.enableEventAlarm) {
			config.enableEventAlarm = false;
			// TODO
			Log.i("***MyService", "stopEventAlarm");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void startQuietPlace() {
		if (!config.enableQuietPlace) {
			config.enableQuietPlace = true;
			placeDetector.setPlacesList(Pool.placeAdapter.selectAll());
			placeDetector.enable();
			Log.i("***MyService", "startQuietPlace");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void stopQuietPlace() {
		if (config.enableQuietPlace) {
			config.enableQuietPlace = false;
			placeDetector.disable();
			Log.i("***MyService", "stopQuietPlace");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void startSMSToSpeech() {
		if (!config.enableSMSToSpeech) {
			config.enableSMSToSpeech = true;
			smsReceiver.start();
			Log.i("***MyService", "startSMSToSpeech");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

	public void stopSMSToSpeech() {
		if (config.enableSMSToSpeech) {
			config.enableSMSToSpeech = false;
			smsReceiver.stop();
			Log.i("***MyService", "stopSMSToSpeech");
			sendEmptyBroadcast(FLAG_CONFIG_CHANGED);
		}
	}

}
