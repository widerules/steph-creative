package com.droid.example.contamination;

import com.droid.gamedev.GameEngine;
import com.droid.gamedev.GameObject;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.object.background.ImageBackground;

public class ScreenEnd extends GameObject {

	/** indicates game result */
	private String scoreMessage;

	public ScreenEnd(GameEngine parent, int score) {
		super(parent);
		if (score <= 0) {
			this.scoreMessage = "You loose, need more practice";
		} else if (score >= ScreenPlay.SCORE_WIN) {
			this.scoreMessage = "You win, congratulations !";
		} else {
			this.scoreMessage = "Game over. Your score is " + score;
		}
	}

	private Background background;

	@Override
	public void initResources() {

		this.background = new ImageBackground(
				this.getImage("res/drawable/backgr.jpg"));
		this.bsInput.setMouseVisible(true);
	}

	@Override
	public void update(long elapsedTime) {
		if (this.click()) {
			this.parent.setNextGameID(0); // go back to menu
			this.finish();
		}
	}

	@Override
	public void render(GameGraphics g) {
		this.background.render(g);
		g.setColor(GameColor.WHITE);

		this.fontManager.getFont("font1").drawString(g, this.scoreMessage,
				GameFont.CENTER, 10, 120, this.getWidth());
	}

}
