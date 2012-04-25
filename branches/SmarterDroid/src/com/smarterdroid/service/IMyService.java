package com.smarterdroid.service;

import com.smarterdroid.object.ServiceConfig;

/** Interface description of MyService operations */
public interface IMyService {

	String SERVICE_ACTION = "MyService.Action";
	int FLAG_SERVICE_STARTED = 0;		//empty intent 
	int FLAG_CONFIG_CHANGED = 1;		//empty intent
	int FLAG_CONFIG_CHANGED_INSIDE = 2; //intent with the whole serviceConfig data
	
	/** send empty broadcast with specified flag */
	void sendEmptyBroadcast(int flag);
	
	/** Send the current service config via a bradcast message*/
	void sendBroadcastConfig();

	ServiceConfig getConfig();

	void setMaxSpeed(float speed);

	void setUpdateInterval(long interval);

	void setPseudo(String pseudo);

	void startRequestLocation();

	void stopRequestLocation();

	void startSpeedLimit();

	void stopSpeedLimit();

	void startEventAlarm();

	void stopEventAlarm();

	void startQuietPlace();

	void stopQuietPlace();

	void startSMSToSpeech();

	void stopSMSToSpeech();

}
