package com.droid.gamedev;

import android.app.Activity;

import com.droid.gamedev.Game.GameExitListener;
import com.droid.gamedev.engine.graphics.and.GFXAnd;
import com.droid.gamedev.util.Log;

public final class GameLoaderAndroid extends GameLoader {
	
	class GameExitListenerAnd implements GameExitListener {

		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * Setup game for Android platform
	 * @param game 
	 * @param activity 
	 */
	public void setup(Game game, Activity activity) {
		
		// choose definitively android Platform
		GameLoader.platform = GamePlatform.ANDROID;
		
		// associate the game
		this.game = game;

		// create the game exit listener
		this.game.setExitListner(new GameExitListenerAnd());

		// time to create the graphics engine
		// create graphics engine
		this.gfx = new GFXAnd(activity);
		this.game.bsGraphics = this.gfx;

		// affect engines to the game
		initEngineAnd();

		Log.ic(this, "setup done");
	}

	private void initEngineAnd(){
		//TODO setup engines for Android platform
	}

}
