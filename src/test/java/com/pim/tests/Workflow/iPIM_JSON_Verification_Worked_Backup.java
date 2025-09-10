package com.pim.tests.Workflow;

import com.ipim.page.iPimMainPage;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;


public class iPIM_JSON_Verification_Worked_Backup extends BaseTest {
	private iPIM_JSON_Verification_Worked_Backup() {

	}
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	FieldSelectionPage fieldselectionpage = new FieldSelectionPage();


	//ProductNotes JSON Verification US
	@PimFrameworkAnnotation(module = Modules.JSON_Verification_PIM, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Json_Verification)
	@Test(description = "JSON_Verification_TC_PIM_JSON_ | verify_productNotes_US_in_JSON", dataProvider = "getCatalogData", groups = {
			"REGRESSION", "US", "Prices" }, dataProviderClass = DataProviderUtils.class)
	public void verify_Product_Notes_JSON_Verification_US(Map<String, String> map) throws InterruptedException {

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

		pimHomepage.clickLogoutButton();
		// Close the PIM browser session
		Driver.quitDriver();
		//int time = FileUtils.calculateTimeDifference(map.get("TestCaseName") + map.get("ItemNumber"));

		//---------------------


		// Build the key like you do in calculateTimeDifference
		String key = map.get("TestCaseName") + map.get("ItemNumber");

		// Get stored timestamp string from properties file
		String expectedTimeStamp = FileUtils.getSavedTimestamp(key);


		// ---- Step 1: Get timestamp from properties file ----
		//String expectedTimeStamp =("2025-08-11 15:54:04");
		DateTimeFormatter propFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime testStartTime = LocalDateTime.parse(expectedTimeStamp, propFormatter);
		log(LogType.EXTENTANDCONSOLE, "Property timestamp: " + testStartTime);
		System.out.println("testStartTime: "+testStartTime);

		// ---- Step 2: Launch IPIM and search item ----
		BaseTest.launchIPIM();
		System.out.println("IPIM has Launched");
		log(LogType.INFO, "iPIM Application Launched");
		iPimMainPage iPimMainPage = new iPimMainPage();
		iPimMainPage.clickUsFlag().enterItemCodeNumber(map.get("ItemNumber")).clickSearchBtn();
		List<WebElement> records=iPimMainPage.getListOfRecords();
		DateTimeFormatter uiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		List<String> ProductNotesInJson = new ArrayList<>();
		boolean matchFound = false;

		// ---- Step 3: Iterate through search results ----
		for (WebElement row : records) {
			String rowTimeText = row.getText().trim();
			LocalDateTime rowTime = LocalDateTime.parse(rowTimeText, uiFormatter);

			// Uncomment if wanted to log all ipim record timestamps
			//log(LogType.EXTENTANDCONSOLE, "Row timestamp: " + rowTime);

			// Only consider rows >= property timestamp
			if (!rowTime.isBefore(testStartTime)) {
				log(LogType.EXTENTANDCONSOLE, "Checking row with timestamp >= property timestamp: " + rowTime);

				// Click row
				row.click();
				BasePage.WaitForMiliSec(2000);

				// Switch to Raw Tab
				iPimMainPage.clickRawTab();
				BasePage.WaitForMiliSec(2000);


				// ---- Step 4: Get JSON content from UI ----
				String jsonContext = DriverManager.getDriver().findElement(By.id("jsonContent")).getText();

				JsonVerificationUtils.validateAllArrayFieldsPresent(jsonContext);
				// ---- Step 5: Parse JSON and extract Product Notes ----
				try {
					String Json_Product_Notes = JsonVerificationUtils.getProductNotesFromIPIM_Json(jsonContext, map.get("ItemNumber"));

					if (Json_Product_Notes != null && !Json_Product_Notes.trim().isEmpty()) {
						log(LogType.EXTENTANDCONSOLE, "Product_Notes Found on IPIM: [" + Json_Product_Notes+"]");
						ProductNotesInJson.add(Json_Product_Notes);
						matchFound = true;
						break; // Stop after first successful match
					} else {
						log(LogType.EXTENTANDCONSOLE,
								"No Product_Notes found in this record. Continuing with next eligible record...");
					}
				} catch (Exception e) {
					log(LogType.EXTENTANDCONSOLE, "Error parsing JSON content from row: " + rowTime);
					e.printStackTrace();
				}
			}
		}
		// ---- Step 6: Final assertion ----
		if (!matchFound) {
			log(LogType.EXTENTANDCONSOLE, "ItemNumber [" + map.get("ItemNumber") + "] did not have Product_Notes in any JSON file after " + testStartTime);
			Assertions.fail("ItemNumber [" + map.get("ItemNumber") + "] did not have Product_Notes in any JSON file after " + testStartTime);
		}



//		//---------------------
//
//		log(LogType.EXTENTANDCONSOLE, "ProductNotesInJsonFileoniPIMUI : " + ProductNotesInJson);
//
//		// Uncomment if you want to compare with Master Catalog Product Notes
		Assertions.assertThat(ProductNotesInJson.contains(master_ProductNotes));

//		Thread.sleep(5000);



	}

}