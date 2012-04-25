package lsi.formation.contamination;

import java.util.ArrayList;
import java.util.Random;

import lsi.formation.contamination.Main.Game.Actor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class Main extends Activity {
   
	Context context;
	SoundEngine soundEngine;
	GraphicEngine graphicEngine;
	EventHandler eventHandler;
	Game game;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;        
        soundEngine = new SoundEngine();
        graphicEngine = new GraphicEngine(); 
        eventHandler = new EventHandler();
        game = new Game();   
        
        setContentView(graphicEngine.screen); 
    }
    
    @Override
    protected void onResume() {
    	game.start();
    	super.onResume();
    }
    
    @Override
    protected void onStop() {
    	game.stop();
    	super.onStop();
    }
    
    /**
     * this class contains all games data and engines
     */
    public class Game {
    	
    	int score = 0;
    	boolean running;
    	ArrayList<Actor> actors = new ArrayList<Main.Game.Actor>();
    	final Random myRandom = new Random();
    	int timeToGenerate = 0;
    	final int GENERATE_TIME = 10;
    	
    	/**
    	 * init all game elements
    	 */
    	public Game(){
    		running = true;
    	}
    	
    	public void start(){
			graphicEngine.start();
		}

		public void stop() {
			running= false;
		}

		/**
    	 * update all game elements
    	 */
    	public void update(){    		
    		ArrayList<Actor> toRemove = new ArrayList<Actor>();
    		
    		//faire déplacer les acteurs
    		for (Actor actor : actors) {
    			if(actor.alive){
    				actor.move();
    			}
    			else{
    				actor.timeToDisappear--;
    				if(actor.timeToDisappear<=0){
    					toRemove.add(actor);
    				}
    			}
			}
    		
    		//supprimer les acteurs invisibles
    		for (Actor actor : toRemove) {
				actors.remove(actor);
			}
    		
    		
    		
    		//générer un nouvel acteur placé à un endroid aléatoire
    		if(timeToGenerate==0){  
    			float y = myRandom.nextFloat()*graphicEngine.HEIGHT;
    			int type = myRandom.nextInt()%2;    			
    			actors.add(new Actor(type, 0, y));
    			timeToGenerate = GENERATE_TIME;
    		}
    		else{
    			timeToGenerate--;
    		}
    		
    	}
    	   	
    	
    	public Actor findActorAt(float x, float y){
    		for (Actor actor : actors) {
    			if(actor.xPos<x && x<(actor.xPos+Actor.DIMENSION) && actor.yPos<y && y<actor.yPos+Actor.DIMENSION){
    				return actor;
    			}
			}
    		return null;
    	}
    	
    	public class Actor{
    		static final int GOOD = 0;
    		static final int BAD = 1;
    		static final int DIMENSION = 48;
    		int type;
    		float xPos;
    		float yPos; 
    		boolean alive;
    		float STEP = 10;
    		int timeToDisappear;
    		
			public Actor(int type, float xPos, float yPos) {
				alive = true;
				this.type = type;
				this.xPos = xPos;
				this.yPos = yPos;
				timeToDisappear = 10;
			}
			
			public void move() {
				xPos+= STEP;				
			}
			
    	}
    }
    
    /**
     * This class handle sound effects and music
     *
     */
    public class SoundEngine{
    	
    	AudioManager audioManager;    	
    	SoundPool soundPool;
    	int streamVolume;
    	int SOUND_BIZZ;
    	int SOUND_BEEP;
    	
    	/**
    	 * Init and load all resources
    	 */
    	public SoundEngine(){
    		audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
    		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
    		SOUND_BIZZ = soundPool.load(context, R.raw.bizz, 1);
    		SOUND_BEEP = soundPool.load(context, R.raw.beep, 1);
    		streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    	}
    	
    	public void playSound(int soundID){    		
    		soundPool.play(soundID, streamVolume, streamVolume, 1, 0, 1f);
    	}
    	
    }
    
    /**
     * This class handle sprite drawing
     */
    public class GraphicEngine extends Thread{
    	
    	GameHandler handler= new GameHandler();
    	Bitmap buffer;
    	Canvas surface;
    	Paint paint;
    	GameScreen screen;
    	int WIDTH = 320;
    	int HEIGHT = 430;
    	
    	//intervalle de rafraichissement de l'écran
    	static final long DELAY = 200; 
    	
    	public GraphicEngine(){
    		screen = new GameScreen(context);
    	}
    	
    	/**
    	 * Game loop
    	 */
    	@Override
    	public void run() {
    		while(game.running){
    			handler.sendEmptyMessage(MSG_UPDATE);
    			handler.sendEmptyMessage(MSG_DRAW);
    			pause();
    		}
    		handler.sendEmptyMessage(MSG_END);
    	}
    	
    	void pause(){
    		try {
    			Thread.sleep(DELAY);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	    	
    	/**
    	 * Draw game scene
    	 */
    	public void draw(){
    		if(paint!=null){
    			clearSreen();
        		for (Actor actor : game.actors) {
    				drawActor(actor);
    			}
        		drawScore();
        		screen.invalidate();
    		}
    	}
    	
    	void clearSreen(){
    		paint.setColor(0xFFFFFFFF);
			surface.drawPaint(paint);
    	}
    	
    	void drawActor(Actor actor){
    		int image;
    		if(!actor.alive){
    			image = R.drawable.blood;
    		}
    		else{
    			if (actor.type==Actor.GOOD){
    				image = R.drawable.mario;
    			}
    			else{
    				image = R.drawable.boo;
    			}
    		}
    		Bitmap tmp = ((BitmapDrawable) context.getResources().getDrawable(image)).getBitmap();
			surface.drawBitmap(tmp, actor.xPos, actor.yPos, null);
    	}
    	
    	void drawScore(){
    		paint.setColor(0xFFFF00FF);    		
			surface.drawText("Score="+game.score, 0, 10, paint);
    	}
    	
    	static final int MSG_UPDATE = 0;
        static final int MSG_DRAW = 1;
        static final int MSG_END = 2; 
    	
    	/**
         * This class handle communication between game loop and main thread
         */
        public class GameHandler extends Handler{
        	
        	@Override
        	public void handleMessage(Message msg) {
        		switch (msg.what) {
    			case MSG_UPDATE: game.update(); break;
    			case MSG_DRAW: draw(); break;
    			case MSG_END:   				
    				break;
    			}
        	}    	
        }
        
        public class GameScreen extends SurfaceView implements Callback{

        	SurfaceHolder holder;
        	
			public GameScreen(Context context) {
				super(context);
				holder = getHolder();
				holder.addCallback(this);
			}
			
			@Override
			public void invalidate() {
				if (holder != null) {
					Canvas c = holder.lockCanvas();
					if (c != null) {
						c.drawBitmap(buffer, 0, 0, null);
						holder.unlockCanvasAndPost(c);
					}				
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Log.i("**surfaceChanged", "width="+width+" height="+height);
				WIDTH = width;
				HEIGHT = height;
				buffer = Bitmap.createBitmap(width, height, Config.ARGB_8888);
				surface = new Canvas(buffer);
				paint = new Paint();
			}

			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				float x = event.getX();
				float y = event.getY();
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					eventHandler.onTouchScreen(x, y);
				}				
				return true;
			}        	
        }
    }
    
    public class EventHandler{
    	    	
    	public void onTouchScreen(float x, float y){
    		Log.i("**EventHandler.onTouchScreen", "x="+x+" y="+y);
    		
    		Actor touchedActor = game.findActorAt(x, y);
    		if(touchedActor!=null){
    			touchedActor.alive = false;
    			Log.i("**kill actor", touchedActor.type==Actor.GOOD? "good":"bad");
    			
    			if(touchedActor.type == Actor.BAD){
    				game.score++;
    				soundEngine.playSound(soundEngine.SOUND_BIZZ);
    			}
    			else{
    				game.score--;
    				soundEngine.playSound(soundEngine.SOUND_BEEP);
    			}
    		}
    	}
    }
    
    
}