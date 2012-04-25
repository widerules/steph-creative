package com.droid.gamedev.engine;

/**
 * BaseInput interface provides all needed functions for polling
 * inputs : keyboard,  mouse, others.
 */
public interface BaseInput {
	
	/**
	 * Indicates no mouse button is being pressed.
	 */
	int NO_BUTTON = Integer.MIN_VALUE;
	
	/**
	 * Indicates no key is being pressed.
	 */
	int NO_KEY = Integer.MIN_VALUE;
	
	
	int MOUSE_BUTTON1 = 1;
	
	int MOUSE_BUTTON2 = 2;
	
	int MOUSE_BUTTON3 = 3;

	int KEY_ESC = 27;
	
	
	/** ************************************************************************* */
	/** ************************** UPDATE FUNCTION ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Updates input engine, this method need to be called in tight loop.
	 * @param elapsedTime The time elapsed since the last update.
	 */
	void update(long elapsedTime);
	
	/**
	 * Refresh all input actions to empty (clear input actions).
	 */
	void refresh();
	
	/**
	 * Releases any system resources hooked by this input engine.
	 */
	void cleanup();
	
	/** ************************************************************************* */
	/** ************************** MOUSE MOTION EVENT *************************** */
	/** ************************************************************************* */
	
	/**
	 * Move the mouse to x, y screen coordinate.
	 * 
	 * @param x the x-coordinate of the new mouse location
	 * @param y the y-coordinate of the new mouse location
	 */
	void mouseMove(int x, int y);
	
	/**
	 * Returns true, if the mouse pointer is in input component area.
	 * @return If the mouse is over the input component.
	 */
	boolean isMouseExists();
	
	/**
	 * Returns the mouse x-coordinate.
	 * @return The x location.
	 */
	int getMouseX();
	
	/**
	 * Returns the mouse y-coordinate.
	 * @return The y location.
	 */
	int getMouseY();
	
	/**
	 * Returns the delta of mouse x-coordinate.
	 * @return The x movement.
	 */
	int getMouseDX();
	
	/**
	 * Returns the delta of mouse y-coordinate.
	 * @return The y movement.
	 */
	int getMouseDY();
	
	/** ************************************************************************* */
	/** **************************** MOUSE EVENT ******************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns mouse button released or {@link #NO_BUTTON} if no button is being
	 * released.
	 * @return The released mouse button.
	 * @see java.awt.event.MouseEvent#BUTTON1
	 * @see java.awt.event.MouseEvent#BUTTON2
	 * @see java.awt.event.MouseEvent#BUTTON3
	 */
	int getMouseReleased();
	
	/**
	 * Returns true if the specified button is being released.
	 * 
	 * @param button the mouse button to be checked
	 * @return true, if the button is released.
	 * @see java.awt.event.MouseEvent#BUTTON1
	 * @see java.awt.event.MouseEvent#BUTTON2
	 * @see java.awt.event.MouseEvent#BUTTON3
	 */
	boolean isMouseReleased(int button);
	
	/**
	 * Returns mouse button pressed or {@link #NO_BUTTON} if no button is being
	 * pressed.
	 * @return The pressed mouse button.
	 * @see java.awt.event.MouseEvent#BUTTON1
	 * @see java.awt.event.MouseEvent#BUTTON2
	 * @see java.awt.event.MouseEvent#BUTTON3
	 */
	int getMousePressed();
	
	/**
	 * Returns true if the specified button is being pressed.
	 * 
	 * @param button the mouse button to be checked
	 * @return true, if the button is pressed.
	 * @see java.awt.event.MouseEvent#BUTTON1
	 * @see java.awt.event.MouseEvent#BUTTON2
	 * @see java.awt.event.MouseEvent#BUTTON3
	 */
	boolean isMousePressed(int button);
	
	/**
	 * Returns true if the specified button is being pressed.
	 * 
	 * @param button the mouse button to be checked
	 * @return true, if the button is being pressed.
	 * @see java.awt.event.MouseEvent#BUTTON1
	 * @see java.awt.event.MouseEvent#BUTTON2
	 * @see java.awt.event.MouseEvent#BUTTON3
	 */
	boolean isMouseDown(int button);
	
	/**
	 * Sets mouse pointer visible status.
	 * 
	 * @param visible true, show mouse pointer
	 */
	void setMouseVisible(boolean visible);
	
	/**
	 * Returns mouse pointer visible status.
	 * @return If the mouse is visible.
	 */
	boolean isMouseVisible();
	
	/** ************************************************************************* */
	/** ***************************** KEY EVENT ********************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns key released or {@link #NO_KEY} if no key is released.
	 * @return The key code of the released key.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	int getKeyReleased();
	
	/**
	 * Returns true if the specified key is released.
	 * 
	 * @param keyCode the key to be checked
	 * @return true, if the key is released.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	boolean isKeyReleased(int keyCode);
	
	/**
	 * Returns key pressed or {@link #NO_KEY} if no key is pressed.
	 * @return The key code of the pressed key.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	int getKeyPressed();
	
	/**
	 * Returns true if the specified key is pressed.
	 * 
	 * @param keyCode the key to be checked
	 * @return true, if the key is pressed.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	boolean isKeyPressed(int keyCode);
	
	/**
	 * Returns true if the specified key is being pressed.
	 * 
	 * @param keyCode the key to be checked
	 * @return true, if the key is being pressed.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	boolean isKeyDown(int keyCode);
	
	/**
	 * Returns key typed or {@link #NO_KEY} if no key is being typed. Key typed
	 * is key event that simulate key typing, key that fired following
	 * {@link #getRepeatDelay() initial repeat delay} and
	 * {@link #getRepeatRate() repeat rate delay}.
	 * @return The key code of the key typed.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	int getKeyTyped();
	
	/**
	 * Returns true if the specified key is being typed. Key typed is key event
	 * that simulate key typing, key that fired following
	 * {@link #getRepeatDelay() initial repeat delay} and
	 * {@link #getRepeatRate() repeat rate delay}.
	 * @param keyCode The key code of the key to check.
	 * @return If the key with the given key code is typed.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	boolean isKeyTyped(int keyCode);
	
	/**
	 * Returns the delay for repeating key typed.
	 * @return The repeat delay.
	 * @see #getKeyTyped()
	 */
	long getRepeatDelay();
	
	/**
	 * Sets the initial delay for repeating key typed.
	 * @param delay The new repeat delay.
	 * @see #getKeyTyped()
	 */
	void setRepeatDelay(long delay);
	
	/**
	 * Returns the repeat rate delay for repeating key typed.
	 * @return The repeat rate.
	 * @see #getKeyTyped()
	 */
	long getRepeatRate();
	
	/**
	 * Sets the repeat rate delay for repeating key typed.
	 * @param rate The new repeat rate.
	 * @see #getKeyTyped()
	 */
	void setRepeatRate(long rate);
	
}
