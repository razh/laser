public class Starfield extends ActorContainer {

	private int mStarCount;

	public Starfield(int starCount) {
	}

	public int getStarCount() {
		return mStarCount;
	}

	public void setStarCount(int starCount) {
		// Remove unwanted actors.
		if (mStarCount > starCount) {
			int count = mStarCount - starCount;
			Actor removedActor;
			while (count > 0) {
				removedActor = getActors().removeIndex(count + starCount - 1);
				removedActor.remove();

				count--;
			}
		}

		mStarCount = starCount;
		// Add any necessary actors.
		getActors().ensureCapacity(mStarCount);

		allocateStarActors();
	}

	public void allocateStarActors() {
		SnapshotArray<Actor> actors = getActors();
		int size = actors.size;

		SpriteActor actor;
		for (int i = size; i < mStarCount; i++) {
			actor = new SpriteActor();
		}
	}
}
