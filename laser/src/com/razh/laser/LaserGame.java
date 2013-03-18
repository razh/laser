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
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
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
	private Sprite ringSprite;
	private Sprite arcSprite;
	private Sprite dashedRingSprite;
	private Sprite dashedLineSprite;

	private ShaderProgram circleShader;
	private ShaderProgram ringShader;
	private ShaderProgram arcShader;
	private ShaderProgram dashedRingShader;
	private ShaderProgram dashedLineShader;

	private Texture laserTexture;
	private TextureRegion laserRegion;
	private Sprite laserSprite;
	private Sprite laserSprite2;
	private Sprite laserSprite3;
	private Sprite laserGlowSprite;
	private ShaderProgram laserShader;
	private ShaderProgram laserGlowShader;

	private float spread = 16;
	private float scale = 1;

	private Map<String, BasicScreen> mScreens;

	public static boolean DEBUG = false;

	@Override
	public void create() {
		Gdx.graphics.setVSync(true);

		mFPSLogger = new FPSLogger();
		if (!Gdx.graphics.isGL20Available()) {
			Gdx.app.exit();
		}

		// Ignore invalid uniforms.
		ShaderProgram.pedantic = false;

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
		distanceField.setSpread(spread);
		distanceField.setDownscale(4);
		pixmap = new Pixmap(1024, 1024, Pixmap.Format.RGBA8888);
		pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
//		pixmap = new Pixmap(Gdx.files.internal("data/libgdx.png"));
		pixmap.setColor(Color.WHITE);
//		pixmap.fillRectangle(256, 0, 5, 512);
//		pixmap.fillCircle(512, 512, 384);
//		pixmap.setColor(Color.BLACK);
//		pixmap.fillRectangle(512, 0, 512, 1024);
		pixmap.fill();
//		distanceFieldPixmap = distanceField.generateDistanceField(pixmap);
		texture = new Texture(pixmap);
//		texture = new Texture(distanceFieldPixmap);
//		pixmap.dispose();
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		region = new TextureRegion(texture, 0, 0, 1, 1);
//		texture.dispose();

//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		region = new TextureRegion(texture, 0, 0, 512, 275);

		sprite = new Sprite(region);
		scale = 256.0f / 256.0f;
		sprite.setSize(256.0f, 256.0f);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, 0);

		ringSprite = new Sprite(region);
		ringSprite.setSize(256f, 256f);
		ringSprite.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.1f);

		arcSprite = new Sprite(region);
		arcSprite.setSize(256.0f, 256.0f);
		arcSprite.setPosition(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.2f);

		dashedRingSprite = new Sprite(region);
		dashedRingSprite.setSize(256.0f, 256.0f);
		dashedRingSprite.setPosition(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight() * 0.5f);
		dashedRingSprite.setOrigin(0.5f * dashedRingSprite.getWidth(), 0.5f * dashedRingSprite.getHeight());

		dashedLineSprite = new Sprite(region);
		dashedLineSprite.setSize(1024.0f, 16.0f);
		dashedLineSprite.setPosition(Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.4f);

