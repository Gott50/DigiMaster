package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

public enum StackEnum {
	PC_I("Control");
	private static final String COMUNICATION_FORMAT = "application/json";
	private static final String SERVER_URL = "http://localhost:8080/SeminarEM_Tomcat/EMServer";

	StackEnum(String name) {
		this.name = name;
	}

	private String plattform; // TODO make it better
	private String name;

	public String toString() {
		return name;
	}

	public static final float CON_PORTION = 0.2f;// ControlPortion of the Stack

	public static String[] getNames() {
		return mergeScreenNames();
	}

	private static String[] mergeScreenNames() {
		String[] out = new String[getNumberOfScreens()];
		int index = 0;
		for (String name : OnlineStacks.getNameArrayFromServer()) {
			out[index++] = name;
		}
		for (StackEnum stackEnum : StackEnum.values()) {
			// TODO make it better
			if (OnlineStacks.getNameArrayFromServer() != null
					&& stackEnum.plattform == "SP")
				continue;
			out[index++] = stackEnum.toString();
		}
		return out;
	}

	private static int numScreens;

	private static int getNumberOfScreens() {
		if (numScreens != 0)
			return numScreens;

		int numScreens = StackEnum.values().length;
		if (OnlineStacks.getNameArrayFromServer() != null) {
			numScreens += OnlineStacks.getNameArrayFromServer().length;
		}
		return numScreens;
	}

	public static void generateStack(Skin skin) {
		for (StackEnum value : StackEnum.values())
			switch (value) {
			case PC_I:
				value.stack = new ConfigStack(skin);
				break;

			default:
				value.stack = new Stack_default(skin);
				break;
			}
	}

	private Stack stack;

	public Stack getStack() {
		return stack;
	}

	private static Stack[] stacks;

	public static Stack[] getStacks(Skin skin) {
		if (stacks != null)
			return stacks;

		stacks = new Stack[getNumberOfScreens()];
		int index = 0;
		for (String name : OnlineStacks.getNameArrayFromServer()) {
			stacks[index++] = OnlineStacks.loadStackDataFromServer(name, skin);
		}
		for (StackEnum stackEnum : StackEnum.values()) {
			// TODO make it better
			if (OnlineStacks.getNameArrayFromServer() != null
					&& stackEnum.plattform == "SP")
				continue;
			stacks[index++] = stackEnum.stack;
		}
		// TODO save StackData local
		return stacks;
	}
}
