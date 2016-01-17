package de.tmgdx.em;

import com.badlogic.gdx.Gdx;

public class Constants {
	// Accelerometer
	// Angle of rotation for dead zone (no movement)
	public static final float ACCEL_ANGLE_DEAD_ZONE = 5.0f;
	// Accelerometer
	// Max angle of rotation needed to gain max movement velocity
	public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;
	// Number of carrots to spawn
	public static final int CARROTS_SPAWN_MAX = 100;
	// Spawn radius for carrots
	public static final float CARROTS_SPAWN_RADIUS = 3.5f;
	// Duration of feather power-up in seconds
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;
	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";
	// Amount of extra lives at level start
	public static final int LIVES_START = 3;
	// Shader
	public static final String shaderMonochromeFragment = "shaders/monochrome.fs";

	// Shader
	public static final String shaderMonochromeVertex = "shaders/monochrome.vs";
	// Location of description file for skins
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
	// Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "images/uiskin.json";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";

	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";
	// Delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 6;
	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;

	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = Gdx.graphics.getHeight(); //800.0f;
	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = Gdx.graphics.getWidth(); //480.0f;

	// Visible game world is 10 meters tall
	public static final float VIEWPORT_HEIGHT = 16.0f;
	// Visible game world is 10 meters wide
	public static final float VIEWPORT_WIDTH = 9.0f;
	public static final boolean DEBUG = true;
	
	
	//Saves:
	//Settings/Preferences
	public static final String PREFERENCES = "Settings.pref";
	public static final String CONTROLS = "Controls.pref";
	
	public static long gold = 0;
}
