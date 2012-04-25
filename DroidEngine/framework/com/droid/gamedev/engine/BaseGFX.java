package com.droid.gamedev.engine;

import com.droid.gamedev.base.GameAppListner;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameRect;
import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * BaseGraphics interface provides 
 * all needed graphics function for drawing unto
 * screen.
 * 
 * @author Steph
 * 
 */
public interface BaseGFX {

	/**
	 * Returns the graphics tool
	 */
	GameGraphics getGraphics();
	
	/**
	 * Flips backbuffer to the screen (primary surface). Since most graphics
	 * engine backbuffer is VolatileImage type, thus the flipping data could be
	 * lost and need to be restored. Therefore, if this method return false,
	 * backbuffer need to be rerendered.
	 */
	boolean flip();
	
	/**
	 * Releases any system graphics resources and do finalization.
	 */
	void cleanup();
	
	/**
	 * Returns graphics engine dimension.
	 */
	GameRect getSize();
	
	/**
	 * Return the input engine associated to this GFX engine
	 */
	BaseInput getInput();
	
	
	/**
	 * Returns graphics engine description, for example: 
	 * fullscreen, windowed, fullscreen with bufferstrategy, etc.
	 * @return The engine description.
	 */
	String getGraphicsDescription();
	
	/**
	 * Sets graphics engine window title.
	 */
	void setWindowTitle(String title);
	
	/**
	 * Returns graphics engine window title or "" if
	 * setting window title is not supported. 
	 */
	String getWindowTitle();
	
	/**
	 * Sets graphics engine window icon image.
	 */
	void setWindowIcon(GameImage icon);
	
	/**
	 * Returns graphics engine window icon image or null if
	 * setting window icon image is not supported.
	 * @return The window icon.
	 */
	GameImage getWindowIcon();
	
	/**
	 * Setup the game AppListner object which raise
	 * Callback on application events
	 * @param listner
	 */
	void setGameAppListner(GameAppListner listner);
		
}
