package com.droid.test.contamination;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;

import com.droid.R;
import com.droid.gamedev.engine.graphics.and.GFXAnd;
import com.droid.gamedev.engine.graphics.and.GraphicsAnd;

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
	private static final int SCORE_INI = 5;//5;
	private static final int GENERATE_TIME = 10; //10

	private boolean running;
	private int score;
	private int timeToGenerate;
	private EventPool evtPool;

	private Context context;
	
//	GameView gfx;
	
	GFXAnd gfx;
	
	private Bitmap imgBlood;
	private List<Bitmap> imgGood;
	private List<Bitmap> imgBad;
	private SpriteInfo spi; 
	
	private List<TempSprite> temps;
	private List<AdvancedSprite> actors;
	
	private AudioManager audioManager;    	
	private SoundPool soundPool;
	
	
	private float soundVolume;
	private float musicVolume;
	
	private int sndBad;
	private int sndGood;
				
	/**
	 * 1. Create game objects
	 * 2. Load images resources
	 * 3. Load audio resources
	 * @param context
	 */
	public void initResources(Context context) {
		this.context = context;
		
		actors = new ArrayList<AdvancedSprite>();
		temps = new ArrayList<TempSprite>();
		
		score = SCORE_INI;
		running = true;		
		timeToGenerate = GENERATE_TIME;
		evtPool = new EventPool();
				
		imgBlood = createBitmap(R.drawable.blood);
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
		spi = SpriteInfo.load(R.drawable.bad1, R.xml.bad, context);
		
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		
		float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		soundVolume = maxVolume/4;
		musicVolume = maxVolume/10;
		
		Log.i("Game", "stream volume :  sound="+soundVolume+ "    music="+musicVolume);
		
		sndBad = soundPool.load(context, R.raw.man, 1);
		sndGood = soundPool.load(context, R.raw.woman, 1);
		
		startMusic();
		
		Log.i("Game", "resource loaded");
	}
	
	public boolean isRunning(){
		return running;
	}

	public void processEvents() {
		MotionEvent event = evtPool.popEvent();
		if (event != null) {
			int x = (int) event.getX();
			int y = (int) event.getY();
			onTouchScreen(x, y);
		}
	}
	
	private void renderBackground(Canvas c){
		Paint paint = new Paint();
		paint.setColor(0xFF008000);
		c.drawPaint(paint);
	}
	
	/**
	 * Render all play field elements
	 * 1. the background is green
	 * 2. each actor is displayed as an image
	 * 3. the score 
	 * When game finished, display whether win or loose
	 * according to score
	 */
	public void render(Canvas c) {
		
		Log.i("Game", "render");

		//draw background
		renderBackground(c);

		//draw sprites
		for (TempSprite temp : temps)
			if (temp.isActive())
				temp.render(c);

		for (AdvancedSprite actor : actors)
			if (actor.isActive())
				actor.render(c);
		
		//render score
		renderScore(c);
	}
	
	private void renderScore(Canvas c){
		Paint paint = new Paint();
		paint.setColor(0xFFFFFFFF); 
		paint.setFakeBoldText(true);
		paint.setTextSize(15);
		c.drawText("Score " + score, 14, 10, paint);
	}
	
	
	public void renderFinished(Canvas c) {
		renderBackground(c);
		String message = score==SCORE_WIN?  "Well done !":"Game over";
		Paint paint = new Paint();
		paint.setColor(0xFFFFFFFF); 
		paint.setFakeBoldText(true);
		paint.setTextSize(15);
		c.drawText(message, 14, 0, paint);		
	}

	public void stop(){
		try{
			musicPlayer.stop();
			musicPlayer.release();
		}catch (Exception e) {
		}
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
		
		cleanup();
		checkCollision();		
		if ((--timeToGenerate) < 0)
			generateActor();
		if (score == 0 || score >= SCORE_WIN)
			running = false;
	}

	/** Delete inactive sprites */
	private void cleanup() { 
		List<TempSprite> tempsCopy = new ArrayList<TempSprite>(temps);
		for(TempSprite temp: tempsCopy) if(!temp.isActive()) temps.remove(temp);
		List<AdvancedSprite> actorsCopy = new ArrayList<AdvancedSprite>(actors);
		for(AdvancedSprite temp: actorsCopy) if(!temp.isActive()) actors.remove(temp);
	}

	private Bitmap createBitmap(int resId) {
		return ((BitmapDrawable) context.getResources().getDrawable(resId)).getBitmap();
	}

	/** check if an alive actor is located at a position */
	private AdvancedSprite findActorAt(int x, int y) {
		for (AdvancedSprite actor : actors)
			if (actor.isActive() && actor.isInsideMe(x, y)){
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
		//Sprite newActor = new Sprite(bmp, screen.getWidth(), screen.getHeight(), good);
//		AdvancedSprite newActor = new AdvancedSprite(bmp, spi, screen.getWidth(), screen.getHeight(), good);
		
		AdvancedSprite newActor = new AdvancedSprite(bmp, spi, gfx.getWidth(), gfx.getHeight(), good);
		
		actors.add(newActor);
		timeToGenerate = GENERATE_TIME;
	}
	
	private void killActor(AdvancedSprite actor) {
		actor.setActive(false);
//	    temps.add(new TempSprite(screen, actor.getX(), actor.getY(), imgBlood));
		
		this.temps.add(new TempSprite(gfx, actor.getX(), actor.getY(), imgBlood));
		
		if(actor.good){
			score--;
			soundPool.play(sndGood, soundVolume, soundVolume, 1, 0, 1f);
		}
		else{
			score++;
			soundPool.play(sndBad, soundVolume, soundVolume, 1, 0, 1f);				
		}		
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
	private void checkCollision() {
		AdvancedSprite sprite1, sprite2;
		int n = actors.size();
//		int nbCheck = 0, found=0;
		// check all sprite combination
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				sprite1 = actors.get(i);
				sprite2 = actors.get(j);
//				nbCheck++;
				if (sprite1.isActive() && sprite2.isActive()
						&& sprite1.collideWith(sprite2)) {
//					found++;					
					onCollision(sprite1, sprite2);
				}
			}
		}
//		if(found>0)Log.i("***Game", " actors="+n+"\tcollision="+found+"/"+nbCheck);
	}

	private void onCollision(AdvancedSprite sprite1, AdvancedSprite sprite2) {
		
//		Log.i("***Game", "collision "+sprite1.toString()+" and "+sprite2.toString());
		
		
		//between friends : each sprite change direction
		if(sprite1.good==sprite2.good){
			sprite1.avoidSprite(sprite2);
			sprite2.avoidSprite(sprite1);
		}
		//between enemies: the good one dies 
		else{
			if(sprite1.good){
				killActor(sprite1);
			}
			else{
				killActor(sprite2);
			}
		}
	}

	/**
	 * Handle event onTouchScreen
	 * check if an alive actor is located at this point and just kill him
	 */
	private void onTouchScreen(int x, int y) {
		AdvancedSprite touchedActor = findActorAt(x, y);
		if(touchedActor!=null){
			killActor(touchedActor);			
		}		
	}

	private MediaPlayer musicPlayer;
	
	private void startMusic(){
		try {
			Log.i("Game", "playing music ...");			
			musicPlayer = MediaPlayer.create(context,R.raw.music);
			musicPlayer.setVolume(musicVolume, musicVolume);
			musicPlayer.start();			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	long startTime = 0;
	final long ticksFPS = 1000 / Game.FPS;
	
	public void sleep(){
		long elapsedTime = System.currentTimeMillis() - startTime;
		long sleepTime = ticksFPS - elapsedTime;
		if(sleepTime<0 ) {
			sleepTime = 10;
		}					
		try {
			Thread.sleep(sleepTime);
		} 
		catch (Exception e) {
		}
		startTime = System.currentTimeMillis();
	}

	public void startGameLoop() {
		Canvas c;
		
		while (this.running) {
			
			this.processEvents();			
			this.update();
			
			if((c=((GraphicsAnd)this.gfx.getGraphics()).getCanvas())!=null){
				this.render(c);			
				this.gfx.flip();
			}
						
			this.sleep();
		}
		
		if((c=((GraphicsAnd)this.gfx.getGraphics()).getCanvas())!=null){
			this.renderFinished(c);	
			this.gfx.flip();
		}
		
	}
}