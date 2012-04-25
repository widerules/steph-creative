package com.creativedroid.service;


import com.creativedroid.ui.Logger;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * A class that connects to a service and handle broadcast messages
 * 
 * @author Steph
 * 
 */
public abstract class ServiceConnector<T> implements IServiceConnector<T> {

	/**
	 * class used to bind to the service
	 */
	class SrvConnection implements ServiceConnection {
		/**
		 * This is called when the connection with the service has been
		 * established, giving us the service object we can use to interact with
		 * the service. Because we have bound to a explicit service that we know
		 * is running in our own process, we can cast its IBinder to a concrete
		 * class and directly access it.
		 */
		@SuppressWarnings("unchecked")
		public void onServiceConnected(ComponentName className, IBinder binder) {
			ServiceConnector.this.boundService = (T) ((IServiceBinder) binder).getService();
			ServiceConnector.this.isBound = true;
			Logger.i("ServiceConnector", "service connected");
			ServiceConnector.this.onServiceConnected();
		}

		/**
		 * This is called when the connection with the service has been
		 * unexpectedly disconnected -- that is, its process crashed. Because it
		 * is running in our same process, we should never see this happen.
		 */
		public void onServiceDisconnected(ComponentName className) {
			ServiceConnector.this.boundService = null;
			ServiceConnector.this.isBound = false;
		}
	}
	
	private T boundService;
	private boolean isBound;
	private SrvConnection srvConnection = new SrvConnection();
	private String serviceName;
	private Context context;
	private Class<?> className;

	public ServiceConnector(Context context, Class<?> className, String serviceName) {
		this.context = context;
		this.className = className;
		this.serviceName = serviceName;
	}

	public void connectToService() {
		Intent intent = new Intent(this.context, this.className);
		this.context.bindService(intent, this.srvConnection, Context.BIND_AUTO_CREATE);
	}

	/** Detach existing connection to service */
	public void disconnectFromService() {
		if (this.isBound) {
			this.context.unbindService(this.srvConnection);
			this.isBound = false;
			Logger.i("ServiceConnector", "disconnect from service");
			this.onServiceDisconnected();
		}
	}

	public T getService() {
		return this.boundService;
	}
	
	public boolean isBound() {
		return this.isBound;
	}

	public boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) this.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(this.serviceName)) {
				return true;
			}
		}
		return false;
	}

	public void startService() {
		if (!this.isServiceRunning()) {
			Intent intent = new Intent(this.context, this.className);
			Logger.i("ServiceConnector", "request starting service... ");
			this.context.startService(intent);			
		}
	}

	public void startService(Bundle bundle) {
		if (!this.isServiceRunning()) {
			Intent intent = new Intent(this.context, this.className);
			intent.putExtras(bundle);
			Logger.i("ServiceConnector", "request starting service with bundle...");
			this.context.startService(intent);
			
		}
	}

	public void stopService() {
		if (this.isServiceRunning()) {
			this.disconnectFromService();
			Intent intent = new Intent(this.context, this.className);
			this.context.stopService(intent);
		}
	}

}

/**
 * Do bind and unbind to a local service
 * @author Steph
 *
 * @param <T>
 */
interface IServiceConnector<T> {

	/**
	 * Establish a connection with the service. We use an explicit class name
	 * because we want a specific service implementation that we know will be
	 * running in our own process (and thus won't be supporting component
	 * replacement by other applications).
	 */
	void connectToService();

	/** Detach existing connection to service */
	void disconnectFromService();

	/** get a pointer to the connected service */
	T getService();
	
	/** check if service is connected */
	boolean isBound();

	/** check if service is running */
	boolean isServiceRunning();

	/**
	 * This is called when the connection with the service has been established
	 */
	void onServiceConnected();

	/** callback when disconnecting from the service */
	void onServiceDisconnected();

	/** start the service */
	void startService();

	/** start the service with a data*/
	void startService(Bundle bundle);

	/** stop the service */
	void stopService();
}
