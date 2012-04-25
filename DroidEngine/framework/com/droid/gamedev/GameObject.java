package com.droid.gamedev;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.util.Log;

/**
 * Game screen Similar like Game class except this class is working
 * under GameEngine framework.
 * 
 * @author Steph
 *
 */
public abstract class GameObject extends BaseGame{
	
	/**
	 * The master GameEngine frame work.
	 */
	public final GameEngine parent;
			
	/** true, to back to game chooser */
	private boolean finish; 
	
	/** true, indicates the game has been initialized 
	 * to avoid double initialization if the game is replaying */
	private boolean initialized; 
		
	/**
	 * Creates new GameObject with specified
	 * GameEngine as the master engine.
	 * @param parent
	 */
	public GameObject(GameEngine parent) {
		this.parent = parent;

		this.grabEngines();
	}

	/**
	 * Starts the game main loop, this method will not return 
	 * until the gameObject is finished
	 */
	@Override
	final void start() {
		
		// grabbing engines from master engine
		this.grabEngines();

		if (!this.initialized) {
			this.initResources();
			this.initialized = true;
		}

		this.finish = false;

		// start game loop!
		// before play, clear memory (runs garbage collector)
		System.gc();
		System.runFinalization();
		
		// callback start game
		this.onStart();

		this.bsInput.refresh();
		this.bsTimer.refresh();
		
		long elapsedTime = 0;
		out: while (true) {
			if (this.parent.isInFocus()) {
				
				// update game
				this.update(elapsedTime);
				
				// update common variables
				this.parent.update(elapsedTime);
				this.bsInput.update(elapsedTime);

			} else {
				// the game is not in focus!
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			}

			do {
				if (this.finish || !this.parent.isRunning()) {
					// if finish, quit this game
					break out;
				}

				// graphics operation
				GameGraphics g =  this.bsGraphics.getGraphics();

				// render game
				this.render(g); 
				
				// render global game
				this.parent.render(g); 

				if (!this.parent.isInFocus()) {
					this.parent.renderLostFocus(g);
				}

			} while (this.bsGraphics.flip() == false);

			elapsedTime = this.bsTimer.sleep();

			if (elapsedTime > 100) {
				// can't lower than 10 FPS
				elapsedTime = 100;
			}
		}
		
		//callback game stopped
		this.onStop();
	}

	@Override
	public final void finish() {
		this.finish = true;
	}
	
	/**
	 * for debugging that this game object is properly disposed
	 */
	@Override
	protected void finalize() throws Throwable {
		Log.ic(this, "destroyed "+ this);
		super.finalize();
	}

	/**
	 * Grab engines from parent GameEngine
	 */
	private void grabEngines() {
		this.bsGraphics = this.parent.bsGraphics;
		this.bsIO = this.parent.bsIO;
		this.bsLoader = this.parent.bsLoader;
		this.bsInput = this.parent.bsInput;
		this.bsTimer = this.parent.bsTimer;
		this.bsMusic = this.parent.bsMusic;
		this.bsSound = this.parent.bsSound;

		this.fontManager = this.parent.fontManager;
	}

}