//		distanceFieldShader = Shader.createDistanceFieldShader();
		circleShader = Shader.createCircleShader();
		ringShader = Shader.createRingShader();
		arcShader = Shader.createArcShader();
		dashedRingShader = Shader.createDashedRingShader();
		dashedLineShader = Shader.createDashedLineShader();

		Pixmap laserPixmap = new Pixmap(1, 1, Format.RGBA8888);
		laserPixmap.setColor(Color.WHITE);
		laserPixmap.fill();
		laserTexture = new Texture(laserPixmap);
		laserTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		laserRegion = new TextureRegion(laserTexture, 0, 0, 1, 1);

		laserSprite = new Sprite(laserRegion);
		laserSprite.setSize(1024f, 32f);
		laserSprite.setPosition(20, 400);

		laserSprite2 = new Sprite(laserRegion);
		laserSprite2.setSize(1024f, 32f);
		laserSprite2.rotate(90.0f);
		laserSprite2.setPosition(500, 100);

		laserSprite3 = new Sprite(laserRegion);
		laserSprite3.setSize(1024f, 32f);
		laserSprite3.setOrigin(0, 16);
		laserSprite3.rotate(120.0f);
		laserSprite3.setPosition(1044f, 400f);

		laserGlowSprite = new Sprite(laserRegion);
		laserGlowSprite.setSize(32f, 32f);
		laserGlowSprite.setPosition(1028f, 400f);

		laserPixmap.dispose();

		laserShader = Shader.createLaserBeamShader();
		laserGlowShader = Shader.createLaserGlowShader();

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

	public float time = 0.0f;
	public float leftAngle = 0.0f;
	public float segmentSpacing = 8.0f;
	@Override
	public void render() {
		super.render();
//		sprite.rotate(0.5f);
		sprite.setSize(sprite.getWidth() * 1.0001f, sprite.getHeight() * 1.0001f);
//		laserSprite.rotate(0.5f);
		laserSprite3.rotate(0.1f);
//		ringSprite.setSize(ringSprite.getWidth() * 1.001f, ringSprite.getHeight() * 1.001f);
//		arcSprite.rotate(0.1f);
//		dashedRingSprite.rotate(2.0f);
		time += 0.1f;
		leftAngle += 1.0f;
		if ( leftAngle > 180)
			leftAngle = 0;

		mSpriteBatch.begin();
		mSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		mSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR);
		mFont.draw(mSpriteBatch,
		           Integer.toString(Gdx.graphics.getFramesPerSecond()),
		           Gdx.graphics.getWidth() * 0.1f,
		           Gdx.graphics.getHeight() * 0.9f);
		mSpriteBatch.setShader(circleShader);
		circleShader.setUniformf("size", sprite.getWidth());
		circleShader.setUniformf("color", Color.WHITE);
//		mSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
//		distanceFieldShader.setUniformf("spread", spread);
//		distanceFieldShader.setUniformf("scale", sprite.getWidth() / 256f);
		sprite.draw(mSpriteBatch);

		mSpriteBatch.setShader(ringShader);
		ringShader.setUniformf("color", Color.WHITE);
		ringShader.setUniformf("size", ringSprite.getWidth());
		ringShader.setUniformf("outerRadius", 0.5f);
		ringShader.setUniformf("innerRadius", 0.4f);
		ringSprite.draw(mSpriteBatch);

		mSpriteBatch.setShader(arcShader);
		arcShader.setUniformf("color", Color.WHITE);
		arcShader.setUniformf("size", arcSprite.getWidth());
		arcShader.setUniformf("outerRadius", 0.5f);
		arcShader.setUniformf("innerRadius", 0.4f);
		arcShader.setUniformf("leftAngle", 45 * MathUtils.degreesToRadians);
		arcShader.setUniformf("rightAngle", 45 * MathUtils.degreesToRadians);
		arcSprite.draw(mSpriteBatch);

		mSpriteBatch.setShader(laserShader);
		mSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		laserShader.setUniformf("time", time);
		laserSprite.draw(mSpriteBatch);
		laserSprite2.draw(mSpriteBatch);
		laserSprite3.draw(mSpriteBatch);

		mSpriteBatch.setShader(dashedRingShader);
		dashedRingShader.setUniformf("color", Color.WHITE);
		dashedRingShader.setUniformf("size", dashedRingSprite.getWidth());
		dashedRingShader.setUniformf("outerRadius", 0.5f);
		dashedRingShader.setUniformf("innerRadius", 0.4f);
		dashedRingShader.setUniformf("segmentAngle", 36.0f);
		dashedRingShader.setUniformf("segmentSpacing", segmentSpacing);
		dashedRingSprite.draw(mSpriteBatch);

		mSpriteBatch.setShader(dashedLineShader);
		dashedLineShader.setUniformf("color", Color.WHITE);
		dashedLineShader.setUniformf("width", dashedLineSprite.getWidth());
		dashedLineShader.setUniformf("height", dashedLineSprite.getHeight());
		dashedLineShader.setUniformf("segmentLength", 64.0f);
		dashedLineShader.setUniformf("segmentSpacing", 8.0f);
		dashedLineSprite.draw(mSpriteBatch);
		dashedLineSprite.rotate(0.2f);

		segmentSpacing -= 0.05f;
		if (segmentSpacing < 0.0f) {
			segmentSpacing = 16.0f;
		}

		mSpriteBatch.setShader(laserGlowShader);
		mSpriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		laserGlowShader.setUniformf("time", time);
		laserGlowSprite.draw(mSpriteBatch);

		mSpriteBatch.setShader(null);
		mSpriteBatch.end();

		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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
