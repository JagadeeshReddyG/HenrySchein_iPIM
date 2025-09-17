package com.pim.tests.Workflow;

import com.ipim.page.iPimMainPage;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.constants.Constants;
import com.pim.driver.Driver;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.*;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import com.pim.utils.JsonVerificationUtils;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;


public class iPIM_JSON_Verification extends BaseTest {
	private iPIM_JSON_Verification() {

	}
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	ItemMediaTab itemMediatab = new ItemMediaTab();
	MediaSubMenu mediaTab = new MediaSubMenu();
	PricePage pricePage = new PricePage();
	GEP_WebDescriptionPage
			gepWebDescPage = new GEP_WebDescriptionPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	LocalAttributePage localAttributePage = new LocalAttributePage();
	ClassificationsPage classificationpage = new ClassificationsPage();



	private void verifyJsonForItem(Map<String, String> map, JsonVerificationUtils.JsonProcessor processor) {

		try {
			// Close the PIM browser session
			Driver.quitDriver();
			// Build key for timestamp
			String key = map.get("TestCaseName") + map.get("ItemNumber");

			// Get stored timestamp string from properties file
			String expectedTimeStamp = FileUtils.getSavedTimestamp(key);

			// ---- Step 1: Get timestamp from properties file ----
			//String expectedTimeStamp =("2025-08-11 15:54:04");
			DateTimeFormatter propFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime testStartTime = LocalDateTime.parse(expectedTimeStamp, propFormatter);
			log(LogType.EXTENTANDCONSOLE, "Property timestamp: " + testStartTime);

			// ---- Step 2: Launch IPIM and search item ----
			BaseTest.launchIPIM();
			log(LogType.INFO, "iPIM Application Launched");
			iPimMainPage iPimMainPage = new iPimMainPage();
			iPimMainPage.clickUsFlag()
					.enterItemCodeNumber(map.get("ItemNumber"))
					.clickSearchBtn();

			List<WebElement> records = iPimMainPage.getListOfRecords();
			DateTimeFormatter uiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			boolean matchFound = false;

			// ---- Step 3: Iterate through search results ----
			for (WebElement row : records) {
				String rowTimeText = row.getText().trim();
				LocalDateTime rowTime = LocalDateTime.parse(rowTimeText, uiFormatter);

				// Uncomment if wanted to log all ipim record timestamps
				//log(LogType.EXTENTANDCONSOLE, "Row timestamp: " + rowTime);

				// Only consider rows >= property timestamp
				if (!rowTime.isBefore(testStartTime)) {
					//log(LogType.EXTENTANDCONSOLE, "Row TimeStamp: " + rowTimeText);
					//log(LogType.EXTENTANDCONSOLE, "Checking row >= property timestamp: " + rowTime);
					log(LogType.EXTENTANDCONSOLE, "Row timestamp [" + rowTime + "] >= Property timestamp [" + testStartTime + "]");
					// Click row
					row.click();
					BasePage.WaitForMiliSec(2000);

					// Switch to Raw Tab
					iPimMainPage.clickRawTab();
					BasePage.WaitForMiliSec(2000);

					// ---- Step 4: Get JSON content from UI ----
					String jsonContext = DriverManager.getDriver().findElement(By.id("jsonContent")).getText();

					// Check all 14 Array Fields are present

					// Un comment later
					//JsonVerificationUtils.validateAllArrayFieldsPresent(jsonContext);

					//Check JSON contains the expected value
					if (processor.process(jsonContext, rowTime)) {
						matchFound = true;
						break;
					}
				}
			}
			if (!matchFound) {
				log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + map.get("ItemNumber") + "] did not have content that you are looking in any JSON file after " + testStartTime);
				Assertions.fail("ItemNumber [" + map.get("ItemNumber") + "] did not have content that you are looking in any JSON file after " + testStartTime);
			}

		} catch (Exception e) {
			throw new RuntimeException("Error during JSON verification", e);
		}

	}
	/* Export_Workflow_Feature */
	/* validate below fields in IPIM application  */

