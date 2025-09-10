package com.pim.tests.Workflow;

import static com.pim.reports.FrameworkLogger.log;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pim.pages.*;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.constants.Constants;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.Javautils;
import com.pim.utils.JsonVerificationUtils;
//import com.sun.tools.sjavac.Log;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;



public class JSON_Verification_PIM extends BaseTest {
	private JSON_Verification_PIM() {

	}

	LocalAttributePage localAttributePage = new LocalAttributePage();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();
	MainMenu mainMenu = new MainMenu();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	Javautils javautils = new Javautils();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	GEP_WebDescriptionPage gepWebDescPage = new GEP_WebDescriptionPage();
	PricePage pricePage = new PricePage();
	ItemMediaTab itemMediatab = new ItemMediaTab();
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.JSON_Verification_PIM)
	@Test(description = "JSON_Verification_From_Local | Check All Values Present in Master are same in JSON", dataProvider = "getCatalogData", groups = {
			"SMOKE", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void US_JSON_Verification_From_Local_Check_All_Values_Present_in_Master_are_same_in_JSON(
			Map<String, String> map) throws IOException {

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = Javautils.getIntValueFromString(map.get("MinutesForJsonFiles"));

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);

		List<String> allJsonCatalogList = new ArrayList<String>();
		Map<String, String> map_LA_FromJSON = new HashMap<String, String>();
		List<String> fullDispDescValueInJSON = new ArrayList<String>();
		List<String> allProductIdsFromJSON = new ArrayList<>();

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, jsonFilePath);
			try {
				List<String> AllProductIdsFromJSON = JsonVerificationUtils.getAllProductIds(jsonFilePath);
				// log(LogType.EXTENTANDCONSOLE, AllProductIdsFromJSON);
				allProductIdsFromJSON.addAll(AllProductIdsFromJSON);

				map_LA_FromJSON = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath,
						allProductIdsFromJSON.get(0));

				List<String> jsonCatalogList = JsonVerificationUtils
						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, allProductIdsFromJSON.get(0));
				allJsonCatalogList.addAll(jsonCatalogList);

				String fullDispDescValue = JsonVerificationUtils.getFullDisplayDescriptionOfGivenProductID(jsonFilePath,
						allProductIdsFromJSON.get(0));
				log(LogType.EXTENTANDCONSOLE, fullDispDescValue);
				fullDispDescValueInJSON.add(fullDispDescValue);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("All first product IDs from JSON files: : " + allProductIdsFromJSON);
		if (!allProductIdsFromJSON.isEmpty()) {
			String firstProductId = allProductIdsFromJSON.get(0);
			System.out.println("First Product ID from first JSON file: " + firstProductId);
		} else {
			System.out.println("No product IDs found in JSON files.");
		}

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(allProductIdsFromJSON.get(0))
				.clickSeachButton().clickOnFirstResult();

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		List<String> master_all_catalogs = new ArrayList<String>();

		master_all_catalogs.addAll(allCatalogsPage.getAllDentalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllMedicalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllZahnCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllSpecialMarketsCatalogList());

		List<String> master_all_catalogs_With_Region = new ArrayList<String>();

		for (String catName : master_all_catalogs) {
			String newCatName = catName.trim() + "_" + Constants.LOCALE;
			master_all_catalogs_With_Region.add(newCatName);
		}
//		System.out.println(master_all_catalogs_With_Region);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown("Local Attribute");
		BasePage.WaitForMiliSec(2000);
		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
		System.out.println("localAttributesNameList : " + localAttributesNameList);

		// remove 2 fields those are not in master
		ArrayList<String> remove1valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(localAttributesNameList, "With (For Description)");
		ArrayList<String> remove2valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(remove1valueLocalAttributesNameList, "Size (For Description)");
		System.out.println("remove2valueLocalAttributesNameList : " + remove2valueLocalAttributesNameList);

		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(remove2valueLocalAttributesNameList);
		System.out.println("map_LA_FromMaster : " + map_LA_FromMaster);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown("GEP Web Description");
		BasePage.WaitForMiliSec(2000);
		String Full_Display_DescriptionInMaster = gepWebDescPage.getFullDisplayDescription();

