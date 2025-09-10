package com.pim.tests.Middleware;

import static com.pim.reports.FrameworkLogger.log;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.DataBaseColumnName;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.queries.WebDescriptionquery;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;
import com.pim.utils.TextFileUtils;

public class MiddlewareWebDescriptionTest extends MiddlewareBaseClassTest{
	
	//MDLW_072
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Web_Description)
	@Test(description = "MDLW_072 | Verify whether description updated for web description gets updated in respective databases", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","USs"})
	public void verify_full_description_in_DB(Map<String, String> map) {
		List<Map<String,String>> full_display_description = null;
		
		//verifying full display description in extract DB
		full_display_description = DatabaseUtilitiies.getMultipleQueryResult(WebDescriptionquery.getFullDisplayDescriptionByItemCode(map.get("ItemNumber")), Javautils.changeunderscoretospace(DataBaseColumnName.Full_Display_Description.name()));
		
		for(Map<String, String> description : full_display_description) {
			Assertions.assertThat(description).containsKey("Full Display Description");
			Assertions.assertThat(description).containsValue(TextFileUtils.readTextFile());
		}
	}
		

}
