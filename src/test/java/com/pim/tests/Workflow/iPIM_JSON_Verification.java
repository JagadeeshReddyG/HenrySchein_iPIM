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
import java.util.stream.Collectors;

import static com.pim.reports.FrameworkLogger.log;


public class iPIM_JSON_Verification extends BaseTest {
	private iPIM_JSON_Verification() {

	}
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	ItemMediaTab itemMediatab = new ItemMediaTab();
	PricePage pricePage = new PricePage();
	GEP_WebDescriptionPage gepWebDescPage = new GEP_WebDescriptionPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	LocalAttributePage localAttributePage = new LocalAttributePage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();



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

			BasePage.WaitForMiliSec(2000);
			// ---- Step 2: Launch IPIM and search item ----
			BaseTest.launchIPIM();
			BasePage.WaitForMiliSec(2000);

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




	//ProductNotes,Item Media,List Price,GEP Web Description
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

	//GTIN(GTIN,UOM),ClassCodesID (Class Code Id,Type)
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
			//--Validate GTIN
			String jsonGTIN = JsonVerificationUtils.getGTINFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGTIN != null && !jsonGTIN.trim().isEmpty()) {
				//Assert json GTIN with Master GTIN
				Assertions.assertThat(jsonGTIN).isEqualTo(master_GTIN);
				log(LogType.EXTENTANDCONSOLE, "GTIN Found on IPIM: [" + jsonGTIN + "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No GTIN in this record, checking next...");
				// no return here, so execution continues to media validation
			}

			//--Validate UOM value from GTIN array
			String jsonUOM = JsonVerificationUtils.getGTINUOMFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonUOM != null && !jsonUOM.trim().isEmpty()) {
				//Assert json jsonUOM with Master jsonUOM
				Assertions.assertThat(jsonUOM).isEqualTo(master_UOM);
				log(LogType.EXTENTANDCONSOLE, "UOM Found on IPIM: [" + jsonUOM + "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No UOM in this record, checking next...");
				// no return here, so execution continues to Class Codes ID validation
			}

			//--Validate Class Codes ID
			String jsonFDClassCodeID = JsonVerificationUtils.getFederalDrugClassCodeFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonFDClassCodeID != null && !jsonFDClassCodeID.trim().isEmpty()) {
				//Assert json jsonFDClassCodeID with Master master_Federal_Drug_Internal_Class_Code
				Assertions.assertThat(jsonFDClassCodeID).isEqualTo(master_Federal_Drug_Internal_Class_Code);
				log(LogType.EXTENTANDCONSOLE, "FDRL_DRG_CLS_CD Found on IPIM: [" + jsonFDClassCodeID + "]");

				return true;   // exits if FDRL_DRG_CLS_CD found & validated
			} else {
				log(LogType.EXTENTANDCONSOLE, "No FDRL_DRG_CLS_CD in this record, checking next...");
			}


			return false; // only hits here if neither GTIN nor FDRL_DRG_CLS_CD matched
		});

	}

	//Dimensions (Dimension Class Name,Dimension Name,Dimension Value),Item_Category_ID,Global_Message_ID,Non Base-Catalog to Product Mapping from Master Catalog,Item Classification
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "iPIM_Dimensions_Headers | Validate that field names Dimensions ,Item_Category_ID,Global_Message_ID,Non Base-Catalog,Item Classifications to Product Mapping from Master Catalog are correctly reflected in iPIM JSON for US region", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Dimensions_ItemCategoryID_GlobalMessageID_NonBaseCatalogToProductMapping_ItemClassification_iPIM_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

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
		// -- Get & Store (Brand, Quantity, item, itemType, Packaging Type) Dimensions From Master Catalog: master_all_dimensionNames

		productDetailSearchPage.selectTabfromDropdown(map.get("tabName"));
		List<String> master_all_dimensionNames = new ArrayList<String>();
		List<String> master_all_dimensionValues = new ArrayList<String>();
		master_all_dimensionNames.addAll(localAttributePage.getAllDimensionsNames());
		master_all_dimensionValues.addAll(localAttributePage.getAllDimensionsValues());

		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Dimensions Names for the  Item is: " + master_all_dimensionNames );
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Dimensions Values for the  Item is: " + master_all_dimensionValues );


		//--Dimension Class Name
		// -- Get & Store (Primary Taxonomy and Structure (Consider Only Numeric Values: 005-07-30-01))
		productDetailSearchPage.selectTabfromDropdown(map.get("tabName2"));
		String master_DimensionClassName = classificationpage.getFormattedPrimaryTaxonomy("Primary Taxonomy");
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Dimensions Class Name for the  Item is: [" + master_DimensionClassName +"]" );


		//--Item_Category_ID
		// -- Get & Store (GEP ECommerce Taxonomy and Structure (Consider Only Numeric Values: 3000-650-40))
		String master_Item_Category_ID = classificationpage.getGepEcommerceTaxonomy("GEP ECommerce Taxonomy");
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Item_Category_ID for the  Item is: [" + master_Item_Category_ID+"]" );


		//--Global_Messages_ID
		// -- Get & Store (zNew Global Message and Structure (Consider Only Numeric Values: MEDICAL_OTP))
		String master_Global_Messages_ID = classificationpage.getFormattedZNewGlobalMessage("zNew Global Message");
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Global_Messages_ID for the  Item is: [" + master_Global_Messages_ID+"]" );

		//--Item Classifications
		// -- Get & Store (UNSPSC  Taxonomy and Structure (Consider Only Numeric Values: 3000-650-40))
		String master_ClassificationCatID = classificationpage.getFormattedUNSPSCTaxonomy("UNSPSC Taxonomy");
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Classification Cat ID for the  Item is: [" + master_ClassificationCatID+"]" );
		String master_ClassificationAttributeName = classificationpage.getUNSPSCTaxonomy("UNSPSC Taxonomy");
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Classification Attribute Name for the  Item is: [" + master_ClassificationAttributeName+"]" );


		//--Non Base-Catalog to Product mapping
		// -- Get & Store (Attribute values under All Catalog Tab)
		productDetailSearchPage.selectTabfromDropdown(map.get("tabName3"));
		List <String> masterMedicalCatalogList =allCatalogsPage.getAllMedicalCatalogList();
		log(LogType.EXTENTANDCONSOLE, "Master Catalog- Catalog IDs for the  Item is: " + masterMedicalCatalogList );



		pimHomepage.clickLogoutButton();

