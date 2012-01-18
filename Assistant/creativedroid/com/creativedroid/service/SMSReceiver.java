package com.creativedroid.service;

import com.creativedroid.ui.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

interface ISMSReceiver{
	
	/** callback receive a SMS */
	void onReceiveSMS(String sender, String content);
}

/**
 * A broadcast receiver to handle SMS
 * @author Steph
 *
 */
public abstract class SMSReceiver extends BasicBroadcastReceiver implements ISMSReceiver{

	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED"; 
	
	private ContactManager contactManager;
	private Intent intent;

	public SMSReceiver(Context context) {
		super(context, SMSReceiver.ACTION);
		this.contactManager = new ContactManager(context);
	}

	@Override
	public final void onReceive(Context context, Intent intent) {
		this.intent = intent;
		// processIntent();
		new Thread(new SMSReceiveTask()).start();
	}
	
	private void processIntent(){
		Bundle bundle = this.intent.getExtras();
		if (bundle != null) {
			Logger.i("SMSReceiver", "receive SMS");
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (Object pdu : pdus) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
				String numero = sms.getDisplayOriginatingAddress();
				String name = this.contactManager.getContactByPhoneNumber(numero);
				String content = sms.getMessageBody();
				this.onReceiveSMS(name, content);
			}
		}
	}
	
	class SMSReceiveTask implements Runnable{

		public void run() {
			SMSReceiver.this.processIntent();
		}
		
	}
	
}
