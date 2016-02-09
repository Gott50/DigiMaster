package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.tmgdx.em.Constants;

public enum StackEnum {// SP_FROM_SERVER(""),
	SP_I("Waermeenergie"), SP_II("Wasser"), PC_I("Control");

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

	private static String[] nameArrayFromServer = null;

	/**
	 * load SPs Names form Server and return them
	 */
	private static void loadNameArrayFromServer() {
		// TODO check last configuration date

		final HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl(SERVER_URL);
		request.setHeader(HttpRequestHeader.ContentType, COMUNICATION_FORMAT);

		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				if (isExpectedStatus(httpResponse)) {
					String[] out = generateNames(httpResponse
							.getResultAsString());

					setNameArrayFromServer(out);
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error("HttpRequest", "something went wrong", t);
				// TODO handle it right
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Cancelled",
						"sendHttpRequest " + request.getMethod());
			}
		});
		// TODO Synchronize!!!
		while (nameArrayFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	private static boolean isExpectedStatus(HttpResponse httpResponse) {
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
		for (StackEnum stack : StackEnum.values())
			switch (stack) {
			case SP_I:
				stack.stack = new SP_I_Stack(stack.name, skin);
				break;
			case SP_II:
				stack.stack = new SP_II_Stack(stack.name, skin);
				break;
			case PC_I:
				stack.stack = new PC_I_Stack(skin);
				break;

			default:
				stack.stack = new Stack_default(skin);
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
		final HttpRequest request = new HttpRequest(HttpMethods.POST);
		request.setUrl(SERVER_URL);
		request.setHeader(HttpRequestHeader.ContentType, COMUNICATION_FORMAT);

		request.setContent(new Json().toJson(dataName));

		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				if (isExpectedStatus(httpResponse)) {
					setStackDataFromServer(generateStackData(httpResponse));
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error("HttpRequest", "something went wrong", t);
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Cancelled",
						"sendHttpRequest " + request.getMethod());
			}
		});
		// TODO Synchronize!!!
		while (stackDataFromServer == null)
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		return new SP_I_Stack(dataName, skin,
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

class Stack_default extends Stack {
	public Stack_default(Skin skin) {
		Label defaultLabel = new Label("DEFAULT Stack", skin);

		this.add(defaultLabel);
	}
}

class SP_I_Stack extends Stack {
	private Array<Label> labelArray = new Array<Label>();
	private Array<TextField> textFieldArray = new Array<TextField>();
	private String[] lableNameArray;

	public SP_I_Stack(String dataName, Skin skin) {
		// Colons can't be used, they case an error
		this(dataName, skin, new String[] { "Datum", "Fernwärme", "Turnhalle",
				"Uebertragungsstation", "Hausmeister" });
	}

	public SP_I_Stack(String dataName, Skin skin, String[] labelsName) {
		// try {
		// this.lableNameArray = getNameArrayFormServer(dataName);
		// } catch (Exception e) {
		// this.lableNameArray = null;
		// }
		if (this.lableNameArray == null)
			this.lableNameArray = labelsName;
		init(dataName, skin);
	}

	private void init(final String dataName, Skin skin) {
		TextButton btnSubmit = new TextButton("Submit", skin);
		btnSubmit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Array<String> dataArray = new Array<String>();
				for (TextField textField : textFieldArray) {
					dataArray.add(textField.getText());
				}
				Array<String> nameArray = new Array<String>();
				for (Label label : labelArray) {
					nameArray.add(label.getText().toString());
				}
				submitDataToServer(new EMData(dataName, nameArray, dataArray));
				// submitData(textArray);
			}
		});

		Table table = new Table();
		// add Labels and TextField to the Table and Array
		for (int i = 0; i < lableNameArray.length; i++) {
			// add Labels to the Table and Array
			Label lable = new Label(lableNameArray[i], skin);
			labelArray.add(lable);
			table.add(lable);
			// add TextField to the Table and Array
			TextField txtField = new TextField("", skin);
			textFieldArray.add(txtField);
			table.add(txtField).width(100);
			table.row();
		}

		// add Submit Button and fuse the columns
		table.add(btnSubmit).colspan(2);

		// add Table to Stack
		this.setSize(
				Constants.VIEWPORT_GUI_WIDTH * (1 - StackEnum.CON_PORTION),
				Constants.VIEWPORT_GUI_HEIGHT);
		this.setPosition(Constants.VIEWPORT_GUI_WIDTH * StackEnum.CON_PORTION,
				0);
		this.add(table);
	}

	private void submitDataToServer(Object data) {
		// set the HttpMethod | final could cause an Error
		final HttpRequest request = new HttpRequest(HttpMethods.POST);
		// set the Url
		request.setUrl("http://localhost:8080/SeminarEM_Tomcat/EMServer");
		// Be sure to set the Content-Type header for POST requests
		request.setHeader(HttpRequestHeader.ContentType, "application/json"); // application/x-www-form-urlencoded
		// set the content
		request.setContent(dataToJsonString(data));

		// send the HttpRequest
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log("Status code", ""
						+ httpResponse.getStatus().getStatusCode());
				Gdx.app.log("Result", httpResponse.getResultAsString());
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error("HttpRequest", "something went wrong", t);
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Cancelled",
						"sendHttpRequest " + request.getMethod());
			}
		});

	}

	private String dataToJsonString(Object data) {
		Json json = new Json();
		Gdx.app.log("dataToJson", json.prettyPrint(data));
		return json.toJson(data);
	}

	class EMData {
		// private String name = "Nate";
		// private int age = 31;
		// private String[] numbers = { "206-555-1234", "425-555-4321" };

		public EMData(String dataName, Array<String> nameArray,
				Array<String> dataArray) {
		}
	}

	class EMData2 {
		private Array<String> numbers = new Array<String>();
		private String[] namesArray;

		public EMData2(Array<String> nameArray, Array<String> dataArray) {
			// Just a Stub
			numbers.add("206-555-1234");
			numbers.add("425-555-4321");
			numbers.add(":Test::");

			namesArray = new String[nameArray.size];
			for (int i = 0; i < namesArray.length; i++) {
				namesArray[i] = nameArray.get(i);
				System.out.println(i + ": " + namesArray[i]);
			}
		}
	}

}

