package de.tmgdx.em;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import de.tmgdx.em.gui.screeens.MainScreen;

public class Main extends DirectedGame {
	private final PlatformSpecificCode platSpeCode;
	public Main(PlatformSpecificCode platSpeCode) {
		this.platSpeCode = platSpeCode;
	}

	@Override
	public void create() {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

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

	/*private void poi() {
		String filesname = "Hello.doc";
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(filesname));
			// Couldn't close the braces at the end as my site did not allow it
			// to close

			HWPFDocument doc = new HWPFDocument(fs);

			WordExtractor we = new WordExtractor(doc);

			String[] paragraphs = we.getParagraphText();

			System.out.println("Word Document has " + paragraphs.length
					+ " paragraphs");
			for (int i = 0; i < paragraphs.length; i++) {
				paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
				System.out.println("Length:" + paragraphs[i].length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
