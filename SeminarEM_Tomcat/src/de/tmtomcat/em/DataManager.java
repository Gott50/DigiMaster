package de.tmtomcat.em;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataManager {
	private Data[] dataArray;

	/**
	 * Currently not used
	 * 
	 * @param nameJsonArray
	 * @param dataJsonArray
	 * @param posRowJsonArray
	 * @param posColJsonArray
	 */
	public DataManager(JSONArray nameJsonArray, JSONArray dataJsonArray,
			JSONArray posRowJsonArray, JSONArray posColJsonArray) {
		dataArray = new Data[nameJsonArray.length()];
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = new Data(nameJsonArray.getString(i),
					dataJsonArray.getString(i), posRowJsonArray.getString(i),
					posColJsonArray.getString(i));
		}
	}

	/**
	 * Currently used constructor.
	 * 
	 * key: name + Col/Row
	 * 
	 * @param nameJsonArray
	 *            {@link JSONArray} that saves the names
	 * @param dataJsonArray
	 *            {@link JSONArray} that saves the data to the name
	 * @param dataPositionJson
	 *            {@link JSONObject} that saves the position to the names
	 */
	public DataManager(String[] nameJsonArray, Integer[] dataJsonArray,
			JSONArray dataPositionArray, int dateInt) {
		// key: name + Col/Row
		dataArray = new Data[nameJsonArray.length];
		int dataSet=dateInt*dataArray.length*2;
		for (int i = 0; i < dataArray.length; i++) {
			dataArray[i] = new Data(nameJsonArray[i],
					dataJsonArray[i] + "",
					dataPositionArray.getInt(dataSet+i * 2)-1+"",
					dataPositionArray.getInt(dataSet+i * 2+1)-1+"");
		}
	}
	/*public DataManager(JSONArray nameJsonArray, JSONArray dataJsonArray,
			JSONArray dataPositionArray, int dateInt) {
		this(convertJSONArray(nameJsonArray, new String[]{}),  convertJSONArray(nameJsonArray,new Integer[]{}),
				 dataPositionArray,  dateInt);
	}
	public static <T> T[] convertJSONArray(JSONArray jsonArray, T[] a) {
		Set<T> out = new HashSet();
		for (Object ob : jsonArray) {
			out.add((T) ob);
		}
		System.out.println(a.getClass()+"-Set: "+out);
		return out.toArray(a);
	}//*/

	public Data[] getDataArray() {
		return dataArray;
	}

	public class Data {
		String name;
		String data;
		short row;
		short column;

		public Data(String name, String data,short column, short row) {
			this.name = name;
			this.data = data;
			this.row = row;
			this.column = column;
		}

		public Data(String name, String data, String column, String row) {
			this(name, data, Short.parseShort(row), Short.parseShort(column));
		}
	}
}
