package com.pim.utils;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.constants.Constants;
import com.pim.enums.LogType;
import com.pim.enums.TestCaseSheet;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.pim.reports.FrameworkLogger.log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataProviderUtils {

	public List<Map<String, String>> list = new ArrayList<>();
	ExcelUtils excelUtils = new ExcelUtils();

	@DataProvider(parallel = false)
	public Object[] getCatalogData(Method method) {

		String testname = method.getName();

		if (list.isEmpty()) {
			list = excelUtils.getTestDetails(method.getAnnotation(TestDataSheet.class).sheetname().name());
		}

		List<Map<String, String>> filteredData = new ArrayList<>();

		for (Map<String, String> map : list) {
			String testNameExcel = map.get("TestCaseName");
			if (testname.equalsIgnoreCase(testNameExcel)) {
				filteredData.add(map);
			}

		}

		Object[] array = new Object[filteredData.size()];

		for (int i = 0; i < filteredData.size(); i++) {
			//log(LogType.EXTENTANDCONSOLE, testname + " Datasheet Value :"+ filteredData.get(i));
			//System.out.println(filteredData.get(i));

			array[i] = filteredData.get(i);

		}
		return array;

	}

	//for getting DAM test data
	@DataProvider(parallel = false)
	public Object[] getDamData(Method method) {

		String testname = method.getName();

		if (list.isEmpty()) {
			list = excelUtils.getDamTestDetails(method.getAnnotation(TestDataSheet.class).sheetname().name());
		}

		List<Map<String, String>> filteredData = new ArrayList<>();

		for (Map<String, String> map : list) {
			String testNameExcel = map.get("TestCaseName");
			if (testname.equalsIgnoreCase(testNameExcel)) {
				filteredData.add(map);
			}

		}

		Object[] array = new Object[filteredData.size()];

		for (int i = 0; i < filteredData.size(); i++) {
			//log(LogType.EXTENTANDCONSOLE, testname + " Datasheet Value :"+ filteredData.get(i));
			//System.out.println(filteredData.get(i));

			array[i] = filteredData.get(i);

		}
		return array;

	}

}
