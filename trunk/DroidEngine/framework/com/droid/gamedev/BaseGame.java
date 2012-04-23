package com.droid.gamedev;

import java.io.File;
import java.util.Locale;

import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.BaseAudio;
import com.droid.gamedev.engine.BaseGFX;
import com.droid.gamedev.engine.BaseIO;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.BaseTimer;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.engine.loader.BaseLoaderWin;
import com.droid.gamedev.engine.loader.ImageUtil;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.GameFontManager;
import com.droid.gamedev.object.PlayField;
import com.droid.gamedev.object.Sprite;
import com.droid.gamedev.object.SpriteGroup;
import com.droid.gamedev.util.Log;
import com.droid.gamedev.util.Utility;

/**
 * Game architecture
 * 
 * @author Steph
 */
abstract class BaseGame{
	
	/** Graphics engine. */
	protected BaseGFX bsGraphics;
	
	/** I/O file engine. */
	protected BaseIO bsIO;
	
	/** Image loader engine. */
	protected BaseLoaderWin bsLoader;
	
	/** Input engine. */
	protected BaseInput bsInput;
	
	/** Timer engine. */
	protected BaseTimer bsTimer;
	
	/** Audio engine for music. */
	protected BaseAudio bsMusic;
	
	/** Audio engine for sound. */
	protected BaseAudio bsSound;

	/** Font manager. */
	protected GameFontManager fontManager;
	
	/** Object represents a specific geographical region */
	private Locale locale;
	
	
	/**
	 * Starts the game main loop, this method will not return until the game is
	 * finished playing/running. To stop the game use either finish()
	 * to quit the game or stop() to hold the game.
	 */
	abstract void start();
	
	/**
	 * End the game and back to operating system.
	 */
	public abstract void finish();
		
	
	/**
	 * All game resources initialization, everything that usually goes to
	 * constructor should be put in here.
	 */
	public abstract void initResources();
	
	/**
	 * Updates game objects.
	 */
	public abstract void update(long elapsedTime);
	
	/**
	 * Renders game to the screen.
	 */
	public abstract void render(GameGraphics g);
			

