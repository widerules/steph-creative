package com.droid.gamedev.object.sprite;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.object.Sprite;
import com.droid.gamedev.object.Timer;

/**
 * A TempSprite is a simple sprite that dissapear after a certain period
 * @author Steph
 *
 */
public class TempSprite extends Sprite{
	
	private Timer timerLive;
	
	/**
	 * Create a temporary sprite
	 * @param blood
	 * @param x
	 * @param y
	 * @param aliveTime : time in milliseconds
	 */
	public TempSprite(GameImage blood, double x, double y, int aliveTime) {
		super(blood, x, y);
		this.setLayer(1);
		this.timerLive = new Timer(aliveTime); 
	}
	
	@Override
	public void update(long elapsedTime) {
		if(timerLive.action(elapsedTime)){
			this.setActive(false);
		}
	}

}
