package com.pim.tests.Middleware;

import static com.pim.reports.FrameworkLogger.log;

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
import com.pim.pages.BasePage;
import com.pim.queries.Catalogqueries;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.Javautils;

public class MiddlewareCatalogTest extends MiddlewareBaseClassTest {

	//checking catalog assigned in DB TC id is MDLW_005
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_005 | Verify whether the catalogs assigned to items are displayed in the respective columns in respective table in DB", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, 
		groups = {"SMOKE","middleware","US"})

	public void verify_catalog_assigned_item_displays_in_database(Map<String, String> map) {
		Map<String, String> catalogresultfrmexporttable = null;
		Map<String, String> catalogresultfromMastertable = null;
		Map<String, String> catalogresultfromIpadcatalogtable = null;

		catalogresultfrmexporttable= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogName.name());

		List<String> cataloglistresultfromexport = new ArrayList<String>(catalogresultfrmexporttable.values());
		Assertions.assertThat(cataloglistresultfromexport).contains(map.get("userDriven"));
		//Assertions.assertThat(list).hasSameElementsAs(MiddlewareCatalog.rule_driven);

		catalogresultfromMastertable= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByCatalogQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.u_catalog_list.name());

		String cataloglistresultfromMaster = catalogresultfromMastertable.toString();
		//List<String> cataloglistresultfromMaster = new ArrayList<String>(catalogresultfromMastertable.values());
		Assertions.assertThat(cataloglistresultfromMaster).contains(map.get("userDriven"));

		catalogresultfromIpadcatalogtable= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogFromIpadTable(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CATALOG.name());

		List<String> cataloglistresultfromipad = new ArrayList<String>(catalogresultfromIpadcatalogtable.values());
		Assertions.assertThat(cataloglistresultfromipad).contains(map.get("userDriven"));
	}

	//checking exception list is updating in DB TC id - MDLW_0056
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_056 | Drop an catalog mapped to an item in PIM and check if changes are available in respective databases", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_all_catalog_tabs_and_exception_list_for_dental_division(Map<String, String> map) {

		Map<String, String> catalogFromExceptionListForExtractDB = null;
		Map<String, String> catalogFromExceptionListForDeviceDB = null;

		catalogFromExceptionListForExtractDB = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogName.name(), DataBaseColumnName.CatalogPublish.name());

		Assertions.assertThat(catalogFromExceptionListForExtractDB).containsValues(map.get("userDriven"),"N");

		catalogFromExceptionListForDeviceDB = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogFromIpadTable(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CATALOG.name());

		List<String> catalogListForExceptionCatalogDeviceDB = new ArrayList<String>(catalogFromExceptionListForDeviceDB.values());
		Assertions.assertThat(catalogListForExceptionCatalogDeviceDB).contains(map.get("userDriven"));
	}

	//checking publish date is future date and flag is N TC id - MDLW_017
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "MDLW_017 | Verify for Publish Flag = Y and Publish Date = future date gets updated in respective table when items are loaded from PIM", priority = 2, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_publish_flag_should_yes_and_publish_date_should_be_future_US_for_Dental_division(Map<String, String> map) {

		Map<String, String> publishdateandflagfromextractdb = null;
		List<Map<String, String>> publishdatefromecommdb = null;
		Map<String, String> publishdatefromdevice = null;

		//verifying in extract DB
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
        
		BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("N");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		//verifying in Device DB
		publishdatefromdevice = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogFromIpadTable(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.MODIFIEDDATE.name());
		BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdatefromdevice).containsKeys("MODIFIEDDATE");
		String dateandtimefordevicedb = publishdatefromdevice.get("MODIFIEDDATE");
		String[] datefordevicedb = Javautils.splitbasedonspace(dateandtimefordevicedb);
		Assertions.assertThat(datefordevicedb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		//verifying in ecomm db
		publishdatefromecommdb = DatabaseUtilitiies.getMultipleQueryResult(Catalogqueries.getAllCatalogsByCatalogQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.dt_modified_on.name());
		BasePage.WaitForMiliSec(10000);
		for(Map<String, String> publishdate : publishdatefromecommdb) {
			Assertions.assertThat(publishdate).containsKey("dt_modified_on");
			String dateandtimeforecommdb = publishdate.get("dt_modified_on");
			String[] dateforecommdb = Javautils.splitbasedonspace(dateandtimeforecommdb);
			Assertions.assertThat(dateforecommdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
		}
	}

	//checking publish date and flag is updating in respective column TCid - MDLW_015
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_015 | Verify the Publish flag and publish date  was updated in  respective table from the outbound file in DB", priority = 2, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, 
		groups = {"SMOKE","middleware","US"})

	public void verify_publish_flag_and_date_update_in_respective_column(Map<String, String> map) {
		Map<String, String> publishdateandflagfromextractdb = null;
		List<Map<String, String>> publishdatefromecommdb = null;
		Map<String, String> publishdatefromdevice = null;

		//device db
		publishdatefromdevice = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogFromIpadTable(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.MODIFIEDDATE.name());
        BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdatefromdevice).containsKeys("MODIFIEDDATE");
		String dateandtimefordevicedb = publishdatefromdevice.get("MODIFIEDDATE");
		String[] datefordevicedb = Javautils.splitbasedonspace(dateandtimefordevicedb);
		Assertions.assertThat(datefordevicedb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		//ecomm db
		publishdatefromecommdb = DatabaseUtilitiies.getMultipleQueryResult(Catalogqueries.getAllCatalogsByCatalogQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.dt_modified_on.name());
        BasePage.WaitForMiliSec(10000);
		for(Map<String, String> publishdate : publishdatefromecommdb) {
			Assertions.assertThat(publishdate).containsKey("dt_modified_on");
			String dateandtimeforecommdb = publishdate.get("dt_modified_on");
			String[] dateforecommdb = Javautils.splitbasedonspace(dateandtimeforecommdb);
			Assertions.assertThat(dateforecommdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
		}

		//extract db
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
        BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
		String flag = publishdateandflagfromextractdb.get("CatalogPublish");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue(flag);
	}


	//checking publish flag updated as N TCID - MDLW_018
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "MDLW_018 | Verify for Publish Flag = N gets updated in respective table  when items are loaded from  PIM", priority = 2, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void verify_publish_flag_should_updated_as_NO_in_Dental_division_for_US(Map<String, String> map){

		Map<String, String> publishdateandflagfromextractdb = null;

		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name());

		Assertions.assertThat(publishdateandflagfromextractdb).containsKey("CatalogPublish");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("N");
	}

	//item should be created in pix TCID - MDLW_016
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_016 | Verify for the Item default values for Publish Flag and Publish Date gets updated in respective table  when items are loaded from PIX to PIM", priority = 5, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class , groups = {"middleware","US"})
	public void verify_publish_date_and_flag_should_updated_when_item_loaded_from_pix_to_pim(Map<String, String> map) {
		Map<String, String> publishdateandflagfromextractdb = null;
		List<Map<String, String>> publishdatefromecommdb = null;
		Map<String, String> publishdatefromdevice = null;

		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());

		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		//Assertions.assertThat(publishdateandflagfromextractdb).containsValue("Y");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		publishdatefromdevice = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogFromIpadTable(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.MODIFIEDDATE.name());

		Assertions.assertThat(publishdatefromdevice).containsKeys("MODIFIEDDATE");
		String dateandtimefordevicedb = publishdatefromdevice.get("MODIFIEDDATE");
		String[] datefordevicedb = Javautils.splitbasedonspace(dateandtimefordevicedb);
		Assertions.assertThat(datefordevicedb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		publishdatefromecommdb = DatabaseUtilitiies.getMultipleQueryResult(Catalogqueries.getAllCatalogsByCatalogQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.dt_modified_on.name());

		for(Map<String, String> publishdate : publishdatefromecommdb) {
			Assertions.assertThat(publishdate).containsKey("dt_modified_on");
			String dateandtimeforecommdb = publishdate.get("dt_modified_on");
			String[] dateforecommdb = Javautils.splitbasedonspace(dateandtimeforecommdb);
			Assertions.assertThat(dateforecommdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
		}
	}

	//checking catalog details are displaying in particular column TCID - MDLW_004
	@PimFrameworkAnnotation(module = Modules.MiddleWare, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "MDLW_004 | Verify whether the Catalog details,are displayed in the respective columns in respective table", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void catalog_details_should_reflect_in_master_table(Map<String, String> map) {
		List<Map<String, String>> exportcatalogmaster = null;

		exportcatalogmaster = DatabaseUtilitiies.getMultipleQueryResult(Catalogqueries.getALlCatalogFromMasterTable(map.get("TabName")), DataBaseColumnName.CMMDMDIV.name());

		for(Map<String, String> mastercatalog : exportcatalogmaster) {
			Assertions.assertThat(mastercatalog).containsValue(map.get("TabName"));
		}
	}


	//checking French description in DB TC id is FDM_22-Verify manually entered French descriptions (for 'Dental' division) are published to the web applications through the middleware layer.

	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_22 | Verify manually entered French descriptions (for 'Dental' division) are published to the web applications through the middleware layer in DB", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void VerifyManuallyEnteredFrenchDescriptionsforDentalDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map) {

		Map<String, String> French_Description= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getFrenchDescriptionFromMasterTable(map.get("ItemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Full_Display_Description.name()), DataBaseColumnName.DateAdded.name());
		Assertions.assertThat(French_Description).containsValue(map.get("FrenchDecription"));
		//Assertions.assertThat(list).hasSameElementsAs(MiddlewareCatalog.rule_driven);
		log(LogType.INFO,"Verifying french description in Export_Item_Description");
	}

	//checking French description in DB TC id is FDM_23-Verify manually entered French descriptions (for 'Medical' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_23 | Verify manually entered French descriptions (for 'Medical' division) are published to the web applications through the middleware layer in DB", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void VerifyManuallyEnteredFrenchDescriptionsforMedicalDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map) {

		Map<String, String> French_Description= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getFrenchDescriptionFromMasterTable(map.get("ItemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Full_Display_Description.name()), DataBaseColumnName.DateAdded.name());
		Assertions.assertThat(French_Description).containsValue(map.get("FrenchDecription"));
		//Assertions.assertThat(list).hasSameElementsAs(MiddlewareCatalog.rule_driven);
		log(LogType.INFO,"Verifying french description in Export_Item_Description");
	}

	//checking French description in DB TC id is FDM_24-Verify manually entered French descriptions (for 'Zahn' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_24 | Verify manually entered French descriptions (for 'Zahn' division) are published to the web applications through the middleware layer in DB", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void VerifyManuallyEnteredFrenchDescriptionsforZahnDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map) {

		Map<String, String> French_Description= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getFrenchDescriptionFromMasterTable(map.get("ItemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Full_Display_Description.name()), DataBaseColumnName.DateAdded.name());
		Assertions.assertThat(French_Description).containsValue(map.get("FrenchDecription"));
		//Assertions.assertThat(list).hasSameElementsAs(MiddlewareCatalog.rule_driven);
		log(LogType.INFO,"Verifying french description in Export_Item_Description");
	}

	//checking French description in DB TC id is FDM_25-Verify manually entered French descriptions (for 'Special Market' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_25 | Verify manually entered French descriptions (for 'Special Market' division) are published to the web applications through the middleware layer in DB", priority = 0, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"middleware","US"})
	public void VerifyManuallyEnteredFrenchDescriptionsforSpecialMarketDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map) {

		Map<String, String> French_Description= DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getFrenchDescriptionFromMasterTable(map.get("ItemNumber")),Javautils.changeunderscoretospace(DataBaseColumnName.Full_Display_Description.name()), DataBaseColumnName.DateAdded.name());
		Assertions.assertThat(French_Description).containsValue(map.get("FrenchDecription"));
		//Assertions.assertThat(list).hasSameElementsAs(MiddlewareCatalog.rule_driven);
		log(LogType.INFO,"Verifying french description in Export_Item_Description");
	}

	//ITP_021
	@PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_021 | Verify whether the Publish Flag for a Dental Item get updated as \"Y\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"middleware","US"})
	public void verify_publish_flag_should_updated_as_Yes_in_Dental_division_for_US(Map<String, String> map) {

		Map<String, String> publishdateandflagfromextractdb = null;

		//verifying in extract DB
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
		BasePage.WaitForMiliSec(10000);

		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("Y");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
	}

	//ITP_022
	@PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_022 | Verify whether the Publish Flag for a Medical Item get updated as \"Y\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"middleware","US"})
	public void verify_publish_flag_should_updated_as_Yes_in_Medical_division_for_US(Map<String, String> map) {

		Map<String, String> publishdateandflagfromextractdb = null;

		//verifying in extract DB
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
		BasePage.WaitForMiliSec(10000);

		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("Y");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
	}

	//ITP_023
	@PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_023 | Verify whether the Publish Flag for a Zahn Item get updated as \"Y\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"middleware","US"})
	public void verify_publish_flag_should_updated_as_Yes_in_Zahn_division_for_US(Map<String, String> map) {

		Map<String, String> publishdateandflagfromextractdb = null;

		//verifying in extract DB
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
		BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("Y");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
	}

	//ITP_024
	@PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemPublish)
	@Test(description = "ITP_024 | Verify whether the Publish Flag for a Special Markets Item get updated as \"Y\" in the Middleware Export File, when the Items loaded from PIX to PIM with Publish Flag as \"Y\"", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"middleware","US"})
	public void verify_publish_flag_should_updated_as_Yes_in_Special_Markets_division_for_US(Map<String, String> map) {

		Map<String, String> publishdateandflagfromextractdb = null;

		//verifying in extract DB
		publishdateandflagfromextractdb = DatabaseUtilitiies.getOneRowQueryResult(Catalogqueries.getAllCatalogsByItemCodeQuery(map.get("ItemNumber"), map.get("userDriven")), DataBaseColumnName.CatalogPublish.name(), DataBaseColumnName.DateExtracted.name(), DataBaseColumnName.DateModified.name());
		BasePage.WaitForMiliSec(10000);
		Assertions.assertThat(publishdateandflagfromextractdb).containsKeys("DateExtracted", "CatalogPublish", "DateModified");
		String dateandtimeforextract = publishdateandflagfromextractdb.get("DateExtracted");
		String modifieddateandtimeforextract = publishdateandflagfromextractdb.get("DateModified");
		Assertions.assertThat(publishdateandflagfromextractdb).containsValue("Y");

		String[] dateforextractdb = Javautils.splitbasedonspace(dateandtimeforextract);
		Assertions.assertThat(dateforextractdb[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());

		String[] modifieddateforextract = Javautils.splitbasedonspace(modifieddateandtimeforextract);
		Assertions.assertThat(modifieddateforextract[0]).isEqualTo(DateandTimeUtils.current_date_form_of_year_month_day());
	}
}
