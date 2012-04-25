package com.droid.gamedev;

import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.engine.BaseGFX;
import com.droid.gamedev.engine.BaseLoader;
import com.droid.gamedev.engine.audio.BaseAudioWin;
import com.droid.gamedev.engine.graphics.GameExitListener;
import com.droid.gamedev.engine.graphics.win.GFXWin;
import com.droid.gamedev.engine.graphics.win.GFXWinFullScreen;
import com.droid.gamedev.engine.io.BaseIOWin;
import com.droid.gamedev.engine.timer.SystemTimer;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.GameFontManager;
import com.droid.gamedev.util.ErrorNotifier;
import com.droid.gamedev.util.Log;

/**
 * Manages Game initialization and launch. 
 * Select here the platform where you want to run your game 
 *  
 * @author Steph
 */
public class GameLoader implements Runnable{
	
		
	public static final int WINDOW = 0;
	public static final int ANDROID = 1;
	static int platform = WINDOW;
	
	public static int getPlatform(){
		return GameLoader.platform;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}

	/**
	 * Graphics engine loaded by this GameLoader.
	 */
	BaseGFX gfx;	
	Game game;
	GameExitListener exitListner;
	
	
	/**
	 * Starts the game that have been loaded by this loader.
	 */
	public void start() {
		// graphics has been initialized, time to start the game
		if (this.gfx != null && this.game != null) {
			new Thread(this).start();
		}
	}
	
	public final void run() {
		Log.ic(this, "start game thread");
		this.game.start();
	}
	
	/**
	 * Stops the game from running, to resume the game call start() again.
	 */
	public final void stop() {
		Log.ic(this, "stop game");
		if (this.game != null) {
			this.game.stop();
		}
	}
	
	/**
	 * Exit game application and go back to system
	 */
	public final void finish() {
		Log.ic(this, "finish game");
		if (this.game != null) {
			this.game.finish();
		}
	}
	
	/**
	 * Initializes graphics engine for Windowed mode 
	 * with specified size, mode, bufferstrategy,
	 * and associates it with specified Game object.
	 * @param game
	 * @param width
	 * @param height
	 * @param fullscreen
	 * @param bufferstrategy
	 */
	public void setup(Game game, int width, int height, boolean fullscreen,
			boolean bufferstrategy) {

		// first thing is definitely use platform = Window
		GameLoader.platform = GameLoader.WINDOW;
		
		//associate the game
		this.game = game;
		
		// create the game exit listner
		this.exitListner = this.getExitListner();
		this.game.setExitListner(this.exitListner);

		// time to create the graphics engine
		try {
			if (fullscreen) {
				// full screen mode
				GFXWinFullScreen mode = new GFXWinFullScreen(width, height,
						bufferstrategy);
				this.gfx = mode;
			} 
			else {
				// windowed mode
				GFXWin mode = new GFXWin(width, height, bufferstrategy);
				this.gfx = mode;
			}
		} catch (Throwable e) {

			ErrorNotifier.notifyError("Graphics Engine Initialization", e);

			// fail-safe
			fullscreen = false;
			this.exitListner.onExit();
		}
		
		this.game.bsGraphics = this.gfx;

		// affect engines to the game
		initEngineWin();

		Log.ic(this, "setup done");
	}

	/**
	 * Initializes graphics engine with specified size, mode, using
	 * buffer strategy by default, and associates it with specified
	 * Game object.
	 * @param game
	 * @param width
	 * @param height
	 * @param fullscreen
	 */
	public final void setup(Game game, int width, int height, boolean fullscreen) {
		this.setup(game, width, height, fullscreen, true);
	}

	private GameExitListener getExitListner() {
		return new GameExitListener() {

			public void onExit() {
				// the game has finished its thread, 
				// time to go back to system properly
				try {
					Log.ic(this, "The game has finished");
//					System.exit(0);
					//application close automatically cause all threads achieved
				} catch (Exception e) {
				}
			}
		};
	}


	/** Initialize game engines for Windowed platform */
	private void initEngineWin(){
		if (this.game.bsTimer == null) {
			this.game.bsTimer = new SystemTimer();
		}
		if (this.game.bsIO == null) {
			this.game.bsIO = new BaseIOWin(this.game.getClass());
		}
		if (this.game.bsLoader == null) {
			this.game.bsLoader = new BaseLoader(this.game.bsIO, GameColor.MAGENTA);
		}
		if (this.game.bsInput == null) {
			this.game.bsInput = this.game.bsGraphics.getInput();
		}
		if (this.game.bsMusic == null) {
			this.game.bsMusic = new BaseAudioWin(this.game.bsIO);
			this.game.bsMusic.setExclusive(true);
		}
		if (this.game.bsSound == null) {
			this.game.bsSound = new BaseAudioWin(this.game.bsIO);
		}
		
		// set default FPS		
		this.game.bsTimer.setFPS(Game.DEFAULT_FPS);
		
		// set background screen size
		Background.screen = this.game.bsGraphics.getSize();
		
		// creates font manager
		if (this.game.fontManager == null) {
			this.game.fontManager = new GameFontManager();
		}	
		
	}
	
	
	
}