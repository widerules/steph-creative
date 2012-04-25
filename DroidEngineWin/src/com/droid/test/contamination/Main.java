package com.droid.test.contamination;

import com.droid.example.contamination.ContaminationGame;
import com.droid.example.contamination.ContaminationGameEngine;
import com.droid.gamedev.Game;
import com.droid.gamedev.GameLoader;

public class Main {

	public static void main(String[] args) {
		GameLoader loader = new GameLoader();
		//Game game = new ContaminationGame();
		Game gameEngine = new ContaminationGameEngine();
		gameEngine.setPauseOnLostFocus(true);
		loader.setup(gameEngine, 480, 320, false);
		loader.start();
	}
}
