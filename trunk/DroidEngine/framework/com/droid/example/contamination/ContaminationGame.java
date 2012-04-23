package com.droid.example.contamination;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.droid.gamedev.Game;
import com.droid.gamedev.base.GameColor;
import com.droid.gamedev.base.GameImage;
import com.droid.gamedev.engine.graphics.GameGraphics;
import com.droid.gamedev.object.Background;
import com.droid.gamedev.object.CollisionManager;
import com.droid.gamedev.object.GameFont;
import com.droid.gamedev.object.PlayField;
import com.droid.gamedev.object.Sprite;
import com.droid.gamedev.object.SpriteGroup;
import com.droid.gamedev.object.Timer;
import com.droid.gamedev.object.background.ColorBackground;
import com.droid.gamedev.object.collision.BasicCollisionGroup;
import com.droid.gamedev.object.sprite.TempSprite;

public class ContaminationGame extends Game {

	public static final int SCORE_WIN = 40; // 40
	public static final int MAX_NBACTOR = 40;// 40
	private static final int SCORE_INI = 5;// 5;
	private static final int GENERATE_TIME = 2000;

	private int score;
	private List<GameImage[]> goodImages, badImages;
	private GameImage blood;

	private Background background;
	private Timer timerGenerate;

	private PlayField field;
	private SpriteGroup goodGuys;
	private SpriteGroup badGuys;
	private SpriteGroup temps;

	private GameFont font1;

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

		this.font1 = this.fontManager.createFont("Verdana", GameFont.BOLD, 16);

		this.background = new ColorBackground(GameColor.DARK_GREEN);
		this.field = new PlayField(this.background);
		this.goodGuys = new SpriteGroup("good guys");
		this.field.addGroup(this.goodGuys);
		this.badGuys = new SpriteGroup("bas guys");
		this.field.addGroup(this.badGuys);
		this.temps = new SpriteGroup("temporary sprites");
		this.field.addCollisionGroup(goodGuys, badGuys,
				manageCollisionGoodBad());
		this.field.addGroup(this.temps);
		this.score = SCORE_INI;
		this.timerGenerate = new Timer(GENERATE_TIME);
		this.field.setComparator(getSpritesComparator());

	}

	@Override
	public void onStart() {
		this.bsSound.play("res/raw/music.wav");
	}

	@Override
	public void onStop() {
		this.bsSound.stopAll();
	}

	@Override
	public void update(long elapsedTime) {
		this.cleanup();
		this.field.update(elapsedTime);
		if (timerGenerate.action(elapsedTime)) {
			generateActor();
		}

		if (bsInput.isMouseDown(MouseEvent.BUTTON1)) {
			onTouchScreen(bsInput.getMouseX(), bsInput.getMouseY());
		}

		if (score == 0 || score >= SCORE_WIN) {
			this.finish();
		}

	}

	@Override
	public void render(GameGraphics g) {
		this.field.render(g);
		g.setColor(GameColor.WHITE);
		this.font1.drawString(g, "FPS:" + getFPS() + " Score " + score,
				GameFont.CENTER, 14, 0, this.getWidth());
	}

	private void cleanup() {
		for (SpriteGroup group : this.field.getGroups()) {
			group.removeInactiveSprites();
		}
	}

	/** check if an alive actor is located at a position */
	private Actor findActorAt(int x, int y) {
		// check for bad guys
		for (Actor actor : getActiveActors()) {
			if (actor.isInsideMe(x, y)) {
				return actor;
			}
		}
		return null;
	}

	/**
	 * Generate a new actor Good are slower that bad ones
	 */
	private void generateActor() {
		if (this.goodGuys.getSize() + this.badGuys.getSize() >= MAX_NBACTOR)
			return;

		Random rnd = new Random();
		boolean good = rnd.nextInt(2) == 1;
		int index = rnd.nextInt(6);
		GameImage[] images = good ? goodImages.get(index) : badImages
				.get(index);

		int x = rnd.nextInt(getWidth() - 50);
		int y = rnd.nextInt(getHeight() - 50);

		double xSpeed = 0, ySpeed = 0;

		double MAX_SPEED = 0.04;

		while (xSpeed == 0 && ySpeed == 0) {
			xSpeed = (2 * rnd.nextDouble() - 1) * MAX_SPEED;
			ySpeed = (2 * rnd.nextDouble() - 1) * MAX_SPEED;
		}

		Actor actor = new Actor(images, x, y, xSpeed, ySpeed, background, good);

		if (good) {
			goodGuys.add(actor);
		} else {
			badGuys.add(actor);
		}
	}

	/** get liost of active actors */
	private List<Actor> getActiveActors() {
		List<Actor> actors = new ArrayList<Actor>();
		Sprite[] sprites = this.goodGuys.getSprites();
		for (int i = 0; i < this.goodGuys.getSize(); i++) {
			if (sprites[i].isActive()) {
				actors.add((Actor) sprites[i]);
			}
		}
		sprites = this.badGuys.getSprites();
		for (int i = 0; i < this.badGuys.getSize(); i++) {
			if (sprites[i].isActive()) {
				actors.add((Actor) sprites[i]);
			}
		}
		return actors;
	}

	private Comparator<Sprite> getSpritesComparator() {
		return new Comparator<Sprite>() {

			public int compare(Sprite o1, Sprite o2) {
				return (int) ((o1.getLayer() - o2.getLayer()) * 1000 + (o1
						.getY() - o2.getY()));
			}

		};
	}

	private void killActor(Actor actor) {
		actor.setActive(false);
		TempSprite tmp = new TempSprite(blood, actor.getX(), actor.getY(), 3000);
		temps.add(tmp);
		if (actor.good) {
			score--;
			this.bsSound.play("res/raw/woman.wav");

			// soundPool.play(sndGood, streamVolume, streamVolume, 1, 0, 1f);
		} else {
			score++;
			this.bsSound.play("res/raw/man.wav");
			// soundPool.play(sndBad, streamVolume, streamVolume, 1, 0, 1f);
		}
	}

	private CollisionManager manageCollisionGoodBad() {
		return new BasicCollisionGroup() {

			@Override
			public void collided(Sprite s1, Sprite s2) {
				Actor act1 = (Actor) s1;
				Actor act2 = (Actor) s2;
				// between friends : each sprite change direction
				if (act1.good == act2.good) {
					act1.changeDirection();
				}
				// Between enemies: the good one dies
				else {
					if (act1.good) {
						killActor(act1);
					} else {
						killActor(act2);
					}
				}

			}
		};
	}

	/**
	 * Handle event onTouchScreen check if an alive actor is located at this
	 * point and just kill him
	 */
	private void onTouchScreen(int x, int y) {
		Actor touchedActor = findActorAt(x, y);
		if (touchedActor != null) {
			killActor(touchedActor);
		}
	}

}
