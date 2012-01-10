package lsi.formation.android.assistant;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MonService extends Service implements OnInitListener{

	LocationManager lmanager;
	Location positionNouvelle;
	Location positionActuelle;
	long delai = 3; //en secondes
	float vitesseMax = 17;
	float vitesse = 0;
	GPSReceiver gpsRec = new GPSReceiver();
	TextToSpeech lecteur;
	SMSReceiver smsRec = new SMSReceiver();
	GestContacts gestc;
	CallReceiver callRec = new CallReceiver();
	Timer timer = new Timer();
	MaTache tache = new MaTache();
	
	@Override
	public void onCreate() {
		lmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
		lecteur = new TextToSpeech(this, this);		
		
		registerReceiver(smsRec, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		registerReceiver(callRec, new IntentFilter("android.intent.action.PHONE_STATE"));
		gestc = new GestContacts(this);
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		lmanager.removeUpdates(gpsRec);
		unregisterReceiver(smsRec);
		unregisterReceiver(callRec);
		timer.cancel();
		lecteur.shutdown();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		String provider = LocationManager.GPS_PROVIDER;
		long minTime = 1000;
		float minDistance = 0;
		lmanager.requestLocationUpdates(provider, minTime, minDistance, gpsRec);
		
		timer.scheduleAtFixedRate(tache, 0, delai*1000);
				
		super.onStart(intent, startId);
	}
	
	private void envoyerDonneAuSysteme(){
		Intent intent = new Intent("monservice.action");
		intent.putExtra("vitesse", vitesse);
		sendBroadcast(intent);
	}
	
	
	@Override
	public void onInit(int status) {
		if(status==TextToSpeech.ERROR){
			Log.e("***TextToSpeech", "Erreur de chargement du moteur voix");
		}
		else{
			lecteur.setLanguage(Locale.FRENCH);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return new MyBinder();
	} 
	
	private void alerte(String text){
		//lire le message passé en paramètre
		lecteur.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	
	public class MyBinder extends Binder{
		
		public MonService getService(){
			return MonService.this;
		}
	}

	public class MaTache extends TimerTask{

		@Override
		public void run() {
			//calcul de la vitesse
			if(positionActuelle!=null && positionActuelle!=positionNouvelle){
				vitesse = (positionNouvelle.distanceTo(positionActuelle))/delai;
				Log.i("***MaTache", "vitesse="+vitesse);
				
				if(vitesse>vitesseMax){
					alerte("Attention, excès de vitesse !");
				}
				
				envoyerDonneAuSysteme();
			}
			
			positionActuelle = positionNouvelle;
			
		}
		
	}
	
	public class SMSReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			Bundle bundle = intent.getExtras();
			if(bundle!=null){
				Object[] pdus = (Object[]) bundle.get("pdus");
				for(Object pdu : pdus){
					SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
					String numero= sms.getDisplayOriginatingAddress();
					String nom = gestc.getContactByPhone(numero);
					String contenu= sms.getMessageBody();
					alerte("Steph, vous avez un nouveau message de "+nom+". Le contenu et . "+contenu);
				}
			}
		}		
	}
	
	public class GPSReceiver implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			positionNouvelle = location;
			Log.i("***GPSReceiver", "nouvelle position "+location.getLatitude()+" "+location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class GestContacts {
		
		ContentResolver cr;
		
		public GestContacts(Context context){
			cr = context.getContentResolver();
		}
			
		public String getContactByPhone(String num) {
			
			String name_r = num==""? "Inconnu" : num;		
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			
			if(cur.getCount() >0)
			{
				while (cur.moveToNext()) {					
					String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));					
					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", 
							new String[]{id}, null);					
					while (pCur.moveToNext()) {
						String Phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "");						
						if (Phone.equals(num)) {
							name_r = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						}
					}
					pCur.close();					
				}
			}
			cur.close();
			return name_r;		
		}
		
	}

	public class CallReceiver extends BroadcastReceiver {
	    
	    
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				// Incoming call
				String numero = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Log.i("***CallReceiver", "incoming call from "+numero);
				String nom = gestc.getContactByPhone(numero);
				alerte("Steph, vous avez un appel de "+nom);
			}
		}
	}
}
