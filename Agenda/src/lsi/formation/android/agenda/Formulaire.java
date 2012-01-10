package lsi.formation.android.agenda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Formulaire extends Activity implements OnClickListener{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formulaire);
		
		findViewById(R.id.btnAjouter).setOnClickListener(this);
		
	}

	public void onClick(View arg0) {
		
		String objet = ((EditText)findViewById(R.id.txtObjet)).getText().toString();
		String date = ((EditText)findViewById(R.id.txtDate)).getText().toString();
		
		Intent intent = new Intent();		
		intent.putExtra("objet", objet);
		intent.putExtra("date", date);
		
		setResult(Activity.RESULT_OK, intent);
		
		finish();
	}
}
