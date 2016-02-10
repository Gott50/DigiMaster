package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.tmgdx.em.Assets;
import de.tmgdx.em.DirectedGame;

public abstract class AbstractScreen implements Screen {
	protected  DirectedGame game;
	
	public AbstractScreen(DirectedGame game) {
		this.game = game;
	}

	public void dispose() {
		Assets.instance.dispose();
	}

	public abstract InputProcessor getInputProcessor(); // Screen Transitions

	public abstract void hide();

	public abstract void pause();

	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	public abstract void show();
}
