package com.droidcontamination;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * The aim of the game is to kill all zombies
 * Scoring is like this
 * . one zombie killed : +1
 * . one zombie missed: -1
 * . one healthy people killed : -1
 * To win you must achieve score = Game.SCORE_WIN
 * If your score is <0 then you loose
 *
 * @author Steph
 *
 */
public class Main extends Activity {

	private GameLoader loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loader = new GameLoader();
		loader.setup(new Game(), this);
		loader.start();

		Toast.makeText(this, "Kill all the zombies, don't miss them !",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		loader.stopGame();
		super.onDestroy();
	}
}
