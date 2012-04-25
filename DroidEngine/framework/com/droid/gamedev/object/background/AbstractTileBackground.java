package com.droid.gamedev.object.background;

import com.droid.gamedev.base.GamePoint;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;

/**
 * The base abstract class to create tiling background, the subclass need to
 * perform the background tile rendering.
 */
public abstract class AbstractTileBackground extends Background {
	
	private int tileWidth, tileHeight;
		
	private int tileX, tileY; // background <x, y> in tiles
	private int offsetX, offsetY; // offset/exceed tiles for smooth scrolling
	        
	private int horiz, vert;
	
	private GamePoint point = new GamePoint(); // return value for getTileAt(...)
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>AbstractTileBackground</code> as big as
	 * <code>horiz</code>, <code>vert</code> tiles, where each tile is as
	 * big as <code>tileWidth</code>, <code>tileHeight</code>.
	 * <p>
	 * 
	 * The actual dimension of the background is : <br>
	 * width = horiz*tileWidth, height = vert*tileHeight.
	 */
	public AbstractTileBackground(int horiz, int vert, int tileWidth,
	        int tileHeight) {
		super(horiz * tileWidth, vert * tileHeight);
		
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		this.horiz = horiz;
		this.vert = vert;
		
		this.tileX = this.tileY = 0;
		this.offsetX = this.offsetY = 0;
	}
	
	/** ************************************************************************* */
	/** ************************ RENDER BACKGROUND ****************************** */
	/** ************************************************************************* */
	
	@Override
	public void render(GameGraphics g, int xbg, int ybg, int x, int y, int w, int h) {
		int x1 = 0, // coordinate counter
		y1 = 0;
		int x2 = x + w, // right boundary
		y2 = y + h; // bottom boundary
		
		int xTile = 0; // tile counter
		int yTile = this.tileY;
		
		for (y1 = y - this.offsetY; y1 < y2; y1 += this.tileHeight) {
			xTile = this.tileX;
			for (x1 = x - this.offsetX; x1 < x2; x1 += this.tileWidth) {
				this.renderTile(g, xTile, yTile, x1, y1);
				xTile++;
			}
			yTile++;
		}
	}
	
	/**
	 * Renders tile at <code>tileX</code>, <code>tileY</code> position to
	 * specified <code>x</code>, <code>y</code> coordinate.
	 */
	public abstract void renderTile(GameGraphics g, int tileX, int tileY, int x, int y);
	
	/** ************************************************************************* */
	/** ************************ BACKGROUND POSITION **************************** */
	/** ************************************************************************* */
	
	@Override
	public void setLocation(double xb, double yb) {
		int oldx = (int) this.getX(), oldy = (int) this.getY();
		
		super.setLocation(xb, yb);
		
		int x = (int) this.getX(), y = (int) this.getY();
		if (x == oldx && y == oldy) {
			// position is not changed
			return;
		}
		
		// convert <x, y> into tiles
		this.tileX = x / this.tileWidth;
		this.tileY = y / this.tileHeight;
		
		this.offsetX = x % this.tileWidth;
		this.offsetY = y % this.tileHeight;
	}
	
	/**
	 * Sets the background location to specified tile.
	 */
	public void setTileLocation(int xs, int ys) {
		this.setLocation(xs * this.tileWidth, ys * this.tileHeight);
	}
	
	/**
	 * Returns current tile-x position.
	 */
	public int getTileX() {
		return this.tileX;
	}
	
	/**
	 * Returns current tile-y position.
	 */
	public int getTileY() {
		return this.tileY;
	}
	
	/**
	 * Returns background tile position of specified coordinate or null if the
	 * coordinate is out of background viewport/boundary.
	 * <p>
	 * 
	 * Used to detect mouse position on this background, for example : <br>
	 * Drawing rectangle at mouse cursor position
	 * 
	 * <pre>
	 * public class YourGame extends Game {
	 * 	
	 * 	AbstractTileBackground bg;
	 * 	
	 * 	public void render(Graphics2D g) {
	 * 		Point tileAt = bg.getTileAt(getMouseX(), getMouseY());
	 * 		if (tileAt != null) {
	 * 			// mouse cursor is in background area
	 * 			// draw pointed tile
	 * 			// convert tile to coordinate
	 * 			int posX = (tileAt.x - bg.getTileX()) * bg.getTileWidth(), posY = (tileAt.y - bg
	 * 			        .getTileY())
	 * 			        * bg.getTileHeight();
	 * 			g.setColor(Color.WHITE);
	 * 			g.drawRect(posX - bg.getOffsetX() + bg.getClip().x, posY
	 * 			        - bg.getOffsetY() + bg.getClip().y, bg.getTileWidth(), bg
	 * 			        .getTileHeight());
	 * 		}
	 * 	}
	 * }
	 * </pre>
	 */
	public GamePoint getTileAt(double screenX, double screenY) {
		if (screenX < this.getClip().x
		        || screenX > this.getClip().x + this.getClip().width
		        || screenY < this.getClip().y
		        || screenY > this.getClip().y + this.getClip().height) {
			// out of background view port
			return null;
		}
		
		this.point.x = (int) (this.getX() + screenX - this.getClip().x)
		        / this.tileWidth;
		this.point.y = (int) (this.getY() + screenY - this.getClip().y)
		        / this.tileHeight;
		
		return this.point;
	}
	
	/**
	 * Returns current background tile offset-x.
	 * <p>
	 * 
	 * This offset value that makes smooth scrolling on the background tile.
	 */
	public int getOffsetX() {
		return this.offsetX;
	}
	
	/**
	 * Returns current background tile offset-y.
	 * <p>
	 * 
	 * This offset value that makes smooth scrolling on the background tile.
	 */
	public int getOffsetY() {
		return this.offsetY;
	}
	
	/** ************************************************************************* */
	/** ************************* TILE PROPERTIES ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Returns the width of the tile.
	 */
	public int getTileWidth() {
		return this.tileWidth;
	}
	
	/**
	 * Returns the height of the tile.
	 */
	public int getTileHeight() {
		return this.tileHeight;
	}
	
	/**
	 * Sets the background tile size.
	 */
	protected void setTileSize(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		super.setSize(this.horiz * this.getTileWidth(), this.vert
		        * this.getTileHeight());
	}
	
	/**
	 * Returns background total horizontal tiles.
	 */
	public int getTotalHorizontalTiles() {
		return this.horiz;
	}
	
	/**
	 * Returns background total vertical tiles.
	 */
	public int getTotalVerticalTiles() {
		return this.vert;
	}
	
	/**
	 * Sets the size of this background (in tiles).
	 * 
	 * @param horiz total background horizontal tiles
	 * @param vert total background vertical tiles
	 */
	@Override
	public void setSize(int horiz, int vert) {
		this.horiz = horiz;
		this.vert = vert;
		
		super.setSize(horiz * this.getTileWidth(), vert * this.getTileHeight());
	}
	
}
