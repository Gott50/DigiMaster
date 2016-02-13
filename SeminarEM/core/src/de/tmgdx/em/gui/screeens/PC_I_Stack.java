package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.tmgdx.em.Constants;
import de.tmgdx.em.gui.screeens.HttpContentObject.Command;

public class PC_I_Stack extends Stack {
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
		new HttpRequestHelper(HttpMethods.POST, new HttpContentObject(
				Command.CONFIG, dataNameTextField.getText(),
				generateConfigDataset(posTextFieldArray2D, nameTextFieldArray))) {
			@Override
			protected void handleResponse(HttpResponse httpResponse) {
				// TODO save local configs + rerender
			}
		}.sendRequest();
	}

	/*
	 * class JsonData { // FIXME private String command = "updateConfigs";
	 * private Array<String> nameArray; private byte[] posByteArray; private
	 * String dataName;
	 * 
	 * public JsonData(String dataName, Array<Array<TextField>>
	 * posTextFieldArray2D, Array<TextField> nameTextFieldArray) { this.dataName
	 * = dataName;
	 * 
	 * this.nameArray = new Array<String>(); for (TextField textField :
	 * nameTextFieldArray) { nameArray.add(textField.getText()); }
	 * this.posByteArray = textFieldArray2DToByteArray(posTextFieldArray2D); }
	 * }//
	 */

	private Array<String>[] generateConfigDataset(
			Array<Array<TextField>> posTextFieldArray2D,
			Array<TextField> nameTextFieldArray) {
		Array<String> nameArray = new Array<String>();
		for (TextField textField : nameTextFieldArray) {
			nameArray.add(textField.getText());
		}
		byte[] posByteArray = textFieldArray2DToByteArray(posTextFieldArray2D);
		Array<String> dataArray = new Array<String>();
		for (byte b : posByteArray) {
			dataArray.add(b + "");
		}

		return new Array[] { nameArray, dataArray };
	}

	private String dataToJsonString(Object data) {
		Json json = new Json();
		Gdx.app.log("dataToJson", json.prettyPrint(data));
		return json.toJson(data);
	}

}