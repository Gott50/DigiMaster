package de.tmgdx.em;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import de.tmgdx.em.gui.screeens.MainScreen;

public class Main extends DirectedGame {
	public final int LOG_LEVEL = Application.LOG_DEBUG;
	private final PlatformSpecificCode platSpeCode;

	public Main(PlatformSpecificCode platSpeCode) {
		this.platSpeCode = platSpeCode;
	}

	@Override
	public void create() {
		// Set Libgdx log level
		Gdx.app.setLogLevel(LOG_LEVEL);

		// Load assets
		Assets.instance.init(new AssetManager());

		// Load preferences for audio settings and start playing music
		// TODO GamePreferences.instance.load();
		// AudioManager.instance.play(Assets.instance.music.song01);

		// Start game at menu screen
		// ScreenTransition transition = ScreenTransitionSlice.init(2,
		// ScreenTransitionSlice.UP_DOWN, 10, Interpolation.pow5Out);
		setScreen(new MainScreen(this));
	}

	public PlatformSpecificCode getPlatSpeCode() {
		return platSpeCode;
	}
}