class SP_II_Stack extends SP_I_Stack {

	public SP_II_Stack(String dataName, Skin skin) {
		super(dataName, skin, new String[] { "Datum", "Kaltwasser FOS gross",
				"Kaltwasser FOS klein", "Warmwasser Turnh.", "Warmwasser FOS",
				"Kaltwasser WS gross", "Kaltwasser WS klein" });
	}

}

class PC_I_Stack extends Stack {
	// TODO Add Units
	private Array<Array<TextField>> posTextFieldArray2D = new Array<Array<TextField>>();
	private Array<TextField> nameTextFieldArray = new Array<TextField>();
	private TextField dataNameTextField;
	private Skin skin = null;

	public PC_I_Stack(Skin skin) {
		// this.skin = skin;
		init(skin);
	}

	/*
	 * private void readConfigs() { //TODO String fileString = "test";
	 * FileHandle fileBin = Gdx.files.local(fileString+".bin"); for (byte
	 * textField : fileBin.readBytes()) {
	 * 
	 * } fileBin.readBytes(); }
	 */

	private String textFieldArrayToString(Array<TextField> nameTextFieldArray) {
		String string = "";
		for (int i = 0; i < nameTextFieldArray.size; i++) {
			string = string.concat(nameTextFieldArray.get(i).getText());
			if (i + 1 < nameTextFieldArray.size)
				string = string.concat(":");
		}
		return string;
	}

	private byte[] textFieldArray2DToByteArray(
			Array<Array<TextField>> posTextFieldArray2D) {
		// TODO make it more flexible e.g. A1
		byte[] bytes = new byte[posTextFieldArray2D.size
				* posTextFieldArray2D.first().size * 2];
		int i = 0;
		for (Array<TextField> array : posTextFieldArray2D) {
			for (TextField textField : array) {
				String text = textField.getText();
				// textField Format must be "1:1" for A1
				String[] stringArray = text.split(":");
				bytes[i++] = Byte.parseByte(stringArray[0]);
				bytes[i++] = Byte.parseByte(stringArray[1]);
			}
		}
		return bytes;
	}

