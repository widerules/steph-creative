package com.droid.gamedev;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.util.Log;

/**
 * Extending Game class functionality to be able to handle multiple game screen
 * in order to separate game logic into separated entities. 
 * For example: manage intro screen, title screen, menu screen, 
 * and main game screen as separated entity.
 * 
 * @author Steph
 */
public abstract class GameEngine extends Game{

	/** Current game */
	private GameObject currentGame;
	private int currentGameID;

	/**
	 * GameObject to be played next, null to exit game.
	 */
	private GameObject nextGame;

	/**
	 * Game ID to be played next, -1 to exit game.
	 */
	private int nextGameID = 0;

	public GameEngine() {
	}
	
	/**
	 * Returns GameObject with specific ID, the returned GameObject will be the
	 * game to be played next.
	 * @param GameID
	 * @return game with specified ID
	 */
	public abstract GameObject getGame(int GameID);

	/**
	 * Setters for the next game
	 * @param nextGameID
	 */
	public void setNextGameID(int nextGameID) {
		this.nextGameID = nextGameID;
	}

	@Override
	final void startGameLoop() {
		// start the timer
		this.bsTimer.startTimer();
		
		while (this.isRunning()) {
			// refresh global game state
			this.bsInput.refresh();
			this.refresh();
			
			// validate game to be played next
			if (this.nextGameID == -1 && this.nextGame == null) {
				// next game is not provided, game ended
				this.finish();
				break;
			}
			
			// get the game object to be played next
			this.currentGameID = this.nextGameID;
			this.currentGame = (this.nextGame != null) ? 
					this.nextGame : this.getGame(this.nextGameID);
			
			if (this.currentGame == null) {
				// game is not available, exit the game
				Log.ec(this, "GameObject with ID = "
						+ this.currentGameID + " is not available!!");
				
				this.finish();
				break;
			}
			
			// clear next game, to avoid this current game played forever
			if (this.nextGame == this.currentGame) {
				this.nextGame = null;
			}
			if (this.nextGameID == this.currentGameID) {
				this.nextGameID = -1;
			}
			
			// running the game in here there's other game loop,
			// loop ended when the game finished, calling GameObject.finish()
			this.currentGame.start();
		}
		
		// dispose everything
		this.bsTimer.stopTimer();
		this.bsSound.stopAll();
		this.bsMusic.stopAll();

		if (this.isFinish()) {
			this.bsGraphics.cleanup();
			this.notifyExit();
		}
	}

	@Override
	public void initResources() {
	}

	@Override
	public void update(long elapsedTime) {
	}

	@Override
	public void render(GameGraphics g) {
	}

	/**
	 * Refresh game global variables, called right before playing next game
	 * object. The implementation of this method provided by the
	 * GameEngine class does nothing.
	 */
	public void refresh() {
	}

	/**
	 * Returns currently playing GameObject entity.
	 * @return current game object
	 */
	public GameObject getCurrentGame() {
		return this.currentGame;
	}

	/**
	 * Returns the ID of currently playing GameObject entity.
	 * @return current game id
	 */
	public int getCurrentGameID() {
		return this.currentGameID;
	}

	public void setNextGame(GameObject game) {
		this.nextGame = game;		
	}
}
