package com.smarterdroid.service;

import com.smarterdroid.object.ServiceConfig;

import creativedroid.service.BasicBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

interface IMyServiceReceiver{
	
	void onConfigReceived(ServiceConfig config);
	
	void onFlagReceived(int flag);
	
}

/**
 * A broadcastReceiver to handle messages from service
 * 
 * @author Steph
 * 
 */
public abstract class MyServiceReceiver extends BasicBroadcastReceiver implements IMyServiceReceiver {

	public MyServiceReceiver(Context context) {
		super(context, IMyService.SERVICE_ACTION);
	}

	@Override
	public final void onReceive(Context context, Intent intent) {
		Log.i("***MyServiceReceiver", "receive message ");
		switch (intent.getFlags()) {
		case IMyService.FLAG_CONFIG_CHANGED_INSIDE:
			Bundle bundle = intent.getExtras();
			ServiceConfig config = bundle.getParcelable("param");
			onConfigReceived(config);
			break;

		default: 
			onFlagReceived(intent.getFlags());
			break;
		}		
	}

	public void onConfigReceived(ServiceConfig config) {
		// TODO Auto-generated method stub
		
	}

	public void onFlagReceived(int flag) {
		// TODO Auto-generated method stub
		
	}
	
}