	private void init(final Skin skin) {
		if (nameTextFieldArray.size == 0)
			nameTextFieldArray.add(new TextField("Name", skin));
		if (posTextFieldArray2D.size == 0)
			posTextFieldArray2D.add(new Array<TextField>());

		// TODO convert letters to numbers
		TextButton btnSave = new TextButton("Save", skin);
		btnSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveConfigs();
			}
		});
		// TODO add smart addition like in Excel
		TextButton btnAddPos = new TextButton("+", skin);
		btnAddPos.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				posTextFieldArray2D.add(new Array<TextField>());
				init(skin);
			}
		});
		// TODO add smart addition like in Excel
		TextButton btnAddVar = new TextButton("+", skin);
		btnAddVar.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nameTextFieldArray.add(new TextField("Name", skin));
				init(skin);
			}
		});

		Table table = new Table();
		// add dataTypeTextField to Table
		if (dataNameTextField == null)
			dataNameTextField = new TextField("dataName", skin);
		table.add(dataNameTextField).row();

		/*
		 * // add Labels and TextField to the Table and Array for (int i = 0; i
		 * < nameTextFieldArray.size; i++) { // add TextField to the Table from
		 * Array table.add(nameTextFieldArray.get(i)); // add "in" Label to the
		 * Table Label lable = new Label(" in ", skin); table.add(lable); // add
		 * TextField to the Table and Array TextField txtField = null; if
		 * (posTextFieldArray.size > i) txtField = posTextFieldArray.get(i);
		 * else { txtField = new TextField("Position", skin);
		 * posTextFieldArray.add(txtField); } table.add(txtField).width(100);
		 * table.row(); }
		 */

		// add NameTextFields to the Table
		for (int i = 0; i < nameTextFieldArray.size; i++) {
			// add TextField to the Table from Array
			table.add(nameTextFieldArray.get(i));
		}
		// add AddVarButton to the Table
		table.add(btnAddVar);
		// add PosTextFields to the Table and Array
		for (Array<TextField> posTextFieldArray : posTextFieldArray2D) {
			table.row();
			for (int i = 0; i < nameTextFieldArray.size; i++) {
				// add TextField to the Table and Array
				TextField posTextField = null;
				if (posTextFieldArray.size > i)
					posTextField = posTextFieldArray.get(i);
				else {
					posTextField = new TextField("1:1", skin); // Position
					posTextFieldArray.add(posTextField);
				}
				table.add(posTextField);
			}
		}

		// add Buttons to Table
		table.add().row();
		table.add(btnAddPos);
		// TODO put SaveButton always left
		table.add(btnSave);

		// add Table to Stack
		this.clear();
		this.setSize(
				Constants.VIEWPORT_GUI_WIDTH * (1 - StackEnum.CON_PORTION),
				Constants.VIEWPORT_GUI_HEIGHT);
		this.setPosition(Constants.VIEWPORT_GUI_WIDTH * StackEnum.CON_PORTION,
				0);
		this.add(table);
	}

	private void saveConfigs() {
		// set the HttpMethod | final could cause an Error
		final HttpRequest request = new HttpRequest(HttpMethods.POST);
		// set the Url
		request.setUrl("http://localhost:8080/SeminarEM_Tomcat/EMServer");
		// Be sure to set the Content-Type header for POST requests
		request.setHeader(HttpRequestHeader.ContentType, "application/json"); // application/x-www-form-urlencoded
		// set the content
		request.setContent(dataToJsonString(new JsonData(dataNameTextField
				.getText(), posTextFieldArray2D, nameTextFieldArray)));

		// send the HttpRequest
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log("Status code", ""
						+ httpResponse.getStatus().getStatusCode());
				Gdx.app.log("Result", httpResponse.getResultAsString());
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error("HttpRequest", "something went wrong", t);
			}

			@Override
			public void cancelled() {
				Gdx.app.log("Cancelled",
						"sendHttpRequest " + request.getMethod());
			}
		});
	}

	class JsonData {
		private Array<String> nameArray;
		private byte[] posByteArray;

		public JsonData(String dataName,
				Array<Array<TextField>> posTextFieldArray2D,
				Array<TextField> nameTextFieldArray) {
			this.nameArray = new Array<String>();
			for (TextField textField : nameTextFieldArray) {
				nameArray.add(textField.getText());
			}
			this.posByteArray = textFieldArray2DToByteArray(posTextFieldArray2D);
		}
	}

	private String dataToJsonString(Object data) {
		Json json = new Json();
		Gdx.app.log("dataToJson", json.prettyPrint(data));
		return json.toJson(data);
	}

}
