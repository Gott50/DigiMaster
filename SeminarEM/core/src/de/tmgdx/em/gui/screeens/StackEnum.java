package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;

import de.tmgdx.em.gui.screeens.HttpContentObject.Command;

public enum StackEnum {// SP_FROM_SERVER(""),SP_I("Waermeenergie"), SP_II("Wasser"),
	PC_I("Control");

	public static final String COMUNICATION_FORMAT = "application/json";
	public static final String SERVER_URL = "http://localhost:8080/SeminarEM_Tomcat/EMServer";

	StackEnum(String name) {
		this.name = name;
	}

	private String plattform; // TODO make it better
	private String name;

	public String toString() {
		return name;
	}

	public static final float CON_PORTION = 0.2f;// ControlPortion of the Stack

	private static String[] nameArrayFromServer = null;

	/**
	 * load SPs Names form Server and return them
	 */
	private static void loadNameArrayFromServer() {
		// TODO check last configuration date
		HttpRequestHelper request = new HttpRequestHelper(){
			@Override
			protected void handleResponse(HttpResponse httpResponse) {
				String[] out = generateNames(httpResponse
						.getResultAsString());

				setNameArrayFromServer(out);
			}
		};
		request.sendRequest();

		// TODO Synchronize!!!
		while (nameArrayFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public static boolean isExpectedStatus(HttpResponse httpResponse) {
		if (httpResponse.getStatus().getStatusCode() != 200) {
			Gdx.app.log("unexpected Status code", ""
					+ httpResponse.getStatus().getStatusCode());
			Gdx.app.log("Result", httpResponse.getResultAsString());
			return false;
		} else
			return true;
	}

	private static String[] generateNames(String restultString) {
		String[] strArray2 = restultString.split("File: ");

		int elements = 0;
		for (String string : strArray2) {
			if (string.isEmpty() || !string.contains(".txt"))// ||
																// !string.endsWith(".txt")
				continue;
			// TODO Filter Directories and /n out
			System.out.println(elements + ": " + string);
			elements++;
		}
		String[] out = new String[elements];
		int i = 0;
		for (String string : strArray2) {
			if (string.isEmpty() || !string.contains(".txt"))
				continue;
			out[i++] = string.substring(0, string.length() - 4);
		}
		return out;
	}

	private static void setNameArrayFromServer(String[] out) {
		nameArrayFromServer = out;
		// TODO save NameArray local
	}

	public static String[] getNames() {
		return mergeScreenNames();
	}

	private static String[] mergeScreenNames() {
		String[] out = new String[getNumberOfScreens()];
		int index = 0;
		for (String name : getNameArrayFromServer()) {
			out[index++] = name;
		}
		for (StackEnum stackEnum : StackEnum.values()) {
			// TODO make it better
			if (getNameArrayFromServer() != null && stackEnum.plattform == "SP")
				continue;
			out[index++] = stackEnum.toString();
		}
		return out;
	}

	private static int getNumberOfScreens() {
		int length = StackEnum.values().length;
		if (getNameArrayFromServer() != null) {
			length += getNameArrayFromServer().length;
			// TODO make it better length -= 2;
		}
		return length;
	}

	public static void generateStack(Skin skin) {
		for (StackEnum value : StackEnum.values())
			switch (value) {
			/*case SP_II:
				value.stack = new Input_Stack(value.name, skin, new String[] {
						"Datum", "Kaltwasser FOS gross",
						"Kaltwasser FOS klein", "Warmwasser Turnh.",
						"Warmwasser FOS", "Kaltwasser WS gross",
						"Kaltwasser WS klein" });
				break;
			case SP_I:
				value.stack = new Input_Stack(value.name, skin, new String[] {
						"Datum", "Fernwärme", "Turnhalle",
						"Uebertragungsstation", "Hausmeister" });
				break;*/
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

	public static Stack[] getStacks(Skin skin) {
		Stack[] out = new Stack[getNumberOfScreens()];
		int index = 0;
		for (String name : getNameArrayFromServer()) {
			out[index++] = loadStackDataFromServer(name, skin);
		}
		for (StackEnum stackEnum : StackEnum.values()) {
			// TODO make it better
			if (getNameArrayFromServer() != null && stackEnum.plattform == "SP")
				continue;
			out[index++] = stackEnum.stack;
		}

		return out;
	}

	private static Stack loadStackDataFromServer(String dataName, Skin skin) {
		// TODO make it more elegant: load all data needed when Started
		HttpRequestHelper request = new HttpRequestHelper(HttpMethods.POST,new HttpContentObject(Command.LOAD, dataName)){
			@Override
			protected void handleResponse(HttpResponse httpResponse) {
				setStackDataFromServer(generateStackData(httpResponse));
			}
		};
		request.sendRequest();
		
		// TODO Synchronize!!!
		while (stackDataFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return new Input_Stack(dataName, skin,
				(String[]) stackDataFromServer.toArray(String.class));
		// TODO save StackData local
	}

	private static Array<String> generateStackData(HttpResponse httpResponse) {
		Array<String> strArray = new Array<String>();
		for (String string : httpResponse.getResultAsString().split(",")) {
			if (string.isEmpty())
				continue;
			// TODO Filter Directories and/n out
			if (string.startsWith("["))
				string = string.substring(2, string.length() - 1);
			else if (string.endsWith("\n"))
				string = string.substring(1, string.length() - 3);
			else
				string = string.substring(1, string.length() - 1);
			strArray.add(string);
		}
		return strArray;
	}

	private static Array<String> stackDataFromServer;

	protected static void setStackDataFromServer(Array<String> stackData) {
		stackDataFromServer = stackData;
	}

	protected static Array<String> getStackDataFromServer() {
		Array<String> out = stackDataFromServer;
		stackDataFromServer = null;
		return out;
	}

	private static String[] getNameArrayFromServer() {
		if (nameArrayFromServer == null)
			loadNameArrayFromServer();
		return nameArrayFromServer;
	}
}
