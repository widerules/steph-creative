package com.droid.gamedev.object;

import com.droid.gamedev.engine.graphics.GameGraphics;

/**
 * <code>GameFont</code> interface is an interface to draw text with alignment.
 */
public interface GameFont {
	
	/**
	 * Text alignment: left alignment.
	 */
	public static final int LEFT = 1;
	
	/**
	 * Text alignment: right alignment.
	 */
	public static final int RIGHT = 2;
	
	/**
	 * Text alignment: center alignment.
	 */
	public static final int CENTER = 3;
	
	/**
	 * Text alignment: justify alignment.
	 */
	public static final int JUSTIFY = 4;

	public static final int BOLD = 1; // = awt.Font.BOLD
	
	/** ************************************************************************* */
	/** *********************** DRAW STRING INTERFACES ************************** */
	/** ************************************************************************* */
	
	/**
	 * Draw a single line string into graphics context.
	 * 
	 * @param g the graphics context
	 * @param s the string to be drawn
	 * @param x the <code>x</code> screen coordinate to draw the text
	 * @param y the <code>y</code> screen coordinate to draw the text
	 * @return The right-edge of <code>x</code> coordinate to draw next text.
	 */
	public int drawString(GameGraphics g, String s, int x, int y);
	
	/**
	 * Draw a single line string into graphics context with specified alignment.
	 * 
	 * @param g the graphics context
	 * @param s the string to be drawn
	 * @param alignment text alignment: LEFT, RIGHT, CENTER, or JUSTIFY
	 * @param x the <code>x</code> screen coordinate to draw the text
	 * @param y the <code>y</code> screen coordinate to draw the text
	 * @param width width of the text
	 * @return The right-edge of <code>x</code> coordinate to draw next text.
	 */
	public int drawString(GameGraphics g, String s, int alignment, int x, int y, int width);
	
	/**
	 * Draw multiple line text into graphics context.
	 * <p>
	 * Example to write two paragraph text:
	 * 
	 * <pre>
	 * // creates bounding box, to ensure the paragraph exactly in the box
	 * g.drawRect(10, 10, 620, 100);
	 * int nexty = GameFont.drawText(g,
	 *         &quot;Paragraph one, sample paragraph using GameFont drawText.&quot;,
	 *         GameFont.LEFT, 10, 10, 620, 0, 50);
	 * GameFont.drawText(g,
	 *         &quot;Paragraph two, notice that each paragraph have 50 pixel indentation.&quot;,
	 *         GameFont.LEFT, // left alignment
	 *         10, // x
	 *         nexty, // y
	 *         620, // width
	 *         0, // no additional vertical spacing
	 *         50); // 50 pixel indentation
	 * </pre>
	 * 
	 * @param g graphics context where the text will be drawn
	 * @param text text to be drawn
	 * @param alignment text alignment: LEFT, RIGHT, CENTER, or JUSTIFY
	 * @param x text <code>x</code> coordinate
	 * @param y text <code>y</code> coordinate
	 * @param width width per line
	 * @param vspace additional vertical spacing, in pixel
	 * @param firstIndent first line indentation, in pixel
	 * @return The bottom-edge <code>y</code> coordinate to draw next
	 *         paragraph.
	 * 
	 * @see #LEFT
	 * @see #RIGHT
	 * @see #CENTER
	 * @see #JUSTIFY
	 */
	public int drawText(GameGraphics g, String text, int alignment, int x, int y, int width, int vspace, int firstIndent);
	
	/** ************************************************************************* */
	/** ******************* FONT PROPERTIES INTERFACES ************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns the width of <code>String st</code> in pixel.
	 * 
	 * @return String width, in pixel.
	 */
	public int getWidth(String st);
	
	/**
	 * Returns the width of <code>char c</code> in pixel.
	 * 
	 * @return Char width, in pixel.
	 */
	public int getWidth(char c);
	
	/**
	 * Returns the height of this font in pixel.
	 * 
	 * @return Font height, in pixel.
	 */
	public int getHeight();
	
	/**
	 * Returns whether the specified <code>char c</code> is available to draw
	 * by this game font.
	 */
	public boolean isAvailable(char c);

}