	public int getRandom(int low, int hi) {
		return Utility.getRandom(low, hi);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public int getWidth() {
		return this.bsGraphics.getSize().width;
	}
	
	public int getHeight() {
		return this.bsGraphics.getSize().height;
	}
	
	public GameImage takeScreenShot() {
		
		GameImage screen =	ImageUtil.singleton.createImage(
				this.getWidth(), this.getHeight(), GameColor.OPAQUE);
		GameGraphics g = screen.createGraphics();
		this.render(g);
		g.dispose();
		
		return screen;
	}
	
	/**
	 * Captures current game screen into specified file.
	 */
	public void takeScreenShot(File f) {
		ImageUtil.singleton.saveImage(this.takeScreenShot(), f);
	}
	
	public int playMusic(String audiofile) {
		return this.bsMusic.play(audiofile);
	}
	
	public int playSound(String audiofile) {
		return this.bsSound.play(audiofile);
	}
	
	public void setFPS(int fps) {
		this.bsTimer.setFPS(fps);
	}
	
	public int getCurrentFPS() {
		return this.bsTimer.getCurrentFPS();
	}
	
	public int getFPS() {
		return this.bsTimer.getFPS();
	}
	
	public void drawFPS(GameGraphics g, int x, int y) {
		this.fontManager.getFont("FPS Font").drawString(g,
		        "FPS = " + this.getCurrentFPS() + "/" + this.getFPS(), x, y);
	}
	
	public int getMouseX() {
		return this.bsInput.getMouseX();
	}
	
	public int getMouseY() {
		return this.bsInput.getMouseY();
	}
	
	/**
	 * Returns whether the mouse pointer is inside specified screen boundary.
	 */
	public boolean checkPosMouse(int x1, int y1, int x2, int y2) {
		return (this.getMouseX() >= x1 && this.getMouseY() >= y1
		        && this.getMouseX() <= x2 && this.getMouseY() <= y2);
	}
	
	/**
	 * Returns whether the mouse pointer is inside specified sprite boundary.
	 */
	public boolean checkPosMouse(Sprite sprite, boolean pixelCheck) {
		Background bg = sprite.getBackground();
		
		// check whether the mouse is in background clip area
		if (this.getMouseX() < bg.getClip().x
		        || this.getMouseY() < bg.getClip().y
		        || this.getMouseX() > bg.getClip().x + bg.getClip().width
		        || this.getMouseY() > bg.getClip().y + bg.getClip().height) {
			return false;
		}
		
		double mosx = this.getMouseX() + bg.getX() - bg.getClip().x;
		double mosy = this.getMouseY() + bg.getY() - bg.getClip().y;
		
		if (pixelCheck) {
			try {
				return ((sprite.getImage().getRGB((int) (mosx - sprite.getX()),
				        (int) (mosy - sprite.getY())) & 0xFF000000) != 0x00);
			}
			catch (Exception e) {
				return false;
			}
			
		}
		else {
			return (mosx >= sprite.getX() && mosy >= sprite.getY()
			        && mosx <= sprite.getX() + sprite.getWidth() && mosy <= sprite
			        .getY()
			        + sprite.getHeight());
		}
	}
	
	/**
	 * Returns sprite in specified sprite group that intersected with mouse
	 * pointer, or null if no sprite intersected with mouse pointer.
	 */
	public Sprite checkPosMouse(SpriteGroup group, boolean pixelCheck) {
		Sprite[] sprites = group.getSprites();
		int size = group.getSize();
		
		for (int i = 0; i < size; i++) {
			if (sprites[i].isActive()
			        && this.checkPosMouse(sprites[i], pixelCheck)) {
				return sprites[i];
			}
		}
		
		return null;
	}
	
	/**
	 * Returns sprite in specified playfield that intersected with mouse
	 * pointer, or null if no sprite intersected with mouse pointer.
	 */
	public Sprite checkPosMouse(PlayField field, boolean pixelCheck) {
		SpriteGroup[] groups = field.getGroups();
		int size = groups.length;
		
		for (int i = 0; i < size; i++) {
			if (groups[i].isActive()) {
				Sprite s = this.checkPosMouse(groups[i], pixelCheck);
				if (s != null) {
					return s;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Check if left mouse button  clicked
	 */
	public boolean click() {
		return this.bsInput.isMousePressed(BaseInput.MOUSE_BUTTON1);
	}
	
	/**
	 * Check if right mouse button  clicked
	 */
	public boolean rightClick() {
		return this.bsInput.isMousePressed(BaseInput.MOUSE_BUTTON3);
	}
	
	/**
	 * Check if key is being pressed
	 */
	public boolean keyDown(int keyCode) {
		return this.bsInput.isKeyDown(keyCode);
	}
	
	public boolean keyPressed(int keyCode) {
		return this.bsInput.isKeyPressed(keyCode);
	}
	
	public void hideCursor() {
		this.bsInput.setMouseVisible(false);
	}
	
	public void showCursor() {
		this.bsInput.setMouseVisible(true);
	}
	
	public void setMaskColor(int c) {
		this.bsLoader.setMaskColor(c);
	}
	
	public GameImage getImage(String imagefile, boolean useMask) {
		return this.bsLoader.getImage(imagefile, useMask);
	}
	public GameImage getImage(String imagefile) {
		return this.bsLoader.getImage(imagefile);
	}
	
	public GameImage[] getImages(String imagefile, int col, int row, boolean useMask) {
		return this.bsLoader.getImages(imagefile, col, row, useMask);
	}
	
	public GameImage[] getImages(String imagefile, int col, int row) {
		return this.bsLoader.getImages(imagefile, col, row);
	}
	
	public GameImage[] getImages(String imagefile, int col, int row, boolean useMask, String sequence, int digit) {
		String mapping = imagefile + sequence + digit;
		GameImage[] image = this.bsLoader.getStoredImages(mapping);
		
		if (image == null) {
			GameImage[] src = this.getImages(imagefile, col, row, useMask);
			int count = sequence.length() / digit;
			image = new GameImage[count];
			for (int i = 0; i < count; i++) {
				image[i] = src[Integer.parseInt(sequence.substring(i * digit,
				        ((i + 1) * digit)))];
			}
			this.bsLoader.storeImages(mapping, image);
		}
		
		return image;
	}
	
	public GameImage[] getImages(String imagefile, int col, int row, String sequence, int digit) {
		return this.getImages(imagefile, col, row, true, sequence, digit);
	}
	
	/**
	 * Returns stripped images with cropped sequence.
	 */
	public GameImage[] getImages(String imagefile, int col, int row, boolean useMask, int start, int end) {
		String mapping = start + imagefile + end;
		GameImage[] image = this.bsLoader.getStoredImages(mapping);
		
		if (image == null) {
			GameImage[] src = this.getImages(imagefile, col, row, useMask);
			int count = end - start + 1;
			image = new GameImage[count];
			for (int i = 0; i < count; i++) {
				image[i] = src[start + i];
			}
			this.bsLoader.storeImages(mapping, image);
		}
		
		return image;
	}
	
	public GameImage[] getImages(String imagefile, int col, int row, int start, int end) {
		return this.getImages(imagefile, col, row, true, start, end);
	}
	
	/**
	 * Callback on starting game since developer doesn't have access
	 * to start() method.
	 * Useful for launching music for example
	 */
	public void onStart(){	
		// do nothing
	}
	
	/** callback game stopped */
	public void onStop() {
		// do nothing
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
