package com.droid.gamedev.object.font;

import java.awt.Font;
import java.awt.FontMetrics;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.engine.graphics.win.GraphicsWin;
import com.droid.gamedev.engine.loader.ImageUtil;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.util.Log;

/**
 * SystemFont is standard AWT Font wrapped in game font interface
 * to be able to draw AWT Font in alignment and other technique.
 * 
 * @author Steph
 *
 */
public class SystemFont implements GameFont {
	
	private final Font font;
	private final FontMetrics fm;
	
	private int height;
	
	private int gap; // gap = awt font base line
	// text is drawn based on this base line
	private int color;
	
	/** ************************************************************************* */
	/** ***************************** CONSTRUCTOR ******************************* */
	/** ************************************************************************* */
	
	
	/**
	 * Creates newSystemFont with specified AWT Font, and color.
	 * 
	 * @param font AWT Font that used to draw this game font
	 * @param color the color to draw the text, or null if the color should
	 *        follow the graphics context active color
	 */
	public SystemFont(Font font, int color) {
		this.font = font;
		this.color = color;
		
		// dummy graphics only to get system font metrics
		GameImage img = ImageUtil.singleton.createImage(1, 1);
		GameGraphics g = img.createGraphics();
		
		
//		this.fm = g.getFontMetrics(font);
		this.fm = ((GraphicsWin)g).getFontMetrics(font);
		this.height = this.fm.getMaxAscent() + this.fm.getMaxDescent()
		        + this.fm.getLeading();
		this.gap = this.height - this.fm.getDescent();
		
		g.dispose();
	}
	
	/**
	 * Creates newSystemFont with specified AWT Font, and the
	 * color is following graphics context active color.
	 * 
	 * @param font AWT Font that used to draw this game font
	 */
	public SystemFont(Font font) {
		this(font, 0);
	}

	/** Create font from awt.Font */

	public SystemFont(String name, int style, int size) {
		this(new Font(name, style, size));
	}

	public SystemFont(String name, int style, int size, int col) {
		this(new Font(name, style, size), col);
	}

	/** ************************************************************************* */
	/** **************************** TEXT DRAWING ******************************* */
	/** ************************************************************************* */
	
	
	public int drawString(GameGraphics g, String s, int x, int y) {
		if (g.getFont() != this.font) {
			g.setFont(this);
		}
		if (this.color != 0) {
			g.setColor(this.color);
		}
		
		g.drawString(s, x, y + this.gap);
		
		return x + this.getWidth(s);
	}
	
	
	public int drawString(GameGraphics g, String s, int alignment, int x, int y, int width) {
		if (alignment == GameFont.LEFT) {
			return this.drawString(g, s, x, y);
			
		}
		else if (alignment == GameFont.CENTER) {
			return this.drawString(g, s, x + (width / 2)
			        - (this.getWidth(s) / 2), y);
			
		}
		else if (alignment == GameFont.RIGHT) {
			return this.drawString(g, s, x + width - this.getWidth(s), y);
			
		}
		else if (alignment == GameFont.JUSTIFY) {
			// calculate left width
			int mod = width - this.getWidth(s);
			if (mod <= 0) {
				// no width left, use standard draw string
				return this.drawString(g, s, x, y);
			}
			
			String st; // the next string to be drawn
			int len = s.length();
			int space = 0; // hold total space; hold space width in pixel
			int curpos = 0; // current string position
			int endpos = 0; // end string relative to curpos (end with ' ')
			
			// count total space
			while (curpos < len) {
				if (s.charAt(curpos++) == ' ') {
					space++;
				}
			}
			
			if (space > 0) {
				// width left plus with total space
				mod += space * this.getWidth(' ');
				
				// space width (in pixel) = width left / total space
				space = mod / space;
			}
			
			curpos = endpos = 0;
			while (curpos < len) {
				endpos = s.indexOf(' ', curpos); // find space
				if (endpos == -1) {
					endpos = len; // no space, draw all string directly
				}
				st = s.substring(curpos, endpos);
				
				this.drawString(g, st, x, y);
				
				x += this.getWidth(st) + space; // increase x-coordinate
				curpos = endpos + 1;
			}
			
			return x;
		}
		
		return 0;
	}
	
	
	public int drawText(GameGraphics g, String text, int alignment, int x, int y, int width, int vspace, int firstIndent) {
		boolean firstLine = true;
		
		int curpos, startpos, endpos, len = text.length();
		
		int posx = firstIndent;
		curpos = startpos = endpos = 0;
		char curChr;
		while (curpos < len) {
			curChr = text.charAt(curpos++);
			posx += this.getWidth(curChr);
			if (curChr - ' ' == 0) { // space
				endpos = curpos - 1;
			}
			
			if (posx >= width) {
				if (firstLine) {
					// draw first line with indentation
					this.drawString(g, text.substring(startpos, endpos),
					        alignment, (alignment == GameFont.RIGHT) ? x : x
					                + firstIndent, y, width - firstIndent);
					firstLine = false;
					
				}
				else {
					this.drawString(g, text.substring(startpos, endpos),
					        alignment, x, y, width);
					
				}
				
				y += this.getHeight() + vspace;
				
				posx = 0;
				startpos = curpos = endpos + 1;
			}
		}
		
		if (firstLine) {
			// only one line, draw with indent
			this.drawString(g, text.substring(startpos, curpos), alignment,
			        (alignment == GameFont.RIGHT) ? x : x + firstIndent, y,
			        width - firstIndent);
			
		}
		else if (posx != 0) {
			// drawing the last line
			this.drawString(g, text.substring(startpos, curpos), alignment,
			// (alignment == RIGHT) ? RIGHT : LEFT,
			        x, y, width);
			
		}
		
		return y + this.getHeight();
	}
	
	/** ************************************************************************* */
	/** ************************** FONT GET METHODS ***************************** */
	/** ************************************************************************* */
	
	/**
	 * Returns the AWT Font used to draw thisSystemFont.
	 */
	public Font getFont() {
		return this.font;
	}
	
	/**
	 * Returns the font metrics used to measure thisSystemFont.
	 */
	public FontMetrics getFontMetrics() {
		return this.fm;
	}
	
	/**
	 * Returns the color of this font, or null if the font is drawn following
	 * the graphics context active color.
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Sets the color of this font, or null to draw the font following the
	 * graphics context active color.
	 */
	public void setColor(int c) {
		this.color = c;
	}
	
	
	public int getWidth(String st) {
		return this.fm.stringWidth(st);
	}
	
	
	public int getWidth(char c) {
		return this.fm.charWidth(c);
	}
	
	
	public int getHeight() {
		return this.height;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + " " + "[basefont=" + this.font + ", color="
		        + this.color + "]";
	}
	
	
	public boolean isAvailable(char c) {
		return true;
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
}
