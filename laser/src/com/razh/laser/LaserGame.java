package com.razh.laser;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.razh.laser.images.DistanceField;
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

	private Pixmap pixmap;
	private Pixmap distanceFieldPixmap;
	private Texture texture;
	private TextureRegion region;
	private Sprite sprite;

	private Map<String, BasicScreen> mScreens;

	public static boolean DEBUG = true;

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

		// Test distance field.
		DistanceField distanceField = new DistanceField();
		distanceField.setSpread(16);
		pixmap = new Pixmap(512, 512, Pixmap.Format.RGBA8888);
//		pixmap = new Pixmap(Gdx.files.internal("data/libgdx.png"));
		pixmap.setColor(Color.WHITE);
//		pixmap.fillRectangle(0, 0, 512, 512);
		pixmap.fillCircle(256, 256, 128);
//		pixmap.fill();
		distanceFieldPixmap = distanceField.generateDistanceField(pixmap);
//		texture = new Texture(pixmap);
		texture = new Texture(distanceFieldPixmap);
//		pixmap.dispose();
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		region = new TextureRegion(texture, 0, 0, 512, 512);
//		texture.dispose();

//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		region = new TextureRegion(texture, 0, 0, 512, 275);

		sprite = new Sprite(region);
		sprite.setSize(1024.0f, 1024.0f);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, 0);

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
		sprite.draw(mSpriteBatch);
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
