package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public enum Input_Stack_Prototyps {
	SP_II("Wasser", "Datum", "Kaltwasser FOS gross", "Kaltwasser FOS klein",
			"Warmwasser Turnh.", "Warmwasser FOS", "Kaltwasser WS gross",
			"Kaltwasser WS klein"), SP_I("Waermeenergie", "Datum", "Fernwärme",
			"Turnhalle", "Uebertragungsstation", "Hausmeister");
	private String name;
	private String[] content;

	Input_Stack_Prototyps(String name, String... content) {
		this.name = name;
		this.content = content;
	}

	public static Stack getStack(Input_Stack_Prototyps SP, Skin skin) {
		return new Input_Stack(SP.name, skin, SP.content);
	}
}
