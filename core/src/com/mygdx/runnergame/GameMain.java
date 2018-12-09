package com.mygdx.runnergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import scenes.Gameplay;

public class GameMain extends Game {
	private SpriteBatch batch;
	private Music music;

	@Override
	public void create () {
		batch = new SpriteBatch();
		// do the music
		music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/music.mp3"));
		music.setLooping(true);
		music.play();
		// open new game screen
		setScreen(new Gameplay(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public SpriteBatch getBatch(){
		return this.batch;
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
