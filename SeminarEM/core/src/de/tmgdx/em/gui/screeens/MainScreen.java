package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.tmgdx.em.Constants;
import de.tmgdx.em.DirectedGame;

public class MainScreen extends AbstractScreen {
	private Stage stage;
	private Skin skinLibgdx;
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;// debug
	private boolean debugEnabled = false;// debug
	private float debugRebuildStage;// debug

	public MainScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	@Override
	public void hide() {
		stage.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (debugEnabled) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				init();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(true);
	}

	@Override
	public void resize(int width, int height) {
		// stage.getViewport().update((int) Constants.VIEWPORT_GUI_WIDTH,(int)
		// Constants.VIEWPORT_GUI_HEIGHT, false);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		init();
	}

	private void init() {
		stage = new Stage();
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
		// generates the Stacks
		StackEnum.generateStack(skinLibgdx);

		// assemble stage for menu screen
		rebuildStage();
	}
	
	private void rebuildStage() {
		stage.clear();
		stage.addActor(buildControlsStack());
		if(mainStack != null)
		stage.addActor(buildMainStack());
	}

	private Stack mainStack = StackEnum.SP_I.getStack();

	private Stack buildMainStack() {
		Stack stack = mainStack;//.getStack()
		stack.setSize(
				Constants.VIEWPORT_GUI_WIDTH * (1 - StackEnum.CON_PORTION),
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.setPosition(Constants.VIEWPORT_GUI_WIDTH * StackEnum.CON_PORTION,
				0);
		return stack;
	}

	private Stack buildControlsStack() {
		Stack conStack = new Stack();
		conStack.setSize(Constants.VIEWPORT_GUI_WIDTH * StackEnum.CON_PORTION,
				Constants.VIEWPORT_GUI_HEIGHT);

		VerticalGroup vertGroup = new VerticalGroup();
		vertGroup.left();

		/*for (final StackEnum stack : StackEnum.values()) {
			TextButton btnSwitchScreen = new TextButton(stack.toString(),
					skinLibgdx);
			vertGroup.addActor(btnSwitchScreen);
			btnSwitchScreen.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					mainStack = stack;
					rebuildStage();
				}
			});
		}*/
		String[] namesArray = StackEnum.getNames();
		final Stack[] stacksArray = StackEnum.getStacks(skinLibgdx);
		for (int i = 0; i < namesArray.length; i++) {
			TextButton btnSwitchScreen = new TextButton(namesArray[i],
					skinLibgdx);
			vertGroup.addActor(btnSwitchScreen);
			final int STACK_NUM = i;
			btnSwitchScreen.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					mainStack = stacksArray[STACK_NUM];
					
					rebuildStage();
				}
			});
		}
		
		conStack.add(vertGroup);

		return conStack;
	}
}
