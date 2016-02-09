package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public class Stack_default extends Stack {
	public Stack_default(Skin skin) {
		Label defaultLabel = new Label("DEFAULT Stack", skin);

		this.add(defaultLabel);
	}

}
