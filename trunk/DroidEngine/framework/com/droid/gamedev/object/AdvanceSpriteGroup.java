package com.droid.gamedev.object;

import java.util.Comparator;

import com.droid.gamedev.base.GameRect;
import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * Subclass of <code>SpriteGroup</code> that designed to update and render
 * visible on the screen sprites only.
 * <p>
 * 
 * In standard sprite group, all registered sprites in the group is updated,
 * rendered, and check for collision in every game loop. If the game has many
 * sprites and many of them are not visible, it is not efficient to update,
 * render, and check for collision all of the sprites.
 * <p>
 * 
 * <code>AdvanceSpriteGroup</code> is designed to optimize the sprite updating
 * and rendering by updating and rendering sprites that only visible on screen,
 * sprites that outside the game view area are not checked.
 * <p>
 * 
 * The main operation is storing sprites that
 * {@linkplain com.droid.gamedev.object.Sprite#isOnScreen() visible on screen}
 * into an inner sprite group, and the inner sprite group that will be update,
 * render, and check for collision.
 */
public class AdvanceSpriteGroup extends SpriteGroup {
	
	/**
	 * Inner sprite group that hold on screen sprites (inside view area sprites)
	 * of this group.
	 */
	private SpriteGroup onScreenGroup;
	
	private GameRect offset;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>AdvanceSpriteGroup</code> with specified name, and
	 * specified screen offset on each side.
	 */
	public AdvanceSpriteGroup(String name, int topOffset, int leftOffset,
	        int bottomOffset, int rightOffset) {
		super(name);
		
//		this.offset = new Insets(topOffset, leftOffset, bottomOffset,
//		        rightOffset);
		this.offset = new GameRect(leftOffset, topOffset, rightOffset-leftOffset, bottomOffset-topOffset);
		
		this.onScreenGroup = new SpriteGroup(name + " #ONSCREEN");
		this.onScreenGroup.setExpandFactor(50);
		// this group has done the scanning
		// on_screen_group no need to do the scanning anymore
		this.onScreenGroup.getScanFrequence().setActive(false);
	}
	
	/**
	 * Creates new <code>AdvanceSpriteGroup</code> with specified name, and
	 * specified screen offset.
	 */
	public AdvanceSpriteGroup(String name, int screenOffset) {
		this(name, screenOffset, screenOffset, screenOffset, screenOffset);
	}
	
	/**
	 * Creates new <code>AdvanceSpriteGroup</code> with specified name without
	 * screen offset (0, 0, 0, 0).
	 */
	public AdvanceSpriteGroup(String name) {
		this(name, 0);
	}
	
	/** ************************************************************************* */
	/** ************************* UPDATE THIS GROUP ***************************** */
	/** ************************************************************************* */
	
	@Override
	public void update(long elapsedTime) {
		// clear previous on screen sprites
		this.onScreenGroup.clear();
		
		// scanning on screen sprites
		Sprite[] s = this.getGroupSprites();
		int size = this.getGroupSize();
		
		for (int i = 0; i < size; i++) {
			if (s[i].isActive()
					&& s[i].isOnScreen(this.offset.x, this.offset.y,
							this.offset.x + this.offset.width, this.offset.y
									+ this.offset.height)) {
				this.onScreenGroup.add(s[i]);
			}
		}
		
		// update only on screen sprites
		this.onScreenGroup.update(elapsedTime);
		
		// schedule to scan inactive sprite
		// since we override update(), we must schedule this manually
		// or inactive sprites in this group will never be thrown !!
		if (this.getScanFrequence().action(elapsedTime)) {
			this.removeInactiveSprites();
		}
	}
	
	/** ************************************************************************* */
	/** ******************** RENDER TO GRAPHICS CONTEXT ************************* */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g) {
		// render only on screen sprites
		this.onScreenGroup.render(g);
	}
	
	/** ************************************************************************* */
	/** ************************** GROUP PROPERTIES ***************************** */
	/** ************************************************************************* */
	
	@Override
	public void setBackground(Background backgr) {
		super.setBackground(backgr);
		
		this.onScreenGroup.setBackground(backgr);
	}
	
	@Override
	public void setComparator(Comparator<Sprite> c) {
		super.setComparator(c);
		
		this.onScreenGroup.setComparator(c);
	}
	
	/**
	 * Returns screen offset of this group. Sprites that outside of screen
	 * bounds that still in this offset still categorized as on screen sprites.
	 */
	public GameRect getScreenOffset() {
		return this.offset;
	}
	
	/** ************************************************************************* */
	/** ************************* REMOVAL OPERATION ***************************** */
	/** ************************************************************************* */
	
	@Override
	public Sprite remove(int index) {
		Sprite s = super.remove(index);
		
		if (s != null) {
			this.onScreenGroup.remove(s);
		}
		
		return s;
	}
	
	@Override
	public boolean remove(Sprite s) {
		this.onScreenGroup.remove(s);
		
		return super.remove(s);
	}
	
	@Override
	public void clear() {
		super.clear();
		
		this.onScreenGroup.clear();
	}
	
	@Override
	public void reset() {
		super.reset();
		
		this.onScreenGroup.reset();
	}
	
	/** ************************************************************************* */
	/** ************************* ON-SCREEN SPRITES ***************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns all <i>on-screen sprites</i> (active, inactive, and also <b>null</b>
	 * sprite) in this group.
	 * 
	 * @see #getSize()
	 */
	@Override
	public Sprite[] getSprites() {
		return this.onScreenGroup.getSprites();
	}
	
	/**
	 * Returns total <b>non-null</b> <i>on-screen sprites</i> (active +
	 * inactive sprites) in this group.
	 */
	@Override
	public int getSize() {
		return this.onScreenGroup.getSize();
	}
	
	/** ************************************************************************* */
	/** *************************** SPRITES GETTER ****************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns all sprites (active, inactive, and also <b>null</b> sprite) in
	 * this group.
	 * 
	 * @see #getGroupSize()
	 */
	public Sprite[] getGroupSprites() {
		return super.getSprites();
	}
	
	/**
	 * Returns total <b>non-null</b> sprites (active + inactive) in this group.
	 */
	public int getGroupSize() {
		return super.getSize();
	}
	
}