//		System.out.println("map_LA_FromMaster : "+ map_LA_FromMaster );
		System.out.println("map_LA_FromJSON : " + map_LA_FromJSON);

		Assertions.assertThat(fullDispDescValueInJSON).contains(Full_Display_DescriptionInMaster);

		Assertions.assertThat(allJsonCatalogList).containsAll(master_all_catalogs_With_Region);

//		Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);

		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.JSON_Verification_PIM)
	@Test(description = "JSON_Verification_From_Local | Check All Values Present in Master are same in JSON", dataProvider = "getCatalogData", groups = {
			"SMOKE", "CA", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void CA_JSON_Verification_From_Local_Check_All_Values_Present_in_Master_are_same_in_JSON(
			Map<String, String> map) {

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = Javautils.getIntValueFromString(map.get("MinutesForJsonFiles"));

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);

		List<String> allJsonCatalogList = new ArrayList<String>();
		Map<String, String> map_LA_FromJSON = new HashMap<String, String>();
		List<String> fullDispDescValueInJSON = new ArrayList<String>();
		List<String> allProductIdsFromJSON = new ArrayList<>();

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, jsonFilePath);
			try {

				List<String> AllProductIdsFromJSON = JsonVerificationUtils.getAllProductIds(jsonFilePath);
				// log(LogType.EXTENTANDCONSOLE, AllProductIdsFromJSON);
				allProductIdsFromJSON.addAll(AllProductIdsFromJSON);

				map_LA_FromJSON = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath,
						allProductIdsFromJSON.get(0));

				List<String> jsonCatalogList = JsonVerificationUtils
						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, allProductIdsFromJSON.get(0));
				allJsonCatalogList.addAll(jsonCatalogList);

				String fullDispDescValue = JsonVerificationUtils.getFullDisplayDescriptionOfGivenProductID(jsonFilePath,
						allProductIdsFromJSON.get(0));
				log(LogType.EXTENTANDCONSOLE, fullDispDescValue);
				fullDispDescValueInJSON.add(fullDispDescValue);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("All first product IDs from JSON files: : " + allProductIdsFromJSON);
		if (!allProductIdsFromJSON.isEmpty()) {
			String firstProductId = allProductIdsFromJSON.get(0);
			System.out.println("First Product ID from first JSON file: " + firstProductId);
		} else {
			System.out.println("No product IDs found in JSON files.");
		}

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(allProductIdsFromJSON.get(0))
				.clickSeachButton().clickOnFirstResult();
		// .clickOnSearchResultOnMasterCatalog();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		List<String> master_all_catalogs = new ArrayList<String>();

		master_all_catalogs.addAll(allCatalogsPage.getAllDentalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllMedicalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllZahnCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllSpecialMarketsCatalogList());

		List<String> master_all_catalogs_With_Region = new ArrayList<String>();

		for (String catName : master_all_catalogs) {
			String newCatName = catName.trim() + "_" + Constants.LOCALE;
			master_all_catalogs_With_Region.add(newCatName);
		}
		System.out.println(master_all_catalogs_With_Region);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown("Local Attribute");
		BasePage.WaitForMiliSec(2000);
		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(localAttributesNameList);
		System.out.println(map_LA_FromMaster);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown("GEP Web Description");
		BasePage.WaitForMiliSec(2000);
		String Full_Display_DescriptionInMaster = gepWebDescPage.getFullDisplayDescription();
		System.out.println(Full_Display_DescriptionInMaster);

		System.out.println(allJsonCatalogList);
		System.out.println(map_LA_FromJSON);
		System.out.println(fullDispDescValueInJSON);

		Assertions.assertThat(fullDispDescValueInJSON).contains(Full_Display_DescriptionInMaster);
		log(LogType.EXTENTANDCONSOLE, "Verified fullDispDescValueInJSON");

		Assertions.assertThat(allJsonCatalogList).containsAll(master_all_catalogs_With_Region);
		log(LogType.EXTENTANDCONSOLE, "Verified allJsonCatalogList");

		// Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);
		log(LogType.EXTENTANDCONSOLE, map_LA_FromJSON + " map_LA_FromJSON");

		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case
		log(LogType.EXTENTANDCONSOLE, "Verified map_LA_FromJSON");
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "JSON_Verification_TC_PIM_JSON_01 | Verify Local Attribute and GEP Web Descriptions changes in JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_DentalItem_updated_In_RespectiveTable(Map<String, String> map) {

		// Navigating to Master
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		// Validating Item Number should present in master With new changes
		String itemValueInMaster = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValueInMaster).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValueInMaster = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValueInMaster).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValueInMaster = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValueInMaster).isEqualTo(map.get("LocalAttributeBrandFieldValue"));

		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
