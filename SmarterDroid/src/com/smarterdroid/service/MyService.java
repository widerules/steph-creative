package com.smarterdroid.service;

import com.smarterdroid.data.object.ServiceConfig;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Background service
 * 
 * @author Steph
 * 
 */
public class MyService extends Service {

	public static final String SERVICE_ACTION = "MyService.Action";
	private ServiceConfig config;

	@Override
	public void onCreate() {
		// TODO restore config from preference*
		config = new ServiceConfig();
		
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return new MyBinder();
	}

	public ServiceConfig getConfig() {
		return config;
	}
	
	/**
	 * Start the service
	 * We want this service to continue running until it is explicitly
	 *  stopped, so return sticky.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//LocationReceiver.i.start();
		sendServiceInfo();
		Log.i("***MyService", "started");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
//		ServiceCore.i.stopAll();
//		LocationReceiver.i.stop();
//		Voice.i.delete();
		Log.i("***MyService", "stopped");
		super.onDestroy();
	}

	private void sendServiceInfo() {
		Intent intent = new Intent(SERVICE_ACTION);
		Bundle bundle = new Bundle();
		bundle.putParcelable("param", config);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class MyBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}

}
