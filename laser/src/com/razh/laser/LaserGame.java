package com.razh.laser;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LaserGame extends Game {

	public static final float WIDTH = 960.0f;
	public static final float HEIGHT = 720.0f;

	private MeshStage mStage;
	private FPSLogger mFPSLogger;

	private SpriteBatch mSpriteBatch;
	private BitmapFont mFont;

	private Map<String, BasicScreen> mScreens;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		mFPSLogger = new FPSLogger();
		if (!Gdx.graphics.isGL20Available()) {
			Gdx.app.exit();
		}

		// Blending.
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		// Backface culling.
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl20.glCullFace(GL20.GL_BACK);

		mSpriteBatch = new SpriteBatch();
		mFont = new BitmapFont();

		mPlayer = new Player();
	}

	@Override
	public void dispose() {
		mStage.dispose();
		mSpriteBatch.dispose();
		mFont.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
