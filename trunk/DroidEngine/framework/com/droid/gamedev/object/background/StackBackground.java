package com.droid.gamedev.object.background;

import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;

/**
 * <code>StackBackground</code> class is a background composed by several
 * backgrounds.
 * <p>
 * 
 * This class automatically handles displaying and scrolling of the stacked
 * backgrounds. The backgrounds are normalized to the size and position of the
 * largest background in the stack. This way, the largest coordinate system is
 * presented, and all backgrounds move together at a smooth rate.
 * <p>
 * 
 * The backgrounds is rendered from the first background on the stack to the
 * last background on the stack, in other word the first background on the stack
 * will be at the back of other backgrounds.
 * <p>
 * 
 * Parallax background usage example : <br>
 * 
 * <pre>
 * StackBackground background;
 * Background bg1, bg2, bg3;
 * background = new StackBackground(new Background[] { bg1, bg2, bg3 });
 * // bg1 is at the back of bg2 and bg2 is at the back of bg3
 * </pre>
 */
public class StackBackground extends Background {
	
	private Background[] stack;
	private int total;
	
	/**
	 * Creates new <code>StackBackground</code>.
	 */
	public StackBackground(Background[] stack) {
		this.stack = stack;
		this.total = stack.length;
		
		this.normalizedView();
	}
	
	private void normalizedView() {
		// find the largest one!
		for (int i = 0; i < this.total; i++) {
			if (this.stack[i].getWidth() > this.getWidth()) {
				this.setSize(this.stack[i].getWidth(), this.getHeight());
			}
			
			if (this.stack[i].getHeight() > this.getHeight()) {
				this.setSize(this.getWidth(), this.stack[i].getHeight());
			}
		}
	}
	
	@Override
	public void setLocation(double xb, double yb) {
		super.setLocation(xb, yb);
		
		for (int i = 0; i < this.total; i++) {
			this.stack[i].setLocation(this.getX()
			        * (this.stack[i].getWidth() - this.getClip().width)
			        / (this.getWidth() - this.getClip().width), this.getY()
			        * (this.stack[i].getHeight() - this.getClip().height)
			        / (this.getHeight() - this.getClip().height));
		}
	}
	
	/** ************************************************************************* */
	/** **************** UPDATE AND RENDER STACKED BACKGROUND ******************* */
	/** ************************************************************************* */
	
	@Override
	public void update(long elapsedTime) {
		for (int i = 0; i < this.total; i++) {
			this.stack[i].update(elapsedTime);
		}
	}
	
	@Override
	public void render(GameGraphics g) {
		for (int i = 0; i < this.total; i++) {
			this.stack[i].render(g);
		}
	}
	
	/**
	 * Returns the stacked parallax backgrounds.
	 */
	public Background[] getStackBackground() {
		return this.stack;
	}
	
	/**
	 * Sets parallax background stacked backgrounds.
	 */
	public void setStackBackground(Background[] stack) {
		this.stack = stack;
		this.total = stack.length;
		
		this.normalizedView();
	}
	
}