//		 Reuse common method
		verifyJsonForItem(map, (jsonContext, rowTime) -> {
			//-- Validate Dimensions Names & Values

			Map<String, String> jsonDimensions = JsonVerificationUtils.getDimensionsFromIPIM_Json(jsonContext, map.get("ItemNumber"), master_all_dimensionNames);
			if (jsonDimensions != null && !jsonDimensions.isEmpty()) {
				// Extract Dimension Names & Values
				List<String> jsonDimensionNames = new ArrayList<>(jsonDimensions.keySet());
				List<String> jsonDimensionValues = new ArrayList<>(jsonDimensions.values());
				// Validate names
				Assertions.assertThat(jsonDimensionNames).containsExactlyInAnyOrderElementsOf(master_all_dimensionNames);
				log(LogType.EXTENTANDCONSOLE, "Dimension Names found on IPIM:" + jsonDimensionNames);

				// Validate values
				Assertions.assertThat(jsonDimensionValues).containsAll(master_all_dimensionValues);
				log(LogType.EXTENTANDCONSOLE, "Dimension Values found on IPIM: " + jsonDimensionValues+ "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Dimensions found in this record, checking next...");
			}

			//-- --Dimension Class Name
			String jsonDimension_Class_Name = JsonVerificationUtils.getFirstDimensionClassNameFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonDimension_Class_Name != null && !jsonDimension_Class_Name.trim().isEmpty()) {
				//Assert json Dimension_Class_Name with Master Dimension_Class_Name
				Assertions.assertThat(jsonDimension_Class_Name).isEqualTo(master_DimensionClassName);
				log(LogType.EXTENTANDCONSOLE, "Dimension_Class_Name found on IPIM: [" + jsonDimension_Class_Name + "]");
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Dimension_Class_Name in this record, checking next...");
			}
			//-- Validate Extra Headers (Sequence, Sequence_Value, Language_ISO_Code)
			List<String> jsonExtraHeaders = JsonVerificationUtils.getDimensionExtraHeadersFromIPIM_Json(jsonContext, map.get("ItemNumber"));

			if (!jsonExtraHeaders.isEmpty()) {
				Assertions.assertThat(jsonExtraHeaders).containsExactlyInAnyOrder("Sequence", "Sequence_Value", "Language_ISO_Code");
				log(LogType.EXTENTANDCONSOLE, "Extra Dimension headers found on IPIM: " + jsonExtraHeaders);
			} else {
				log(LogType.EXTENTANDCONSOLE, "No extra headers (Sequence, Sequence_Value, Language_ISO_Code) found in this record.");
			}

			//-- Validate Item_Category_ID
			String jsonItem_Category_ID = JsonVerificationUtils.getItemCategoryIDFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonItem_Category_ID != null && !jsonItem_Category_ID.trim().isEmpty()) {
				//Assert json Item_Category_ID with Master Item_Category_ID
				Assertions.assertThat(jsonItem_Category_ID).isEqualTo(master_Item_Category_ID);
				log(LogType.EXTENTANDCONSOLE, "Item_Category_ID found on IPIM: [" + jsonItem_Category_ID + "]");

				//return true;
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Item_Category_ID in this record, checking next...");
			}

			//-- Validate Global_Message_ID
			String jsonGlobal_Message_ID = JsonVerificationUtils.getGlobalMessageIDFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonGlobal_Message_ID != null && !jsonGlobal_Message_ID.trim().isEmpty()) {
				//Assert json jsonGlobal_Message_ID with Master Global_Messages_ID
				Assertions.assertThat(jsonGlobal_Message_ID).contains(master_Global_Messages_ID);
				log(LogType.EXTENTANDCONSOLE, "Global_Message_ID found on IPIM: [" + jsonGlobal_Message_ID + "]");

			} else {
				log(LogType.EXTENTANDCONSOLE, "No Global_Message_ID in this record, checking next...");
			}
			//-- Validate Non Base-Catalog to Product Mapping
			String jsonCatalogIDs = JsonVerificationUtils.getCatalogIdsFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonCatalogIDs != null && !jsonCatalogIDs.trim().isEmpty()) {
				//Assert json jsonCatalogIDs with Master masterMedicalCatalogList
				Assertions.assertThat(jsonCatalogIDs).contains(masterMedicalCatalogList);
				log(LogType.EXTENTANDCONSOLE, "Non Base-Catalog to Product Mapping found on IPIM: [" + jsonCatalogIDs + "]");
				//return true;
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Non Base-Catalog to Product Mapping in this record, checking next...");
			}
			//-- Validate Item Classifications
			List<Map<String, Object>> jsonItemClassifications = JsonVerificationUtils.getItemClassificationsFromIPIM_Json(jsonContext, map.get("ItemNumber"));
			if (jsonItemClassifications != null && !jsonItemClassifications.isEmpty()) {
				List<String> classificationList = new ArrayList<>();
				for (Map<String, Object> classification : jsonItemClassifications) {
					String attributeName = (String) classification.get("Classification_Attribute_Name");
					String catId = (String) classification.get("Classification_Cat_Id");

					// Add to list for assertion
					if (attributeName != null && catId != null)
					{
						classificationList.add(attributeName + ":" + catId);
					}

					// Assert language_ISO_code is present and not null
					Assertions.assertThat(classification).containsKey("language_ISO_code");
					//Assertions.assertThat(classification.get("language_ISO_code")).isNotNull();
				}

				// Assert master Classification AttributeName:CatId is present
				Assertions.assertThat(classificationList).contains(master_ClassificationAttributeName + ":" + master_ClassificationCatID);
				log(LogType.EXTENTANDCONSOLE, "Item Classifications found on IPIM: " + classificationList);
				return true;
			} else {
				log(LogType.EXTENTANDCONSOLE, "No Item Classifications found in this record, checking next...");
			}


			return false; // only hits here if Dimensions,Dimension Class Name,Item_Category_ID,Global Message ID nor Non Base-Catalog to Product Mapping matched
		});

	}



	//Media(Media Id)
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
					break; // Exit loop once matching itemNumber is found
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