package lsi.formation.android.agenda;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Main extends Activity {
    
	AgendaAdapter maBase;
	ListView maListe;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        maBase = new AgendaAdapter(this);
        maBase.open();
        
//        maBase.inserer("aller chez le dentiste", "samedi 24 à 12h");
//        maBase.inserer("aller chez au cinema", "samedi 24 à 20h");
//        maBase.inserer("anniversaire de Fatima", "dimanche 25 à 21h");
//        maBase.inserer("acheter les cadeaux", "vendredi 23 à 17h");
        
        maListe = (ListView) findViewById(R.id.maListe);
        
        maListe.setOnCreateContextMenuListener(this);
        
        afficher();
        
    }
    
    
    
    private void afficher() {
		Cursor cursor = maBase.lister();
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.item,cursor,
				new String[]{"_id", "objet", "date"},
				new int[]{R.id.txtId, R.id.txtObjet, R.id.txtDate}
				);
		
		maListe.setAdapter(adapter);
	}



	@Override
    protected void onDestroy() {
    	maBase.close();
    	super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 100, 0, "Ajouter");
		menu.add(0, 101, 1, "Supprimer tout");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			//lancer l'activité Formulaire pour ajouter
			Intent intent = new Intent(this, Formulaire.class);
			startActivityForResult(intent, 0);
			
			break;

		case 101:
			maBase.vider();
			afficher();			
			break;
		}
		
		return true;
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode==Activity.RESULT_OK){
			
			String objet = data.getStringExtra("objet");
			String date = data.getStringExtra("date");
			maBase.inserer(objet, date);
			afficher();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		menu.setHeaderTitle("Options");
		menu.add(1, 102, 0, "Reporter");
		menu.add(1, 103, 0, "Supprimer");
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		long id = info.id;
		
		switch (item.getItemId()) {
		case 102:
			//ici le code pour reporter
			
			break;

		case 103:
			//ici le code pour reporter
			maBase.supprimer(id);
			afficher();
			
			break;
		}

		return true;
	}
	
	
	
	
}