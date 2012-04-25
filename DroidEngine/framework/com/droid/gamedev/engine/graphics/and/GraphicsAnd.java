package com.droid.gamedev.engine.graphics.and;

import java.awt.Font;

import android.graphics.Canvas;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.GameFont;

public class GraphicsAnd implements GameGraphics{
	
	private Canvas canvas;
	

	public GraphicsAnd(Canvas c) {
		this.canvas = c;
	}
	
	//TODO remove this
	public Canvas getCanvas(){
		return this.canvas;
	}

	public int stringWidth(String str) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int stringHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void drawString(String str, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public boolean drawImage(GameImage image, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean drawImageAlpha(GameImage image, int x, int y, float alpha) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean drawImage(GameImage img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void fillRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public Font getFont() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setColor(int code) {
		// TODO Auto-generated method stub
		
	}

	public void setFont(GameFont systemFont) {
		// TODO Auto-generated method stub
		
	}

	public void drawRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
