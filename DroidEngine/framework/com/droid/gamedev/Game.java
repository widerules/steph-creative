package com.droid.gamedev;

import java.net.URL;

import com.droid.gamedev.base.GameAppListner;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.graphics.GameExitListener;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.engine.loader.ImageUtil;
import com.droid.gamedev.engine.timer.SystemTimer;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.util.ErrorNotifier;
import com.droid.gamedev.util.Log;

/**
 * Implementation of a single screen game
 * 
 * @author Steph
 * 
 */
public abstract class Game extends BaseGame{
	
	/** Default Game number of frames per second */
	static final int DEFAULT_FPS = 10;

	/** true, indicates the game is currently running/playing */
	private boolean running;

	/**
	 * true, indicates the game has been ended. An ended game can't be played
	 * anymore
	 */
	private boolean finish;

	/** Font used to display FPS */
	private GameFont fpsFont;

	/**
	 * true, indicates the game has been initialized used when the game is
	 * stopped and played again to avoid multiple initialization
	 */
	private boolean initialized;
		
	/** exit listener */
	private GameExitListener exitListener;

	/** used to ask user to come back on focus */
	private boolean inFocus = true;

	/** if enabled the game will pause on focus lost */
	private boolean pauseOnLostFocus = false;

	/** boolean to blink the message : COME BACK TO FOCUS */
	private boolean inFocusBlink;
		
	public Game() {
	}
	
	/**
	 * Stops the game from running
	 */
	final void stop() {
		this.running = false;
	}

	@Override
	public final void finish() {
		this.finish = true;
		this.stop();
	}

	/**
	 * Returns whether the game is currently running/playing or not. Running
	 * game means the game is in game main-loop (update and render loop).
	 */
	boolean isRunning() {
		return this.running;
	}

	@Override
	final void start() {
		if (this.running || this.finish) {
			return;
		}
		this.running = true;

		try {
			if (this.initialized == false) {
				this.initialized = true;
				this.initialize();
			}

			// callback start game
			this.onStart();

			this.startGameLoop();
		} 
		catch (Throwable e) {
			this.notifyError("Game unexpected error", e);
			this.finish();
		}

	}
	
	/**
	 * Sets whether the game is paused when the game is lost the input focus or
	 * not. 
	 */
	public final void setPauseOnLostFocus(boolean b) {
		this.pauseOnLostFocus = b;

		if (this.pauseOnLostFocus == false) {
			// if not paused on lost focus, make sure the game is in focus
			this.inFocus = true;
		}
	}

	public final boolean isInFocus() {
		return inFocus;
	}

	/**
	 * Notified when the game is about to quit.
	 */
	final void notifyExit() {
		if(this.exitListener!=null){
			this.exitListener.onExit();
		}
	}

	/**
	 * Notified of any unexpected or uncatch error thrown by the game.
	 */
	final void notifyError(String title, Throwable error) {
		//use gameErrorNotifier util to notify this error
		ErrorNotifier.notifyError(title, error);
		
		//immediately exit game
		this.notifyExit();
	}
	
	/**
	 * Renders information when the game is not in focused.
	 * Useful on game pause
	 */
	public void renderLostFocus(GameGraphics g) {
		String st1 = "GAME IS NOT IN FOCUSED";
		String st2 = "CLICK HERE TO GET THE FOCUS BACK";
		
		g.setFont(this.fontManager.createFont("Dialog", GameFont.BOLD, 15));

		int charHeight = g.stringHeight();
		int st1Width = g.stringWidth(st1);
		int st2Width = g.stringWidth(st2);

		int posy = (this.getHeight() / 2) - ((charHeight + 10));
		int x = (this.getWidth() / 2) - (st2Width / 2) - 20;
		int y = posy - 25;
		int width = st2Width + 40;
		int height = 2 * charHeight + 30;

		g.setColor(GameColor.BLACK);
		g.fillRect(x, y, width - 1, height - 1);
		g.setColor(GameColor.RED);
		g.drawRect(x, y, width - 1, height - 1);

		this.inFocusBlink = !this.inFocusBlink;

		if (!this.inFocusBlink) {

			g.setColor(GameColor.RED);
			g.drawString(st1, (this.getWidth() / 2) - (st1Width / 2), posy);
			posy = posy + charHeight + 10;
			g.drawString(st2, (this.getWidth() / 2) - (st2Width / 2), posy);
		}
	}
	
