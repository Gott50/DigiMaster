package de.tmtomcat.em;

import java.io.IOException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestClass {

	public static void main(String[] args) throws IOException {
		JSONObject jsonObject = createJsonFromRequest();

		String datum = "2015";//new Date().getYear() + "";// jsonObject.getString("datum");
		ExcelFileManager excelFileManager = new ExcelFileManager("TestClass.xls", datum); 

		JSONArray nameArray = jsonObject.getJSONArray("nameArray");
		System.out.println(nameArray.toString());
		JSONArray dataArray = jsonObject.getJSONArray("dataArray");
		System.out.println(dataArray);
		
		for (short i = 0; i < nameArray.length(); i++) {
			excelFileManager.setCellString(i, (short) 0, nameArray.get(i).toString());
			excelFileManager.setCellString(i, (short) 1, dataArray.get(i).toString());
		}

		excelFileManager.saveWorkbook(null);
	}

	private static JSONObject createJsonFromRequest() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nameArray", new JSONArray("[Datum,Fernwärme,Turnhalle,Uebertragungsstation,Hausmeister]"));
		jsonObject.put("dataArray", new JSONArray("[1,2,3,4,5]"));
		return jsonObject;
	}

}
