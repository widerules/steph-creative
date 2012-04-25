package com.droidcontamination;

import android.app.Activity;
import android.util.Log;

/**
 * GameLoader implements the game loop and control the game FPS
 * 
 * @author Steph
 *
 */
public final class GameLoader extends Thread {
	
	private static final long FRAME_PERIOD = 1000 / Game.FPS;
	private Game game;

	/**
	 * Here is the game loop
	 * 0. wait until gamesScreen ready
	 * 1. ask for update
	 * 2. ask for render
	 * 3. compute the time to sleep and do a pause to control game FPS
	 * 
	 * */
	public void run() {
		waitUntilScreenIsReady();
		
		long startTime;
		long sleepTime;
		long elapsedTime;
		long frameTime;
		int currentFPS;
		
		while (game.isRunning()) {
			startTime = System.currentTimeMillis();
			
			//execute game frame
			game.processEvents();
			game.update();	
			game.render();
			
			elapsedTime = System.currentTimeMillis() - startTime;
			sleepTime = FRAME_PERIOD - elapsedTime;			
			try {
				if (sleepTime > 0)
					Thread.sleep(sleepTime);
				else
					Thread.sleep(10);
			} catch (Exception e) {
			}
			
			frameTime = System.currentTimeMillis() - startTime;
			currentFPS = (int) (1000/frameTime);
			game.setCurrentFPS(currentFPS);
		}
		game.renderFinished();
		Log.i("***GameLoader", "game stopped properly");
	}
	
	public void setup(Game game, Activity act) {
		this.game = game;
		this.game.initResources(act);
		act.setContentView(game.getGameView());
	}

	/** Stop the game loop by setting game.isRunning() to false*/
	public void stopGame() {
		game.stop();
	}
	
	private void waitUntilScreenIsReady() {
		while (!game.getGameView().isReady()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}
	
}
