package com.droid.gamedev.engine.graphics.win;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameImageWin;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.object.font.SystemFont;
import com.droid.gamedev.util.Log;

/**
 * Graphics tool for Windows
 * 
 * @author Steph
 * 
 */
public class GraphicsWin implements GameGraphics {

	private Graphics2D g;

	public GraphicsWin(Graphics2D g) {
		this.g = g;
	}

	public void setFont(GameFont font) {
		Font f = ((SystemFont) font).getFont();
		this.g.setFont(f);
	}

	private FontMetrics getFontMetrics() {
		return this.g.getFontMetrics();
	}

	public void drawString(String str, int x, int y) {
		this.g.drawString(str, x, y);
	}

	public void dispose() {
		this.g.dispose();
	}

	public boolean drawImage(GameImage img, int x, int y) {
		BufferedImage buffImg = ((GameImageWin) img).getBufferedImage();
		return this.g.drawImage(buffImg, x, y, null);
	}

	public boolean drawImageAlpha(GameImage image, int x, int y, float alpha) {
		Composite old = this.g.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha);
		this.g.setComposite(ac);
		this.drawImage(image, x, y);
		this.g.setComposite(old);
		return true;
	}

	public void fillRect(int x, int y, int width, int height) {
		this.g.fillRect(x, y, width, height);
	}

	public Font getFont() {
		return this.g.getFont();
	}

	/** used in SystemFont */
	public FontMetrics getFontMetrics(Font f) {
		return this.g.getFontMetrics(f);
	}

	public void setColor(int code) {
		Color color = new Color(code);
		this.g.setColor(color);
	}

	public void drawRect(int x, int y, int width, int height) {
		this.g.drawRect(x, y, width, height);
	}

	public boolean drawImage(GameImage img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2) {
		BufferedImage buffImg = ((GameImageWin) img).getBufferedImage();
		return this.g.drawImage(buffImg, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
				sy2, null);
	}

	public int stringWidth(String str) {
		return this.getFontMetrics().stringWidth(str);
	}

	public int stringHeight() {
		return this.getFontMetrics().getHeight();
	}

	{
		// show log that an instance of this class has been created
		Log.i(this);
	}


}
