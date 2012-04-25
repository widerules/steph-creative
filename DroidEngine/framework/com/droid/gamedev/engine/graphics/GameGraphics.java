package com.droid.gamedev.engine.graphics;

import java.awt.Font;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.object.GameFont;

/**
 * Generic adapter to provide sophisticated control to the game view (GFX
 * Engine), over geometry, coordinate transformations, color management, and
 * text layout. 
 * equals to Graphics2D for Windowed platform 
 * equals to Canvas for Android
 * 
 * @author Steph
 * 
 */
public interface GameGraphics {
	
	/** get the width of the given string according to the current FontMetrics */
	int stringWidth(String str);
	
	/** get the string height of the FontMetrics */
	int stringHeight();
	

//	void addRenderingHints(Map<?, ?> hints);
//
//	void clip(Shape s);
//
//	void draw(Shape s);

//	void drawGlyphVector(GlyphVector g, float x, float y);
//
//	boolean drawImage(Image img, AffineTransform xform, ImageObserver obs);
//
//	void drawImage(BufferedImage img, BufferedImageOp op, int x, int y);
//
//	void drawRenderableImage(RenderableImage img, AffineTransform xform);
//
//	void drawRenderedImage(RenderedImage img, AffineTransform xform);
//
	void drawString(String str, int x, int y);
//
//	void drawString(String str, float x, float y);
//
//	void drawString(AttributedCharacterIterator iterator, int x, int y);
//
//	void drawString(AttributedCharacterIterator iterator, float x, float y);
//
//	void fill(Shape s);
//
//	Color getBackground();
//
//	Composite getComposite();
//
//	GraphicsConfiguration getDeviceConfiguration();
//
//	FontRenderContext getFontRenderContext();
//
//	Paint getPaint();
//
//	Object getRenderingHint(Key hintKey);
//
//	RenderingHints getRenderingHints();
//
//	Stroke getStroke();
//
//	AffineTransform getTransform();
//
//	boolean hit(Rectangle rect, Shape s, boolean onStroke);
//
//	void rotate(double theta);
//
//	void rotate(double theta, double x, double y);
//
//	void scale(double sx, double sy);
//
//	void setBackground(Color color);
//
//	void setComposite(Composite comp);
//
//	void setPaint(Paint paint);
//
//	void setRenderingHint(Key hintKey, Object hintValue);
//
//	void setRenderingHints(Map<?, ?> hints);
//
//	void setStroke(Stroke s);
//
//	void setTransform(AffineTransform Tx);
//
//	void shear(double shx, double shy);
//
//	void transform(AffineTransform Tx);
//
//	void translate(int x, int y);
//
//	void translate(double tx, double ty);
//
//	void clearRect(int x, int y, int width, int height);
//
//	void clipRect(int x, int y, int width, int height);
//
//	void copyArea(int x, int y, int width, int height, int dx, int dy);
//
//	Graphics create();
//
	void dispose();
//
//	void drawArc(int x, int y, int width, int height, int startAngle,
//			int arcAngle);
//
//	boolean drawImage(GameImage image, int x, int y, ImageObserver observer);

	/** just draw image at given coordinates */
	boolean drawImage(GameImage image, int x, int y);
	
	/** draw image with an alpha */
	boolean drawImageAlpha(GameImage image, int x, int y, float alpha);

	//
//	boolean drawImage(Image img, int x, int y, Color bgcolor,
//			ImageObserver observer);
//
//	boolean drawImage(Image img, int x, int y, int width, int height,
//			ImageObserver observer);
//
//	boolean drawImage(Image img, int x, int y, int width, int height,
//			Color bgcolor, ImageObserver observer);
//
	boolean drawImage(GameImage img, int dx1, int dy1, int dx2, int dy2, int sx1,
			int sy1, int sx2, int sy2);
//
//	boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1,
//			int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer);
//
//	void drawLine(int x1, int y1, int x2, int y2);
//
//	void drawOval(int x, int y, int width, int height);
//
//	void drawPolygon(int[] xPoints, int[] yPoints, int nPoints);
//
//	void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);
//
//	void drawRoundRect(int x, int y, int width, int height, int arcWidth,
//			int arcHeight);
//
//	void fillArc(int x, int y, int width, int height, int startAngle,
//			int arcAngle);
//
//	void fillOval(int x, int y, int width, int height);
//
//	void fillPolygon(int[] xPoints, int[] yPoints, int nPoints);
//
	void fillRect(int x, int y, int width, int height);
//
//	void fillRoundRect(int x, int y, int width, int height, int arcWidth,
//			int arcHeight);
//
//	Shape getClip();
//
//	Rectangle getClipBounds();
//
//	Color getColor();
//
	Font getFont();

//	FontMetrics getFontMetrics(Font f);

//	FontMetrics getFontMetrics();
	
//	void setClip(Shape clip);
//
//	void setClip(int x, int y, int width, int height);
//
	void setColor(int code);

	void setFont(GameFont systemFont);
//
//	void setPaintMode();
//
//	void setXORMode(Color c1);
	
	void drawRect(int x, int y, int width, int height);

}
