package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.razh.laser.screens.BasicScreen;
import com.razh.laser.screens.GameScreen;

public class LaserGame extends Game {

	public static final float WIDTH = 960.0f;
	public static final float HEIGHT = 720.0f;

	private InputMultiplexer mInputMultiplexer;
	private Player mPlayer;
	private FPSLogger mFPSLogger;

	private SpriteBatch mSpriteBatch;
	private BitmapFont mFont;

	private Map<String, BasicScreen> mScreens;

	public static final float PTM_RATIO = 120.0f;

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

		// Screens.
		mScreens = new HashMap<String, BasicScreen>();
		mScreens.put("GAME", new GameScreen(this));

		// Input.
		mInputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(mInputMultiplexer);
		Gdx.input.setCatchBackKey(true);

		setScreen(getScreens().get("GAME"));
	}

	@Override
	public void dispose() {
		mSpriteBatch.dispose();
		mFont.dispose();
	}

	@Override
	public void render() {
		super.render();

		mSpriteBatch.begin();
		mFont.draw(mSpriteBatch,
		           Integer.toString(Gdx.graphics.getFramesPerSecond()),
		           Gdx.graphics.getWidth() * 0.1f,
		           Gdx.graphics.getHeight() * 0.9f);
		mSpriteBatch.end();

		mFPSLogger.log();
	}

	public Map<String, BasicScreen> getScreens() {
		return mScreens;
	}

	public void setScreenByName(String name) {
		Screen screen = getScreens().get(name);
		if (screen != null) {
			setScreen(screen);
		}
	}

	public Player getPlayer() {
		return mPlayer;
	}

	public InputMultiplexer getInputMultiplexer() {
		return mInputMultiplexer;
	}
}
