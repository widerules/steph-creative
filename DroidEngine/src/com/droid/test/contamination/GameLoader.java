package com.droid.test.contamination;

import android.app.Activity;

import com.droid.gamedev.engine.graphics.and.GFXAnd;

/**
 * GameLoader implements the game loop and control the game FPS
 * 
 * @author Steph
 * 
 */
public final class GameLoader implements Runnable {

	private Game game;

	/**
	 * Here is the game loop 
	 * 0. wait until gamesScreen ready 
	 * 1. ask for update
	 * 2. ask for render 
	 * 3. compute the time to sleep and do a pause to control
	 * game FPS
	 * 
	 * */
	public void run() {
		waitUntilScreenIsReady();
		this.game.startGameLoop();
	}

	public void setup(Game game, Activity activity) {

		this.game = game;

		GFXAnd gfx = new GFXAnd(activity);
		this.game.gfx = gfx;
		activity.setContentView(gfx);

		this.game.initResources(activity);
	}

	public void srart() {
		new Thread(this).start();
	}

	/** Stop the game loop by setting game.isRunning() to false */
	public void stop() {
		game.stop();
	}

	private void waitUntilScreenIsReady() {
		if (this.game != null) {
			while (!game.gfx.isReady()) {
				game.gfx.requestFocus();
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}
		}
	}

}
