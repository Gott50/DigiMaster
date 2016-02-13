package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.utils.Array;

public class HttpContentObject {
	private String command;
	private String dataName;
	private DataObject content;

	/**
	 * @param content
	 *            [0] = String dataName
	 * @param content
	 *            [1] = Array<String> nameArray
	 * @param content
	 *            [2] = Array<String> dataArray
	 * 
	 */
	public HttpContentObject(Command command,String dataName, Array<String>... content) {
		this.command = command.getCommand();
		this.dataName = dataName;
		this.content = command.getContent(content);
	}

	public enum Command {
		CONFIG("updateConfigs"), DATA("uploadData"), LOAD("loadDataName");
		private String command;

		private Command(String command) {
			this.command = command;
		}

		public String getCommand() {
			return command;
		}

		public DataObject getContent(Array<String>[] content) {
			switch (this) {
			case CONFIG:
				return new DataObject(content) /*{
					private void setDataArray(Array<String> dataArray) {
						Array new_dataArray = new Array<Byte>();
						for (String string : dataArray) {
							new_dataArray.add(Byte.parseByte(string));
						}
						super.setDataArray(new_dataArray);
					}
				}*/;
			case DATA:
			case LOAD:
				 if (content.length == 2)
				return new DataObject(content);
			default:
				return null;
			}
		}
	}

	private static class DataObject {
		private Array<String> nameArray;
		private Array<String> dataArray;
		private Array<String>[] allContent;

		public DataObject(Array<String> nameArray,
				Array<String> dataArray) {
			this.setNameArray(nameArray);
			this.setDataArray(dataArray);
		}

		public DataObject(Array<String>[] content) {
			this( (Array<String>) content[0],
					(Array<String>) content[1]);
			allContent = content;
		}

		private void setNameArray(Array<String> nameArray) {
			this.nameArray = nameArray;
		}

		private void setDataArray(Array<String> dataArray) {
			this.dataArray = dataArray;
		}
	}

	/*
	 * private static class ConfigObject { private String dataName; private
	 * Array<String> nameArray; private byte[] posByteArray;
	 * 
	 * 
	 * public ConfigObject(Object[] content) { this((String) content[0],
	 * (Array<String>) content[1], (Array<String>) content[2]); }
	 * 
	 * public ConfigObject(String dataName, Array<String> nameArray,
	 * Array<String> posByteArray) { this.dataName = dataName; this.nameArray =
	 * nameArray; this.posByteArray = new byte[posByteArray.size];
	 * 
	 * int index = 0; for (String string : posByteArray) {
	 * this.posByteArray[index] = Byte.parseByte(string); } } }
	 */

}
