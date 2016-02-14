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
	private Array<Array<TextField>> posTextFieldArray2D;
	private Array<TextField> nameTextFieldArray;
	private TextField dataNameTextField;

	public PC_I_Stack(Skin skin) {
		init(skin);
	}

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
		this.clear();
		this.setSize(
				Constants.VIEWPORT_GUI_WIDTH * (1 - StackEnum.CON_PORTION),
				Constants.VIEWPORT_GUI_HEIGHT);
		this.setPosition(Constants.VIEWPORT_GUI_WIDTH * StackEnum.CON_PORTION,
				0);

		posTextFieldArray2D = new Array<Array<TextField>>();
		nameTextFieldArray = new Array<TextField>();
		nameTextFieldArray.add(new TextField("Name", skin));
		posTextFieldArray2D.add(new Array<TextField>());

		Table table = buildCoordinatTabel(skin, buildAddVarButton(skin));
		table.add().row();
		table.add(buildAddPosButton(skin));
		table.add(buildSaveButton(skin));
		this.add(table);
	}

	private Table buildCoordinatTabel(final Skin skin, TextButton btnAddVar) {
		Table table = new Table();
		dataNameTextField = new TextField("dataName", skin);
		table.add(dataNameTextField).row();

		for (int i = 0; i < nameTextFieldArray.size; i++) {
			table.add(nameTextFieldArray.get(i));
		}
		table.add(btnAddVar);
		for (Array<TextField> posTextFieldArray : posTextFieldArray2D) {
			table.row();
			for (int i = 0; i < nameTextFieldArray.size; i++)
				table.add(getPosTextField(skin, posTextFieldArray, i));
		}
		return table;
	}

	private TextField getPosTextField(final Skin skin,
			Array<TextField> posTextFieldArray, int index) {
		TextField posTextField = null;
		if (posTextFieldArray.size > index)
			posTextField = posTextFieldArray.get(index);
		else {
			posTextField = new TextField(generateCoordinate(), skin); // Position
			posTextFieldArray.add(posTextField);
		}
		return posTextField;
	}

	private String generateCoordinate() {
		// TODO add smart addition like in Excel
		return "1:1";
	}

	private TextButton buildAddVarButton(final Skin skin) {
		TextButton btnAddVar = new TextButton("+", skin);
		btnAddVar.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nameTextFieldArray.add(new TextField("Name", skin));
				init(skin);
			}
		});
		return btnAddVar;
	}

	private TextButton buildAddPosButton(final Skin skin) {
		TextButton btnAddPos = new TextButton("+", skin);
		btnAddPos.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				posTextFieldArray2D.add(new Array<TextField>());
				init(skin);
			}
		});
		return btnAddPos;
	}

	private TextButton buildSaveButton(final Skin skin) {
		TextButton btnSave = new TextButton("Save", skin);
		btnSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveConfigs();
			}
		});
		return btnSave;
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