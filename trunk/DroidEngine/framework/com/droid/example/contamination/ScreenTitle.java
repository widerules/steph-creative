package com.droid.example.contamination;

import com.droid.gamedev.GameEngine;
import com.droid.gamedev.GameObject;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.object.Timer;
import com.droid.gamedev.object.background.ImageBackground;

public class ScreenTitle extends GameObject {

	private Background background;
	private Timer timerstart;
	private boolean displayStart;

	public ScreenTitle(GameEngine parent) {
		super(parent);
	}

	@Override
	public void initResources() {

		this.background = new ImageBackground(
				this.getImage("res/drawable/backgr.jpg"));

		this.timerstart = new Timer(1000);
		this.bsInput.setMouseVisible(true);

	}

	@Override
	public void update(long elapsedTime) {
		if (this.timerstart.action(elapsedTime)) {
			displayStart = !displayStart;
		}
		if (this.click()) {
			this.parent.setNextGameID(1); // play game
			this.finish();
		}
		if (this.keyDown(BaseInput.KEY_ESC)) {
			this.finish();
		}
	}

	@Override
	public void render(GameGraphics g) {
		this.background.render(g);
		g.setColor(GameColor.WHITE);
		GameFont f = this.fontManager.getFont("font1");
		f.drawString(g, "Droid contamination", GameFont.CENTER, 10, 40,
				this.getWidth());
		if (this.displayStart) {
			f.drawString(g, "click to start", GameFont.CENTER, 10, 120,
					this.getWidth());
		}

	}

}
