package com.droid.gamedev.engine.input;

import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.object.Timer;

/**
 * <code>KeyTyped</code> class is a class to simulate key typing. Key typed is
 * a key that pressed for some time and the key event is fired following
 * {@link #getRepeatDelay() initial repeat delay} and
 * {@link #getRepeatRate() repeat rate delay}.
 * 
 * @see #update(long)
 * @see #getKeyTyped()
 */
class KeyTyped {
	
	private BaseInput bsInput;
	
	private Timer repeatDelayTimer; // timer for starting repeat key
	private Timer repeatRateTimer; // timer for repeating repeat key
	
	private int key; // store last pressed key
	private int keyTyped; // currently typed key
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Constructs new <code>KeyTyped</code> using following input engine, and
	 * specified initial repeat delay and repeat rate delay.
	 * @param bsInput The {@link BaseInput} to use.
	 * @param repeatDelay The repeat delay.
	 * @param repeatRate The repeat rate.
	 */
	public KeyTyped(BaseInput bsInput, int repeatDelay, int repeatRate) {
		this.bsInput = bsInput;
		
		this.repeatDelayTimer = new Timer(repeatDelay);
		this.repeatRateTimer = new Timer(repeatRate);
		
		this.repeatDelayTimer.setActive(false);
		
		this.key = this.keyTyped = BaseInput.NO_KEY;
	}
	
	/**
	 * Constructs new <code>KeyTyped</code> with 450 ms repeat delay and 40 ms
	 * repeat rate.
	 * @param bsInput The {@link BaseInput} to use.
	 */
	public KeyTyped(BaseInput bsInput) {
		this(bsInput, 450, 40);
	}
	
	/** ************************************************************************* */
	/** ************************* UPDATE KEY TYPED ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Updates key typing.
	 * @param elapsedTime The elapsed time since last update.
	 */
	public void update(long elapsedTime) {
		this.keyTyped = this.bsInput.getKeyPressed();
		
		if (this.keyTyped != BaseInput.NO_KEY) {
			// save key code for repeat key implementation
			this.key = this.keyTyped;
			this.repeatDelayTimer.setActive(true);
			
		}
		else {
			// check whether repeat key has been released or not
			if (this.bsInput.getKeyReleased() == this.key) {
				// repeat key has been released
				this.key = BaseInput.NO_KEY;
				this.repeatDelayTimer.setActive(false);
				
			}
			else if (this.key != BaseInput.NO_KEY) {
				// check for first time repeatness
				if (this.repeatDelayTimer.isActive()) {
					// first time delay
					if (this.repeatDelayTimer.action(elapsedTime)) {
						this.repeatDelayTimer.setActive(false);
						this.repeatRateTimer.refresh();
						this.keyTyped = this.key;
					}
					
				}
				else {
					// repeat key
					if (this.repeatRateTimer.action(elapsedTime)) {
						this.keyTyped = this.key;
					}
				}
			}
		}
	}
	
	/**
	 * Refresh and clears key typing input.
	 */
	public void refresh() {
		this.repeatDelayTimer.refresh();
		this.repeatRateTimer.refresh();
		
		this.repeatDelayTimer.setActive(false);
		
		this.key = this.keyTyped = BaseInput.NO_KEY;
	}
	
	/** ************************************************************************* */
	/** ************************* GETTING KEY TYPED ***************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns key typed or {@link BaseInput#NO_KEY} if no key is being typed.
	 * @return The typed key or {@link BaseInput#NO_KEY}.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	public int getKeyTyped() {
		return this.keyTyped;
	}
	
	/**
	 * Returns true if the specified key is being typed.
	 * @param keyCode The code of the key to check.
	 * @return If the given key is pressed.
	 * @see java.awt.event.KeyEvent#VK_1
	 */
	public boolean isKeyTyped(int keyCode) {
		return (this.keyTyped == keyCode);
	}
	
	/** ************************************************************************* */
	/** ************************* REPEAT RATE DELAY ***************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns the key typed initial delay.
	 * @return The repeat delay.
	 * @see #getKeyTyped()
	 */
	public long getRepeatDelay() {
		return this.repeatDelayTimer.getDelay();
	}
	
	/**
	 * Sets the key typed initial delay.
	 * @param delay The repeat delay.
	 * @see #getKeyTyped()
	 */
	public void setRepeatDelay(long delay) {
		this.repeatDelayTimer.setDelay(delay);
	}
	
	/**
	 * Returns the key typed repeat rate delay.
	 * @return The repeat rate.
	 * @see #getKeyTyped()
	 */
	public long getRepeatRate() {
		return this.repeatRateTimer.getDelay();
	}
	
	/**
	 * Sets the key typed repeat rate delay.
	 * @param rate The repat rate.
	 * @see #getKeyTyped()
	 */
	public void setRepeatRate(long rate) {
		this.repeatRateTimer.setDelay(rate);
	}
	
}
