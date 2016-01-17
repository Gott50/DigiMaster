package de.tmtomcat.em;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tmtomcat.em.DataManager.Data;

/**
 * Servlet implementation class EMServer
 */
@WebServlet(description = "Energy Management Server", urlPatterns = { "/EMServer" })
public class EMServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userDataFolderString;
	private String positionFolderString;
	private String dataFolderString;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		// creating Folders and Strings
		userDataFolderString = getServletContext().getRealPath("/UserData");
		System.out.println("cretated new UserDataFoler? "
				+ new File(userDataFolderString).mkdir());
		positionFolderString = userDataFolderString + "/Positions";
		System.out.println("cretated new PositionFolder? "
				+ new File(positionFolderString).mkdir());
		dataFolderString = userDataFolderString + "/Data";
		System.out.println("cretated new DataFolderString? "
				+ new File(dataFolderString).mkdir());
	}

	JSONObject dataPositionJson;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EMServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	private JSONObject createJsonObjectFromFile(String file,
			JSONObject standardJsonObject) {
		System.out.println("create Json form:" + file);
		JSONObject jsonObject = null;
		// Load the file
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		PrintWriter writer = null;
		try {
			File f = new File(file);
			if (!f.exists()) {
				// creates new standard File //TODO make it better!!!
				System.out.println("File not exists: " + file);
				jsonObject = standardJsonObject != null ? standardJsonObject
						: dataPositionJson;
				// TODO test it!
				writer = new PrintWriter(new FileWriter(f));
				jsonObject.write(writer);

				if (writer != null) {
					writer.close();
				}
			} else {
				fileReader = new FileReader(f);
				bufferedReader = new BufferedReader(fileReader);

				// create JSONObject form bufferedReader
				StringBuffer jb = new StringBuffer();
				String line = null;
				try {
					while ((line = bufferedReader.readLine()) != null)
						jb.append(line);
				} catch (Exception e) { /* report an error */
				}

				try {
					jsonObject = new JSONObject(jb.toString());
				} catch (JSONException e) {
					// crash and burn
					throw new JSONException("Error parsing JSON request string");
				}
			}

		} catch (Exception ex) {
			if (writer != null) {
				writer.close();
			}
		}
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Request: " + request.getReader().readLine());
		/*
		 * try { JSONObject jsonObject = createJsonFromRequest(request);
		 * System.out.println("JsonObject created: " + jsonObject.toString());
		 * if (jsonObject.getString("command") == "loadDataName") {
		 * sendDataNameInfo(response, jsonObject);
		 * 
		 * return; } } catch (JSONException e) { System.err.println(e); }
		 */

		// response.getWriter().println("positionFolder");

		File folder = new File(positionFolderString);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				// response.getWriter().println("File: " + file.getName());
				response.getWriter().print("File: " + file.getName());
				/*
				 * BufferedReader bReader = new BufferedReader(new
				 * FileReader(file)); while (true){ String line =
				 * bReader.readLine(); if (line == null) break;
				 * response.getWriter().println(line); }
				 */
			} else if (file.isDirectory()) {
				// response.getWriter().println("Dire: " + file.getName());
			}
		}
	}

	private final String dataExtetsion = ".txt";

	private void sendDataNameInfo(HttpServletResponse response,
			JSONObject jsonObject) throws IOException {
		// load dataPositionJson for the position
		String dataPositionFile = positionFolderString +"/"
				+ jsonObject.getString("dataName") + dataExtetsion;
		dataPositionJson = createJsonObjectFromFile(dataPositionFile, null);
		System.out.println("loaded: " + dataPositionJson);
		JSONArray nameJsonArray = dataPositionJson.getJSONArray("nameArray");

		/*
		 * int length = nameJsonArray.length(); String[] nameStringArray = new
		 * String[length]; for (int i = 0; i < length; i++) { nameStringArray[i]
		 * = nameJsonArray.getString(i); }
		 */

		response.setContentType("application/json");
		response.getWriter().println(nameJsonArray.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObject = createJsonFromRequest(request);
		System.out.println("JsonObject created: " + jsonObject.toString());

		String command = null;
		try {
			command = jsonObject.getString("command");
		} catch (JSONException e) {
			throw new JSONException("no command found: " + e);
		}

		switch (command) {
		case "updateConfigs":
			updateConfigs(request, response, jsonObject);
			break;
		case "uploadData":
			uploadData(request, response, jsonObject);
			break;
		case "loadDataName":
			sendDataNameInfo(response, jsonObject);
			break;
		default:
			response.getWriter().println("Unknown command: " + command);
			break;
		}
	}

	private void uploadData(HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject)
			throws JSONException, IOException {
		// load dataPositionJson for the position
		String dataPositionFile = positionFolderString + "/"
				+ jsonObject.getString("dataName")+dataExtetsion;
		dataPositionJson = createJsonObjectFromFile(dataPositionFile, null);

		// year is stored as a static member
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		System.out.println("using Date: year: " +year+"; month: " +month);
		String direction = dataFolderString + "/"
				+ jsonObject.getString("dataName") + ".xls";
		ExcelFileManager excelFileManager = new ExcelFileManager("HSSF",
				direction, year + "");

		JSONArray nameArray = jsonObject.getJSONArray("nameArray");
		System.out.println("nameArray: "+jsonObject.getJSONArray("nameArray"));
		JSONArray dataArray = jsonObject.getJSONArray("dataArray");
		System.out.println("dataArray: "+jsonObject.getJSONArray("dataArray"));
		JSONArray posArray = dataPositionJson.getJSONArray("posByteArray");
		System.out.println("posArray: "+posArray.toString());

		DataManager dataManager = new DataManager(nameArray, dataArray,
				posArray, month);
		for (Data data : dataManager.getDataArray()) {
			excelFileManager.setCellString(data.row, data.column, data.data);
		}

		excelFileManager.saveWorkbook(null);
		response.getWriter().println("Data saved at: " + direction);

		// response.getWriter().println(jsonObject.toString());
	}

	private void updateConfigs(HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject)
			throws IOException {
		System.out.println("updateConfigs of " + jsonObject.toString());
		String file = positionFolderString + "/"
				+ jsonObject.getString("dataName")+dataExtetsion;
		jsonObject.remove("command");

		// JSONArray nameArray = jsonObject.getJSONArray("nameArray");
		// JSONArray posByteArray = jsonObject.getJSONArray("posByteArray");

		saveJsonObjectInFile(file, jsonObject);
		response.getWriter().println("Configs saved at: " + file);
	}

	private void saveJsonObjectInFile(String file, JSONObject jsonObject) {
		PrintWriter writer = null;
		File f = new File(file);
		if (f.exists()) {
			System.err.println("File already exists");
		}
		try {
			writer = new PrintWriter(new FileWriter(f));
			System.out.println("Write to File: " + jsonObject.toString());
			jsonObject.write(writer);

			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static JSONObject createJsonFromRequest(ServletRequest request) {
		// TODO Not by ME
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /* report an error */
		}

		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			return jsonObject;
		} catch (JSONException e) {
			// crash and burn
			throw new JSONException("Error parsing JSON request string");
		}
		// Work with the data using methods like...
		// int someInt = jsonObject.getInt("intParamName");
		// String someString = jsonObject.getString("stringParamName");
		// JSONObject nestedObj = jsonObject.getJSONObject("nestedObjName");
		// JSONArray arr = jsonObject.getJSONArray("arrayParamName");
		// etc...
	}
}
