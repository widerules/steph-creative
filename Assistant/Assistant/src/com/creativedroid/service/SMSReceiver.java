package com.creativedroid.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

interface ISMSReceiver{
	
	/** Process a message */
	void onReceiveSMS(String sender, String content);
}

/**
 * A broadcast receiver to handle SMS
 * @author Steph
 *
 */
public abstract class SMSReceiver extends BasicBroadcastReceiver implements ISMSReceiver{

	private ContactManager contactManager;
	
	public SMSReceiver(Context context) {
		super(context, "android.provider.Telephony.SMS_RECEIVED");
		contactManager = new ContactManager(context);
	}

	@Override
	public final void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Log.i("***SMSReceiver", "receive SMS");
			Object[] pdus = (Object[]) bundle.get("pdus");
			for(Object pdu : pdus){
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
				String numero = sms.getDisplayOriginatingAddress();
				String name = contactManager.getContactByPhoneNumber(numero);
				String content = sms.getMessageBody();
				onReceiveSMS(name, content);
			}
		}
	}
	
}
