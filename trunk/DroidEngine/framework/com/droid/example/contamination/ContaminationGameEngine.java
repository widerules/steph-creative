package com.droid.example.contamination;

import com.droid.gamedev.GameEngine;
import com.droid.gamedev.GameObject;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.GameFont;

public class ContaminationGameEngine extends GameEngine {

	@Override
	public GameObject getGame(int GameID) {
		switch (GameID) {
		case 0:
			return new ScreenTitle(this);
		case 1:
			return new ScreenPlay(this);
		}
		return null;
	}

	@Override
	public void render(GameGraphics g) {
		this.drawFPS(g, 10, 10);
	}

	@Override
	public void initResources() {

		// setup game icon
		GameImage icon = this.bsLoader.getImage("res/drawable/icon.gif");
		this.bsGraphics.setWindowIcon(icon);

		// create and store a commonly used font in the game
		// GameFont f = this.fontManager.createFont("Verdana", GameFont.BOLD,
		// 24);
		GameFont f = this.fontManager.createFont("Courrier New", GameFont.BOLD,
				24, GameColor.WHITE);
		this.fontManager.putFont("font1", f);

	}

}
