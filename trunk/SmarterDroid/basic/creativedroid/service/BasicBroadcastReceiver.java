package creativedroid.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Basic broadcastReceiver that handle register and unregister
 * 
 * @author Steph
 * 
 */
public abstract class BasicBroadcastReceiver extends BroadcastReceiver
		implements IStartable {

	protected Context context;
	protected String action;
	private boolean registered;

	public BasicBroadcastReceiver(Context context, String action) {
		this.context = context;
		this.action = action;
		registered = false;
	}
	
	public boolean isRunning() {
		return registered;
	}

	public void start() {
		registered = true;
		context.registerReceiver(this, new IntentFilter(action));
	}

	public void stop() {
		if (registered) {
			context.unregisterReceiver(this);
		}
	}

}