	void setExitListner(GameExitListener exl){
		this.exitListener = exl;
	}

	/**
	 * Returns true, if the game has been finished playing and the game is about
	 * to return back to operating system.
	 */
	final boolean isFinish() {
		return this.finish;
	}

	void startGameLoop() {

		Log.ic(this, "startGameLoop");

		// before play, runs garbage collector to clear unused memory
		System.gc();
		System.runFinalization();

		// start the timer
		this.bsTimer.startTimer();
		this.bsTimer.refresh();
		
		long elapsedTime = 0;
		out: while (true) {
			if (this.inFocus) {
				// update game
				this.update(elapsedTime);
				this.bsInput.update(elapsedTime); // update input

			} 
			else {
				// the game is not in focus!
				try {
					Thread.sleep(300);
				} 
				catch (InterruptedException e) {
				}
			}

			do {
				if (!this.running) {
					// if not running, quit this game
					break out;
				}
				
				// graphics operation
				GameGraphics g = this.bsGraphics.getGraphics();

				// render game
				this.render(g); 

				if (!this.inFocus) {
					this.renderLostFocus(g);
				}
				
			} while (this.bsGraphics.flip() == false);

			elapsedTime = this.bsTimer.sleep();

			if (elapsedTime > 100) {
				// the elapsedTime can't be greater than 100 (10 fps)
				// it's a workaround so the movement is not too jumpy
				elapsedTime = 100;
			}
		}
		
		//callback game stopped
		this.onStop();
		
		// stop the timer
		this.bsTimer.stopTimer();
		this.bsSound.stopAll();
		this.bsMusic.stopAll();
		
		if (this.finish) {
			this.bsGraphics.cleanup();
			this.notifyExit();
		}
	}

	/** Game engine initialization */
	private void initialize() {

		// initialize all engines
		this.initEngine();

		// setup game application listener
		this.setupGameAppListner();

		// show splash screen ;)
		this.showLogo();

		// load fps font
		this.loadFPSFont();

		// before play, clear unused memory (runs garbage collector)
		System.gc();
		System.runFinalization();

		// load resources
		this.initResources();

		Log.ic(this, "initialize done");
	}
	
	private void setupGameAppListner(){		
		this.bsGraphics.setGameAppListner(new GameAppListner() {
			
			public void onFocusGained() {
				Game.this.inFocus = true;
			}
			
			public void onFocusLost() {
				if (Game.this.pauseOnLostFocus) {
					Game.this.inFocus = false;
				}
			}
			
			public void onAppClosed() {
				Game.this.finish();
			}
		});
	}
	
	private void loadFPSFont(){
		try {
			URL fontURL = com.droid.gamedev.Game.class.getResource("Game.fnt");
			Log.ic(this, "FPS Font URL : "+fontURL);
			
			GameImage fpsImage = ImageUtil.singleton.getImage(fontURL);
			
			this.fpsFont = this.fontManager.createFont(fpsImage);
			
			this.fontManager.putFont("FPS Font", this.fpsFont);
			
			//delete image
			fpsImage.flush();
			this.fpsFont = null;
			
		}
		catch (Exception e) {
			this.notifyError("load FPS Font", e);
			this.finish();
		}
	}

	/**
	 * Game engines is initialized in this method. 
	 * Thus modifying or changing any game engines should be done within this
	 * method.
	 */
	private void initEngine() {
		// this method has been moved to Gameloader.Setup()
	}
	

	

