
package com.droid.gamedev.engine.loader;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.droid.gamedev.util.ErrorNotifier;

/**
 * Utility class for creating, loading, and manipulating image.
 */
class BufferedImageUtil {
	
	private static final GraphicsConfiguration CONFIG = GraphicsEnvironment
	        .getLocalGraphicsEnvironment().getDefaultScreenDevice()
	        .getDefaultConfiguration();
	
	private static final MediaTracker tracker;
	static {
		// dummy component
		Canvas canvas = new Canvas();
		
		// create media tracker
		tracker = new MediaTracker(canvas);
	}
	
	private static Composite composite;
	
	private BufferedImageUtil() {
	}
	
	/** ************************************************************************* */
	/** ********************* LOADING IMAGE OPERATION *************************** */
	/** ************************************************************************* */
	
	/**
	 * Here we return a default image on getImage fail
	 * @param e : the exception thown
	 */
	private static BufferedImage createDefaultImageOnException(Exception e){
		ErrorNotifier.notifyError("Image loading", e);
		return BufferedImageUtil.createImage(50, 50);
	}
	
	/**
	 * Loads an image from URL and specified transparency.
	 * 
	 * @param url image url
	 * @param transparency image transparency
	 * @return Loaded image.
	 * @see Transparency#OPAQUE
	 * @see Transparency#BITMASK
	 * @see Transparency#TRANSLUCENT
	 */
	public static BufferedImage getImage(URL url, int transparency){
		// if url==null show error notification and return default image
		if (url == null) {
			Exception e = new NullPointerException(
					"Image URL is null. will be replaced by default image");
			return BufferedImageUtil.createDefaultImageOnException(e);
		}
		
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(url);
			BufferedImageUtil.waitForResource(image);
			
			BufferedImage buffImage = BufferedImageUtil.createImage(image
			        .getWidth(null), image.getHeight(null), transparency);
			
			Graphics2D g = buffImage.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.drawImage(image, 0, 0, null);
			g.dispose();
			
			return buffImage;
		}
		catch (Exception e) {
//			System.err.println("ERROR: Unable to load Image = " + url);
//			e.printStackTrace();
//			return BufferedImageUtil.createImage(50, 50);
			return BufferedImageUtil.createDefaultImageOnException(e);
		}
	}
	
	/**
	 * Loads an image from URL without transparency (completely opaque).
	 * 
	 * @param url image url
	 * @return Loaded image.
	 */
	public static BufferedImage getImage(URL url) {
		return BufferedImageUtil.getImage(url, Transparency.OPAQUE);
	}
	
	/**
	 * Loads an image using the specified URL and masking color. This function
	 * will wait until the image has been loaded from file. Note: Using this
	 * function will always return a a new image loaded from file.
	 * 
	 * @param url image url
	 * @param keyColor masking color
	 * @return Loaded image.
	 */
	public static BufferedImage getImage(URL url, Color keyColor) {

		// if url==null show error notification and return default image
		if (url == null) {
			Exception e = new NullPointerException(
					"Image URL is null. will be replaced by default image");
			return BufferedImageUtil.createDefaultImageOnException(e);
		}
		
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(url);
			BufferedImageUtil.waitForResource(image);
			
			return BufferedImageUtil.applyMask(image, keyColor);
		}
		catch (Exception e) {
//			System.err.println("ERROR: Unable to load Image = " + url);
//			e.printStackTrace();
//			return BufferedImageUtil.createImage(50, 50);
			return BufferedImageUtil.createDefaultImageOnException(e);
		}
	}
	
	/**
	 * Loads and splits image from URL and transparency. The images will be
	 * splitted by specified column and row.
	 * 
	 * @param url image url
	 * @param col column
	 * @param row row
	 * @param transparency image transparency
	 * @return Loaded and splitted images.
	 * @see Transparency#OPAQUE
	 * @see Transparency#BITMASK
	 * @see Transparency#TRANSLUCENT
	 */
	public static BufferedImage[] getImages(URL url, int col, int row, int transparency) {
		return BufferedImageUtil.splitImages(BufferedImageUtil.getImage(url, transparency),
		        col, row);
	}
	
	/**
	 * Loads and splits image from URL, without transparency. The images will be
	 * splitted by specified column and row.
	 * 
	 * @param url image url
	 * @param col column
	 * @param row row
	 * @return Loaded and splitted images.
	 */
	public static BufferedImage[] getImages(URL url, int col, int row) {
		return BufferedImageUtil.splitImages(BufferedImageUtil.getImage(url), col, row);
	}
	
	/**
	 * Loads and splits image from URL with specified masking color. The images
	 * will be split by specified column and row.
	 * 
	 * @param url image url
	 * @param col column
	 * @param row row
	 * @param keyColor masking color
	 * @return Loaded and splitted images.
	 */
	public static BufferedImage[] getImages(URL url, int col, int row, Color keyColor) {
		return BufferedImageUtil.splitImages(BufferedImageUtil.getImage(url, keyColor), col,
		        row);
	}
	
	/**
	 * Creates blank image with specified width, height, and transparency.
	 * 
	 * @param width image width
	 * @param height image height
	 * @param transparency image transparency
	 * @return Blank image.
	 * @see Transparency#OPAQUE
	 * @see Transparency#BITMASK
	 * @see Transparency#TRANSLUCENT
	 */
	public static BufferedImage createImage(int width, int height, int transparency) {
		// return new BufferedImage(width, height, transparency);
		return BufferedImageUtil.CONFIG.createCompatibleImage(width, height, transparency);
	}
	
	/**
	 * Creates blank image with specified width, height, without transparency
	 * (opaque).
	 * 
	 * @param width image width
	 * @param height image height
	 * @return Blank image.
	 */
	public static BufferedImage createImage(int width, int height) {
		return BufferedImageUtil.createImage(width, height, Transparency.OPAQUE);
	}
	
	private static void waitForResource(Image image) throws Exception {
		if (image == null) {
			throw new NullPointerException("WaitForResource image is null");
		}
		
		try {
			BufferedImageUtil.tracker.addImage(image, 0);
			BufferedImageUtil.tracker.waitForAll();
			if ((BufferedImageUtil.tracker.statusID(0, true) & MediaTracker.ERRORED) != 0) {
				throw new RuntimeException();
			}
		}
		catch (Exception e) {
			BufferedImageUtil.tracker.removeImage(image, 0);
			throw e;
		}
	}
	
	/** ************************************************************************* */
	/** ********************** IMAGE MANIPULATION ******************************* */
	/** ************************************************************************* */
	
	/**
	 * Applying mask into image using specified masking color. Any Color in the
	 * image that matches the masking color will be converted to transparent.
	 * 
	 * @param img The source image
	 * @param keyColor Masking color
	 * @return Masked image
	 */
	public static BufferedImage applyMask(Image img, Color keyColor) {
		BufferedImage alpha = BufferedImageUtil.createImage(img.getWidth(null), img
		        .getHeight(null), Transparency.BITMASK);
		
		Graphics2D g = alpha.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		for (int y = 0; y < alpha.getHeight(); y++) {
			for (int x = 0; x < alpha.getWidth(); x++) {
				int col = alpha.getRGB(x, y);
				if (col == keyColor.getRGB()) {
					// make transparent
					alpha.setRGB(x, y, col & 0x00ffffff);
				}
			}
		}
		
		return alpha;
	}
	
	/**
	 * Splits a single image into an array of images. The image is cut by
	 * specified column and row.
	 * 
	 * @param image the source image
	 * @param col image column
	 * @param row image row
	 * @return Array of images cutted by specified column and row.
	 */
	public static BufferedImage[] splitImages(BufferedImage image, int col, int row) {
		int total = col * row; // total returned images
		int frame = 0; // frame counter
		int i, j; // i=column, j=row
		int w = image.getWidth() / col, h = image.getHeight() / row; // w=width,
		// h=height
		BufferedImage[] images = new BufferedImage[total];
		
		for (j = 0; j < row; j++) {
			for (i = 0; i < col; i++) {
				int transparency = image.getColorModel().getTransparency();
				images[frame] = BufferedImageUtil.createImage(w, h, transparency);
				Graphics2D g = images[frame].createGraphics();
				g.drawImage(image, 0, 0, w, h, // destination
				        i * w, j * h, (i + 1) * w, (j + 1) * h, // source
				        null);
				g.dispose();
				
				frame++;
			}
		}
		
		return images;
	}
	
	/**
	 * Rotates an image by specified angle (clockwise).
	 * <p>
	 * 
	 * For example: <br>
	 * 
	 * <pre>
	 * BufferedImage image;
	 * // rotate the image by 90 degree clockwise
	 * BufferedImage rotated = AWTImageUtil.rotate(image, 90);
	 * </pre>
	 * 
	 * @param src the source image
	 * @param angle angle rotation
	 * @return Rotated image.
	 */
	public static BufferedImage rotate(BufferedImage src, int angle) {
		int w = src.getWidth(), h = src.getHeight(), transparency = src
		        .getColorModel().getTransparency();
		BufferedImage image = BufferedImageUtil.createImage(w, h, transparency);
		
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.rotate(Math.toRadians(angle), w / 2, h / 2);
		g.drawImage(src, 0, 0, null);
		g.dispose();
		
		return image;
	}
	
	/**
	 * Resizes an image into specified size.
	 * <p>
	 * 
	 * For example: <br>
	 * 
	 * <pre>
	 * BufferedImage image;
	 * // resize the image to 200x300 size
	 * BufferedImage resized = AWTImageUtil.resize(image, 200, 300);
	 * // double the size of the image
	 * BufferedImage doubleResize = AWTImageUtil.resize(image, image.getWidth() * 2,
	 *         image.getHeight() * 2);
	 * </pre>
	 * 
	 * @param src the source image
	 * @param w width of the resized image
	 * @param h height of the resized image
	 * @return Resized image.
	 */
	public static BufferedImage resize(BufferedImage src, int w, int h) {
		int transparency = src.getColorModel().getTransparency();
		BufferedImage image = BufferedImageUtil.createImage(w, h, transparency);
		
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(src, 0, 0, w, h, // destination
		        0, 0, src.getWidth(), src.getHeight(), // source
		        null);
		g.dispose();
		
		return image;
	}
	
	/**
	 * Flips an image horizontally.
	 * 
	 * @param src the source image
	 * @return Horizontally flipped image.
	 */
	public static BufferedImage flipHorizontal(BufferedImage src) {
		int w = src.getWidth(), h = src.getHeight();
		BufferedImage flipped = BufferedImageUtil.createImage(w, h, src.getColorModel()
		        .getTransparency());
		Graphics2D g = flipped.createGraphics();
		g.drawImage(src, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		
		return flipped;
	}
	
	/**
	 * Flips an image vertically.
	 * 
	 * @param src the source image
	 * @return Vertically flipped image.
	 */
	public static BufferedImage flipVertical(BufferedImage src) {
		int w = src.getWidth(), h = src.getHeight();
		BufferedImage flipped = BufferedImageUtil.createImage(w, h, src.getColorModel()
		        .getTransparency());
		Graphics2D g = flipped.createGraphics();
		g.drawImage(src, 0, 0, w, h, 0, h, w, 0, null);
		g.dispose();
		
		return flipped;
	}
	
	/** ************************************************************************* */
	/** ************************** SAVING IMAGE ********************************* */
	/** ************************************************************************* */
	
	/**
	 * Saves image into specified file.
	 * 
	 * @param image image to be saved
	 * @param imagefile file where the image will be saved
	 */
	public static void saveImage(BufferedImage image, File imagefile) {
		try {
			ImageIO.write(image, "png", imagefile);
		}
		catch (IOException e) {
			System.err.println("ERROR: unable to save = " + imagefile);
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves image into byte array.
	 * 
	 * @param image image to be saved
	 * @param extension the image extension (png)
	 * @return Image in byte array.
	 * @see #fromByteArray(byte[])
	 */
	public static byte[] toByteArray(BufferedImage image, String extension) {
		if (image != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			try {
				ImageIO.write(image, extension, baos);
			}
			catch (IOException e) {
				throw new IllegalStateException(e.toString());
			}
			byte[] b = baos.toByteArray();
			
			return b;
		}
		
		return new byte[0];
	}
	
	/**
	 * Constructs image from byte array.
	 * 
	 * @param imagebytes image in byte array
	 * @return Image object.
	 * @see #toByteArray(BufferedImage, String)
	 */
	public static BufferedImage fromByteArray(byte[] imagebytes) {
		try {
			if (imagebytes != null && (imagebytes.length > 0)) {
				BufferedImage im = ImageIO.read(new ByteArrayInputStream(
				        imagebytes));
				return im;
			}
			
			return null;
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}
	
	public static void saveComposite(Graphics2D g) {
		BufferedImageUtil.composite = g.getComposite();
	}
	
	public static void loadComposite(Graphics2D g) {
		g.setComposite(BufferedImageUtil.composite);
		
		BufferedImageUtil.composite = null;
	}
	
	public static void setTransparent(Graphics2D g, float alpha) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		        alpha));
	}
	
	
}
