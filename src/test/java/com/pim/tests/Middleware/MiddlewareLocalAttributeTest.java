package com.pim.tests.Middleware;

import java.util.List;
import java.util.Map;

import com.pim.utils.AssertionUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import static com.pim.reports.FrameworkLogger.log;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.DataBaseColumnName;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.queries.LocalAttributequery;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.Javautils;

public class MiddlewareLocalAttributeTest extends MiddlewareBaseClassTest{

	//MDLW_002
	@PimFrameworkAnnotation(module = Modules.SEO, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.SEO_Tab)
	@Test(description = "MDLW_002 | Verify whether the Local data attributes displayed in the export file are updated in the respective columns in respective table", groups = {"middleware","US"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void Verify_Local_Attribute_Fields_Are_getting_Populated_SEO_item_and_ItemType(Map<String, String> map) {
		List<Map<String, String>> localattributefromextractdb = null;

		localattributefromextractdb = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")), Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));	
		Assertions.assertThat(localattributefromextractdb).extracting("Dimenstion Value").contains(map.get("LocalAttributeColourFieldValue"));
		
		List<Map<String, String>> localAttributeFromExtractDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),DataBaseColumnName.Dimension.name());
		Assertions.assertThat(localAttributeFromExtractDB2).extracting("Dimension").containsAnyOf("Brand","Size");
		
		log(LogType.INFO,"Verifying local attribute values");
	}

	//MDLW_023
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_023 | Verify whether the Local data attributes displayed in the export file are updated in the respective columns in respective table for Dental", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_localAttribute_changes_For_DentalItem_updated_In_RespectiveTable(Map<String, String> map) {
		List<Map<String, String>> localAttributeFromExtractDB = null;

		localAttributeFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));
		BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(localAttributeFromExtractDB).extracting("Dimenstion Value").contains(map.get("LocalAttributeBrandFieldValue"), map.get("LocalAttributeItemFieldValue"), map.get("LocalAttributeitemTypeFieldValue"));

		List<Map<String, String>> localAttributeFromExtractDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),DataBaseColumnName.Dimension.name());
		Assertions.assertThat(localAttributeFromExtractDB2).extracting("Dimension").containsAnyOf("Brand","Size");
		log(LogType.INFO,"Verifying local attribute values");
	}

	//MDLW_024
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_024 | Verify whether the Local data attributes displayed in the export file are updated in the respective columns in respective table for Medical", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_localAttribute_changes_For_Medical_Item_updated_In_RespectiveTable(Map<String, String> map) {
		List<Map<String, String>> localAttributeFromExtractDB = null;

		localAttributeFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));
		Assertions.assertThat(localAttributeFromExtractDB).extracting("Dimenstion Value").contains(map.get("LocalAttributeBrandFieldValue"), map.get("LocalAttributeItemFieldValue"), map.get("LocalAttributeitemTypeFieldValue"));

		List<Map<String, String>> localAttributeFromExtractDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),DataBaseColumnName.Dimension.name());
		Assertions.assertThat(localAttributeFromExtractDB2).extracting("Dimension").containsAnyOf("Brand","Size");
		log(LogType.INFO,"Verifying local attribute values");
	}

	//MDLW_025
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_025 | Verify whether the Local data attributes displayed in the export file are updated in the respective columns in respective table for special Market", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_localAttribute_changes_For_SpecialMarketItem_updated_In_RespectiveTable_Item(Map<String, String> map) {
		List<Map<String, String>> localAttributeFromExtractDB = null;

		localAttributeFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));
		Assertions.assertThat(localAttributeFromExtractDB).extracting("Dimenstion Value").contains(map.get("LocalAttributeBrandFieldValue"), map.get("LocalAttributeItemFieldValue"), map.get("LocalAttributeitemTypeFieldValue"));

		List<Map<String, String>> localAttributeFromExtractDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),DataBaseColumnName.Dimension.name());
		Assertions.assertThat(localAttributeFromExtractDB2).extracting("Dimension").containsAnyOf("Brand","Size");
		log(LogType.INFO,"Verifying local attribute values");
	}


	//MDLW_026
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "MDLW_026 | Verify whether the Local data attributes displayed in the export file are updated in the respective columns in respective table for Zahn", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_localAttribute_changes_For_ZahnItem_updated_In_RespectiveTable(Map<String, String> map) {
		List<Map<String, String>> localAttributeFromExtractDB = null;

		localAttributeFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));
		Assertions.assertThat(localAttributeFromExtractDB).extracting("Dimenstion Value").contains(map.get("LocalAttributeBrandFieldValue"), map.get("LocalAttributeItemFieldValue"), map.get("LocalAttributeitemTypeFieldValue"));

		List<Map<String, String>> localAttributeFromExtractDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")),DataBaseColumnName.Dimension.name());
		Assertions.assertThat(localAttributeFromExtractDB2).extracting("Dimension").containsAnyOf("Brand","Size");
		log(LogType.INFO,"Verifying local attribute values");
	}

	//INT_012
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "INT_012 | Verify the item changes in PIM reflected in Middleware DB tables", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, 
		groups = {"SMOKE","middleware","US"})
	public void verify_the_ItemChanges_In_PIM_reflected_In_Middleware_DB_Test(Map<String, String> map) {
		List<Map<String, String>> localAttributeFromExtractDB = null;

		localAttributeFromExtractDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.getAllLocalAttributefromExtractDB(map.get("itemNumber")), DataBaseColumnName.Division.name(), DataBaseColumnName.Dimension.name(), Javautils.changeunderscoretospace(DataBaseColumnName.Dimenstion_Value.name()));
		Assertions.assertThat(localAttributeFromExtractDB).extracting("Dimenstion Value").contains(map.get("LocalAttributeBrandFieldValue"), map.get("LocalAttributeItemFieldValue"), map.get("LocalAttributeitemTypeFieldValue"));

		for(Map<String, String> localAttribute : localAttributeFromExtractDB) {
			Assertions.assertThat(localAttribute).containsKeys("Division","Dimension","Dimenstion Value");
		}
	}

	//MDLW_073
	@PimFrameworkAnnotation(module = Modules.LOCAL_ATTRIBUTE, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.TestDataPix)
	@Test(description = "MDLW_073 | local attributes field applicable for the item should be available with label and corresponding value ", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void createItem_In_Pix_without_specified_any_division(Map<String, String> map) {
		List<Map<String, String>> localAttributeValueFromEcomBuildDB = null;

		localAttributeValueFromEcomBuildDB = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.get_All_Local_Attribute_Value_From_Ecom_Build(map.get("itemcode")),DataBaseColumnName.DimValue.name());

		Assertions.assertThat(localAttributeValueFromEcomBuildDB).extracting("DimValue").contains(map.get("item"));

		List<Map<String, String>> localAttributeValueFromEcomBuildDB2 = DatabaseUtilitiies.getMultipleQueryResult(LocalAttributequery.get_All_Local_Attribute_Value_From_Ecom_Build(map.get("itemcode")),DataBaseColumnName.DimName.name());
		Assertions.assertThat(localAttributeValueFromEcomBuildDB2).extracting("DimName").containsAnyOf(map.get("dimName"));
		log(LogType.INFO,"Verifying local attribute values in Ecomm DB");
	}

}
