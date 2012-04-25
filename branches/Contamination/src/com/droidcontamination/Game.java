package com.droidcontamination;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;

/**
 * This class handle all game basic functionalities:
 * managing game status, gathering inputs, updating, rendering.
 * @author Steph
 *
 */
public class Game {

	public static final long FPS = 8;
	public static final int SCORE_WIN = 40; //40
	public static final int MAX_NBACTOR = 40;//40
	private static final int SCORE_INI = 5;

	private boolean running;
	private int score;
	private int timeToGenerate;
	private EventPool evtPool;

	private Context context;
	private GameView screen;
	private Bitmap imgBlood;
	private List<Bitmap> imgGood;
	private List<Bitmap> imgBad;
	private List<TempSprite> temps;
	private List<Sprite> actors;
	
	private AudioManager audioManager;    	
	private SoundPool soundPool;
	private int streamVolume;
	private int sndBad;
	private int sndGood;
	private int currentFSP;
	
	public GameView getGameView(){
		return screen;
	}
		
	/**
	 * 1. Create game objects
	 * 2. Load images resources
	 * 3. Load audio resources
	 * @param context
	 */
	public void initResources(Context context) {
		this.context = context;
		actors = new ArrayList<Sprite>();
		temps = new ArrayList<TempSprite>();
		score = SCORE_INI;
		running = true;		
		evtPool = new EventPool();
		
		screen = new GameView(context, evtPool);
		imgBlood = createBitmap(R.drawable.blood1);
		imgGood = new ArrayList<Bitmap>();
		imgGood.add(createBitmap(R.drawable.good1));
		imgGood.add(createBitmap(R.drawable.good2));
		imgGood.add(createBitmap(R.drawable.good3));
		imgGood.add(createBitmap(R.drawable.good4));
		imgGood.add(createBitmap(R.drawable.good5));
		imgGood.add(createBitmap(R.drawable.good6));
		imgBad = new ArrayList<Bitmap>();
		imgBad.add(createBitmap(R.drawable.bad1));
		imgBad.add(createBitmap(R.drawable.bad2));
		imgBad.add(createBitmap(R.drawable.bad3));
		imgBad.add(createBitmap(R.drawable.bad4));
		imgBad.add(createBitmap(R.drawable.bad5));
		imgBad.add(createBitmap(R.drawable.bad6));
		
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		sndBad = soundPool.load(context, R.raw.man, 1);
		sndGood = soundPool.load(context, R.raw.woman, 1);
		
		Log.i("***Game", "resource loaded");
	}
	
	public boolean isRunning(){
		return running;
	}

	public void processEvents() {
		MotionEvent event = evtPool.popEvent();
		if (event != null) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				onTouchScreen(x, y);
			}
		}
	}
	
	/**
	 * Render all play field elements
	 * 1. the background is green
	 * 2. each actor is displayed as an image
	 * 3. the score 
	 * When game finished, display whether win or loose
	 * according to score
	 */
	public void render() {
		screen.fillScreen(0xFF008000);
		for (TempSprite temp : temps)
			temp.onDraw(screen.getCanavas());
		for (Sprite actor : actors)
			actor.onDraw(screen.getCanavas());
		screen.drawText("FPS [" + currentFSP + "/" + FPS + "] Score " + score,	0xFFFFFFFF, 14, 0, 15);
		screen.invalidate();
	}
	
	public void renderFinished() {
		screen.fillScreen(0xFF008000);
		String message = score<0? "Game over" : "Well done !";
		screen.drawText(message, 0xFFFFFFFF, 32, screen.getWidth()/4, screen.getHeight()/2);
		screen.invalidate();
	}

	public void setCurrentFPS(int currentFPS) {
		this.currentFSP = currentFPS;
	}
		
	public void stop(){
		running = false;
	}

	/**
	 * Update the game objects
	 * 1. perform move for each alive actor
	 * 2. manage collisions
	 * 3. remove invisible actors
	 * 4. if needed the generate new actor
	 * 5. check if game finished 
	 */
	public void update() {
		manageCollision();
		cleanTempSprite();
		if ((--timeToGenerate) < 0)
			generateActor();
		if (score < 0 || score >= SCORE_WIN)
			running = false;
	}

	/** Safe deletion of temSprite that life==0*/
	private void cleanTempSprite() {
		List<TempSprite> copy = new ArrayList<TempSprite>(temps);
		for(TempSprite temp: copy) if(temp.done()) temps.remove(temp);
	}

	private Bitmap createBitmap(int resId) {
		return ((BitmapDrawable) context.getResources().getDrawable(resId)).getBitmap();
	}

	/** check if an alive actor is located at a position */
	private Sprite findActorAt(int x, int y) {
		for (Sprite actor : actors)
			if (actor.isCollision(x, y)){
				return actor;
			}
		return null;
	}
	
	
	/** Generate a new actor 
	 * Good are slower that bad ones */
	private void generateActor() {
		if (actors.size() >= MAX_NBACTOR)
			return;

		Random rnd = new Random();
		boolean good = rnd.nextInt(2) == 1;
		int index = rnd.nextInt(6);
		Bitmap bmp = good ? imgGood.get(index) : imgBad.get(index);
		actors.add(new Sprite(screen, bmp, good));
		timeToGenerate = 10;
	}
	
	private void killActor(Sprite actor) {		
	    temps.add(new TempSprite(screen, actor.getX(), actor.getY(), imgBlood));
		if(actor.isGood()){
			score--;
			soundPool.play(sndGood, streamVolume, streamVolume, 1, 0, 1f);
		}
		else{
			score++;
			soundPool.play(sndBad, streamVolume, streamVolume, 1, 0, 1f);				
		}
		actors.remove(actor);
	}

	/**
	 * Manage collision of a good with bad actor, 
	 * so the good will be killed 
	 * condition : 
	 * 1. actor1 and actor2 are enemies
	 * 2. they are in collision 
	 * Consequence: 
	 * . kill the good one
	 */
	private void manageCollision() {
		int n = actors.size();
		Sprite[] copy = new Sprite[n];
		for (int i = 0; i < n; i++) {
			copy[i] = actors.get(i);
		}
		
		// process all sprite combination
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				if (copy[i].collideWith(copy[j])) {
					// collision of two enemies : the good one dies
					if (copy[i].isGood() != copy[j].isGood()) {
						// have to kill copy[i]
						if (copy[i].isGood()) {
							killActor(copy[i]);
							j = n; // skip other collision of i
						}
						// have to kill copy[j]
						else {
							killActor(copy[j]);
							// shift left
							for (int k = j; k < n - 1; k++) {
								copy[k] = copy[k + 1];
							}
							j--;
							n--;
						}
					}
					// collision of friends : change direction
					else {
						copy[i].changeDirection();
					}
				}
			}
		}
	}

	/**
	 * Handle event onTouchScreen
	 * check if an alive actor is located at this point and just kill him
	 */
	private void onTouchScreen(int x, int y) {
		Sprite touchedActor = findActorAt(x, y);
		if(touchedActor!=null){
			killActor(touchedActor);			
		}		
	}

}