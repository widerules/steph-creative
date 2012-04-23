package com.droid.test.contamination;


/**
 * 
 * The aim of the game is to kill all zombies
 * Scoring is like this
 * . one zombie killed : +1
 * . one healthy people killed : -1
 * To win you must achieve score = Game.SCORE_WIN
 * If your score is <0 then you loose
 *
 * @author Steph
 *
 */
import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
	
	private GameLoader loader = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);        
//        GFXAnd gfx = new GFXAnd(this);        
//        this.setContentView(gfx);
        
//        this.setContentView(R.layout.main);
		
		loader = new GameLoader();
		loader.setup(new Game(), this);
		
		loader.srart();
    }
    
    @Override
	protected void onDestroy() {
		loader.stop();
		super.onDestroy();
	}
}