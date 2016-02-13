package de.tmgdx.em.gui.screeens;

import com.badlogic.gdx.utils.Array;

public class HttpContentObject {
	private String command;
	private String dataName;
	private DataObject content;

	/**
	 * @param content
	 *            [1] = Array<String> nameArray
	 * @param content
	 *            [2] = Array<String> dataArray
	 */
	public HttpContentObject(Command command, String dataName,
			Array<String>... content) {
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
			if (content.length >= 2)
				return new DataObject(content);
			else
				return null;

		}
	}

	private static class DataObject {
		private Array<String> nameArray;
		private Array<String> dataArray;
		private Array<String>[] allContent;

		public DataObject(Array<String> nameArray, Array<String> dataArray) {
			this.setNameArray(nameArray);
			this.setDataArray(dataArray);
		}

		public DataObject(Array<String>[] content) {
			this((Array<String>) content[0], (Array<String>) content[1]);
			if (content.length > 2)
				allContent = content;
		}

		private void setNameArray(Array<String> nameArray) {
			this.nameArray = nameArray;
		}

		private void setDataArray(Array<String> dataArray) {
			this.dataArray = dataArray;
		}
	}
}