	private void showLogo() {
		Log.ic(this, "show logo... ");
		
		this.hideCursor();
		SystemTimer dummyTimer = new SystemTimer();
		dummyTimer.setFPS(20);
		this.bsInput.refresh();
		
		// loading logo for splash screen
		GameImage logo = null;
		try {
			URL logoURL = com.droid.gamedev.Game.class.getResource("Game.dat");
			Log.ic(this, "logo URL : "+logoURL);
			GameImage orig = ImageUtil.singleton.getImage(logoURL);

			logo = ImageUtil.singleton.resize(orig, this.getWidth(), this.getHeight());

			orig.flush();
			orig = null;
		} 
		catch (Exception e) {
			this.notifyError("Show logo", e);
			this.finish();
		}
		
		// time to show splash screen
		// clear background with black color
		// and wait for a second
		try {
			this.clearScreen(GameColor.BLACK);
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
		}
		
		// check for focus owner
		if (!this.inFocus) {
			while (!this.inFocus) {
				// the game is not in focus!
				GameGraphics g = this.bsGraphics.getGraphics();
				g.setColor(GameColor.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				this.renderLostFocus(g);
				this.bsGraphics.flip();
				
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
				}
			}
			
			this.bsInput.refresh();
			
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
			}
		}
		
		// gradually show (alpha blending)
		float alpha = 0.0f;
		dummyTimer.startTimer();
		boolean firstTime = true;
		while (alpha < 1.0f) {
			do {
				if (!this.running) {
					return;
				}
				GameGraphics g = this.bsGraphics.getGraphics();
				
				g.setColor(GameColor.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				g.drawImageAlpha(logo, 0, 0, alpha);
				
			} while (this.bsGraphics.flip() == false);
			
			if (firstTime) {
				firstTime = false;
				dummyTimer.refresh();
			}
			
			long elapsedTime = dummyTimer.sleep();
			double increment = 0.00065 * elapsedTime;
			if (increment > 0.22) {
				increment = 0.22 + (increment / 6);
			}
			alpha += increment;
			
			if (this.isSkip(elapsedTime)) {
				this.clearScreen(GameColor.BLACK);
				logo.flush();
				logo = null;
				return;
			}
		}
		
		// show the shiny logo for 2500 ms
		do {
			if (!this.running) {
				return;
			}
			GameGraphics g =  this.bsGraphics.getGraphics();
			
			g.drawImage(logo, 0, 0);
		} while (this.bsGraphics.flip() == false);
		
		int i = 0;
		while (i++ < 50) { // 50 x 50 = 2500
			if (!this.running) {
				return;
			}
			
			try {
				Thread.sleep(50L);
			}
			catch (InterruptedException e) {
			}
			
			if (this.isSkip(50)) {
				this.clearScreen(GameColor.BLACK);
				logo.flush();
				logo = null;
				return;
			}
		}
		
		// gradually disappeared
		alpha = 1.0f;
		dummyTimer.refresh();
		while (alpha > 0.0f) {
			do {
				if (!this.running) {
					return;
				}
				GameGraphics g = this.bsGraphics.getGraphics();
				
				g.setColor(GameColor.BLACK);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImageAlpha(logo, 0, 0, alpha);
				
			} while (this.bsGraphics.flip() == false);
			
			long elapsedTime = dummyTimer.sleep();
			double decrement = 0.00055 * elapsedTime;
			if (decrement > 0.15) {
				decrement = 0.15 + ((decrement - 0.04) / 2);
			}
			alpha -= decrement;
			
			if (this.isSkip(elapsedTime)) {
				this.clearScreen(GameColor.BLACK);
				logo.flush();
				logo = null;
				return;
			}
		}
		
		logo.flush();
		logo = null;
		dummyTimer.stopTimer();
		dummyTimer = null;
		
		// black wait before playing
		try {
			this.clearScreen(GameColor.BLACK);
			Thread.sleep(100L);
		}
		catch (InterruptedException e) {
		}		
	}
	
	private boolean isSkip(long elapsedTime) {
		boolean skip = (this.bsInput.getKeyPressed() != BaseInput.NO_KEY 
				|| this.bsInput.getMousePressed() != BaseInput.NO_BUTTON);
		this.bsInput.update(elapsedTime);
		
		return skip;
	}
	
	private void clearScreen(int col) {
		GameGraphics g = this.bsGraphics.getGraphics();
		g.setColor(col);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		this.bsGraphics.flip();
	}
}
