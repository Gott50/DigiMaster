package de.tmtomcat.em;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Loads and saves an Excel File to store Data in it
 * 
 * @author timomorawitz
 *
 */
public class ExcelFileManager {
	private Workbook workbook;
	private Sheet worksheet;
	private String type;
	private String direction; // Direction with name and suffix

	public ExcelFileManager(String type, String direction, String sheetName) {
		this.type = type != null ? type : generateTypeFromSuffix(direction);
		System.out.println("type: " + type);
		this.direction = direction;
		System.out.println("direction: " + direction);
		try {
			workbook = loadWorkbook(type, direction, sheetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		worksheet = getWorkSheet(workbook, sheetName);
	}

	public Workbook loadWorkbook(String type, String direction, String sheetName)
			throws IOException {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(direction);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fileInputStream = null;
		}
		//TODO if workbook is not there, copy the last one and clean data
		Workbook workbook = createWorkbook(type, fileInputStream);
		return workbook;
	}

	public Sheet getWorkSheet(Workbook workbook, String sheetName) {
		Sheet worksheet = workbook.getSheet(WorkbookUtil
				.createSafeSheetName(sheetName));
		if (worksheet == null)
			// this utility replaces invalid characters with a space (' ')
			worksheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName(sheetName));
		return worksheet;
	}

	/**
	 * Saves the Workbook at a specific location (or the current)
	 *
	 * @param direction
	 *            the system-dependent direction(includes filename) if it is
	 *            null, the Workbook is saved in the current location
	 */
	public void saveWorkbook(String direction) throws IOException {
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(
				direction != null ? direction + "." + getFileSuffix(type)
						: this.direction);
		workbook.write(fileOut);
		System.out.println("saveWorkbook at: " + fileOut.toString());
		fileOut.close();
	}

	private static String generateTypeFromSuffix(String fileName) {
		// TODO make it smarter
		String type = null;
		if (fileName.contains("xls"))
			type = "HSSF";
		else if (fileName.contains("xlsx"))
			type = "XSSF";

		System.out.println("Type generated form Suffix: " + type);
		return type;
	}

	private static Workbook createWorkbook(String type,
			FileInputStream fileInputStream) throws IOException {
		// TODO Not completely by ME
		if (fileInputStream != null) {
			if ("HSSF".equals(type))
				return new HSSFWorkbook(fileInputStream);
			else if ("XSSF".equals(type))
				return new XSSFWorkbook(fileInputStream);
			else if ("SXSSF".equals(type))
				return new SXSSFWorkbook(new XSSFWorkbook(fileInputStream));
			else
				usage("Unknown type \"" + type + "\"");
		} else {
			if ("HSSF".equals(type))
				return new HSSFWorkbook();
			else if ("XSSF".equals(type))
				return new XSSFWorkbook();
			else if ("SXSSF".equals(type))
				return new SXSSFWorkbook();
			else
				usage("Unknown type \"" + type + "\"");
		}
		return null;
	}

	/**
	 * Creates a cell and aligns it a certain way.
	 *
	 * @param wb
	 *            the workbook
	 * @param row
	 *            the row to create the cell in
	 * @param column
	 *            the column number to create the cell in
	 * @param halign
	 *            the horizontal alignment for the cell.
	 */
	public static void createCell(Workbook wb, Row row, short column,
			short halign, short valign, String content) {
		// TODO Not by ME
		CreationHelper ch = wb.getCreationHelper();
		Cell cell = row.createCell(column);
		cell.setCellValue(ch.createRichTextString(content));
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		cell.setCellStyle(cellStyle);
	}

	public void setCellString(short column, short rownum, String content) {
		Row row = this.worksheet.getRow(rownum);
		if (row == null)
			row = this.worksheet.createRow(rownum);
		createCell(this.workbook, row, column, CellStyle.ALIGN_RIGHT,
				CellStyle.VERTICAL_CENTER, content);
	}

	public String getCellString(short column, short rownum) {
		String str = worksheet.getRow(rownum).getCell((short) column)
				.getStringCellValue();
		return str;
	}

	private static void usage(String message) {
		// TODO Not by ME
		System.err.println(message);
		System.err
				.println("usage: java SSPerformanceTest HSSF|XSSF|SXSSF rows cols saveFile (0|1)? ");
		System.exit(1);
	}

	private static String getFileSuffix(String type) {
		// TODO Not by ME
		if ("HSSF".equals(type))
			return "xls";
		else if ("XSSF".equals(type))
			return "xlsx";
		else if ("SXSSF".equals(type))
			return "xlsx";
		return null;
	}
}
