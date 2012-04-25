package com.droid.gamedev.object.sprite;

import java.awt.image.BufferedImage;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * <code>AdvanceSprite</code> class is animated sprite that has status and
 * direction attributes, that way the animation is fully controlled by its
 * status and direction.
 * <p>
 * 
 * This class can be used directly, however this class meant to be subclassed.
 * The method that control the sprite animation and the one that need to be
 * override is {@link #animationChanged(int, int, int, int) animationChanged(int
 * oldStat, int oldDir, int status, int direction)}.
 * <p>
 * 
 * The <code>animationChanged</code> is the method that taking care the sprite
 * animation, everytime sprite {@link #setStatus(int) status} or
 * {@link #setDirection(int) direction} is being changed, the
 * <code>animationChanged</code> is called automatically, and this sprite need
 * to adjust its animation based on the new status and direction.
 * 
 * @see #setStatus(int)
 * @see #setDirection(int)
 * @see #animationChanged(int, int, int, int)
 */
public class AdvanceSprite extends AnimatedSprite {
	
	private int[] animationFrame;
	
	private int status = -1;
	private int direction = -1;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>AdvanceSprite</code> with specified images and
	 * location.
	 * 
	 * @param image sprite images
	 * @param x sprite x-coordinate
	 * @param y sprite y-coordinate
	 */
	public AdvanceSprite(GameImage[] image, double x, double y) {
		super(image, x, y);
	}
	
	/**
	 * Creates new <code>AdvanceSprite</code> with specified images and
	 * located at (0, 0).
	 * <p>
	 * 
	 * @see #setLocation(double, double)
	 */
	public AdvanceSprite(GameImage[] image) {
		super(image);
	}
	
	/**
	 * Creates new <code>AdvanceSprite</code> with specified location.
	 * 
	 * @param x sprite x-coordinate
	 * @param y sprite y-coordinate
	 */
	public AdvanceSprite(double x, double y) {
		super(x, y);
	}
	
	/**
	 * Creates new <code>AdvanceSprite</code> with null image and located at
	 * (0, 0).
	 * <p>
	 * 
	 * The sprite images must be set before rendering.
	 * 
	 * @see #setImages(BufferedImage[])
	 * @see #setLocation(double, double)
	 */
	public AdvanceSprite() {
		super();
	}
	
	/** ************************************************************************* */
	/** ************************ SETTING ANIMATION ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Sets sprite animation frame to specified animation array. The sprite will
	 * be animated according to the animation array.
	 * <p>
	 * 
	 * Use this if only the animation is not following the standard image
	 * frames, for example if the sprite walking the animation is 2-3-4-2-1,
	 * then set the animation frame to : setAnimationFrame(new int[] { 2, 3, 4,
	 * 2, 1 });
	 * 
	 * @see #setAnimationFrame(int, int)
	 */
	public void setAnimationFrame(int[] animation) {
		if (this.animationFrame != animation) {
			this.animationFrame = animation;
			
			this
			        .setAnimationFrame(
			                0,
			                (this.animationFrame != null) ? (this.animationFrame.length - 1)
			                        : (this.getImages().length - 1));
		}
	}
	
	/**
	 * Returns sprite animation frame or null if the sprite use standard
	 * animation frame.
	 */
	public int[] getAnimationFrame() {
		return this.animationFrame;
	}
	
	/** ************************************************************************* */
	/** ************************* IMAGE OPERATION ******************************* */
	/** ************************************************************************* */
	
	@Override
	public GameImage getImage() {
		return (this.animationFrame == null) ? super.getImage() : this
		        .getImage(this.animationFrame[this.getFrame()]);
	}
	
	@Override
	public void setImages(GameImage[] image) {
		super.setImages(image);
		
		if (this.animationFrame != null) {
			this.setAnimationFrame(0, this.animationFrame.length - 1);
		}
	}
	
	/** ************************************************************************* */
	/** ****************** NOTIFY STATUS / DIRECTION CHANGED ******************** */
	/** ************************************************************************* */
	
	/**
	 * The sprite status and/or direction is changed, set appropriate sprite
	 * animation.
	 * <p>
	 * 
	 * This method is responsible to set the sprite animation. By default this
	 * method do nothing.
	 * 
	 * @see #setAnimationFrame(int, int)
	 * @see #setAnimationFrame(int[])
	 * @see #setImages(BufferedImage[])
	 * @see #setAnimate(boolean)
	 * @see #setLoopAnim(boolean)
	 */
	protected void animationChanged(int oldStat, int oldDir, int status, int direction) {
	}
	
	/** ************************************************************************* */
	/** *********************** STATUS / DIRECTION ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Sets new sprite direction. Direction is the direction which the sprite is
	 * facing, like left, right, up, down, each direction has its own animation
	 * based on its status.
	 * <p>
	 * 
	 * If the sprite direction is changed (current direction != new direction),
	 * {@link #animationChanged(int, int, int, int)} will be notified.
	 * 
	 * @see #animationChanged(int, int, int, int)
	 * @see #setStatus(int)
	 */
	public void setDirection(int dir) {
		if (this.direction != dir) {
			int oldDir = this.direction;
			this.direction = dir;
			
			this.animationChanged(this.status, oldDir, this.status,
			        this.direction);
		}
	}
	
	/**
	 * Returns current sprite direction.
	 */
	public int getDirection() {
		return this.direction;
	}
	
	/**
	 * Sets new sprite status. Status is the sprite status no matter in which
	 * directional the sprite is facing, like walking status, standing status,
	 * jumping status.
	 * <p>
	 * 
	 * If the sprite status is changed (current status != new status),
	 * {@link #animationChanged(int, int, int, int)} will be notified.
	 * 
	 * @see #animationChanged(int, int, int, int)
	 * @see #setDirection(int)
	 */
	public void setStatus(int stat) {
		if (this.status != stat) {
			int oldStat = this.status;
			this.status = stat;
			
			this.animationChanged(oldStat, this.direction, this.status,
			        this.direction);
		}
	}
	
	/**
	 * Returns current sprite status.
	 */
	public int getStatus() {
		return this.status;
	}
	
	/**
	 * Sets sprite animation to specified status and direction. This method to
	 * simplify setting sprite status and direction at once.
	 * 
	 * @param stat the new sprite status
	 * @param dir the new sprite direction
	 * @see #animationChanged(int, int, int, int)
	 */
	public void setAnimation(int stat, int dir) {
		if (this.status != stat || this.direction != dir) {
			int oldStat = this.status;
			int oldDir = this.direction;
			
			this.status = stat;
			this.direction = dir;
			
			this.animationChanged(oldStat, oldDir, stat, dir);
		}
	}
	
	/** ************************************************************************* */
	/** ************************** RENDER SPRITE ******************************** */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g, int xs, int ys) {
		if (this.animationFrame != null) {
			g.drawImage(this.getImage(this.animationFrame[this.getFrame()]),
			        xs, ys);
			
		}
		else {
			super.render(g, xs, ys);
		}
	}
	
}
