package com.razh.laser.components;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.razh.laser.ActorContainer;
import com.razh.laser.sprites.SpriteActor;
import com.razh.laser.sprites.SpriteActor.Origin;

public class MissilePathComponent extends Component {

	private static final Vector2 mTempVector = new Vector2();
	private static final Vector2 mTempVector2 = new Vector2();

	public enum Mode {
		PEN_DOWN,
		PEN_UP
	}

	private Mode mDrawingMode;

	private Sprite mPathSprite;
	private final Color mPathColor;

	private final ActorContainer mPathActors;
	// The order the actors are stored in this deque is the same
	// as the order they are placed along the path.
	private final Deque<Actor> mCurrentPathActors;
	private Actor mCurrentPathActor;

	private int mSegmentCount;

	private float mPathWidth;
	private float mSegmentLength;
	private float mSegmentSpacing;
	private float mAlphaDecayRate;

	private final Vector2 mLastPosition;
	private float mDistanceTraveled;

	public MissilePathComponent() {
		mDrawingMode = Mode.PEN_UP;

		mPathSprite = new Sprite(new Texture("data/missile_path2.png"));
		mPathColor = new Color(218 / 255.0f, 56 / 255.0f, 56 / 255.0f, 1.0f);

		mPathActors = new ActorContainer();
		mCurrentPathActors = new LinkedList<Actor>();

		mSegmentCount = 8;

		mPathWidth = 15.0f;
		mSegmentLength = 100.0f;
		mSegmentSpacing = 5.0f;

		mAlphaDecayRate = 0.6f;

		mLastPosition = new Vector2();
		mDistanceTraveled = 0.0f;

		// TODO: Allocating path actors here means we can't
		// add from the child ActorContainer.
		allocatePathActors();

		// Get the first path actor available and remove it from the
		// unused path actors queue.
		mCurrentPathActor = getFirstPathActor();
	}

	@Override
	public void act(float delta) {
		Actor actor = getActor();
		mTempVector.set(actor.getX(), actor.getY());

		if (mDrawingMode == Mode.PEN_UP) {
			// Add distance traveled in this frame from last position.
			mDistanceTraveled += mTempVector.dst(mLastPosition);
			mLastPosition.set(mTempVector);

			if (mDistanceTraveled > mSegmentSpacing) {
				// Add a new path actor.
				mCurrentPathActor = mCurrentPathActors.removeLast();

				mCurrentPathActors.addFirst(mCurrentPathActor);
				mCurrentPathActor.setVisible(true);
				mCurrentPathActor.setColor(mPathColor);
				mCurrentPathActor.setWidth(0.0f);

				// Set segment start to current position.
				mCurrentPathActor.setPosition(mTempVector.x, mTempVector.y);
				mCurrentPathActor.setRotation((float) Math.atan2(mTempVector.y, mTempVector.x) * MathUtils.radiansToDegrees);
				mDistanceTraveled = 0.0f;

				// Start drawing.
				mDrawingMode = Mode.PEN_DOWN;
			}
		} else if (mDrawingMode == Mode.PEN_DOWN && mCurrentPathActor != null) {
			// Lengthen current path actor.
			// Get vector from mCurrentPathActor position to current position.
			mTempVector2.set(mCurrentPathActor.getX(), mCurrentPathActor.getY());
			mTempVector.sub(mTempVector2);

			mCurrentPathActor.setRotation((float) Math.atan2(mTempVector.y, mTempVector.x) * MathUtils.radiansToDegrees);

			// If length is greater, limit it, and stop drawing.
			float length = mTempVector.len();
			if (length > mSegmentLength) {
				length = Math.min(length, mSegmentLength);

				// Set position where segment spacing begins.
				mLastPosition.set(actor.getX(), actor.getY());

				// Stop drawing.
				mDrawingMode = Mode.PEN_UP;
			}

			// Set mCurrentPathActor to new length.
			mCurrentPathActor.setWidth(length);
		}

		// Update actors and check if we need can move them to
		// unused path actors queue (actor is not visible).
		SnapshotArray<Actor> pathActorArray = mPathActors.getActors();
		Actor[] pathActors = pathActorArray.begin();

		Actor pathActor;
		float alpha;
		for (int i = 0, n = pathActorArray.size; i < n; i++) {
			pathActor = pathActors[i];

			if (!pathActor.isVisible()) {
				continue;
			}

			alpha = Math.max(pathActor.getColor().a - mAlphaDecayRate * delta, 0.0f);
			if (alpha <= 0.0f) {
				pathActor.setVisible(false);
			}

			pathActor.getColor().a = alpha;
		}

		pathActorArray.end();
	}

	/**
	 * Pre-allocates path actors.
	 */
	public void allocatePathActors() {
		SnapshotArray<Actor> actors = mPathActors.getActors();
		int size = actors.size;

		SpriteActor spriteActor;
		for (int i = size; i < mSegmentCount; i++) {
			spriteActor = new SpriteActor();
			spriteActor.setSprite(mPathSprite);
			spriteActor.setOrigin(Origin.LEFT);
			spriteActor.setHeight(mPathWidth);

			actors.add(spriteActor);
			mCurrentPathActors.add(spriteActor);
		}
	}

	public Actor getPathActorAtIndex(int index) {
		SnapshotArray<Actor> pathActors = mPathActors.getActors();
		if (pathActors.size <= index) {
			return null;
		}

		Actor[] actors = pathActors.begin();
		Actor actor = actors[index];
		pathActors.end();

		return actor;
	}

	public Actor getFirstPathActor() {
		return getPathActorAtIndex(0);
	}

	public Actor getLastPathActor() {
		return getPathActorAtIndex(mPathActors.getActors().size - 1);
	}

	public ActorContainer getPathActors() {
		return mPathActors;
	}

	public int getSegmentCount() {
		return mSegmentCount;
	}

	/**
	 * Sets number of Actors representing segments.
	 * Goes ahead and pre-allocates that number.
	 * @param segmentCount
	 */
	public void setSegmentCount(int segmentCount) {
		mSegmentCount = segmentCount;
		mPathActors.getActors().ensureCapacity(mSegmentCount);
		allocatePathActors();
	}

	public Sprite getPathSprite() {
		return mPathSprite;
	}

	public void setPathSprite(Sprite sprite) {
		mPathSprite = sprite;

		SnapshotArray<Actor> pathActorArray = mPathActors.getActors();
		Actor[] pathActors = pathActorArray.begin();

		Actor pathActor;
		for (int i = 0, n = pathActorArray.size; i < n; i++) {
			pathActor = pathActors[i];

			if (pathActor instanceof SpriteActor) {
				((SpriteActor) pathActor).setSprite(mPathSprite);
			}
		}

		pathActorArray.end();
	}

	public float getPathWidth() {
		return mPathWidth;
	}

	public void setPathWidth(float pathWidth) {
		mPathWidth = pathWidth;
	}

	public Color getPathColor() {
		return mPathColor;
	}

	public float getSegmentLength() {
		return mSegmentLength;
	}

	public void setSegmentLength(float segmentLength) {
		mSegmentLength = segmentLength;
	}

	public float getSegmentSpacing() {
		return mSegmentSpacing;
	}

	public void setSegmentSpacing(float segmentSpacing) {
		mSegmentSpacing = segmentSpacing;
	}

	public float getAlphaDecayRate() {
		return mAlphaDecayRate;
	}

	public void setAlphaDecayRate(float alphaDecayRate) {
		mAlphaDecayRate = alphaDecayRate;
	}
}
