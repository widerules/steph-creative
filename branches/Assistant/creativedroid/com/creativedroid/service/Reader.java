package com.creativedroid.service;

import com.creativedroid.ui.Logger;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

/** An engine to read text */
public class Reader implements OnInitListener {

	/** Here the name of the user */
	private TextToSpeech tts;
	private Context context;
	private boolean readerOK;
	private String text;

	public Reader(Context context) {
		this.context = context;
		this.tts = new TextToSpeech(context, this);
	}

	public void read(String text) {
		this.text = text;
		if (this.readerOK) {			
			this.tts.speak(this.text, TextToSpeech.QUEUE_FLUSH, null);
			// new Thread(new ThreadRead()).start();
			Logger.i("Reader", text);
		} else {
			Logger.i("Reader", "Unable to read text : " + this.text);
		}
	}

	public void delete() {
		tts.shutdown();
	}

	/** initializing text to speech engine */
	public void onInit(int status) {
		if (status == TextToSpeech.ERROR) {
			Toast.makeText(context, "Error loading text to speech engine",
					Toast.LENGTH_LONG).show();
		}
		else{
			this.readerOK = true;
		}
	}
	
	class ThreadRead implements Runnable{

		public void run() {
			Reader.this.tts.speak(Reader.this.text, TextToSpeech.QUEUE_FLUSH, null);
		}
		
	}

}
