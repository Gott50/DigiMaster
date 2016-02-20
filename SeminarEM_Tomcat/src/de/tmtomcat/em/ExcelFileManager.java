package de.tmtomcat.em;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Woodstox;

/**
 * Loads and saves an Excel File to store Data in it
 * 
 */
public class ExcelFileManager {
	private Sheet worksheet;
	private String file;

	public ExcelFileManager(String file, String sheetName) {
		System.out.println("type: " + generateTypeFromSuffix(file));
		this.file = file;
		System.out.println("direction: " + file);
		try {
			worksheet = getWorkSheet(loadWorkbook(file), sheetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Workbook loadWorkbook(String direction) throws IOException {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(direction);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fileInputStream = null;
		}
		// TODO if workbook is not there, copy the last one and clean data
		return createWorkbook(generateTypeFromSuffix(direction),
				fileInputStream);
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
				direction != null ? direction + "."
						+ getFileSuffix(generateTypeFromSuffix(direction))
						: this.file);
		worksheet.getWorkbook().write(fileOut);
		System.out.println("saveWorkbook at: " + fileOut.toString());
		fileOut.close();
	}

	private static String generateTypeFromSuffix(String fileName) {
		if (fileName.contains("xls"))
			return "HSSF";
		else if (fileName.contains("xlsx"))
			return "XSSF";
		else
			return null;
	}

	private static Workbook createWorkbook(String type,
			FileInputStream fileInputStream) throws IOException {
		if (fileInputStream != null)
			return loadWorkbook(type, fileInputStream);
		else
			return generateNewWorkbook(type);
	}

	private static Workbook loadWorkbook(String type,
			FileInputStream fileInputStream) throws IOException {
		switch (type) {
		case "HSSF":
			return new HSSFWorkbook(fileInputStream);
		case "XSSF":
			return new XSSFWorkbook(fileInputStream);
		case "SXSSF":
			return new SXSSFWorkbook(new XSSFWorkbook(fileInputStream));
		default:
			usage("Unknown type \"" + type + "\"");
			break;
		}
		return null;
	}

	private static Workbook generateNewWorkbook(String type) {
		switch (type) {
		case "HSSF":
			return new HSSFWorkbook();
		case "XSSF":
			return new XSSFWorkbook();
		case "SXSSF":
			return new SXSSFWorkbook();
		default:
			usage("Unknown type \"" + type + "\"");
			break;
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
		Cell cell = row.createCell(column);
		cell.setCellValue(wb.getCreationHelper().createRichTextString(content));
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		cell.setCellStyle(cellStyle);
	}

	public void setCellString(short column, short rownum, String content) {
		Row row = this.worksheet.getRow(rownum);
		if (row == null)
			row = this.worksheet.createRow(rownum);
		createCell(worksheet.getWorkbook(), row, column, CellStyle.ALIGN_RIGHT,
				CellStyle.VERTICAL_CENTER, content);
		refreshCells(generateTypeFromSuffix(file), worksheet.getWorkbook());
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
		switch (type) {
		case "HSSF":
			return "xls";
		case "XSSF":
		case "SXSSF":
			return "xlsx";
		default:
			return null;
		}
	}
	
	private void refreshCells(String type,Workbook workbook){
		switch (type) {
		case "HSSF":
			HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
			break;
		case "SXSSF":
		case "XSSF":
			 XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook) workbook);
			 break;
		default:
			System.err.println("refreshCells faild, Thus you crash and burn");
		}
		
	}
}
