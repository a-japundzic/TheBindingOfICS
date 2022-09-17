package com.japundzic.icsgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screens.menuScreen;
import screens.playScreen;
/**
 * Sets up the game and loads the music and sounds
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class icsGame extends Game {
	//Virutal screen size and Box2D scale(Pixels per meter)
	public SpriteBatch batch;
	public static final int V_WIDTH = 418;
	public static final int V_HEIGHT = 286;
	public static final float PPM = 100;

	//Box2D Collision Bits
	public static final short DEAFAULT_BIT = 1;
	public static final short ISAAC_BIT = 2;
	public static final short ISAAC_HEAD_BIT = 4;
	public static final short BULLET_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short CAMERA_BIT = 32;
	public static final short DOOR_BIT = 64;
	public static final short ENEMY_BULLET_BIT= 128;

	public static AssetManager manager;

	/**
	 * Creates and loads all the music and sounds
	 *
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/gameMusic.mp3", Music.class);
		manager.load("audio/music/titleScreenIntro.ogg", Music.class);
		manager.load("audio/music/titleScreenJingle.ogg", Music.class);
		manager.load("audio/music/titleScreenLoop.ogg", Music.class);
		manager.load("audio/music/credits.ogg", Music.class);
		manager.load("audio/music/death.ogg", Music.class);
		manager.load("audio/music/bossFight.ogg", Music.class);
		manager.load("audio/sounds/tearImpact.mp3", Sound.class);
		manager.load("audio/sounds/tearFire.mp3", Sound.class);
		manager.load("audio/sounds/deathBurst.mp3", Sound.class);
		manager.load("audio/sounds/isaacdies.mp3", Sound.class);
		manager.load("audio/sounds/Isaac_Hurt_Grunt0.mp3", Sound.class);
		manager.load("audio/sounds/bossFire.mp3", Sound.class);
		manager.load("audio/sounds/bossGrunt.mp3", Sound.class);
		manager.load("audio/sounds/bossDeathBurst.mp3", Sound.class);
		manager.finishLoading();
		setScreen(new menuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
