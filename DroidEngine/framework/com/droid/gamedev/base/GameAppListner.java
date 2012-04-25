package com.droid.gamedev.base;

/**
 * Each GFX Engine should handle a GameAppListner which raise an event when 
 * - focus is lost or gain 
 * - application is closed
 * 
 * @author Steph
 * 
 */
public interface GameAppListner {
	
	/**
	 * Callback when focus is gained
	 */
	void onFocusGained();

	/**
	 * Callback when fowus is lost
	 */
	void onFocusLost();
	
	/**
	 * Callback when application is closed
	 */
	void onAppClosed();

}