//		Map<String, String> map_LA_FromMaster = localAttributePage
//				.getValuesOfGivenLocalAttributesName(localAttributesNameList);
//
//		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");
		
		ArrayList<String> remove1valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(localAttributesNameList, "For (For Description)");
		
		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(remove1valueLocalAttributesNameList);

		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");

		// JSON Verification
				String folderDirectory = Constants.getJSONFilePath_Archive();
				int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
				log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

				List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
				Map<String, String> map_LA_FromJSON = new HashMap<>();

				String itemNumber = map.get("itemNumber");
				boolean matchFound = false;

				for (String jsonFileName : jsonFileNames) {
				    String jsonFilePath = folderDirectory + jsonFileName;
				    log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

				    try {
				        Map<String, String> dimensionMap = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath, itemNumber);

				        if (dimensionMap != null && !dimensionMap.isEmpty()) {
				            map_LA_FromJSON = dimensionMap;
				            log(LogType.EXTENTANDCONSOLE, "Dimension data found for ItemNumber [" + itemNumber + "] in file: " + jsonFileName);
				            matchFound = true;
				            break; // ✅ Stop checking further files
				        } else {
				            log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				        }

				    } catch (IOException e) {
				        log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				        e.printStackTrace();
				    }
				}

				if (!matchFound) {
				    log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
				    Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
				}


		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case

		// Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);

	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "JSON_Verification_TC_PIM_JSON_02 | Verify Local Attribute and GEP Web Descriptions changes in JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_Medical_Item_updated_In_RespectiveTable(Map<String, String> map) {

		// Navigating to Master
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		// Validating Item Number should present in master With new changes
		String itemValueInMaster = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValueInMaster).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValueInMaster = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValueInMaster).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValueInMaster = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValueInMaster).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
		
		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
//		Map<String, String> map_LA_FromMaster = localAttributePage
//				.getValuesOfGivenLocalAttributesName(localAttributesNameList);
//		
//		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");
		
		// remove 2 fields those are not in master
		ArrayList<String> remove1valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(localAttributesNameList, "With (For Description)");
		
		
//		ArrayList<String> remove2valueLocalAttributesNameList = localAttributePage
//				.removeElementByValue(remove1valueLocalAttributesNameList, "Size (For Description)");
//		System.out.println("remove2valueLocalAttributesNameList : " + remove2valueLocalAttributesNameList);

		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(remove1valueLocalAttributesNameList);

		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		Map<String, String> map_LA_FromJSON = new HashMap<>();

		String itemNumber = map.get("itemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
		    String jsonFilePath = folderDirectory + jsonFileName;
		    log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

		    try {
		        Map<String, String> dimensionMap = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath, itemNumber);

		        if (dimensionMap != null && !dimensionMap.isEmpty()) {
		            map_LA_FromJSON = dimensionMap;
		            log(LogType.EXTENTANDCONSOLE, "Dimension data found for ItemNumber [" + itemNumber + "] in file: " + jsonFileName);
		            matchFound = true;
		            break; // ✅ Stop checking further files
		        } else {
		            log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
		        }

		    } catch (IOException e) {
		        log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
		        e.printStackTrace();
		    }
		}

		if (!matchFound) {
		    log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
		    Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case

		// Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "JSON_Verification_TC_PIM_JSON_03 | Verify Local Attribute and GEP Web Descriptions changes in JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_SpecialMarketItem_updated_In_RespectiveTable_Item(
			Map<String, String> map) {

		// Navigating to Master
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		// Validating Item Number should present in master With new changes
		String itemValueInMaster = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValueInMaster).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValueInMaster = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValueInMaster).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValueInMaster = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValueInMaster).isEqualTo(map.get("LocalAttributeBrandFieldValue"));

		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
