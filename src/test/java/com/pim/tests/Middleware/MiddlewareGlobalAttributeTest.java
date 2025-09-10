package com.pim.tests.Middleware;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
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
import com.pim.queries.*;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;

public class MiddlewareGlobalAttributeTest extends MiddlewareBaseClassTest {

	//checking global attribute in DB TCIS - MDLW_001
	@PimFrameworkAnnotation(module = Modules.GLOBAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GA)
	@Test(description = "MDLW_001 | Verify whether the Global data attributes displayed in the export file are updated in the respective columns in respective table in DB", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_global_attribute_field(Map<String, String> map) {
		Map<String, String> globalattributefromexporttable = null;
		List<Map<String,String>> globalattributesfromecommbuild = null;

		globalattributefromexporttable= DatabaseUtilitiies.getOneRowQueryResult(GlobalAttributequery.getAllGlobalAttributeByItemCodeQuery(map.get("ItemNumber")), Javautils.changeunderscoretospace(DataBaseColumnName.HSI_Item_Number.name()),
				Javautils.changeunderscoretospace(DataBaseColumnName.Manufacturer_Code.name()), Javautils.changeunderscoretospace(DataBaseColumnName.JDE_Description.name()), Javautils.changeunderscoretospace(DataBaseColumnName.Supplier_Code.name()), DataBaseColumnName.Classification.name());
		Assertions.assertThat(globalattributefromexporttable).containsKeys("HSI Item Number","Manufacturer Code","Supplier Code","Classification","JDE Description");
		Assertions.assertThat(globalattributefromexporttable).containsValues(map.get("ItemNumber"), map.get("Manufacturere_code"),
				map.get("JDE_Description"), map.get("Supplier_Code"), map.get("Classification"));
		log(LogType.INFO,"Verifying global attribute in export table");

		globalattributesfromecommbuild = DatabaseUtilitiies.getMultipleQueryResult(GlobalAttributequery.getGlobalAttributeFromItemMastTable(map.get("ItemNumber")),  DataBaseColumnName.DESCRIPT.name());

		Assertions.assertThat(globalattributesfromecommbuild).extracting("DESCRIPT").contains(map.get("JDE_Description"));
		log(LogType.INFO,"Verifying global attribute in ecomm db");
	}
	
	@PimFrameworkAnnotation(module = Modules.ITEM_CREATION, category = CategoryType.SMOKE)
    @TestDataSheet(sheetname = TestCaseSheet.TestDataPix)
    @Test(description = "Creating item in pix and validating it on PIM and Midleware ", dataProvider = "getCatalogData", 
    dataProviderClass = DataProviderUtils.class, groups = {"Integration-Smoke"}, priority = 3)
    public void verifycreateditemreflectinPIMAndMiddleware(Map<String, String> map) {
		Map<String, String> getJDEDescription = null;
		Map<String, String> getCatalogName = null;
		
		getJDEDescription = DatabaseUtilitiies.getOneRowQueryResult(GlobalAttributequery.getGlobalAttributesByJDEDescription(ApplicationUtils.getJDEDescription(map.get("productName"))),
				Javautils.changeunderscoretospace(DataBaseColumnName.JDE_Description.name()));
		Assertions.assertThat(getJDEDescription).containsValue(ApplicationUtils.getJDEDescription(map.get("productName")));
		log(LogType.INFO,"JDE Description is present");
		
		Map<String, String> getHSIItemNumber =  DatabaseUtilitiies.getOneRowQueryResult(GlobalAttributequery.getGlobalAttributesByJDEDescription(ApplicationUtils.getJDEDescription(map.get("productName"))),
				Javautils.changeunderscoretospace(DataBaseColumnName.HSI_Item_Number.name()));
		
		String dateandtimefordevicedb = getHSIItemNumber.get("HSI Item Number");
		
		getCatalogName = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(dateandtimefordevicedb, map.get("catalogType")),DataBaseColumnName.CatalogName.name());
		Assertions.assertThat(getCatalogName).containsValue(map.get("catalogType"));
		log(LogType.INFO,"Dental Catalog is present");
	}
	
	@PimFrameworkAnnotation(module = Modules.ITEM_CREATION, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Canada_Catalog_TestData_PIX)
	@Test(description = "Creating item in pix and validating it on PIM and Midleware ", dataProvider = "getCatalogData", 
	dataProviderClass = DataProviderUtils.class, groups = {"Integration-Smoke"}, priority = 3)
	public void verifying_created_ca_item_in_PIM_and_Middleware(Map<String, String> map) throws InterruptedException, AWTException, IOException {
		Map<String, String> getJDEDescription = null;
		
		getJDEDescription = DatabaseUtilitiies.getOneRowQueryResult(GlobalAttributequery.getGlobalAttributesByJDEDescriptionForCA(ApplicationUtils.getJDEDescription(map.get("productName"))),
				Javautils.changeunderscoretospace(DataBaseColumnName.JDE_Description.name()));
		Assertions.assertThat(getJDEDescription).containsValue(ApplicationUtils.getJDEDescription(map.get("productName")));
		log(LogType.INFO,"JDE Description is present");
	} 
}
