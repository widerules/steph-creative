package com.droid.gamedev.engine.loader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.base.GameImageWin;

final class ImageUtilWin extends ImageUtil{

	@Override
	public GameImage getImage(URL url) {
		BufferedImage buffImg = BufferedImageUtil.getImage(url);
		return new GameImageWin(buffImg);
	}

	@Override
	public GameImage resize(GameImage orig, int width, int height) {
		BufferedImage src = ((GameImageWin)orig).getBufferedImage();
		BufferedImage buffImg = BufferedImageUtil.resize(src, width, height);
		return new GameImageWin(buffImg);
	}

	@Override
	public void saveImage(GameImage image, File imagefile) {
		BufferedImage buffImg = ((GameImageWin)image).getBufferedImage();
		BufferedImageUtil.saveImage(buffImg, imagefile);
	}

	@Override
	public GameImage getImage(URL url, int maskColor) {
		//convert maskcolor to awtColor 
		Color c = new Color(maskColor);	
		BufferedImage buffImg = BufferedImageUtil.getImage(url, c);
		return new GameImageWin(buffImg);
	}

	@Override
	public GameImage[] getImages(URL url, int col, int row, int maskColor) {
		//convert maskcolor to awtColor 
		Color c = new Color(maskColor);			
		BufferedImage[] buffImgs = BufferedImageUtil.getImages(url, col, row, c);
		return BufferedImageArrayToImageDataArray(buffImgs);
	}
	
	private static GameImage[] BufferedImageArrayToImageDataArray(
			BufferedImage[] buffImgs) {
		
		GameImage[] imgData = new GameImage[buffImgs.length];
		for(int i=0; i<buffImgs.length; i++){
			imgData[i] = new GameImageWin(buffImgs[i]);
		}
		
		return imgData;
	}

	@Override
	public GameImage[] getImages(URL url, int col, int row) {
		BufferedImage[] buffImgs = BufferedImageUtil.getImages(url, col, row);
		return BufferedImageArrayToImageDataArray(buffImgs);
	}

	
	@Override
	public GameImage createImage(int width, int height) {
		BufferedImage buffImg = BufferedImageUtil.createImage(width, height);
		return new GameImageWin(buffImg);
	}

	@Override
	public GameImage createImage(int width, int height, int opaque) {
		BufferedImage buffImg = BufferedImageUtil.createImage(width, height, opaque);
		return new GameImageWin(buffImg);
	}

	@Override
	public GameImage applyMask(GameImage image, int backgr) {
		BufferedImage src = ((GameImageWin)image).getBufferedImage();
		Color color = new Color(backgr);
		BufferedImage imgBuff = BufferedImageUtil.applyMask(src, color);
		return new GameImageWin(imgBuff);
	}
	
	
	

	
	
}
