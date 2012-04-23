package com.droid.gamedev.object.background;

import java.awt.image.BufferedImage;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * The basic tiling background, creates a one layer background tile.
 * <p>
 * 
 * <code>TileBackground</code> takes up two parameter, the first one is a two
 * dimensional array of integer (int[][] tiles) that makes up the background
 * tiling, and the second one is the tiling image array (BufferedImage[]
 * tileImages).
 * <p>
 * 
 * This tile background is the basic subclass of
 * <code>AbstractTileBackground</code> that overrides <code>renderTile</code>
 * method to draw one layer tile background :
 * 
 * <pre>
 *    public void render(Graphics2D g, int tileX, int tileY, int x, int y) {
 *       //
 * <code>
 * tiles
 * </code>
 *  is the two dimensional background tiling
 *       int tile = tiles[tileX][tileY];
 *       if (tile &gt;= 0) {
 *          //
 * <code>
 * tileImages
 * </code>
 *  is the tiling images
 *          g.drawImage(tileImages[tile], x, y, null);
 *       }
 *    }
 * </pre>
 * 
 * To create multiple layer, simply subclass <code>AbstractTileBackground</code>
 * and override the <code>renderTile</code> method to render the tile multiple
 * times.
 * <p>
 * 
 * Tile background usage example :
 * 
 * <pre>
 * TileBackground background;
 * BufferedImage[] tileImages;
 * int[][] tiles = new int[40][30]; // 40 x 30 tiling
 * // fill tiles with random value
 * for (int i = 0; i &lt; tiles.length; i++)
 * 	for (int j = 0; j &lt; tiles[0].length; j++)
 * 		tiles[i][j] = getRandom(0, tileImages.length - 1);
 * // create the background
 * background = new TileBackground(tileImages, tiles);
 * </pre>
 * 
 * @see com.droid.gamedev.object.background.AbstractTileBackground
 */
public class TileBackground extends AbstractTileBackground {
		
	private transient GameImage[] tileImages;
	
	private int[][] tiles;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Creates new <code>TileBackground</code> with specified tile images and
	 * array of tiles.
	 * <p>
	 * 
	 * The array of tiles that makes up the background tiling, tiles[0][0] = 2
	 * means the tileImages[2] will be drawn on tile 0, 0 coordinate on the map.
	 * 
	 * @param tileImages an array of images for the tile
	 * @param tiles a two dimensional array that makes up the background
	 */
	public TileBackground(GameImage[] tileImages, int[][] tiles) {
		super(tiles.length, tiles[0].length, tileImages[0].getWidth(),
		        tileImages[0].getHeight());
		
		this.tileImages = tileImages;
		this.tiles = tiles;
	}
	
	/**
	 * Creates new <code>TileBackground</code> with specified tile images, as
	 * big as <code>horiz</code>, <code>vert</code> tiles.
	 * <p>
	 * 
	 * Generates tile background with tile as big as horiz and vert (tiles = new
	 * int[horiz][vert]) and using the first image of the tile images
	 * (tileImages[0]) for all the tiles.
	 * 
	 * @param tileImages an array of images for the tile
	 * @param horiz total horizontal tiles
	 * @param vert total vertical tiles
	 */
	public TileBackground(GameImage[] tileImages, int horiz, int vert) {
		this(tileImages, new int[horiz][vert]);
	}
	
	/** ************************************************************************* */
	/** ************************ RENDER BACKGROUND ****************************** */
	/** ************************************************************************* */
	
	@Override
	public void renderTile(GameGraphics  g, int tileX, int tileY, int x, int y) {
		int tile = this.tiles[tileX][tileY];
		
		if (tile >= 0) {
			g.drawImage(this.tileImages[tile], x, y);
			// g.drawString(tileX+","+tileY, x, y-20);
		}
	}
	
	/** ************************************************************************* */
	/** ************************* BACKGROUND TILE ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Return the tile background tile images.
	 */
	public GameImage[] getTileImages() {
		return this.tileImages;
	}
	
	/**
	 * Sets the tile background tile images.
	 */
	public void setTileImages(GameImage[] tileImages) {
		this.tileImages = tileImages;
		
		this.setTileSize(tileImages[0].getWidth(), tileImages[0].getHeight());
	}
	
	/**
	 * Returns the background tiling.
	 */
	public int[][] getTiles() {
		return this.tiles;
	}
	
	/**
	 * Sets the background tiling.
	 * <p>
	 * 
	 * This array of tiles that makes up the background tiling, tiles[0][0] = 2
	 * means the tileImages[2] will be drawn on tile 0, 0 coordinate on the map.
	 * 
	 * @see #setTileImages(BufferedImage[])
	 */
	public void setTiles(int[][] tiles) {
		this.tiles = tiles;
		
		super.setSize(tiles.length, tiles[0].length);
	}
	
	@Override
	public void setSize(int horiz, int vert) {
		if (horiz != this.tiles.length || vert != this.tiles[0].length) {
			// enlarge/shrink old tiles
			int[][] old = this.tiles;
			
			this.tiles = new int[horiz][vert];
			
			int minx = Math.min(this.tiles.length, old.length), miny = Math
			        .min(this.tiles[0].length, old[0].length);
			for (int j = 0; j < miny; j++) {
				for (int i = 0; i < minx; i++) {
					this.tiles[i][j] = old[i][j];
				}
			}
		}
		
		super.setSize(horiz, vert);
	}
	
}
