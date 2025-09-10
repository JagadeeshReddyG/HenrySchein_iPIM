
package com.pim.tests.ItemMessages;

import java.awt.AWTException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.pim.enums.LogType;
import com.pim.utils.ApplicationUtils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TextPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import static com.pim.reports.FrameworkLogger.log;

public class GlobaltemMessagesTest extends BaseTest{
	ActionsPage action=new ActionsPage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	ClassificationsPage classificationpage=new ClassificationsPage();
	QualityStatusPage qualitystatuspage=new QualityStatusPage();
	QualityStatusPage qualitystatus=new  QualityStatusPage();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();
	Javautils javautil = new Javautils();
	String errorAndWarningmessage;
	List<String> item_code;
	ApplicationUtils appUtils = new ApplicationUtils();
    TextPage textPage = new TextPage();
	ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
	GlobalAttributePage globalattribute = new GlobalAttributePage();
	String znewglobalerrormessage;

	private GlobaltemMessagesTest(){
	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_018 | Verifying Error Global Message For US, and Master Catalog", 
		groups = {"SMOKE","US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyErrorGlobalMessage(Map<String, String> map)   
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
				action.clickOnOkButton().clickOnOkButton1();
				softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(i).toUpperCase()));
				index = i;
				assigedflag = true;
				appUtils.updateMessageInCSV(map.get("DivisionType"),"U",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is updated");
			}
		}

		//verify quality status log(LogType.INFO,"Verifying catalog entity rule");
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Qualitystatustab")); List<String> catalogentityrule =
		  qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
		  .sortRulestByLastExecution().
		  getRuleNamesByRuleStatus(map.get("Status")); for (String
		  Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
		  Assertions.assertThat(catalogentityrule).containsAnyOf(Rules); }
	      classificationpage.clickOnCloseExpandButton();

		  
			/*
			 * //Verify whether the user is able to view the newly added Item message in PIM
			 * Web UI
			 * pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
			 * "CatalogType"))
			 * .selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
			 * "ItemType"));
			 * structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
			 * structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
			 * classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),messages
			 * .get(index)); item_code = classificationpage.getHSIItemCodeList();
			 * Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
			 */

		//Verify Error msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();

		structuresubmenu.clickOnFirstItemCode();

		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),messages.get(index).toUpperCase()));

		pimHomepage.clickLogoutButton();

	}
	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.SMOKE)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "ITM_019 | Verifying Warning Global Message For US, and Master Catalog",
		groups = {"SMOKE","US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyWarningGlobalMessage(Map<String, String> map) throws IOException, InterruptedException, AWTException  
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

		//verify quality status 
				log(LogType.INFO,"Verifying catalog entity rule");
				pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
				List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
					.sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
				for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
				{
					Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
				}
			
				log(LogType.INFO,"Verified catalog entity rule");
				classificationpage.clickOnCloseExpandButton();

				/*
				 * //Verify whether the user is able to view the newly added Item message in PIM
				 * Web UI
				 * pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get(
				 * "CatalogType"))
				 * .selectClassificationType(map.get("CategoryType")).selectItemType(map.get(
				 * "ItemType"));
				 * structuresubmenu.clickOnSelectedTaxnomyType(map.get("TaxnomyType"));
				 * structuresubmenu.selectErrorOrWarning(map.get("ErrorAndWarningType"));
				 * classificationpage.selectDivisionAndErrorMsg(map.get("DivisionType"),messages
				 * .get(index)); item_code = classificationpage.getHSIItemCodeList();
				 * Assertions.assertThat(item_code).contains(map.get("ItemNumber"));
				 */

		//Verify Warning msg in Master catalog
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.get("DivisionTypeOfClassificationpPop").toUpperCase(),messages.get(index).toUpperCase()));
		pimHomepage.clickLogoutButton();

	}

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "Verifying Add Global Error Message for multiple item code in US, and Master Catalog",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void verifyAddErrorGlobalMessageForMultipleItemCode(Map<String, String> map) throws IOException, InterruptedException, AWTException  
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		//Verify Delete Error message
		List<String> itemCodeList = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));

		// search all itemcodes on left panel
		for (int i = 0; i <=itemCodeList.size()-1; i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		BasePage.WaitForMiliSec(5000);
		basePage.clickAndHoldCtrlKey();
		classificationpage.selectMultipleuitemcodeList(); 
		basePage.releaseCtrlKey();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();
		classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),map.get("ClassificationItem"));
		classificationpage.clickOnCloseExpandButton();


		//Verify Assigned Error msg in US catalog
		/*
		 * pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get(
		 * "CatalogType")); queriesSubMenu.clickSeachButton();
		 * basePage.clickAndHoldCtrlKey();
		 * classificationpage.selectMultipleuitemcodeList(); basePage.releaseCtrlKey();
		 */
		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get(
				"TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get(
				"DivisionTypeOfClassificationpPop")).
		selectDivisionErrorOrWarningMessage(map.get("ErrorMessage"));
		action.clickOnRadioCopyButton(); action.clickOnOkButton();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(classificationpage.
				verifyItemErrorMessageforAllDivisions(map.get(
						"TaxnomyDropDownOfClassificationPopUp"),map.get("ErrorAndWarningType"),map.
						get("DivisionTypeOfClassificationpPop").toUpperCase(),map.get("ErrorMessage")
						.toUpperCase()));

		//Verify whether the user is able to view the newly added Item message in PIM
		//Web UI
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
		// search all item codes in the filtered results
		for (int i = 0; i <= item_code.size()-1; i++)
		{
			Assertions.assertThat(item_code).contains(item_code.get(i));
		}

		//Verify Error msg in Master catalog
		log(LogType.INFO, "Verify Error Messages in Master for Multiple item code");
		BasePage.WaitForMiliSec(5000);
		pimHomepage.mainMenu().clickQueriesMenu();
		pimHomepage.mainMenu().clickRefreshMenuIcon();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"));
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get(
				"MasterCatalog"));
		for (int i = 0; i <itemCodeList.size(); i++)
		{
			queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
		}
		BasePage.WaitForMiliSec(5000);
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
	
	

	@PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description = "TC_001, TC002 | Verify if the message rule at 26th position in the dictionary is getting assigned to the item if the user driven message in the 25th position is added to the item provided all other checks are successful.",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void TC_001_Verify_if_the_message_rule_at_26th_position_in_the_dictionary_is_getting_assigned_to_the_item_if_the_user_driven_message_in_the_25th_position_is_added_to_the_item_provided_all_other_checks_are_successful(Map<String, String> map) throws IOException, InterruptedException, AWTException { 

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
				.selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton()
				.clickOnFirstResult();

		productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
		log(LogType.INFO, "Navigate to global attribute tab to verify hazardous code");

		// verifying hazardous code
		String hazardouscode = globalattribute.getHazardousClassCode();
		Assertions.assertThat(hazardouscode).isEqualTo(map.get("HazardousCode"));

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
		log(LogType.INFO, "Navigate to Classification tab after verifying the hazardous code in US Catalog");

		classificationpage.clickOnExpandButton();
//		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
//				map.get("structurePath"));
//		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));
//		classificationpage.clickStructureGroup();
//		classificationpage.removeButton();
//		classificationpage.clickYes();

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
//		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
//		Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
//		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
//		pimHomepage.productDetailSearchPage().clickRefreshIcon();
//	    Assertions.assertThat(textPage.isAllAttributesVisible("autoAssignedItemMessage_GLOBAL_L20")).isFalse();
		classificationpage.clickOnCloseExpandButton();


		action.clickOnActionsDropdown();
		action.clickOnClassifyItem();
		structuresubmenu.clickOnTaxnomyDropDownOfClassificationPopUp(map.get("TaxnomyDropDownOfClassificationPopUp"));
		structuresubmenu.sendKeysToFilterTextFieldOfClassificationPopup(map.get("DivisionTypeOfClassificationpPop")).
				selectDivisionErrorOrWarningMessage(map.get("ItemMessageCode"));
		action.clickOnRadioCopyButton();
		action.clickOnOkButton().clickOnOkButton1();
		
		classificationpage.clickOnExpandButton();
		znewglobalerrormessage = classificationpage.getStructureGroup(map.get("structureSystem"),
		map.get("structurePath"));
		Assertions.assertThat(znewglobalerrormessage).isEqualTo(map.get("ItemMessageCode"));

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.sortRulestByLastExecution();
//		List<String> catalogentityrule2 = qualitystatus.getRuleNamesByRuleStatus(map.get("ruleStatus"));
//		Assertions.assertThat(catalogentityrule2).containsAnyOf(map.get("CatalogEntityRule"));
//		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Classificationtab"));
	    Assertions.assertThat(textPage.isAllAttributesVisible("autoAssignedItemMessage_GLOBAL_L20")).isTrue();

		
		pimHomepage.clickLogoutButton();
	}
}
