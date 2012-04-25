package com.creativedroid.service;

import com.creativedroid.ui.Logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Basic broadcastReceiver that handle register and unregister
 * 
 * @author Steph
 * 
 */
public abstract class BasicBroadcastReceiver extends BroadcastReceiver	implements IStartable {

	protected Context context;
	protected String action;
	private boolean registered;

	public BasicBroadcastReceiver(Context context, String action) {
		this.context = context;
		this.action = action;
		this.registered = false;
	}
	
	public boolean isRunning() {
		return this.registered;
	}

	public void start() {
		if(!this.registered){
			this.registered = true;
			this.context.registerReceiver(this, new IntentFilter(this.action));
			Logger.i("BasicBroadcastReceiver", "register receiver for "
					+ this.action);
		}		
	}

	public void stop() {
		if (this.registered) {
			this.context.unregisterReceiver(this);
			Logger.i("BasicBroadcastReceiver", "unregister receiver for "
					+ this.action);
		}
	}

}
