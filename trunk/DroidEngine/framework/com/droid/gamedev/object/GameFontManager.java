package com.droid.gamedev.object;

import java.util.HashMap;
import java.util.Map;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.loader.ImageUtil;
import com.droid.gamedev.object.font.BitmapFont;
import com.droid.gamedev.object.font.SystemFont;
import com.droid.gamedev.util.Log;
import com.droid.gamedev.util.Utility;

/**
 * Simplify GameFont creation and also behave as the storage of
 * loaded font.
 * 
 * @author Steph
 *
 */
public class GameFontManager {
	
	private final Map<String, GameFont> fontBank = 
			new HashMap<String, GameFont>();

	public GameFontManager() {
	}

	/**
	 * Inserts font with specified name to font manager storage.
	 */
	public GameFont putFont(String name, GameFont font) {
		return this.fontBank.put(name, font);
	}

	/**
	 * Returns font with specified name.
	 */
	public GameFont getFont(String name) {
		return this.fontBank.get(name);
	}

	/**
	 * Removes font with specified name from font manager storage.
	 */
	public GameFont removeFont(String name) {
		return this.fontBank.remove(name);
	}

	/**
	 * Removed all loaded font from the storage.
	 */
	public void clear() {
		this.fontBank.clear();
	}

	/**
	 * Returns default AdvanceBitmapFont
	 * The images should be following this letter sequence :
	 * 
	 * <pre>
	 *         ! &quot; # $ % &amp; ' ( ) * + , - . / 0 1 2 3
	 *       4 5 6 7 8 9 : ; &lt; = &gt; ? @ A B C D E F G
	 *       H I J K L M N O P Q R S T U V W X Y Z [
	 *       \ ] &circ; _ a b c d e f g h i j k l m n o p
	 *       q r s t u v w x y z { | } &tilde;
	 * </pre>
	 * 
	 * How to: Creating <i>Bitmap Font Writer</i> Font <br>
	 * The image size shall be cut exactly according to the font size, but
	 * leaving one pixel row above the characters. <br>
	 * This row of pixels is used to define each characters width. <br>
	 * The first pixel (0,0) will be used as the font width delimiters.
	 * 
	 * @param bitmap the font images
	 * @return Bitmap GameFont.
	 */
	public GameFont createFont(GameImage bitmap) {		
		return new BitmapFont(this.cutLetter(bitmap));
	}
	
	/**
	 * Returns AdvanceBitmapFont
	 * 
	 * How to: Creating <i>Bitmap Font Writer</i> Font <br>
	 * The image size shall be cut exactly according to the font size, but
	 * leaving one pixel row above the characters. <br>
	 * This row of pixels is used to define each characters width. <br>
	 * The first pixel (0,0) will be used as the font width delimiters.
	 * 
	 * @param bitmap the font images
	 * @param letterSequence the letter sequence of the bitmap
	 * @return Bitmap GameFont.
	 */
	public GameFont createFont(GameImage bitmap, String letterSequence) {
		return new BitmapFont(this.cutLetter(bitmap), letterSequence);
	}
	
	private GameImage[] cutLetter(GameImage bitmap) {
		int delimiter = bitmap.getRGB(0, 0); // pixel <0,0> : delimiter
		int[] width = new int[100]; // assumption : 100 letter
		int ctr = 0;
		int last = 0; // last width point
		
		for (int i = 1; i < bitmap.getWidth(); i++) {
			if (bitmap.getRGB(i, 0) == delimiter) {
				// found delimiter
				width[ctr++] = i - last;
				last = i;
				
				if (ctr >= width.length) {
					width = (int[]) Utility.expand(width, 50);
				}
			}
		}
		
		// create bitmap font
		GameImage[] imagefont = new GameImage[ctr];
		int backgr = bitmap.getRGB(1, 0);
		int height = bitmap.getHeight() - 1;
		int w = 0;
		for (int i = 0; i < imagefont.length; i++) {
			imagefont[i] = ImageUtil.singleton.applyMask(bitmap.getSubimage(w, 1,
			        width[i], height), backgr);
			
			w += width[i];
		}
		
		return imagefont;
	}
	
	/**
	 * Returns bitmap font with specified images following this letter sequence :
	 * 
	 * <pre>
	 *         ! &quot; # $ % &amp; ' ( ) * + , - . / 0 1 2 3
	 *       4 5 6 7 8 9 : ; &lt; = &gt; ? @ A B C D E F G
	 *       H I J K L M N O P Q R S T U V W X Y Z [
	 *       \ ] &circ; _ a b c d e f g h i j k l m n o p
	 *       q r s t u v w x y z { | } &tilde;
	 * </pre>
	 * 
	 */
	public GameFont createFont(GameImage[] bitmap) {
		return new BitmapFont(bitmap);
	}

	/**
	 * Returns bitmap font with specified font images and letter sequence.
	 */
	public GameFont createFont(GameImage[] bitmap, String letterSequence) {
		return new BitmapFont(bitmap, letterSequence);
	}

	/** Returns a new SystemFont */
	public GameFont createFont(String name, int style, int size) {
		return new SystemFont(name, style, size);
	}

	/**
	 * Returns a new SystemFont with specified system font and color.
	 */
	public GameFont createFont(String name, int style, int size, int col) {
		if(col==0) {
			return new SystemFont(name, style, size);
		}
		else{
			return new SystemFont(name, style, size, col);
		}		
	}
	
	{
		// show log that an instance of this class has been created
		Log.i(this);
	}
	
}
