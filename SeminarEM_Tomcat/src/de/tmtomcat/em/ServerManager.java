package de.tmtomcat.em;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.tmtomcat.em.DataManager.Data;

public class ServerManager {
	public FolderManager folder;
	private static final String EXC_DATA_TYPE = "HSSF";
	private static final String EXC_DATA_EXT = ".xls";
	private static final long serialVersionUID = 1L;
	private static final JSONObject standardJsonObject = null;
	private static final String POSDATA_EXT = ".txt";

	public ServerManager(String folderPath) {
		folder = new FolderManager(folderPath);
	}
	
	class FolderManager {
		private String userDataFolder;
		String positionFolder;
		String dataFolder;

		public FolderManager(String userDataPath) {
			init(userDataPath);
		}

		private void init(String userDataPath) {
			userDataFolder = userDataPath;
			System.out.println("cretated new UserDataFoler? "
					+ new File(userDataFolder).mkdir());
			positionFolder = userDataFolder + "/Positions";
			System.out.println("cretated new PositionFolder? "
					+ new File(positionFolder).mkdir());
			dataFolder = userDataFolder + "/Data";
			System.out.println("cretated new DataFolderString? "
					+ new File(dataFolder).mkdir());
		}

		public String[] getAll() {
			return new String[] { userDataFolder, positionFolder, dataFolder, };
		}
	}

	void doGet(HttpServletResponse response) throws IOException {
		//		System.out.println("Request: " + request.getReader().readLine());
		for (File file : new File(folder.positionFolder).listFiles()) {
			if (file.isFile()) {
				setResponse(response, "File: " + file.getName());
			} else if (file.isDirectory()) {
				// response.getWriter().println("Dir: " + file.getName());
			}
		}
	}

	void setResponse(HttpServletResponse response, String string)
			throws IOException {
		response.getWriter().print(string);
	}

	void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject jsonObject = createJsonFromRequest(request);
	//		System.out.println("JsonObject created: " + jsonObject.toString());
	
		try {
			switch (jsonObject.getString("command")) {
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
				response.getWriter().println(
						"Unknown command: " + jsonObject.getString("command"));
				break;
			}
		} catch (JSONException e) {
			throw new JSONException("no command found: " + e);
		}
	}

	private JSONObject createJsonObjectFromFile(String filePath) {
	//		System.out.println("create Json form:" + filePath);
			JSONObject jsonObject = null;
			BufferedReader bufferedReader = null;
			try {
				File f = new File(filePath);
				if (!f.exists()) {
	//				System.out.println("File not exists: " + filePath);
					writeJSONtoFile(filePath, standardJsonObject);
					jsonObject = standardJsonObject;
				} else {
					bufferedReader = new BufferedReader(new FileReader(f));
					jsonObject = createJson(bufferedReader);
				}
			} catch (Exception e) {
	//			System.out.println(e.getMessage());
			}
	
			return jsonObject;
		}

	private JSONObject createJson(BufferedReader bufferedReader) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		JSONObject jsonObject;
		try {
			while ((line = bufferedReader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			jsonObject = new JSONObject(jb.toString());
		} catch (JSONException e) {
			throw new JSONException("Error parsing JSON request string");
		}
		if (bufferedReader != null)
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return jsonObject;
	}

	/**
		 * load dataPositionJson for the position and send it to client
		 */
		void sendDataNameInfo(HttpServletResponse response,
				JSONObject jsonObject) throws IOException {
	//		System.out.println("loaded: "
	//				+ createJsonObjectFromFile(folder.positionFolder + "/"
	//						+ jsonObject.getString("dataName") + POSDATA_EXT));
			JSONArray nameJsonArray = createJsonObjectFromFile(
					folder.positionFolder + "/" + jsonObject.getString("dataName")
							+ POSDATA_EXT).getJSONArray("nameArray");
			response.setContentType("application/json");
			response.getWriter().println(nameJsonArray.toString());
		}

	void uploadData(HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject)
			throws JSONException, IOException {
	
		ExcelFileManager excelFileManager = new ExcelFileManager(
				folder.dataFolder + "/" + jsonObject.getString("dataName")
						+ EXC_DATA_EXT, Calendar.getInstance().get(
						Calendar.YEAR)
						+ "");
	
		for (Data data : new DataManager(jsonObject.getJSONObject("content")
				.getJSONArray("nameArray"), jsonObject.getJSONObject("content")
				.getJSONArray("dataArray"), createJsonObjectFromFile(
				folder.positionFolder + "/" + jsonObject.getString("dataName")
						+ POSDATA_EXT).getJSONArray("posByteArray"), Calendar
				.getInstance().get(Calendar.MONTH)).getDataArray()) {
			excelFileManager.setCellString(data.row, data.column, data.data);
		}
		excelFileManager.saveWorkbook(null);
	}

	void updateConfigs(HttpServletRequest request,
			HttpServletResponse response, JSONObject jsonObject)
			throws IOException {
		String file = folder.positionFolder + "/"
				+ jsonObject.getString("dataName") + POSDATA_EXT;
		JSONObject content = jsonObject.getJSONObject("content");
		if (new File(file).exists()) {
			System.err.println("File already exists");
		} else
			writeJSONtoFile(file, content);
	}

	private void writeJSONtoFile(String file, JSONObject content) {
	//		System.out.println("Write to File: " + content.toString());
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new FileWriter(new File(file)));
				content.write(writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (writer != null)
				writer.close();
		}

	static JSONObject createJsonFromRequest(ServletRequest request)
			throws IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);
		try {
			JSONObject jsonObject = new JSONObject(jb.toString());
			return jsonObject;
		} catch (JSONException e) {
			throw new JSONException("Error parsing JSON request string");
		}
	}
}
