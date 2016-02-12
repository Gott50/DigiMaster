package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import de.tmgdx.em.Constants;

public class Input_Stack extends Stack {
	private Array<Label> labelArray;
	private Array<TextField> textFieldArray;
	private String[] lableNameArray;

	public Input_Stack(String dataName, Skin skin, String[] labelsName) {
		// Colons can't be used, they case an error

		// if (this.lableNameArray == null)
		this.lableNameArray = labelsName;
		init(dataName, skin);
	}

	private void init(final String dataName, Skin skin) {
		labelArray = new Array<Label>();
		textFieldArray = new Array<TextField>();

		TextButton btnSubmit = createSubmitButton(dataName, skin);
		Table table = createInputTable(skin);
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

	private TextButton createSubmitButton(final String dataName, Skin skin) {
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
		return btnSubmit;
	}

	private Table createInputTable(Skin skin) {
		Table table = new Table();
		for (int i = 0; i < lableNameArray.length; i++) {
			Label lable = new Label(lableNameArray[i], skin);
			labelArray.add(lable);
			table.add(lable);
			TextField txtField = new TextField("", skin);
			textFieldArray.add(txtField);
			table.add(txtField).width(100);
			table.row();
		}
		return table;
	}

	private void submitDataToServer(Object data) {
		new HttpRequestHelper(HttpMethods.POST,data){
			@Override
			protected void handleResponse(HttpResponse httpResponse) {
				//TODO submit data Stuff
			}
		}.sendRequest();
	}

	private String dataToJsonString(Object data) {
		Json json = new Json();
		Gdx.app.log("dataToJson", json.prettyPrint(data));
		return json.toJson(data);
	}

	class EMData {
		private String command = "uploadData", dataName;
		private Array<String> nameArray, dataArray;

		public EMData(String dataName, Array<String> nameArray,
				Array<String> dataArray) {
			this.dataName = dataName;
			this.nameArray = nameArray;
			this.dataArray = dataArray;
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