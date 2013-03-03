package com.razh.laser;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "laser";
		cfg.useGL20 = true;
		cfg.width = 320;
		cfg.height = 640;
		cfg.useCPUSynch = false;

		new LwjglApplication(new LaserGame(), cfg);
	}
}
