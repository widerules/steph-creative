package creativedroid.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

/** An engine to read text */
public class Voice implements OnInitListener {

	/** Here the name of the user */
	private TextToSpeech reader;
	private Context context;
	private boolean readerOK;

	public Voice(Context context) {
		this.context = context;
		reader = new TextToSpeech(context, this);
	}

	public void read(String text) {
		if(readerOK){
			reader.speak(text, TextToSpeech.QUEUE_ADD, null);
			Log.i("***Voice", text);
		}
		else{
			Log.i("***Voice", "Unable to read text");
			Toast.makeText(context, "Unable to read text", Toast.LENGTH_SHORT).show();
		}
		
	}

	public void delete() {
		reader.shutdown();
	}

	/** initializing text to speech engine */
	public void onInit(int status) {
		if (status == TextToSpeech.ERROR) {
			Toast.makeText(context, "Error loading text to speech engine",
					Toast.LENGTH_LONG).show();
		}
		else{
			readerOK = true;
		}
	}

}
