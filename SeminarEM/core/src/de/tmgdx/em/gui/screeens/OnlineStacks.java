package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;

import de.tmgdx.em.gui.screeens.HttpContentObject.Command;

public class OnlineStacks {
	private static String[] nameArrayFromServer;
	
	public static String[] getNameArrayFromServer() {
		if (nameArrayFromServer == null)
			loadNameArrayFromServer();
		return nameArrayFromServer;
	}
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

		while (nameArrayFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	private static void setNameArrayFromServer(String[] out) {
		nameArrayFromServer = out;
	}
	private static String[] generateNames(String resultString) {
		String[] strArray = resultString.split("File: ");

		int elements = 0;
		for (String string : strArray) {
			if (string.isEmpty() || !string.contains(".txt"))
				continue;
			System.out.println(elements + ": " + string);
			elements++;
		}
		String[] out = new String[elements];
		int i = 0;
		for (String string : strArray) {
			if (string.isEmpty() || !string.contains(".txt"))
				continue;
			out[i++] = string.substring(0, string.length() - 4);
		}
		return out;
	}
	
	public static Stack loadStackDataFromServer(String dataName, Skin skin) {
		// TODO make it more elegant: load all data needed when Started
		HttpRequestHelper request = new HttpRequestHelper(HttpMethods.POST,new HttpContentObject(Command.LOAD, dataName)){
			@Override
			protected void handleResponse(HttpResponse httpResponse) {
				setStackDataFromServer(OnlineStacks.generateStackData(httpResponse));
			}
		};
		request.sendRequest();
		
		while (OnlineStacks.stackDataFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		Stack out = new Input_Stack(dataName, skin,
				(String[]) OnlineStacks.stackDataFromServer.toArray(String.class));
		OnlineStacks.stackDataFromServer = null;
		return out ;
	}
	static Array<String> generateStackData(HttpResponse httpResponse) {
		String result = httpResponse.getResultAsString();
		System.out.println("Result: "+result);
		Array<String> strArray = new Array<String>();
		
		for (String string : result.split(",")) {
			if (string.isEmpty())
				continue;
			if (string.startsWith("["))
				string = string.substring(2, string.length() - 1);
			else if (string.endsWith("\n"))
				string = string.substring(1, string.length() - 3);
			if(string.endsWith("\"]"))
				string = string.substring(0, string.length() - 2);
			else
				string = string.substring(1, string.length() - 1);
			strArray.add(string);
		}
		return strArray;
	}
	static Array<String> stackDataFromServer;
	protected static Array<String> getStackDataFromServer() {
		Array<String> out = stackDataFromServer;
		stackDataFromServer = null;
		return out;
	}
	static void setStackDataFromServer(Array<String> stackData) {
		stackDataFromServer = stackData;
	}


}
