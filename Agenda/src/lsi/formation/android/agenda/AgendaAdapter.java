package lsi.formation.android.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AgendaAdapter {
	
	SQLiteDatabase db;
	DBHelper DBH;
	
	public AgendaAdapter(Context context){
		DBH=  new DBHelper(context, "agenda.db", null, 1);
		
	}
	
	public void open() {
		db = DBH.getWritableDatabase();
	}
	
	public void close(){
		db.close();
	}
	
	public void vider(){
		db.execSQL("DELETE FROM planning;");
	}
	
	public void supprimer(long id){
		db.delete("planning", "_id="+id, null);
	}
	
	public void inserer(String objet, String date){
		ContentValues values = new ContentValues();
		values.put("objet", objet);
		values.put("date", date);
		db.insert("planning", null, values);
	}
	
	public Cursor lister(){
		return db.query("planning", 
				new String[]{"_id", "objet", "date"}, 
				null, null, null, null, null);
	}
	

}
