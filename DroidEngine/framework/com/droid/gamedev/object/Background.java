package com.droid.gamedev.object;

import com.droid.gamedev.base.GameRect;
import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * <code>Background</code> is the area where every sprites lived.
 * 
 * @author Steph
 * 
 */
public class Background  {
	
	/** Screen resolution dimension. 
	 * Used to determine background clipping area. */
	public static GameRect screen = new GameRect(640, 480);

	/** coordinates of the view port regarding background 
	 * (double precision) */
	protected double x, y;

	/** dimension */
	private int width, height;
	        
	/** view port regarding screen 
	 * (screen clipping) */
	private final GameRect clip; // 
	
	/** Singleton*/
	private static Background backgr;
	
	/**
	 * Returns the default background used by every newly created sprite.
	 */
	public static Background getDefaultBackground() {
		if (Background.backgr == null) {
			Background.backgr = new Background();
		}
		
		return Background.backgr;
	}
	
	/**
	 * Creates new Background with specified size, and default clipping area (as
	 * large as screen size).
	 */
	public Background(int w, int h) {
		this.x = this.y = 0;
		this.width = w;
		this.height = h;

		this.clip = new GameRect(0, 0, Background.screen.width,
				Background.screen.height);
	}
	
	/**
	 * Creates new Background, with size and clipping area as large as screen
	 * size.
	 * <p>
	 * 
	 * Clipping area is the view port of the background, anything outside
	 * clipping area will not be rendered.
	 */
	public Background() {
		this(Background.screen.width, Background.screen.height);
	}
	
	/**
	 * Returns the x coordinate of this background.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Returns the y coordinate of this background.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Returns the width of this background.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Returns the height of this background.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Sets the size of this background.
	 */
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		
		// revalidate position againts new size
		this.setLocation(this.x, this.y);
	}
	
	/**
	 * Sets background location to specified coordinate. The location is bounded
	 * to background boundary (0, 0, Background.getWidth(),
	 * Background.getHeight()).
	 * 
	 * @param xb the <code>x</code> coordinate of the background
	 * @param yb the <code>y</code> coordinate of the background
	 */
	public void setLocation(double xb, double yb) {
		// check background bounds
		if (xb > this.width - this.clip.width) {
			xb = this.width - this.clip.width; // right bound
		}
		if (yb > this.height - this.clip.height) {
			yb = this.height - this.clip.height; // bottom bound
		}
		if (xb < 0) {
			xb = 0; // left bound
		}
		if (yb < 0) {
			yb = 0; // top bound
		}
		
		this.x = xb;
		this.y = yb;
	}
	
	/**
	 * Moves background location by specified pixels.
	 */
	public void move(double dx, double dy) {
		this.setLocation(this.x + dx, this.y + dy);
	}
	
	/**
	 * Sets specified rectangle as the center of the background's viewport.
	 */
	public void setToCenter(int x, int y, int w, int h) {
		this.setLocation(x + (w / 2) - (this.clip.width / 2), y + (h / 2)
		        - (this.clip.height / 2));
	}
	
	/**
	 * Sets specified sprite as the center of the background's viewport.
	 */
	public void setToCenter(Sprite centered) {
		this.setToCenter((int) centered.getX(), (int) centered.getY(), centered
		        .getWidth(), centered.getHeight());
	}
	
	/** ************************************************************************* */
	/** ************************ BACKGROUND VIEWPORT **************************** */
	/** ************************************************************************* */
	
	/**
	 * Sets background clipping area (viewport). Clipping area is the viewport
	 * of the background, anything outside viewport area will not be rendered.
	 * <p>
	 * 
	 * By default background viewport is as large as screen size.
	 * 
	 * @see #screen
	 */
	public void setClip(int x, int y, int width, int height) {
		this.clip.setBounds(x, y, width, height);
	}
	
	/**
	 * Sets background clipping area (viewport). Clipping area is the viewport
	 * of the background, anything outside viewport area will not be rendered.
	 * <p>
	 * 
	 * By default background viewport is as large as screen size.
	 * 
	 * @see #screen
	 */
	public void setClip(GameRect r) {
		this.clip.setBounds(r);
	}
	
	/**
	 * Returns background screen viewport (clipping area), anything outside
	 * viewport area will not be rendered.
	 */
	public GameRect getClip() {
		return this.clip;
	}
	
	/**
	 * Updates this background, this method is usually used to create background
	 * animation or other special effect on the background.
	 * <p>
	 * 
	 * The implementation of this method provided by the <code>Background</code>
	 * class does nothing.
	 */
	public void update(long elapsedTime) {
	}
	
	/**
	 * Renders background to specified graphics context.
	 */
	public void render(GameGraphics g) {
		this.render(g, (int) this.x, (int) this.y, this.clip.x, this.clip.y,
		        (this.width < this.clip.width) ? this.width : this.clip.width,
		        (this.height < this.clip.height) ? this.height
		                : this.clip.height);
	}
	
	/**
	 * Renders background from specified position and clipping area to specified
	 * graphics context.
	 * <p>
	 * 
	 * This method to simplify background subclass rendering, the subclass only
	 * need to render the background from specified x, y coordinate with
	 * specified clipping area.
	 * <p>
	 * 
	 * For example: <br>
	 * 
	 * <pre>
	 * Background backgr;
	 * Graphics2D g;
	 * backgr.render(g, 100, 100, 5, 10, 100, 200);
	 * </pre>
	 * 
	 * Means the background must render itself from background coordinate 100,
	 * 100 to specified graphics context, starting from 5, 10 screen pixel as
	 * large as 100 x 200 dimension.
	 * 
	 * @param g graphics context
	 * @param xbg background x-coordinate
	 * @param ybg background y-coordinate
	 * @param x screen start x clipping
	 * @param y screen start y clipping
	 * @param w clipping width
	 * @param h clipping height
	 */
	public void render(GameGraphics g, int xbg, int ybg, int x, int y, int w, int h) {
	}
	
}
