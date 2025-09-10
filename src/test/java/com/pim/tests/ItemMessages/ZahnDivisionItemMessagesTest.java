package com.pim.tests.ItemMessages;

import static com.pim.reports.FrameworkLogger.log;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pim.utils.ApplicationUtils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class ZahnDivisionItemMessagesTest extends BaseTest{
	ActionsPage action=new ActionsPage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	ClassificationsPage classificationpage=new ClassificationsPage();
	QualityStatusPage qualitystatuspage=new QualityStatusPage();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();
	String errorAndWarningmessage;
	Javautils javautil = new Javautils();
	List<String> item_code;
	Javautils javautils = new Javautils();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	ApplicationUtils appUtils = new ApplicationUtils();

	private ZahnDivisionItemMessagesTest(){}


	//ITM_022
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_022 | Verifying Item Error Message for Zahn Division US, and Master Catalog",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemErrorMessageForZahnDivision(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		List<String> messages = javautil.readMultipleValuesFromExcel(map.get("ErrorMessage"));
		List<String> structurePaths = javautil.readMultipleValuesFromExcel(map.get("ClassificationItem"));
		SoftAssert softAssert = new SoftAssert();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();

		int index = 0;
		boolean assigedflag = false;
		for(int i=0;i<messages.size();i++){
			if(classificationpage.isMessageAdded(map.get("TaxnomyType"),structurePaths.get(i))){
				classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),structurePaths.get(i));
				appUtils.updateMessageInCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is deleted");
				continue;
			}else if(!assigedflag){
				action.clickOnActionsDropdown();
				action.clickOnClassifyItem();
				structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
				structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
				selectDivisionErrorOrWarningMessage(messages.get(i));
				action.clickOnRadioCopyButton();
				action.clickOnOkButton();
				productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
				softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(i).toUpperCase()));
				index = i;
				assigedflag = true;
				appUtils.updateMessageInCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is updated");
			}
		}
		//classificationpage.clickOnCloseExpandButton();


		//verify quality status 
		log(LogType.INFO,"Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();

		//Verify whether the user is able to view the newly added Item message in PIM Web UI

		/*
		 * log(LogType.INFO,
		 * "Verify user is able to view the newly added Item message in PIM Web UI");
		 * pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
		 * "CatalogType"))
		 * .selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
		 * "ItemType"));
		 * structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		 * structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		 * classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),
		 * messages.get(index)); item_code = classificationpage.getHSIItemCodeList();
		 * Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
		 * log(LogType.INFO,
		 * "Verify user is able to view the newly added Item message in PIM Web UI");
		 */

		//Verify Error msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),messages.get(index).toUpperCase()));
		pimHomepage.clickLogoutButton();
		log(LogType.INFO, "Verify Error Messages in Master");
	}
	
	//ITM_032
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_032 | Verifying Error Global Message for Zahn Item US, and Master Catalog",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyErrorGlobalMessageForZahnItem(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify delete Error message
		log(LogType.INFO, "Verifying Delete Error Global Messages for Zahn Item");
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();


		//Verify Error msg in US catalog
		log(LogType.INFO, "Verifying Assign Error Global Messages for Zahn Item");
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton();
		action.clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));


		//verify quality status 
		log(LogType.INFO,"Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();

		//Verify whether the user is able to view the newly added Item message in PIM Web UI
		log(LogType.INFO, "Verify user is able to view the newly added Item message in PIM Web UI");
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),map.get("ErrorMessage"));
		item_code = classificationpage.getHSIItemCodeList();
		Assertions.assertThat(item_code).contains(map.get("ItemNumber"));


		//Verify Error msg in master catalog
		log(LogType.INFO, "Verify Error Global Messages in Master for Zahn Item");
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));
		pimHomepage.clickLogoutButton();

	}
	//ITM_037
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_037 | Verifying Warning Global Message for Zahn Item US, and Master Catalog", groups = {"US","pim","ITEM_MESSAGES"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyWarningGlobalMessageForZahnItem(Map<String, String> map) throws IOException, InterruptedException, AWTException  

	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify Delete Warning message
		log(LogType.INFO, "Verifying Delete Global Warning Messages For Zahn Item");
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();


		//Verify Assigned Warning msg in US catalog
		log(LogType.INFO, "Verifying Assign Global Warning Messages For Zahn Item");
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton();
		action.clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));

		//verify quality status
		log(LogType.INFO,"Verifying catalog entity rule");
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();



		//Verify whether the user is able to view the newly added Item message in PIM Web UI
		log(LogType.INFO, "Verify user is able to view the newly added Item message in PIM Web UI");
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),map.get("ErrorMessage"));
		item_code = classificationpage.getHSIItemCodeList();
		Assertions.assertThat(item_code).contains(map.get("ItemNumber"));

		//Verify Warning msg in Master catalog
		log(LogType.INFO, "Verify Global Warnning Messages in Master for Zahn Item");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));
		pimHomepage.clickLogoutButton();


	}

	//TestCaseID--N/A
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "TCID | Verifying Item Warning Message for Zahn Division US, and Master Catalog",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyItemWarningMessageForZahnDivision(Map<String, String> map) throws IOException, InterruptedException, AWTException  
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		List<String> messages = javautil.readMultipleValuesFromExcel(map.get("ErrorMessage"));
		List<String> structurePaths = javautil.readMultipleValuesFromExcel(map.get("ClassificationItem"));
		SoftAssert softAssert = new SoftAssert();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();

		int index = 0;
		boolean assigedflag = false;
		for(int i=0;i<messages.size();i++){
			if(classificationpage.isMessageAdded(map.get("TaxnomyType"),structurePaths.get(i))){
				classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),structurePaths.get(i));
				appUtils.updateMessageInCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is deleted");
				continue;
			}else if(!assigedflag){
				action.clickOnActionsDropdown();
				action.clickOnClassifyItem();
				structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
				structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
				selectDivisionErrorOrWarningMessage(messages.get(i));
				action.clickOnRadioCopyButton();
				action.clickOnOkButton();
				productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
				softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(i).toUpperCase()));
				index = i;
				assigedflag = true;
				appUtils.updateMessageInCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is updated");
			}
		}
		//classificationpage.clickOnCloseExpandButton();



		//verify quality status 
		log(LogType.INFO,"Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		//Verify whether the user is able to view the newly added Item message in PIM Web UI

		log(LogType.INFO, "Verify user is able to view the newly added Item message in PIM Web UI");
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"));
		structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"), messages.get(index));
		item_code = classificationpage.getHSIItemCodeList();
		Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
	

		//Verify Warning msg in Master catalog
		log(LogType.INFO, "Verify Warnning Messages in Master");
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),
				messages.get(index).toUpperCase()));
		pimHomepage.clickLogoutButton();

	}
	//ITM_042
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_042 | Verifying Add Zahn Item Error Message for multiple item code in US, and Master Catalog - ITM_042", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"US","pim","ITEM_MESSAGES"})
	public void verifyAddItemErrorMessageForMultipleItemCodeInZahnDivision(Map<String, String> map) throws IOException, InterruptedException, AWTException  
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify Delete Error message
		log(LogType.INFO, "Verifying Delete Error Messages for Multiple Item Code");
		List<String> itemCodeList = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));

		// search all itemcodes on left panel
		for (int i = 0; i <=itemCodeList.size()-1; i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		basePage.WaitForMiliSec(3000);
		basePage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList(); 
		basePage.releaseCtrlKey();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();

		//Verify Assigned Error msg in US catalog
		log(LogType.INFO, "Verify assign Error Messages for Multiple Item Code");
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get(
				"CatalogType"));
		queriesSubMenu.clickSeachButton();
		basePage.WaitForMiliSec(3000);
		basePage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList(); 
		basePage.releaseCtrlKey();
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get(
				"TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get(
				"DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton().clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.
				verifyItemErrorMessageforAllDivisions(map.get(
						"TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.
						get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage")
						.toUpperCase()));
		log(LogType.INFO, "Verify assigned Error Messages for Multiple Item Code");

		log(LogType.INFO,"Verifying Quality Status rules");

		for(int i = 0; i < itemCodeList.size(); i++) {
			pimHomepage.mainMenu().clickRefreshMenuIcon();
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"));
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"));
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
			structuresubmenu.clickOnFirstItemCode();
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
					.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
			}
			qualitystatus.minimizeQualityStatusTab();
		}


		//Verify whether the user is able to view the newly added Item message in PIM
		//Web UI
		log(LogType.INFO, "Verify user is able to view the newly added Item message in PIM Web UI for Multiple Item Code");
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
				"CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
				"ItemType"));
		structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
		structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
		classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),map.get(
				"ErrorMessage"));
		item_code = classificationpage.getHSIItemCodeList();
		System.out.println(map.get("ItemNumber"));
		// search all itemcodes on left panel
		for (int i = 0; i < item_code.size(); i++)
		{
			Assertions.assertThat(item_code).contains(item_code.get(i));
		}

		//Verify Error msg in Master catalog
		log(LogType.INFO, "Verify Error Messages in Master for Multiple item code");
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get(
				"MasterCatalog"));
		for (int i = 0; i <itemCodeList.size(); i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		basePage.WaitForMiliSec(3000);
		productDetailSearchPage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList();
		productDetailSearchPage.releaseCtrlKey();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.
				verifyItemErrorMessageforAllDivisions(map.get(
						"TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.
						get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage")
						.toUpperCase())); pimHomepage.clickLogoutButton();
	}


	//WorkFlow_025
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "WRFL_025 | Verify CatalogEntityRules are executed for ZahnPrivilege User", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"US","pim","ITEM_MESSAGES"})
	public void verifyCatalogEntityRulesAreExecutedForZahnUser(Map<String, String> map)
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify Deleted zahn divisions message code
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();

		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		errorAndWarningmessage = classificationpage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.removeButton();
		classificationpage.clickYes();
		Assertions.assertThat(errorAndWarningmessage).isNotEqualTo(map.get("ErrorMessage"));
		classificationpage.clickOnCloseExpandButton();

		//Verify Assigned zahn divisions message code
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton();
		action.clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));

		//Verify Catalog Entity Rules in Quality Status for an assigned message code
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatuspage.selectRuleTypeFromDropDown(map.get("Qualitystatus"));
		qualitystatuspage.sortRule();
		List<String> catalogentityrule = qualitystatuspage.getRuleNamesByRuleStatus(map.get("ruleStatus"));
		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		String znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"), map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));

		//Verify assigned zahn divisions msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage").toUpperCase()));
		pimHomepage.clickLogoutButton();

	}


	//ItemMessages_032
		@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.Valid_Failure)
		@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
		@Test(description = "ITM_032 | verify GlobalErrorMessage Global_002 gets assigned to zahn item using user CAEnglishDS",groups = {"CA","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
		public void verifyGlobalErrorMessageGetsAssignedToZahnItem(Map<String, String> map) {
			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

			//Verify Deleted global division error message code
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
			.selectCatalogType(map.get("CatalogType"))
			.enterHsiItemNumber(map.get("ItemNumber"));
			queriesSubMenu.clickSeachButton();
			structuresubmenu.clickOnFirstItemCode();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			classificationpage.clickOnExpandButton();
			classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
			classificationpage.clickOnCloseExpandButton();

	        //Verify Assigned global division error message code
			classificationpage.clickOnExpandButton();
			action.clickOnActionsDropdown();
			action.clickOnClassifyItem();
			structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
			structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
					selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
			action.clickOnRadioCopyButton();
			action.clickOnOkButton();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), map.get("ErrorMessage").toUpperCase()));
			classificationpage.clickOnCloseExpandButton();

			//verify quality status
			log(LogType.INFO,"Verifying catalog entity rule");
			qualitystatus.maximizeQualityStatusTab();
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			List<String> catalogEntityRule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
					.sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			{
				Assertions.assertThat(catalogEntityRule).containsAnyOf(Rules);
			}
			qualitystatus.minimizeQualityStatusTab();
			log(LogType.INFO,"Verifying catalog entity rule");


			//Verify assigned dental division msg in Master catalog
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
			queriesSubMenu.clickSeachButton();
			structuresubmenu.clickOnFirstItemCode();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), map.get("ErrorMessage").toUpperCase()));
			pimHomepage.clickLogoutButton();

		}

		//ItemMessages_037
		@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.Valid_Failure)
		@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
		@Test(description = "ITM_037 | verify GlobalWarningMessage Global_01R gets assigned to zahn item using using CAEnglishDS",groups = {"CA","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
		public void verifyGlobalWarningMessageGetsAssignedToZahnItem(Map<String, String> map) {
			PimHomepage pimHomepage = new LoginPage()
					.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
					.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

			//Verify Deleted global division warning message code
			pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
					.selectCatalogType(map.get("CatalogType"))
					.enterHsiItemNumber(map.get("ItemNumber"));
			queriesSubMenu.clickSeachButton();
			structuresubmenu.clickOnFirstItemCode();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			classificationpage.clickOnExpandButton();
			classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
			classificationpage.clickOnCloseExpandButton();

			//Verify Assigned global division warning message code
			action.clickOnActionsDropdown();
			action.clickOnClassifyItem();
			structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
			structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
			selectDivisionErrorOrWarningMessage(map.get("WarningMessage"));
			action.clickOnRadioCopyButton();
			action.clickOnOkButton();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), map.get("WarningMessage").toUpperCase()));

			//Verify Catalog Entity Rules in Quality Status for an assigned message code
			qualitystatus.maximizeQualityStatusTab();
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"));
			qualitystatus.sortRule();
			List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
			qualitystatus.minimizeQualityStatusTab();
			Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));

			//Verify assigned dental division msg in Master catalog
			pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
			queriesSubMenu.clickSeachButton();
			structuresubmenu.clickOnFirstItemCode();
			productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
			softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), map.get("WarningMessage").toUpperCase()));
			pimHomepage.clickLogoutButton();

		}

}