//			"GTIN",
//			"Class_Codes_ID",
//			"Dimensions",
//			"Descriptions", TC1
//			"Item_Category_ID",
//			"Global_Messages_ID",
//			"Non_Base-Catalog_to_Product_mapping",
//			"Item_Classifications",
//			"Product_References",
//			"Media", TC1
//			"Prices", TC1
//			"WebPrices",
//			"DivisionalPrices",
//			"Competitors"




	//ProductNotes JSON Verification US
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_DESCRIPTIONS_MEDIA_PRICES_Headers | Validate that field names (Product Notes, GEP Web Description, Item Media, List Price) from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Notes_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

		//Add Product Notes column & search item in master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"))
				.clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu()
				.selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton();

		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon()
				.clickHSISelectionOption()
				.clickSettingIcon()
				.clickFieldSelectionOption();

		//log(LogType.INFO, "Validating Product Note form Master Catalog Item Number");
		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();
		//Adding Product Notes & Language in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header"))
				.clickHeaderText(map.get("HeaderText"))
				.clickAddButton()
				.enterProductNotesLanguageValue(map.get("productNoteslanguage"))
				.clickOkButton();

		//--Product Notes
		// -- Get & Store Product Notes From Master Catalog: master_ProductNotes
		String master_ProductNotes=productDetailSearchPage.getDisplayedProductNotes();
		//BasePage.WaitForMiliSec(2000);
		log(LogType.EXTENTANDCONSOLE, "Master Catalog Product Notes for the  Item is: [" + master_ProductNotes + "]");


		//--Item Media
		// -- Get & Store all the media item images under Item Media Master Catalog: master_ProductNotes
		productDetailSearchPage.clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName"));
		qualityStatusPage.maximizeQualityStatusTab();
		List<String> master_all_media_Image_name = new ArrayList<String>();
		master_all_media_Image_name.addAll(itemMediatab.getAllImageName());
		log(LogType.EXTENTANDCONSOLE, " Master Catalog Item media image names: "+ master_all_media_Image_name );

		//-- List Price
		// -- Get & Store Price from Price tab
		productDetailSearchPage.selectTabfromDropdown(map.get("tabName2"));
		pricePage.sortPriceByValidFrom();
		String master_list_Price = pricePage.GetTheListPrice();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog List Price of Item is: [" + master_list_Price + "]");

		//-- GEP Web Description
		// -- Get & Store GEP Full Web Description from GEP Web Description tab
		productDetailSearchPage.selectTabfromDropdown(map.get("tabName3"));
		//pimHomepage.productDetailSearchPage().selectTabfromDropdown("GEP Web Description");
		String master_Full_Display_Description = gepWebDescPage.getFullDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Web Description for Item is: [" + master_Full_Display_Description + "]");



		pimHomepage.clickLogoutButton();

		// Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {
			//Validate Product Notes
			String jsonProductNotes = JsonVerificationUtils.getProductNotesFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonProductNotes != null && !jsonProductNotes.trim().isEmpty()) {
				//Assert json product notes with Master product notes
				Assertions.assertThat(jsonProductNotes).isEqualTo(master_ProductNotes);
				log(LogType.EXTENTANDCONSOLE, "Product_Notes Found on IPIM: [" + jsonProductNotes + "]");

				//return true;   // exits early if Product Notes found & validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Product_Notes in this record, checking next...");
				// no return here, so execution continues to media validation
			}

			//Validate Item Media
			List<String> jsonMediaIds = JsonVerificationUtils.getItemMediaFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonMediaIds != null && !jsonMediaIds.isEmpty()) {
				//Assert json all_media_Image_name with all Master media_Image_name
				Assertions.assertThat(jsonMediaIds).containsAll(master_all_media_Image_name);
				log(LogType.EXTENTANDCONSOLE, "Item Media Image Names Found on IPIM: " + jsonMediaIds);

				//return true;   // exits if media validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Item Media in this record, checking next...");
			}

			//Validate List Price
			String jsonListPrice = JsonVerificationUtils.getListPriceFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonListPrice != null && !jsonListPrice.isEmpty()) {
				//Assert json list price with all Master list price
				Assertions.assertThat(jsonListPrice).isEqualTo(master_list_Price);
				log(LogType.EXTENTANDCONSOLE, "List Price Found on IPIM[: " + jsonListPrice+ "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No List Price in this record, checking next...");
			}

			//Validate GEP Full Displayed Web Description
			String jsonGepFullDisplayedWebDesc = JsonVerificationUtils.getGEP_Full_Displayed_Web_DescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepFullDisplayedWebDesc != null && !jsonGepFullDisplayedWebDesc.isEmpty()) {
				//Assert json GEP Full Displayed Web Description with Master GEP Full Displayed Web Description
				Assertions.assertThat(jsonGepFullDisplayedWebDesc).isEqualTo(master_Full_Display_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Full Displayed Web Description Found on IPIM[: " + jsonGepFullDisplayedWebDesc+ "]");

				return true;   // exits if all & GEP Full Web Description validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Full Displayed Web Description in this record, checking next...");
			}

			return false; // only hits here if neither notes nor media matched
		});

	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_GTIN_Class COde ID_Headers | Validate that field names (GTIN,Class Codes ID) from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_GTINAndClassCodesID_In_IPIMJson_US(Map<String, String> map) throws InterruptedException {

		//Add GTIN column & search item in master
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"))
				.clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu()
				.selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton();


		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon()
				.clickHSISelectionOption()
				.clickSettingIcon()
				.clickFieldSelectionOption();

		//log(LogType.INFO, "Validating GTIN form Master Catalog Item Number");
		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();
		//Adding GTIN in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header"))
				.selectGTINField()
				.clickAddButton()
				.clickCloseButton()
				.clickOkButton();

		//--GTIN
		// -- Get & Store GTIN From Master Catalog: master_GTIN
		String master_GTIN=productDetailSearchPage.getDisplayedValue();
		//BasePage.WaitForMiliSec(2000);
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GTIN for the  Item is: [" + master_GTIN + "]");

		productDetailSearchPage.clickSettingIcon()
				.clickHSISelectionOption()
				.clickSettingIcon()
				.clickFieldSelectionOption();

		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();

		//AddingPrimary UOM	 in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header1"))
				.selectPricingUOMField()
				.clickAddButton()
				.clickCloseButton()
				.clickOkButton();

		//--UOM
		// -- Get & Store UOM From Master Catalog: master_UOM
		String master_UOM=productDetailSearchPage.getDisplayedValue();
		//BasePage.WaitForMiliSec(2000);
		log(LogType.EXTENTANDCONSOLE, "Master Catalog UOM for the  Item is: [" + master_UOM + "]");



		//-- Class Codes ID
		// -- Get & Store (Federal Drug Internal Class Code:) Class Codes ID From Master Catalog: master_Federal_Drug_Internal_Class_Code
		productDetailSearchPage.clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName"));

		String master_Federal_Drug_Internal_Class_Code = globalAttributePage.getFdac();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog Federal_Drug_Internal_Class_Code for the  Item is: [" + master_Federal_Drug_Internal_Class_Code + "]");

//	// -- Get Dimensions
//		productDetailSearchPage.selectTabfromDropdown(map.get("tabName2"));
//		List<String> master_all_dimensions = new ArrayList<String>();
//		master_all_dimensions.addAll(localAttributePage.getAllDimensions());
//		System.out.println("DIMENSIONS: "+master_all_dimensions);



		pimHomepage.clickLogoutButton();

		// Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {
			//Validate GTIN
			String jsonGTIN = JsonVerificationUtils.getGTINFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGTIN != null && !jsonGTIN.trim().isEmpty()) {
				//Assert json GTIN with Master GTIN
				Assertions.assertThat(jsonGTIN).isEqualTo(master_GTIN);
				log(LogType.EXTENTANDCONSOLE, "GTIN Found on IPIM: [" + jsonGTIN + "]");

				//return true;   // exits early if GTIN found & validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GTIN in this record, checking next...");
				// no return here, so execution continues to media validation
			}
			//Validate Class Codes ID
			String jsonFDClassCodeID = JsonVerificationUtils.getFederalDrugClassCodeFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonFDClassCodeID != null && !jsonFDClassCodeID.trim().isEmpty()) {
				//Assert json jsonFDClassCodeID with Master master_Federal_Drug_Internal_Class_Code
				Assertions.assertThat(jsonFDClassCodeID).isEqualTo(master_Federal_Drug_Internal_Class_Code);
				log(LogType.EXTENTANDCONSOLE, "FDRL_DRG_CLS_CD Found on IPIM: [" + jsonFDClassCodeID + "]");

				return true;   // exits early if FDRL_DRG_CLS_CD found & validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No FDRL_DRG_CLS_CD in this record, checking next...");
			}


			return false; // only hits here if neither GTIN nor FDRL_DRG_CLS_CD matched
		});

	}

	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_Dimensions_Headers | Validate that field names (Dimensions) from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Dimensions_iPIM_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"))
				.clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu()
				.selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton();
		productDetailSearchPage.clickOnFirstResult();


		//-- Dimensions
		// -- Get & Store (Brand, Quantity, item, itemType, Packaging Type) DimensionsFrom Master Catalog: master_GTIN

		productDetailSearchPage.selectTabfromDropdown(map.get("tabName"));
		List<String> master_all_dimensions = new ArrayList<String>();
		master_all_dimensions.addAll(localAttributePage.getAllDimensions());
		System.out.println("DIMENSIONS: "+master_all_dimensions);

		log(LogType.EXTENTANDCONSOLE, "Master Catalog Dimensions for the  Item is: " + master_all_dimensions );


		//--Item_Category_ID
		// -- Get & Store (GEP ECommerce Taxonomy and Structure (Consider Only Numeric Values: 3000-650-40))
		productDetailSearchPage.selectTabfromDropdown(map.get("tabName2"));
		String gepEcommercetaxonomy = classificationpage.getGepEcommerceTaxonomy("GEP ECommerce Taxonomy");
		System.out.println("gepEcommercetaxonomy: "+gepEcommercetaxonomy);







		pimHomepage.clickLogoutButton();

