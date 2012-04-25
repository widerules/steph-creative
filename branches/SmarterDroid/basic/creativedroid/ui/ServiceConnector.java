package creativedroid.ui;

import creativedroid.service.IServiceBinder;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

interface IServiceConnector<T> {

	/** get a pointer to the connected service */
	T getService();

	/** check if service is connected */
	boolean isBound();

	/** start the service */
	void startService();

	/** stop the service */
	void stopService();

	/**
	 * Establish a connection with the service. We use an explicit class name
	 * because we want a specific service implementation that we know will be
	 * running in our own process (and thus won't be supporting component
	 * replacement by other applications).
	 */
	void connectToService();

	/** Detach existing connection to service */
	void disconnectFromService();

	/** check if service is running */
	boolean isServiceRunning();

	/**
	 * This is called when the connection with the service has been established
	 */
	void onServiceConnected();

	/** callback when disconnecting from the service */
	void onServiceDisconnected();
}

/**
 * A class that connects to a service and handle broadcast messages
 * 
 * @author Steph
 * 
 */
public abstract class ServiceConnector<T> implements IServiceConnector<T> {

	private T boundService;
	private boolean isBound;
	private SrvConnection srvConnection = new SrvConnection();
	private String serviceName;
	private Context context;
	private Class<?> className;

	public ServiceConnector(Context context, Class<?> className,
			String serviceName) {
		this.context = context;
		this.className = className;
		this.serviceName = serviceName;
	}

	public final boolean isBound() {
		return isBound;
	}

	public T getService() {
		return boundService;
	}

	public final boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	public final void startService() {
		if (!isServiceRunning()) {
			Intent intent = new Intent(context, className);
			context.startService(intent);
			Log.i("ServiceConnector", "request starting service");
		}
	}

	public final void stopService() {
		if (isServiceRunning()) {
			disconnectFromService();
			Intent intent = new Intent(context, className);
			context.stopService(intent);
		}
	}

	public final void connectToService() {
		Intent intent = new Intent(context, className);
		context.bindService(intent, srvConnection, Context.BIND_AUTO_CREATE);
	}

	/** Detach existing connection to service */
	public final void disconnectFromService() {
		if (isBound) {
			context.unbindService(srvConnection);
			isBound = false;
			Log.i("***ServiceConnector", "disconnect from service");
			onServiceDisconnected();
		}
	}

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
			boundService = (T) ((IServiceBinder) binder).getService();

			isBound = true;
			Log.i("**ServiceConnector", "onServiceConnected");
			ServiceConnector.this.onServiceConnected();
		}

		/**
		 * This is called when the connection with the service has been
		 * unexpectedly disconnected -- that is, its process crashed. Because it
		 * is running in our same process, we should never see this happen.
		 */
		public void onServiceDisconnected(ComponentName className) {
			boundService = null;
			isBound = false;
			// ServiceConnector.this.onServiceDisconnected();
		}
	}

}
