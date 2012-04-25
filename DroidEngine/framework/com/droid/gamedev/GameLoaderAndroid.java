package com.droid.gamedev;

import android.app.Activity;

import com.droid.gamedev.engine.graphics.GameExitListener;
import com.droid.gamedev.engine.graphics.and.GFXAnd;
import com.droid.gamedev.util.Log;

public final class GameLoaderAndroid extends GameLoader {
	
	/**
	 * Setup game for Android platform
	 * @param game 
	 * @param activity 
	 */
	public void setup(Game game, Activity activity) {

		// first thing is definitely use platform = Android
		GameLoader.platform = GameLoader.ANDROID;

		// associate the game
		this.game = game;

		// create the game exit listner
		this.exitListner = this.getExitListner();
		this.game.setExitListner(this.exitListner);

		// time to create the graphics engine
		// create graphics engine
		this.gfx = new GFXAnd(activity);
		this.game.bsGraphics = this.gfx;

		// affect engines to the game
		initEngineAnd();

		Log.ic(this, "setup done");
	}

	private GameExitListener getExitListner() {
		return new GameExitListener() {

			public void onExit() {
				// the game has finished its thread,
				// time to go back to system properly
				try {
					Log.ic(this, "The game has finished");
					// System.exit(0);
					// application close automatically cause all threads
					// achieved
				} catch (Exception e) {
				}
			}
		};
	}
	
	@Override
	public void setup(Game game, int width, int height, boolean fullscreen,
			boolean bufferstrategy) {
		Log.ex(this, "This setup method is inappropriate for android platform");
	}
		
	private void initEngineAnd(){
		//TODO setup engines for Android platform
	}

}
