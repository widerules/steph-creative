package com.droid.test.contamination;

import java.util.Hashtable;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * Describe a sprite loaded from a bitmap and a XML file
 * 
 * @author Steph
 * 
 */
public class SpriteInfo {

	/** name of the sprite */
	public String name;

	/** color filter */
	public int colorFilter;

	/** animation list */
	public Hashtable<String, AnimationSequence> animations;

	/** Load a sprite information from an XML file */
	public static SpriteInfo load(int bmpResourceId, int XmlAnimationResourceId, Context context) {

		SpriteInfo spriteInfo = new SpriteInfo();
	
		// load the xml
		XmlResourceParser xmlRP = context.getResources().getXml(XmlAnimationResourceId);

		//create animations Hashtable
		spriteInfo.animations = new Hashtable<String, AnimationSequence>();

		try {
			int eventType = xmlRP.getEventType();			
			
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_DOCUMENT) {
//					Log.i("xml", "Start document");
				} 
				
				else if (eventType == XmlPullParser.END_DOCUMENT) {
//					Log.i("xml", "End document");

				} 
				
				else if (eventType == XmlPullParser.START_TAG) {
					
//					Log.i("xml", "Start tag " + xmlRP.getName());
					
					//read a sprite : obviously only one sprite exists in XML file
					if (xmlRP.getName().toLowerCase().equals("sprite")) {
						
						spriteInfo.name = xmlRP.getAttributeValue(null, "name");
						spriteInfo.colorFilter = xmlRP.getAttributeIntValue(null, "filter", 0);
						
						Log.i("SpriteInfo", "read info : "+spriteInfo.name);
					} 
					
					//read an animation
					else if (xmlRP.getName().toLowerCase().equals("animation")) {
						
						AnimationSequence animation = new AnimationSequence();
						animation.name = xmlRP.getAttributeValue(null, "name");
						animation.loop = xmlRP.getAttributeBooleanValue(null, "loop", false);
						animation.frameDelay = xmlRP.getAttributeIntValue(null, "delay", 0);
						
						int framesCount = xmlRP.getAttributeIntValue(null, "frames", 0);				
											
						int top = xmlRP.getAttributeIntValue(null, "top", 0);
						int bottom = xmlRP.getAttributeIntValue(null, "bottom",	0);
						int left = xmlRP.getAttributeIntValue(null, "left", 0);
						int right = xmlRP.getAttributeIntValue(null, "right", 0);
						
						animation.rect = new Rect[framesCount];
						animation.bitmaps = new Bitmap[framesCount];
						animation.width = (right - left + 1) / framesCount;
						animation.height = bottom - top + 1;

						for (int i = 0; i < framesCount; i++) {
							int frameLeft = left + animation.width * i;
							int frameRight = frameLeft + animation.width - 1;
							animation.rect[i] = new Rect(frameLeft, top, frameRight, bottom);
//							animation.bitmaps[i] = Bitmap.createBitmap(source, frameLeft, top, frameRight, bottom);
						}
						
						Log.i("SpriteInfo", "add "+animation.name+" seq="+animation.bitmaps.length);
						spriteInfo.animations.put(animation.name, animation);
						
					}
					
				} 
				else if (eventType == XmlPullParser.END_TAG) {
					
//					Log.i("xml", "End tag " + xmlRP.getName());
				} 
				else if (eventType == XmlPullParser.TEXT) {
					
//					Log.i("xml", "Text " + xmlRP.getText());
				}
				
				eventType = xmlRP.next();
			}
		} 
		catch (Exception e) {
			Log.e("ERROR", "ERROR IN SPRITE TILE  CODE:" + e.toString());
		}
		
		Log.i("xml", "Sprite Loaded ");
		
		generateImages(spriteInfo, bmpResourceId, context);

		return spriteInfo;
	}
	
	private static void generateImages(SpriteInfo spi, int bmpResourceId, Context context){
		Bitmap source = ((BitmapDrawable) context.getResources().getDrawable(bmpResourceId)).getBitmap();
		
		Log.i("SpriteInfo", "cutting image "+spi.name+" "+source.getWidth()+"x"+source.getHeight());
		
		AnimationSequence seq;
		Rect rect;
		
		//for each animation of the sprite
		for (String key : spi.animations.keySet()) {
			seq = spi.animations.get(key);
			int n = seq.rect.length;
			for (int i = 0; i < n; i++) {
				rect = seq.rect[i];
//				Log.i("SpriteInfo",	"cut  "+i+" : "+ rect.left+ ","+	rect.top+" to "+ rect.right+ ","+ rect.bottom);
				try {				
					seq.bitmaps[i] = Bitmap.createBitmap(source, rect.left,	rect.top, seq.width, seq.height);
//					Log.i("SpriteInfo",	"cut done  "+i+" : "+ rect.left+ ","+	rect.top+" to "+ rect.right+ ","+ rect.bottom);
					
				} catch (Exception e) {
//					Log.e("SpriteInfo", "Error cutting image"+i+" anim="+ seq.name+  " : "+ rect.left+ ","+	rect.top+" to "+ rect.right+ ","+ rect.bottom);
					e.printStackTrace();
				}
			}
			Log.i("Sprite info", "created bitmaps for " + seq.name);
			
		}
	}
	
	
	/** Display sprite info*/
	public static void print(SpriteInfo spi){
		Log.i("xml", "Sprite information");
		Log.i("xml", "name : "+spi.name);
		Log.i("xml", "filtercolor: "+spi.colorFilter);
		Log.i("xml", "Animations:");
		for (String key : spi.animations.keySet()) {
			AnimationSequence anim = spi.animations.get(key);
			Log.i("xml", "name: " + anim.name + " loop:" + anim.loop
					+ " delay:" + anim.frameDelay + " frames:"
					+ anim.rect.length + " width:" + anim.width
					+ " height:" + anim.height);
		}
	}

}
