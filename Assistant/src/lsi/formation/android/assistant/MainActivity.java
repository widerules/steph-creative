package lsi.formation.android.assistant;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    
	CheckBox maCheckBox;
	TextView maTextView;
	
	VitesseReceiver vitesseRec = new VitesseReceiver();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        maCheckBox = (CheckBox) findViewById(android.R.id.checkbox);
        maTextView = (TextView) findViewById(android.R.id.text1);
        
        if(monServiceEstDemarre()){
        	maCheckBox.setText("Démarré");
        	maCheckBox.setChecked(true);
        }
        
        maCheckBox.setOnClickListener(this);
        
        IntentFilter filter = new IntentFilter("monservice.action");
        registerReceiver(vitesseRec, filter);
        
    }
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(vitesseRec);
		super.onDestroy();
	}

	private boolean monServiceEstDemarre() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		String nomDeMonService = "lsi.formation.android.assistant.MonService";
		for(RunningServiceInfo info : manager.getRunningServices(100)){
			
			if(info.service.getClassName().equals(nomDeMonService)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void onClick(View arg0) {
		Intent service = new Intent(this, MonService.class);
		if(maCheckBox.isChecked()){
			startService(service);
			maCheckBox.setText("Démarré");
		}
		else{
			stopService(service);
			maCheckBox.setText("Arrrêt");
		}
	}
	
	public class VitesseReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			float vitesseRecue = intent.getFloatExtra("vitesse", 0);
			maTextView.setText("vitesse actuelle : "+vitesseRecue);
		}
	}	
}