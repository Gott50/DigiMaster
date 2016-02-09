package de.tmgdx.em;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	public class AssetFonts {
		public final BitmapFont defaultBig;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultSmall;

		public AssetFonts() {
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(
					Gdx.files.internal("images/arial-15.fnt"), true);
			// set font sizes
			/*
			 * TODO defaultSmall.setScale(0.75f); defaultNormal.setScale(1.0f);
			 * defaultBig.setScale(2.0f);
			 */
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture()
					.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}

	public static final Assets instance = new Assets();
	public static final String TAG = Assets.class.getName();

	public AssetFonts fonts;

	private AssetManager assetManager;

	// singleton: prevent instantiation from other classes
	private Assets() {
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error(AssetDescriptor filename, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'",
				(Exception) throwable);

	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// set asset manager error handler
		assetManager.setErrorListener(this);
		loadAssets(assetManager);
		Gdx.app.debug(TAG,
				"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
	}

	private void loadAssets(AssetManager assetManager) {
		// The code should look like this:
		/*
		 * // load texture atlas
		 * assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
		 * TextureAtlas.class); // load sounds
		 * assetManager.load("sounds/jump.wav", Sound.class); // load music
		 * assetManager.load("music/keith303_-_brand_new_highscore.mp3",
		 * Music.class); // start loading assets and wait until finished
		 * assetManager.finishLoading();
		 */
	}
}