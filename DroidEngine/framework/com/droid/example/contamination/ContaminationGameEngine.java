package com.droid.example.contamination;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.droid.gamedev.GameEngine;
import com.droid.gamedev.GameObject;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.BaseInput;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.CollisionManager;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.object.PlayField;
import com.droid.gamedev.object.Sprite;
import com.droid.gamedev.object.SpriteGroup;
import com.droid.gamedev.object.Timer;
import com.droid.gamedev.object.background.ColorBackground;
import com.droid.gamedev.object.background.ImageBackground;
import com.droid.gamedev.object.collision.BasicCollisionGroup;
import com.droid.gamedev.object.sprite.TempSprite;

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
		
		//setup game icon		
		GameImage icon = this.bsLoader.getImage("res/drawable/icon.gif");
		this.bsGraphics.setWindowIcon(icon);
		
		//create and store a commonly used font in the game 
//		GameFont f = this.fontManager.createFont("Verdana", GameFont.BOLD, 24);
		GameFont f = this.fontManager.createFont("Courrier New", GameFont.BOLD, 24,GameColor.WHITE);		
		this.fontManager.putFont("font1", f);
		
	}
	
	
	public class ScreenTitle extends GameObject {
	
		private Background background;
		private Timer timerstart;
		private boolean displayStart;
	
		public ScreenTitle(GameEngine parent) {
			super(parent);
		}
	
		@Override
		public void initResources() {			
			
			this.background = new ImageBackground(this.getImage("res/drawable/backgr.jpg"));
			
			
			this.timerstart = new Timer(1000);
			this.bsInput.setMouseVisible(true);
			
		}
	
		@Override
		public void update(long elapsedTime) {
			if (this.timerstart.action(elapsedTime)) {
				displayStart = !displayStart;
			}
			if (this.click()) {
				this.parent.setNextGameID(1); //play game
				this.finish();
			}
			if(this.keyDown(BaseInput.KEY_ESC)){
				this.finish();
			}
		}
	
		@Override
		public void render(GameGraphics g) {
			this.background.render(g);
			g.setColor(GameColor.WHITE);
			GameFont f = this.fontManager.getFont("font1");
			f.drawString(g, "Droid contamination", GameFont.CENTER, 10,
					40, this.getWidth());
			if (this.displayStart) {
				f.drawString(g, "click to start", GameFont.CENTER, 10, 120,
						this.getWidth());
			}
	
		}
	
	}

	public class ScreenEnd extends GameObject {

		/** indicates game result */
		private String scoreMessage;
		
		public ScreenEnd(GameEngine parent, int score) {
			super(parent);
			if(score<=0){
				this.scoreMessage = "You loose, need more practice";
			}
			else if(score>=ScreenPlay.SCORE_WIN){
				this.scoreMessage = "You win, congratulations !";
			}
			else{
				this.scoreMessage = "Game over. Your score is "+score;
			}
		}

		
		private Background background;


		@Override
		public void initResources() {
			
			this.background = new ImageBackground(this.getImage("res/drawable/backgr.jpg"));
			this.bsInput.setMouseVisible(true);
		}

		@Override
		public void update(long elapsedTime) {
			if (this.click()) {
				this.parent.setNextGameID(0); //go back to menu
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
	
	public class ScreenPlay extends GameObject{

		public ScreenPlay(GameEngine parent) {
			super(parent);
		}

		public static final int SCORE_WIN = 20; // 40
		private final int MAX_NBACTOR = 40;// 40
		private final int SCORE_INI = 5;// 5;
		private final int GENERATE_TIME = 2000;		
		private final int PLAY_TIME = 218;//play time in seconds
		
		private int remainTime;
		/** used to compute remain time */
		private Timer remainTimer;
		

		private int score;
		private List<GameImage[]> goodImages, badImages;
		private GameImage blood;

		private Background background;
		private Timer timerGenerate;

		private PlayField field;
		private SpriteGroup goodGuys;
		private SpriteGroup badGuys;
		private SpriteGroup temps;
		
		@Override
		public void initResources() {
			this.bsInput.setMouseVisible(true);
			this.setFPS(10);
			
		
			badImages = new ArrayList<GameImage[]>();
			badImages.add(getImages("res/drawable/bad1.png", 3, 4));
			badImages.add(getImages("res/drawable/bad2.png", 3, 4));
			badImages.add(getImages("res/drawable/bad3.png", 3, 4));
			badImages.add(getImages("res/drawable/bad4.png", 3, 4));
			badImages.add(getImages("res/drawable/bad5.png", 3, 4));
			badImages.add(getImages("res/drawable/bad6.png", 3, 4));
			goodImages = new ArrayList<GameImage[]>();
			goodImages.add(getImages("res/drawable/good1.png", 3, 4));
			goodImages.add(getImages("res/drawable/good2.png", 3, 4));
			goodImages.add(getImages("res/drawable/good3.png", 3, 4));
			goodImages.add(getImages("res/drawable/good4.png", 3, 4));
			goodImages.add(getImages("res/drawable/good5.png", 3, 4));
			goodImages.add(getImages("res/drawable/good6.png", 3, 4));
			blood = getImage("res/drawable/blood.png");
			
			this.background = new ColorBackground(GameColor.DARK_GREEN);
			
						 
			this.field = new PlayField(this.background);
			this.goodGuys = new SpriteGroup("good guys");
			this.field.addGroup(this.goodGuys);
			this.badGuys = new SpriteGroup("bas guys");
			this.field.addGroup(this.badGuys);		
			this.temps = new SpriteGroup("temporary sprites");
			this.field.addCollisionGroup(goodGuys, badGuys, manageCollisionGoodBad());
			this.field.addGroup(this.temps);		
			this.score = this.SCORE_INI;		
			this.timerGenerate = new Timer(this.GENERATE_TIME);
			this.field.setComparator(getSpritesComparator());
			
			
			this.remainTime = this.PLAY_TIME;
			this.remainTimer = new Timer(1000);
			
		}
		
		@Override
		public void onStart() {		
			this.bsMusic.play("res/raw/music.wav");	
		}
		
		@Override
		public void onStop() {
			this.bsMusic.stopAll();
		}
		
		@Override
		public void render(GameGraphics g) {
			this.field.render(g);
			g.setColor(GameColor.WHITE);
			this.fontManager.getFont("FPS Font").drawString(g, "SCORE:" + score +"/"+SCORE_WIN+ " TIME:"+this.remainTime,
					GameFont.CENTER, 14, 10, this.getWidth());
		}


		
		@Override
		public void update(long elapsedTime) {
			this.cleanup();
			
			this.field.update(elapsedTime);
			
			if(timerGenerate.action(elapsedTime)){
				generateActor();
			}
			
			if(this.click()){
				onTouchScreen(bsInput.getMouseX(), bsInput.getMouseY());
			}
			
			if(this.remainTimer.action(elapsedTime)){
				this.remainTime--;
			}
			
			if (this.score <= 0 || this.score >= SCORE_WIN
					|| this.remainTime <= 0) {

				this.parent.setNextGame(new ScreenEnd(this.parent, this.score));
				this.finish();
			}
			
			if(this.keyDown(27)){
				this.parent.setNextGame(new ScreenTitle(this.parent));
				this.finish();
			}
			
			
				
		}
		


		private void cleanup() { 
			for(SpriteGroup group: this.field.getGroups()){
				group.removeInactiveSprites();
			}
		}
		/** check if an alive actor is located at a position */
		private Actor findActorAt(int x, int y) {
			//check for bad guys
			for (Actor actor : getActiveActors()) {
				if (actor.isInsideMe(x, y)) {
					return actor;
				}
			}
			return null;
		}
			
		/** Generate a new actor 
		 * Good are slower that bad ones */
		private void generateActor() {
			if (this.goodGuys.getSize()+this.badGuys.getSize() >= MAX_NBACTOR)
				return;

			Random rnd = new Random();
			boolean good = rnd.nextInt(2) == 1;
			int index = rnd.nextInt(6);
			GameImage[] images = good ? goodImages.get(index) : badImages.get(index);

			int x = rnd.nextInt(getWidth() - 50);
			int y = rnd.nextInt(getHeight() - 50);

			double xSpeed = 0, ySpeed = 0;

			double MAX_SPEED = 0.04;

			while (xSpeed == 0 && ySpeed == 0) {
				xSpeed = (2 * rnd.nextDouble() - 1) * MAX_SPEED;
				ySpeed = (2 * rnd.nextDouble() - 1) * MAX_SPEED;
			}

			Actor actor = new Actor(images, x, y, xSpeed, ySpeed, background, good);

			if(good){
				goodGuys.add(actor);			
			}
			else{
				badGuys.add(actor);
			}
		}
		
		/** get liost of active actors*/
		private List<Actor> getActiveActors(){
			List<Actor> actors = new ArrayList<Actor>();
			Sprite[] sprites = this.goodGuys.getSprites();
			for(int i=0; i<this.goodGuys.getSize(); i++){
				if(sprites[i].isActive()){
					actors.add((Actor)sprites[i]);
				}
			}
			sprites = this.badGuys.getSprites();
			for(int i=0; i<this.badGuys.getSize(); i++){
				if(sprites[i].isActive()){
					actors.add((Actor)sprites[i]);
				}
			}
			return actors;
		}
		
		private Comparator<Sprite> getSpritesComparator() {
			return new Comparator<Sprite>() {

				
				public int compare(Sprite o1, Sprite o2) {				
					return (int) ((o1.getLayer()-o2.getLayer())*1000 + (o1.getY()-o2.getY()));
				}
				
			};
		}
		
//		private AudioManager audioManager;    	
//		private SoundPool soundPool;
//		private int streamVolume;
//		private int sndBad;
//		private int sndGood;
		
		private void killActor(Actor actor) {
			actor.setActive(false);
			TempSprite tmp =  new TempSprite(blood,  actor.getX(), actor.getY(), 3000);
		    temps.add(tmp);
			if(actor.good){
				score--;
				this.bsSound.play("res/raw/woman.wav");
				
//				soundPool.play(sndGood, streamVolume, streamVolume, 1, 0, 1f);
			}
			else{
				score++;
				this.bsSound.play("res/raw/man.wav");
//				soundPool.play(sndBad, streamVolume, streamVolume, 1, 0, 1f);				
			}		
		}

		private CollisionManager manageCollisionGoodBad(){
			return new BasicCollisionGroup() {
				
				
				@Override
				public void collided(Sprite s1, Sprite s2) {
					Actor act1 = (Actor) s1;
					Actor act2 = (Actor) s2;
					//between friends : each sprite change direction
					if(act1.good==act2.good){
						act1.changeDirection();
					}
					//betwen ennemies: the good one dies 
					else{
						if(act1.good){
							killActor(act1);
						}
						else{
							killActor(act2);
						}
					}
					
				}
			};
		}
		
		/**
		 * Handle event onTouchScreen
		 * check if an alive actor is located at this point and just kill him
		 */
		 private void onTouchScreen(int x, int y) {
			Actor touchedActor = findActorAt(x, y);
			if(touchedActor!=null){
				killActor(touchedActor);			
			}		
		}


	}
	
}
