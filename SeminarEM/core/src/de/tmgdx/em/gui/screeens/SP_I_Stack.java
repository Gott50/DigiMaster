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

public class SP_I_Stack extends Stack {
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