//		 Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {
			//Validate GTIN
			String jsonGTIN = JsonVerificationUtils.getGTINFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGTIN != null && !jsonGTIN.trim().isEmpty()) {
				//Assert json GTIN with Master GTIN
				Assertions.assertThat(jsonGTIN).isEqualTo(master_all_dimensions);
				log(LogType.EXTENTANDCONSOLE, "Dimensions Found on IPIM: [" + jsonGTIN + "]");

				return true;   // exits early if GTIN found & validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GTIN in this record, checking next...");
				// no return here, so execution continues to media validation
			}


			return false; // only hits here if neither notes nor media matched
		});

	}




	//ProductDescription JSON Verification US
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_DESCRIPTIONS_Headers | Validate that field names (Abbreviated Display Description, Detail Description, Search Description, Technical Description) from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Description_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"))
				.clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu()
				.selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton();

		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon()
				.clickHSISelectionOption()
				.clickSettingIcon()
				.clickFieldSelectionOption();

		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();
		//Adding Product Notes & Language in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header"))
				.clickHeaderText(map.get("HeaderText"))
				.clickAddButton()
				.clickOkButton();

		// -- Get & Store Product Description From Master Catalog: master_ProductDesc
		String master_ProductDesc=productDetailSearchPage.getDisplayedValue();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog Product Description for the  Item is: [" + master_ProductDesc + "]");


		//-- GEP Web Description
		// -- Get & Store GEP Abbreviated Web Description from GEP Web Description tab
		productDetailSearchPage.clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName3"));
		String master_Abbreviated_Display_Description = gepWebDescPage.getAbbreviatedDisplayDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Abbreviated Web Description for Item is: [" + master_Abbreviated_Display_Description + "]");

		// -- Get & Store GEP Search Description from GEP Web Description tab
		String master_Search_Description = gepWebDescPage.getSearchDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Search Description for Item is: [" + master_Search_Description + "]");

		// -- Get & Store GEP Detail Description from GEP Web Description tab
		String master_Detail_Description = gepWebDescPage.getDetailedDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Detail Description for Item is: [" + master_Detail_Description + "]");

		// -- Get & Store GEP Technical Description from GEP Web Description tab
		String master_Technical_Description = gepWebDescPage.getTechnicalDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Technical Description for Item is: [" + master_Technical_Description + "]");

		// -- Get & Store GEP Look Ahead Search Description from GEP Web Description tab
		String master_Look_Ahead_Search_Description = gepWebDescPage.getLookAheadSearchDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Look Ahead Search Description for Item is: [" + master_Look_Ahead_Search_Description + "]");

		// -- Get & Store GEP Extended Web Description from GEP Web Description tab
		String master_Extended_Web_Description = gepWebDescPage.getExtendedWebDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Extended Web Description for Item is: [" + master_Extended_Web_Description + "]");

		// -- Get & Store GEP Print Catalog Description from GEP Web Description tab
		String master_Print_Catalog_Description = gepWebDescPage.getPrintCatalogDescription();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog GEP Print Catalog Description for Item is: [" + master_Print_Catalog_Description + "]");

		pimHomepage.clickLogoutButton();

		// Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {
			//validate GEP Abbreviated Displayed Web Description
			String jsonGepAbbreviatedDisplayedWebDesc= JsonVerificationUtils.getGEP_Abbreviated_Displayed_Web_DescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepAbbreviatedDisplayedWebDesc != null && !jsonGepAbbreviatedDisplayedWebDesc.isEmpty()) {
				//Assert json GEP Abbreviated Displayed Web Description with Master GEP Abbreviated Displayed Web Description
				Assertions.assertThat(jsonGepAbbreviatedDisplayedWebDesc).isEqualTo(master_Abbreviated_Display_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Abbreviated Displayed Web Description Found on IPIM[: " + jsonGepAbbreviatedDisplayedWebDesc+ "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Abbreviated Displayed Web Description in this record, checking next...");
			}

			//validate GEP Product Description
			String jsonGepProductDesc= JsonVerificationUtils.getProductDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepProductDesc != null && !jsonGepProductDesc.isEmpty()) {
				//Assert json GEP Product Description with Master GEP Product Description
				Assertions.assertThat(jsonGepProductDesc).isEqualTo(master_ProductDesc);
				log(LogType.EXTENTANDCONSOLE, "GEP Product Description Found on IPIM[: " + jsonGepProductDesc+ "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Product Description in this record, checking next...");
			}

			//validate GEP Search Description
			String jsonSearchDesc= JsonVerificationUtils.getSearchDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonSearchDesc != null && !jsonSearchDesc.isEmpty()) {
				//Assert json GEP Look ahead Search Web Description with Master GEP Look ahead Search Description
				Assertions.assertThat(jsonSearchDesc).isEqualTo(master_Search_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Search Description Found on IPIM[: " + jsonSearchDesc+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Search Description in this record, checking next...");
			}

			//validate GEP Detail Description
			String jsonDetailDesc= JsonVerificationUtils.getDetailDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonDetailDesc != null && !jsonDetailDesc.isEmpty()) {
				//Assert json GEP Detail Web Description with Master GEP Detailed or extended Description
				Assertions.assertThat(jsonDetailDesc).isEqualTo(master_Detail_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Detail Description Found on IPIM[: " + jsonDetailDesc+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Detail Description in this record, checking next...");
			}

			//validate GEP Technical Description
			String jsonTechnicalDesc= JsonVerificationUtils.getTechnicalDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonTechnicalDesc != null && !jsonTechnicalDesc.isEmpty()) {
				//Assert json GEP Technical Web Description with Master GEP Technical Specification Description
				Assertions.assertThat(jsonTechnicalDesc).isEqualTo(master_Technical_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Technical Description Found on IPIM[: " + jsonTechnicalDesc+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Technical Description in this record, checking next...");
			}

			//validate GEP Look Ahead Search Description
			String jsonGepLookAheadSearchDesc= JsonVerificationUtils.getLookAheadSearchDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepLookAheadSearchDesc != null && !jsonGepLookAheadSearchDesc.isEmpty()) {
				//Assert json GEP Look ahead Search Web Description with Master GEP Look ahead Search Description
				Assertions.assertThat(jsonGepLookAheadSearchDesc).isEqualTo(master_Look_Ahead_Search_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Look Ahead Search Description Found on IPIM[: " + jsonGepLookAheadSearchDesc+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Look Ahead Search Description in this record, checking next...");
			}

			//validate GEP Print Catalog Description
			String jsonPrintCatalogDesc= JsonVerificationUtils.getPrintCatalogDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonPrintCatalogDesc != null && !jsonPrintCatalogDesc.isEmpty()) {
				//Assert json GEP Print Catalog Description with Master GEP Print Catalog Description
				Assertions.assertThat(jsonPrintCatalogDesc).isEqualTo(master_Print_Catalog_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Print Catalog Description Found on IPIM[: " + jsonPrintCatalogDesc+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Print Catalog Description in this record, checking next...");
			}

			//validate GEP Extended Web Description
			String jsonExtendedWebDesc= JsonVerificationUtils.getExtendedWebDescriptionFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonExtendedWebDesc != null && !jsonExtendedWebDesc.isEmpty()) {
				//Assert json GEP Extended Web Description with Master GEP Extended Web Description
				Assertions.assertThat(jsonExtendedWebDesc).isEqualTo(master_Extended_Web_Description);
				log(LogType.EXTENTANDCONSOLE, "GEP Extended Web Description Found on IPIM[: " + jsonExtendedWebDesc+ "]");
				return true;   // exits if all & GEP Description validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Extended Web Description in this record, checking next...");
			}

			return false; // only hits here if neither descriptions matched
		});

	}

	//ProductDescription JSON Verification US
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_MEDIA_Headers | Validate that field names (URL, Sequence, DocumentId) from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Media_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password"))
				.clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu()
				.selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("MasterCatalog"))
				.enterHsiItemNumber(map.get("ItemNumber"))
				.clickSeachButton();

		log(LogType.INFO, "Navigate to Field Selection Page from Setting Icon");

		productDetailSearchPage.clickSettingIcon()
				.clickHSISelectionOption()
				.clickSettingIcon()
				.clickFieldSelectionOption();

		fieldselectionpage.clearAllFieldsExceptItemNumandDesc();
		//Adding Product Notes & Language in Field Selection Page
		fieldselectionpage.enterFieldName(map.get("Header"))
				.clickHeaderText(map.get("HeaderText"))
				.clickAddButton()
				.clickOkButton();

		// -- Get & Store Product Description From Master Catalog: master_ProductDesc
		String master_ProductDesc=productDetailSearchPage.getDisplayedValue();

		productDetailSearchPage.clickOnFirstResult();
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("tabName3"));

		// -- Get & Store GEP URL from GEP Media tab
		String master_Filename = mediaTab.getFileNameFromMediaTab();
		String master_URL = master_Filename.substring(0,master_Filename.indexOf("."))
				+"_-"+master_ProductDesc.replace(' ', '-')
				+master_Filename.substring(master_Filename.indexOf("."));
		log(LogType.EXTENTANDCONSOLE, "Master Product URL for Item is: [" + master_URL + "]");

		// -- Get & Store GEP Media Mime_Type from GEP Media tab
		String master_MimeType = master_Filename.substring(master_Filename.indexOf("."));
		log(LogType.EXTENTANDCONSOLE, "Master Product Media MIME Type for Item is: [" + master_MimeType + "]");

		// -- Get & Store GEP Media Serialization from GEP Media tab
		String master_SerializationNumber = mediaTab.getSerializationValueFromMediaTab();
		log(LogType.EXTENTANDCONSOLE, "Master Product Serialization for Item is: [" + master_SerializationNumber + "]");

		// -- Get & Store GEP Media SHOT-type from GEP Media tab
		String master_MediaShotType = mediaTab.getShotTypeValueFromMediaTab();
		log(LogType.EXTENTANDCONSOLE, "Master Product Media Shot Type for Item is: [" + master_MediaShotType + "]");

	//	mediaTab.clickImageInfoIconFromMediaTab();//

		productDetailSearchPage.selectTabfromDropdown(map.get("tabName"));
		String master_HSIItemNumber = globalAttributePage.getHSI_Item_Number();
		log(LogType.EXTENTANDCONSOLE, "Master Product HSI Item number is: [" + master_HSIItemNumber + "]");

		pimHomepage.clickLogoutButton();

		// Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {

			//validate GEP Media URL
			List<String> jsonGepMediaURL= JsonVerificationUtils.getMediaURLFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepMediaURL != null && !jsonGepMediaURL.isEmpty()) {
				//Assert json GEP Product Media URL with Master GEP Product [Filename+AlternateText]
				Assertions.assertThat(jsonGepMediaURL).contains(master_URL);
				log(LogType.EXTENTANDCONSOLE, "GEP Product Media URL Found on IPIM : [ " + master_URL+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Product Media URL in this record, "+jsonGepMediaURL+" checking next...");
			}

			//validate GEP MIME-TYPE
			List<String> jsonGepMediaMimeType= JsonVerificationUtils.getMediaMimeTypFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepMediaMimeType != null && !jsonGepMediaMimeType.isEmpty()) {
				//Assert json GEP Product Media MIME-TYPE with Master GEP Product MIME-TYPE
				boolean found = false;
				for(String mime:jsonGepMediaMimeType){
					String actualMime = mime.substring(mime.lastIndexOf("/")+1);
					if(actualMime.equalsIgnoreCase(master_MimeType)){
						found = true;
						break;
					}
				}
				Assertions.assertThat(found);
				log(LogType.EXTENTANDCONSOLE, "GEP Product Media MIME_TYPE Found on IPIM: [ " + master_MimeType+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Product Media MIME-TYPE in this record, "+jsonGepMediaMimeType+" checking next...");
			}

			//validate GEP DocumentId
			List<String> jsonGepDocumentIDs= JsonVerificationUtils.getDocumentIDFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGepDocumentIDs != null && !jsonGepDocumentIDs.isEmpty()) {
				//Assert json GEP Media DocumentID with Master GEP Product HSI itemnumber
				Assertions.assertThat(jsonGepDocumentIDs).contains(master_HSIItemNumber);
				log(LogType.EXTENTANDCONSOLE, "GEP Product Document Found on IPIM: [ " + master_HSIItemNumber+ "]");
				return true;   // exits if all & attribues validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No GEP Product Document IDs in this record, "+jsonGepDocumentIDs+" checking next...");
			}
			return false; // only hits here if neither attributes matched
		});

	}
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "PIM_JSON_Verification_TC_PIM_JSON_07 | verify_all_catalog_tabs_and_exception_list_for_dental_division", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Json_Verification" }, dataProviderClass = DataProviderUtils.class)
	public void verify_items_media_files_US(Map<String, String> map) {

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




}