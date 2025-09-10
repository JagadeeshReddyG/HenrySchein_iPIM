package com.pim.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.pim.constants.Constants;
import com.pim.exceptions.FrameworkException;
import com.pim.exceptions.InvalidPathForExcelException;
import com.pim.exceptions.NullPointerForExcelException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

	Row row = null;
	File file;
	FileInputStream fs = null;
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	String excelPath;

	// for checking *login credentials* we are reading this excel utils...
	// returntype is map< <map> >

	private static Map<String, Map<String, String>> LoginUserIDData = new HashMap<>();

	public static Map<String, Map<String, String>> getLoginData() {
		return LoginUserIDData;
	}

	static {

		File file = new File(Constants.getExcelPath());

		try {
			FileInputStream inputStream = new FileInputStream(file);
			Workbook Workbook = new XSSFWorkbook(inputStream);
			Sheet Sheet = Workbook.getSheet(Constants.getLoginDataSheet());

			int rowCount = Sheet.getLastRowNum() - Sheet.getFirstRowNum();

			int coloumnCount = Sheet.getRow(0).getLastCellNum();
			Row row = Sheet.getRow(0);
			;

			List<String> rowheaders = new ArrayList<>();
			for (int i = 1; i <= rowCount; i++) {
				rowheaders.add(Sheet.getRow(i).getCell(0).getStringCellValue());
			}

			for (int i = 1; i <= rowCount; i++) {

				Map<String, String> singleRowData = new HashMap<String, String>();
				Row row1 = Sheet.getRow(0);
				Row row2 = Sheet.getRow(i);
				for (int j = 1; j < coloumnCount; j++) {
					singleRowData.put(row1.getCell(j).getStringCellValue(), row2.getCell(j).getStringCellValue());

				}
				LoginUserIDData.put(rowheaders.get(i - 1), singleRowData);

			}

		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		}

	}

	// Author: Shubham
	public static void writeAssetDataToNewExcel(String outputFilePath, List<String> assetNames,
			List<String> documentIds) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("AssetsData");

		// Create header
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Asset Name");
		header.createCell(1).setCellValue("Document ID");

		// Write data
		for (int i = 0; i < assetNames.size(); i++) {
			Row row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(assetNames.get(i));
			row.createCell(1).setCellValue(documentIds.size() > i ? documentIds.get(i) : "");
		}

		// Write to file
		FileOutputStream fos = new FileOutputStream(outputFilePath);
		workbook.write(fos);
		fos.close();
		workbook.close();
	}

	// Author: Shubham
	public static String generateOutputFilePath() {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String folderPath = System.getProperty("user.dir") + "/src/test/resources/excel/output";
		new File(folderPath).mkdirs(); // Create folder if it doesn't exist
		return folderPath + "/AssetData_" + timestamp + ".xlsx";
	}
	
	// Author: Shubham - Read from Excel
	public static Map<String, String> readAssetDataFromExcel(String filePath) throws IOException {
	    Map<String, String> assetDataMap = new LinkedHashMap<>();
	    FileInputStream fis = new FileInputStream(new File(filePath));
	    Workbook workbook = new XSSFWorkbook(fis);
	    Sheet sheet = workbook.getSheet("AssetsData");

	    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	        Row row = sheet.getRow(i);
	        if (row != null) {
	            String assetName = row.getCell(0).getStringCellValue();
	            String docId = row.getCell(1).getStringCellValue();
	            assetDataMap.put(docId, assetName);
	        }
	    }

	    workbook.close();
	    fis.close();
	    return assetDataMap;
	}

	public static Object[][] getExcelMultipleData(String filePath, String sheetName)
			throws IOException, EncryptedDocumentException, InvalidFormatException {
		FileInputStream fis = new FileInputStream(filePath);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet(sheetName);

		int rowCount = sheet.getPhysicalNumberOfRows();
		int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

		Object[][] data = new Object[rowCount - 1][colCount];

		for (int i = 1; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			for (int j = 0; j < colCount; j++) {
				Cell cell = row.getCell(j);
				data[i - 1][j] = cell.getStringCellValue();
			}
		}

		workbook.close();
		fis.close();
		return data;
	}

	// for checking Ruledriven we are reading this excel utils... returntype is list
	// <map>
	public List<Map<String, String>> getTestDetails(String sheetname) {
		List<Map<String, String>> list = null;
		try (FileInputStream fs = new FileInputStream(Constants.getExcelPath())) {

			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet(sheetname);

			int lastrownum = sheet.getLastRowNum();
			int lastcolnum = sheet.getRow(0).getLastCellNum();

			Map<String, String> map = null;
			list = new ArrayList<>();

			for (int i = 1; i <= lastrownum; i++) {
				map = new HashMap<>();
				for (int j = 0; j < lastcolnum; j++) {
					// String key = sheet.getRow(0).getCell(j).getStringCellValue();
					// try {
					String key = sheet.getRow(0).getCell(j).getStringCellValue();
					String value = sheet.getRow(i).getCell(j).getStringCellValue();
					map.put(key, value);
					// }catch(NullPointerException e) {
					// log(LogType.SKIP,"Null pointer handled");
					// }
					// map.put(key, value);
				}
				list.add(map);
			}

		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		} catch (NullPointerException e) {
			throw new NullPointerForExcelException("Excel cell you trying to read is empty");
		}

		return list;
	}

	public List<Map<String, String>> getDamTestDetails(String sheetname) {
		List<Map<String, String>> list = null;
		try (FileInputStream fs = new FileInputStream(Constants.getDamExcelPath())) {

			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet(sheetname);

			int lastrownum = sheet.getLastRowNum();
			int lastcolnum = sheet.getRow(0).getLastCellNum();

			Map<String, String> map = null;
			list = new ArrayList<>();

			for (int i = 1; i <= lastrownum; i++) {
				map = new HashMap<>();
				for (int j = 0; j < lastcolnum; j++) {

					FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
					CellReference cellReference = new CellReference(i, j);
					Row row = sheet.getRow(cellReference.getRow());
					Cell cell = row.getCell(cellReference.getCol());

					String key = sheet.getRow(0).getCell(j).getStringCellValue();
					String value = null;

					if (cell != null) {
						switch (evaluator.evaluateFormulaCell(cell)) {
						case Cell.CELL_TYPE_BOOLEAN:
							System.out.println(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							System.out.println(cell.getNumericCellValue());
							break;
						case Cell.CELL_TYPE_STRING:
							value = sheet.getRow(i).getCell(j).getStringCellValue();
							break;
						case Cell.CELL_TYPE_BLANK:
							break;
						case Cell.CELL_TYPE_ERROR:
							System.out.println(cell.getErrorCellValue());
							break;
						// CELL_TYPE_FORMULA will never occur
						case Cell.CELL_TYPE_FORMULA:
							break;
						default:
							value = sheet.getRow(i).getCell(j).getStringCellValue();
						}
					}
					map.put(key, value);
				}
				list.add(map);
			}

		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		} catch (NullPointerException e) {
			throw new NullPointerForExcelException("Excel cell you trying to read is empty");
		}

		return list;
	}

	public ExcelUtils getSheet(String excelPath, String sheetname) {
		try {
			this.excelPath = excelPath;
			fs = new FileInputStream(excelPath);

			workbook = new XSSFWorkbook(fs);
			sheet = workbook.getSheet(sheetname);

		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		}
		return this;

	}

	public int getLastRowCount() {

		int lastrownum = sheet.getLastRowNum();

		return lastrownum;

	}

	public ExcelUtils createRow(int index) {

		row = sheet.createRow(index);
		return this;

	}

	public ExcelUtils setRow(int index) {

		row = sheet.getRow(index);
		return this;

	}

	public String getCellValue(int rowIndex, int coloumnIndex) {

		String cellData = "";
		if (sheet.getRow(rowIndex).getCell(coloumnIndex).getCellTypeEnum().equals(CellType.NUMERIC)) {

			cellData = Double.toString(sheet.getRow(rowIndex).getCell(coloumnIndex).getNumericCellValue());
		} else if (sheet.getRow(rowIndex).getCell(coloumnIndex).getCellTypeEnum().equals(CellType.STRING)) {
			cellData = sheet.getRow(rowIndex).getCell(coloumnIndex).getStringCellValue();
		}

		return cellData;

	}

	public ExcelUtils excelDataSheetToWrite(String excelPath, String sheetname) {

		try {
			this.excelPath = excelPath;
			file = new File(excelPath);
			fs = new FileInputStream(file);

			workbook = new XSSFWorkbook(fs);
			sheet = workbook.getSheet(sheetname);

			int lastrownum = sheet.getLastRowNum();

			row = sheet.createRow(lastrownum + 1);

		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		}
		return this;
	}

	public ExcelUtils setData(int index, String value) {

		row.createCell(index).setCellValue(value);
		return this;

	}

	public List<String> getColumnValues(int index) {

		List<String> list = new ArrayList<>();
		int count = sheet.getLastRowNum();
		for (int i = 1; i < count; i++) {
			list.add(getCellValue(i, index));

		}
		return list;

	}

	public Map<String, String> getSpecifiedRowDataRowHeader(String cellValue) {
		int lastrownum = sheet.getLastRowNum();
		int lastcolnum = sheet.getRow(0).getLastCellNum();
		int rowNumber = 0;
		for (int i = 0; i < lastrownum; i++) {
			if (getCellValue(i, 0).equals(cellValue)) {
				rowNumber = i;
				break;
			}

		}

		Map<String, String> map = new HashMap<>();
		for (int j = 0; j < lastcolnum; j++) {
			String key = getCellValue(0, j);
			String value = getCellValue(rowNumber, j);
			map.put(key, value);
		}

		return map;

	}

	public void closeWriting() {

		sheet.autoSizeColumn(1);
		try {
			workbook.write(new FileOutputStream(file));
			workbook.close();
		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		}

	}

	public ExcelUtils writeItemCodeInExcel(String columnName, String itemcode) {
		String path = Constants.getDamExcelPath();
		// sheet.getRow(rowIndex).getCell(coloumnIndex)
		FileInputStream fs;
		try {
			fs = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet("CAItemCode");
			int lastrownum = sheet.getLastRowNum();
			int lastcolnum = sheet.getRow(0).getLastCellNum();
			int colNumber = 0;
			for (int i = 0; i < lastcolnum; i++) {
				if (sheet.getRow(0).getCell(i).getStringCellValue().equals(columnName)) {
					colNumber = i;
					System.out.println(i);
					break;
				}
			}
			Row row = sheet.getRow(1);
			Cell cell = row.createCell(colNumber, CellType.STRING);
			cell.setCellValue(itemcode);
			workbook.setForceFormulaRecalculation(true);
			FileOutputStream fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		} catch (NullPointerException e) {
			throw new NullPointerForExcelException("Excel cell you trying to read is empty");
		}
		return this;
	}

	public ExcelUtils writeMSDSFileNameinUSExcel(String columnName, String msdsfileName) {
		String path = Constants.getDamExcelPath();
		// sheet.getRow(rowIndex).getCell(coloumnIndex)
		FileInputStream fs;
		try {
			fs = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet("MSDSFileName");
			int lastrownum = sheet.getLastRowNum();
			int lastcolnum = sheet.getRow(0).getLastCellNum();
			int colNumber = 0;
			for (int i = 0; i < lastcolnum; i++) {
				if (sheet.getRow(0).getCell(i).getStringCellValue().equals(columnName)) {
					colNumber = i;
					System.out.println(i);
					break;
				}
			}
			Row row = sheet.getRow(1);
			Cell cell = row.createCell(colNumber, CellType.STRING);
			cell.setCellValue(msdsfileName);
			workbook.setForceFormulaRecalculation(true);
			FileOutputStream fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		} catch (NullPointerException e) {
			throw new NullPointerForExcelException("Excel cell you trying to read is empty");
		}
		return this;
	}

	public void writeItemCode() {
		writeItemCodeInExcel("SingleBadImage", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("MultipleImages", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("SingleGoodImage", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("Task", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("FamilySet", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("ChildFamilyset", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("MultipleImagesShotTypeFrom", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("SDSFile", Javautils.getItemNumberforCAFromDB());
		writeItemCodeInExcel("ExistingSDS", Javautils.getItemNumberforCAFromDB());
	}

	// for pim
	public ExcelUtils writeItemCodeInExcelForPIM(String columnName, String itemcode) {
		String path = Constants.getExcelPath();
		// sheet.getRow(rowIndex).getCell(coloumnIndex)
		FileInputStream fs;
		try {
			fs = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fs);
			XSSFSheet sheet = workbook.getSheet("CAItemCode");
			int lastrownum = sheet.getLastRowNum();
			int lastcolnum = sheet.getRow(0).getLastCellNum();
			int colNumber = 0;
			for (int i = 0; i < lastcolnum; i++) {
				if (sheet.getRow(0).getCell(i).getStringCellValue().equals(columnName)) {
					colNumber = i;
					System.out.println(i);
					break;
				}
			}
			Row row = sheet.getRow(1);
			Cell cell = row.createCell(colNumber, CellType.STRING);
			cell.setCellValue(itemcode);
			workbook.setForceFormulaRecalculation(true);
			FileOutputStream fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			throw new InvalidPathForExcelException("Excel File you trying to read is not found");
		} catch (IOException e) {
			throw new FrameworkException("Some io exception happened  while reading excel file");
		} catch (NullPointerException e) {
			throw new NullPointerForExcelException("Excel cell you trying to read is empty");
		}
		return this;
	}

	public void writeItemCodeForPIM() {
		writeItemCodeInExcelForPIM("PIXTOPIM", Javautils.getItemNumberforCAFromDB());
	}

}