//		Map<String, String> map_LA_FromMaster = localAttributePage
//				.getValuesOfGivenLocalAttributesName(localAttributesNameList);
//
//		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");
		
		ArrayList<String> remove1valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(localAttributesNameList, "With (For Description)");
		ArrayList<String> remove2valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(remove1valueLocalAttributesNameList, "For (For Description)");
		ArrayList<String> remove3valueLocalAttributesNameList = localAttributePage
				.removeElementByValue(remove2valueLocalAttributesNameList, "Quoted Equipment Flag");
		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(remove3valueLocalAttributesNameList);
		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		Map<String, String> map_LA_FromJSON = new HashMap<>();

		String itemNumber = map.get("itemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
		    String jsonFilePath = folderDirectory + jsonFileName;
		    log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

		    try {
		        Map<String, String> dimensionMap = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath, itemNumber);

		        if (dimensionMap != null && !dimensionMap.isEmpty()) {
		            map_LA_FromJSON = dimensionMap;
		            log(LogType.EXTENTANDCONSOLE, "Dimension data found for ItemNumber [" + itemNumber + "] in file: " + jsonFileName);
		            matchFound = true;
		            break; // ✅ Stop checking further files
		        } else {
		            log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
		        }

		    } catch (IOException e) {
		        log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
		        e.printStackTrace();
		    }
		}

		if (!matchFound) {
		    log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
		    Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case

		// Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description = "JSON_Verification_TC_PIM_JSON_04 | Verify Local Attribute and GEP Web Descriptions changes in JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_localAttribute_changes_For_ZahnItem_updated_In_RespectiveTable(Map<String, String> map) {

		// Navigating to Master
		loginPage.enterUserName(map.get("Username")).enterPassword(map.get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		// Validating Item Number should present in master With new changes
		String itemValueInMaster = localAttributePage.getItemValue().trim();
		Assertions.assertThat(itemValueInMaster).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		String itemTypeValueInMaster = localAttributePage.getItemTypeValue().trim();
		Assertions.assertThat(itemTypeValueInMaster).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
		String brandValueInMaster = localAttributePage.getBrandValue().trim();
		Assertions.assertThat(brandValueInMaster).isEqualTo(map.get("LocalAttributeBrandFieldValue"));

		ArrayList<String> localAttributesNameList = localAttributePage.getKeyForJSONFromLocalAttributeTab();
		Map<String, String> map_LA_FromMaster = localAttributePage
				.getValuesOfGivenLocalAttributesName(localAttributesNameList);

		log(LogType.EXTENTANDCONSOLE, map_LA_FromMaster + " map_LA_FromMaster");

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		Map<String, String> map_LA_FromJSON = new HashMap<>();

		String itemNumber = map.get("itemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
		    String jsonFilePath = folderDirectory + jsonFileName;
		    log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

		    try {
		        Map<String, String> dimensionMap = JsonVerificationUtils.getAll_Dimension_Name_And_Dimension_Values_In_Map(jsonFilePath, itemNumber);

		        if (dimensionMap != null && !dimensionMap.isEmpty()) {
		            map_LA_FromJSON = dimensionMap;
		            log(LogType.EXTENTANDCONSOLE, "Dimension data found for ItemNumber [" + itemNumber + "] in file: " + jsonFileName);
		            matchFound = true;
		            break; // ✅ Stop checking further files
		        } else {
		            log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
		        }

		    } catch (IOException e) {
		        log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
		        e.printStackTrace();
		    }
		}

		if (!matchFound) {
		    log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
		    Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Map<String, String> normalizedMaster = localAttributePage.normalizeKeys(map_LA_FromMaster);
		Map<String, String> normalizedJSON = localAttributePage.normalizeKeys(map_LA_FromJSON);

		Assertions.assertThat(normalizedMaster).usingRecursiveComparison()
				.withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class) // for values
				.isEqualTo(normalizedJSON); // will now ignore key and value case

		// Assertions.assertThat(map_LA_FromMaster).isEqualToComparingFieldByFieldRecursively(map_LA_FromJSON);
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.GEPWebDescription)
	@Test(description = "JSON_Verification_TC_PIM_JSON_05 | Verify Local Attribute and GEP Web Descriptions changes in JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void updationOfGEPWebDescriptionAfterUpdationOfLocalAttributeParameterForUS(Map<String, String> map) {

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogTypeMaster")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown("GEP Web Description");

		qualityStatusPage.maximizeQualityStatusTab();

		// Validating Item Number should present in master With new changes
		String Full_Display_DescriptionInMaster = gepWebDescPage.getFullDisplayDescription();

//		// JSON Verification
//		String folderDirectory = Constants.getJSONFilePath_Archive();
//		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
//		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");
//
//		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
//
//		List<String> fullDispDescValueInJSON = new ArrayList<String>();
//
//		for (String jsonFileName : jsonFileNames) {
//			String jsonFilePath = folderDirectory + jsonFileName;
//			log(LogType.EXTENTANDCONSOLE, jsonFilePath);
//			try {
//				String fullDispDescValue = JsonVerificationUtils.getFullDisplayDescriptionOfGivenProductID(jsonFilePath,
//						map.get("itemNumber"));
//				log(LogType.EXTENTANDCONSOLE, fullDispDescValue);
//				fullDispDescValueInJSON.add(fullDispDescValue);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		
		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> fullDispDescValueInJSON = new ArrayList<>();

		String itemNumber = map.get("itemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
		    String jsonFilePath = folderDirectory + jsonFileName;
		    log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

		    try {
		        String fullDispDescValue = JsonVerificationUtils.getFullDisplayDescriptionOfGivenProductID(jsonFilePath, itemNumber);

		        if (fullDispDescValue != null && !fullDispDescValue.trim().isEmpty()) {
		            log(LogType.EXTENTANDCONSOLE, "Full Display Description found: " + fullDispDescValue);
		            fullDispDescValueInJSON.add(fullDispDescValue);
		            matchFound = true;
		            break; // ✅ Stop once itemNumber is matched
		        } else {
		            log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
		        }

		    } catch (IOException e) {
		        log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
		        e.printStackTrace();
		    }
		}

		if (!matchFound) {
		    log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
		    Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		System.out.println("fullDispDescValueInJSON : " + fullDispDescValueInJSON);
		log(LogType.EXTENTANDCONSOLE, fullDispDescValueInJSON + " fullDispDescValueInJSONfiles");

		// Assertion
		Assertions.assertThat(fullDispDescValueInJSON).contains(Full_Display_DescriptionInMaster);

	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "JSON_Verification_TC_PIM_JSON_06 | verify_rule_driven_for_dental_division", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_rule_driven_for_dental_division(Map<String, String> map) {

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab"));

		qualityStatusPage.maximizeQualityStatusTab();

		List<String> master_all_catalogs = new ArrayList<String>();

		master_all_catalogs.addAll(allCatalogsPage.getAllDentalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllMedicalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllZahnCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllSpecialMarketsCatalogList());

		List<String> master_all_catalogs_With_Region = new ArrayList<String>();

		for (String catName : master_all_catalogs) {
			String newCatName = catName.trim() + "_" + Constants.LOCALE;
			master_all_catalogs_With_Region.add(newCatName);
		}
		log(LogType.EXTENTANDCONSOLE, master_all_catalogs_With_Region + " master_all_catalogs_With_Region");

//		// JSON Verification
//		String folderDirectory = Constants.getJSONFilePath_Archive();
//		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
//		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");
//
//		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
//
//		List<String> allJsonCatalogList = new ArrayList<String>();
//
//		for (String jsonFileName : jsonFileNames) {
//			String jsonFilePath = folderDirectory + jsonFileName;
//			log(LogType.EXTENTANDCONSOLE, jsonFilePath);
//			try {
//				List<String> jsonCatalogList = JsonVerificationUtils
//						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, map.get("ItemNumber"));
//				log(LogType.EXTENTANDCONSOLE, jsonCatalogList + " jsonCatalogList");
//				//allJsonCatalogList.addAll(jsonCatalogList);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		List<String> normalizedMaster = localAttributePage.normalizeList(master_all_catalogs_With_Region);
//		List<String> normalizedJSON = localAttributePage.normalizeList(allJsonCatalogList);
//
//		Assertions.assertThat(normalizedJSON).containsExactlyInAnyOrderElementsOf(normalizedMaster);
//		c

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> allJsonCatalogList = new ArrayList<>();

		String itemNumber = map.get("ItemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				List<String> jsonCatalogList = JsonVerificationUtils
						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, itemNumber);

				if (jsonCatalogList != null && !jsonCatalogList.isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, jsonCatalogList + " jsonCatalogList");
					allJsonCatalogList.addAll(jsonCatalogList);
					matchFound = true;
					break; // ✅ Exit loop once matching itemNumber is found
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Assertions.assertThat(allJsonCatalogList).containsAll(master_all_catalogs_With_Region);
	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description = "JSON_Verification_TC_PIM_JSON_07 | verify_all_catalog_tabs_and_exception_list_for_dental_division", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "LOCAL_ATTRIBUTE" }, dataProviderClass = DataProviderUtils.class)
	public void verify_all_catalog_tabs_and_exception_list_for_dental_division(Map<String, String> map) {

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab"));

		qualityStatusPage.maximizeQualityStatusTab();

		List<String> master_all_catalogs = new ArrayList<String>();

		master_all_catalogs.addAll(allCatalogsPage.getAllDentalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllMedicalCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllZahnCatalogList());
		master_all_catalogs.addAll(allCatalogsPage.getAllSpecialMarketsCatalogList());

		List<String> master_all_catalogs_With_Region = new ArrayList<String>();

		for (String catName : master_all_catalogs) {
			String newCatName = catName.trim() + "_" + Constants.LOCALE;
			master_all_catalogs_With_Region.add(newCatName);
		}
		log(LogType.EXTENTANDCONSOLE, master_all_catalogs_With_Region + " master_all_catalogs_With_Region");

//		// JSON Verification
//		String folderDirectory = Constants.getJSONFilePath_Archive();
//		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
//		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");
//
//		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
//
//		List<String> allJsonCatalogList = new ArrayList<String>();
//
//		for (String jsonFileName : jsonFileNames) {
//			String jsonFilePath = folderDirectory + jsonFileName;
//			log(LogType.EXTENTANDCONSOLE, jsonFilePath);
//			try {
//				List<String> jsonCatalogList = JsonVerificationUtils
//						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, map.get("ItemNumber"));
//				log(LogType.EXTENTANDCONSOLE, jsonCatalogList + " jsonCatalogList");
//				//allJsonCatalogList.addAll(jsonCatalogList);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		List<String> normalizedMaster = localAttributePage.normalizeList(allJsonCatalogList);
//		List<String> normalizedJSON = localAttributePage.normalizeList(master_all_catalogs_With_Region);
//
//		Assertions.assertThat(normalizedMaster).containsExactlyInAnyOrderElementsOf(normalizedJSON);
//
//		//Assertions.assertThat(allJsonCatalogList).containsAll(master_all_catalogs_With_Region);

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> allJsonCatalogList = new ArrayList<>();

		String itemNumber = map.get("ItemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				List<String> jsonCatalogList = JsonVerificationUtils
						.getListOfCatalog_Name_OfGivenProductId(jsonFilePath, itemNumber);

				if (jsonCatalogList != null && !jsonCatalogList.isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, jsonCatalogList + " jsonCatalogList");
					allJsonCatalogList.addAll(jsonCatalogList);
					matchFound = true;
					break; // ✅ Exit loop once matching itemNumber is found
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Assertions.assertThat(allJsonCatalogList).containsAll(master_all_catalogs_With_Region);

	}
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Prices)
	@Test(description = "JSON_Verification_TC_PIM_JSON_07 | verify_all_catalog_tabs_and_exception_list_for_dental_division", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_List_Price_JSON_Verification_For_An_Item(Map<String, String> map) {

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("itemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName"));

		qualityStatusPage.maximizeQualityStatusTab();
		pricePage.sortPriceByValidFrom();
		String master_list_Price = pricePage.GetTheListPrice();

		log(LogType.EXTENTANDCONSOLE, "List Price of Item is [" + master_list_Price + "]");
		/*pricePage.ClickRefreshIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("USCatalog")).enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));
		qualityStatusPage.maximizeQualityStatusTab();
		pricePage.sortPriceByValidFrom();
		String US_list_Price = pricePage.GetTheListPrice();
		log(LogType.EXTENTANDCONSOLE, "List Price of Item is in US catalog [" + US_list_Price + "]");
		Assertions.assertThat(US_list_Price.contains(master_list_Price));
*/
		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("itemNumber"));
		log(LogType.EXTENTANDCONSOLE, (map.get("TestCaseName") + map.get("itemNumber")));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> ListPriceInJson = new ArrayList<>();

		String itemNumber = map.get("itemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				String Json_List_Price = JsonVerificationUtils.getPriceInsidePrices(jsonFilePath, itemNumber);

				if (Json_List_Price != null && !Json_List_Price.trim().isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, "Price Found: " + Json_List_Price);
					ListPriceInJson.add(Json_List_Price);
					matchFound = true;
					break; // ✅ Stop once itemNumber is matched
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		System.out.println("ListPriceInJson : " + ListPriceInJson);
		log(LogType.EXTENTANDCONSOLE, ListPriceInJson + " fullDispDescValueInJSONfiles");

		// Assertion

		Assertions.assertThat(ListPriceInJson.contains(master_list_Price));


	}
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "JSON_Verification_TC_PIM_JSON_07 | verify_all_catalog_tabs_and_exception_list_for_dental_division", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Json_Verification" }, dataProviderClass = DataProviderUtils.class)
	public void verify_items_media_files(Map<String, String> map) {

		// Navigating to Master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton().clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName"));

		qualityStatusPage.maximizeQualityStatusTab();

		List<String> all_media_Image_name = new ArrayList<String>();

		all_media_Image_name.addAll(itemMediatab.getAllImageName());

		log(LogType.EXTENTANDCONSOLE, all_media_Image_name + " all the media item images");

//
		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> allImageItemMediaList = new ArrayList<>();

		String itemNumber = map.get("ItemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				List<String> imageMediaList = JsonVerificationUtils
						.getItemMedia_Id(jsonFilePath, itemNumber);

				if (imageMediaList != null && !imageMediaList.isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, imageMediaList + " imageMediaList");
					allImageItemMediaList.addAll(imageMediaList);
					matchFound = true;
					break; // ✅ Exit loop once matching itemNumber is found
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		Assertions.assertThat(allImageItemMediaList).containsAll(all_media_Image_name);

	}
	//ProductNotes JSON Verification US
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "JSON_Verification_TC_PIM_JSON_ | verify_productNotes_US_in_JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Notes_JSON_Verification_US(Map<String, String> map) {

		//Add Product Notes column & search item in master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon().clickHSISelectionOption().clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Validating Product Note form Master Catalog Item Number");
		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();

		//Adding Product Notes & Language in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.enterProductNotesLanguageValue(map.get("productNoteslanguage")).clickOkButton();

		String master_ProductNotes=productDetailSearchPage.getDisplayedProductNotes();
		BasePage.WaitForMiliSec(2000);


		log(LogType.EXTENTANDCONSOLE, "Product Notes for the  Item is [" + master_ProductNotes + "]");

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_Archive();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
		log(LogType.EXTENTANDCONSOLE, (map.get("TestCaseName") + map.get("ItemNumber")));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> ProductNotesInJson = new ArrayList<>();

		String itemNumber = map.get("ItemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				String Json_Product_Notes = JsonVerificationUtils.getProductNotesInsideDescriptions(jsonFilePath, itemNumber);

				if (Json_Product_Notes != null && !Json_Product_Notes.trim().isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, "Json_Product_Notes Found: " + Json_Product_Notes);
					ProductNotesInJson.add(Json_Product_Notes);
					matchFound = true;
					break;
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		System.out.println("ProductNotesInJsonFile : " + ProductNotesInJson);
		log(LogType.EXTENTANDCONSOLE, "ProductNotesInJsonFile : " + ProductNotesInJson);

		// Assertion

		Assertions.assertThat(ProductNotesInJson.contains(master_ProductNotes));


	}
	//ProductNotes JSON Verification CA
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "JSON_Verification_TC_PIM_JSON_ | verify_productNotes_CA_in_JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Notes_JSON_Verification_CA(Map<String, String> map) {

		//Add Product Notes column & search item in master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon().clickHSISelectionOption().clickSettingIcon().clickFieldSelectionOption();
		log(LogType.INFO, "Validating Product Note form CA Catalog Item Number");
		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();

		//Adding Product Notes & Language in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header")).clickHeaderText(map.get("HeaderText")).clickAddButton()
				.enterProductNotesLanguageValue(map.get("productNoteslanguage")).clickOkButton();

		String master_ProductNotes=productDetailSearchPage.getDisplayedProductNotes();
		BasePage.WaitForMiliSec(2000);


		log(LogType.EXTENTANDCONSOLE, "Product Notes for the  Item is [" + master_ProductNotes + "]");

		// JSON Verification
		String folderDirectory = Constants.getJSONFilePath_ArchiveCA();
		int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));
		log(LogType.EXTENTANDCONSOLE, (map.get("TestCaseName") + map.get("ItemNumber")));
		log(LogType.EXTENTANDCONSOLE, time + " minutes for checking json files");

		List<String> jsonFileNames = FileUtils.getRequiredFileNamesFromGivenFolder(folderDirectory, time);
		List<String> ProductNotesInJson = new ArrayList<>();

		String itemNumber = map.get("ItemNumber");
		boolean matchFound = false;

		for (String jsonFileName : jsonFileNames) {
			String jsonFilePath = folderDirectory + jsonFileName;
			log(LogType.EXTENTANDCONSOLE, "Checking file: " + jsonFilePath);

			try {
				String Json_Product_Notes = JsonVerificationUtils.getProductNotesInsideDescriptions(jsonFilePath, itemNumber);

				if (Json_Product_Notes != null && !Json_Product_Notes.trim().isEmpty()) {
					log(LogType.EXTENTANDCONSOLE, "Json_Product_Notes Found: " + Json_Product_Notes);
					ProductNotesInJson.add(Json_Product_Notes);
					matchFound = true;
					break;
				} else {
					log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in file: " + jsonFileName);
				}

			} catch (IOException e) {
				log(LogType.EXTENTANDCONSOLE, "Error reading file: " + jsonFileName);
				e.printStackTrace();
			}
		}

		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + itemNumber + "] not found in any JSON file.");
			Assertions.fail("ItemNumber [" + itemNumber + "] not found in any JSON file.");
		}

		System.out.println("ProductNotesInJsonFile : " + ProductNotesInJson);
		log(LogType.EXTENTANDCONSOLE, "ProductNotesInJsonFile : " + ProductNotesInJson);

		// Assertion

		Assertions.assertThat(ProductNotesInJson.contains(master_ProductNotes));


	}
}