package com.pim.tests.Workflow;


import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;

import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogAssignmentPage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.PimHomepage_DetailViewPage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.SeoTabPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.ApplicationUtils;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class WorkflowTest_DisplayPriceFunctionality extends BaseTest {

	LocalAttributePage localAttributePage = new LocalAttributePage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	SeoTabPage seoTabPage = new SeoTabPage();
	GlobalAttributePage globalAttributePage = new GlobalAttributePage();
	WebDescription webDescription = new WebDescription();
	MainMenu mainMenu = new MainMenu();
	MainMenu mainmenu=new MainMenu(); 
	BasePage basePage = new BasePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils=new Javautils();
	PimHomepage_DetailViewPage detailViewPage = new PimHomepage_DetailViewPage();
	CatalogAssignmentPage catalogAssignmentPage = new CatalogAssignmentPage();
	ActionsPage action = new ActionsPage();
	ClassificationsPage classificationpage = new ClassificationsPage();
	QualityStatusPage qualitystatuspage = new QualityStatusPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
	Javautils javautil = new Javautils();
	String errorAndWarningmessage;
	List<String> item_code;
	ApplicationUtils appUtils = new ApplicationUtils();
	SoftAssert softAssert = new SoftAssert();
	CatalogTypePage catalogTypePage = new CatalogTypePage();
	AllCatalogsPage allCatalogsPage = new AllCatalogsPage();
	ProductDetailSearchPage productdetailspage = new ProductDetailSearchPage();
	PimHomepage pimHomepage = new PimHomepage();
	LoginPage loginPage = new LoginPage();
	List<String> user_driven;
	List<String> all_dental_catalog;
	List<String> user_and_rule_driven = new ArrayList<>();


	private WorkflowTest_DisplayPriceFunctionality(){
	}
	
	// WRFL_099
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
	@Test(description ="WRFL_099 | Verify if Display Price Status is merged to Master", groups = {"US","pim","WorkFlow"}, 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

	public void WRFL_099_Verify_if_Display_Price_Status_is_merged_to_Master(Map<String, String> map){
		//Login to PIM
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType")).removeAndEnterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		//editing Display Price
		String updatedDisplayPrice = detailViewPage.editDisplayPriceTxtfld();
		log(LogType.EXTENTANDCONSOLE, "Updated Display price as --> "+updatedDisplayPrice);

		BasePage.WaitForMiliSec(10000);
		String USCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();

		Assertions.assertThat(USCatalogDisplayPriceValue).contains(updatedDisplayPrice);
		
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules")))
		{
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		
		BasePage.WaitForMiliSec(300000);
        
		//Verifying from Master Catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnFirstResult();
		String masterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(USCatalogDisplayPriceValue);

		log(LogType.EXTENTANDCONSOLE, "Verifying Display Price Status");
		pimHomepage.clickLogoutButton();

	}

	// WRFL_100   --> Try to use same data in testdata which has been used in "verify_all_catalog_tabs_and_exception_list_for_dental_division"
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="WRFL_100 | Verify if Display Price Status remains unchanged,  when workflow is triggered by adding the Catalog to an Exception", groups = {"US","pim","WorkFlow"}, 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)

	public void WRFL_100_Verify_if_Display_Price_Status_remains_unchanged_when_workflow_is_triggered_by_adding_the_Catalog_to_an_Exception (Map<String, String> map){
		
		//Login to PIM
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType")).removeAndEnterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		//editing Display Price
		String updatedDisplayPrice = detailViewPage.editDisplayPriceTxtfld();
		log(LogType.EXTENTANDCONSOLE, "Updated Display price as --> "+updatedDisplayPrice);

		BasePage.WaitForMiliSec(10000);
		String USCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(USCatalogDisplayPriceValue).contains(updatedDisplayPrice);
		
		BasePage.WaitForMiliSec(300000);
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		String MasterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		
		//Verifying in US Catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton().clickOnFirstResult();
		String masterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(MasterCatalogDisplayPriceValue);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		user_driven = catalogTypePage.getUserDrivenCatalogs();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();

		user_and_rule_driven.addAll(rule_driven);
		user_and_rule_driven.addAll(user_driven); // took user and rule driven
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(user_and_rule_driven).containsAll(all_dental_catalog);// verified all catalog without
																					// exception

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();
		List<String> master_all_dental_catalog = allCatalogsPage.getAllDentalCatalogList();
		// verified all catalog tab in master
		Assertions.assertThat(user_and_rule_driven).containsAll(master_all_dental_catalog);

		// verifying with exception list
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> userdrivencatalogs = catalogTypePage.getExceptionListCatalogs();
		List<String> multiplevaluesfromexcel = javautils.readMultipleValuesFromExcel(map.get("userDriven"));
		for (String userdriven : multiplevaluesfromexcel) {
			if (userdrivencatalogs.contains(userdriven)) {
				// delete userdriven
				catalogTypePage.clearExceptionListField(userdriven);
				productdetailspage.clickRefreshIcon();
				log(LogType.INFO, "Deleting the Userdriven");
			}
		}
		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickExceptionListField().selectExceptionCatalogsFromList(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("AllCatalogtab")).clickRefreshIcon();

		List<String> all_catalog_except_exceptionallist = allCatalogsPage.getAllDentalCatalogList();
		Assertions.assertThat(all_catalog_except_exceptionallist).isNotEqualTo(user_and_rule_driven);
		
		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		// verifying with exception list in master catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon()
				.selectTabfromDropdown(map.get("AllCatalogtab"));
		List<String> master_exception_alldental__catalog = allCatalogsPage.getAllDentalCatalogList();

		Assertions.assertThat(user_and_rule_driven).isNotEqualTo(master_exception_alldental__catalog);
		Assertions.assertThat(all_catalog_except_exceptionallist).containsAll(master_exception_alldental__catalog);

		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(detailViewPage.getDisplayPriceTxtfldValue());

		log(LogType.INFO,"Verifying Display Price Status");
		pimHomepage.clickLogoutButton();

	}
	
	// WRFL_101   --> Try to use the same data in testdata which has been used in "verify_rule_driven_for_dental_division"
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Catalog)
	@Test(description ="WRFL_101 | Verify if Display Price Status remains unchanged,  when workflow is triggered by adding the Catalog", groups = {"US","pim","WorkFlow"}, 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void WRFL_101_Verify_if_Display_Price_Status_remains_unchanged_when_workflow_is_triggered_by_adding_the_Catalog(Map<String, String> map){
		//Login to PIM
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType")).removeAndEnterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		//editing Display Price
		String updatedDisplayPrice = detailViewPage.editDisplayPriceTxtfld();
		log(LogType.EXTENTANDCONSOLE, "Updated Display price as --> "+updatedDisplayPrice);

		BasePage.WaitForMiliSec(10000);
		String USCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(USCatalogDisplayPriceValue).contains(updatedDisplayPrice);
		
		BasePage.WaitForMiliSec(300000);
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		String MasterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		
		//Verifying in US Catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton().clickOnFirstResult();
		String masterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(MasterCatalogDisplayPriceValue);

		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("TabName"));

		catalogTypePage.clearUserDriven(map.get("userDriven"));

		BasePage.WaitForMiliSec(3000);
		catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
		pimHomepage.productDetailSearchPage().clickRefreshIcon();
		List<String> rule_driven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAnyOf(map.get("RuleDriven"));

		// verify quality status
		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
				.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules : javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
			Assertions.assertThat(catalogentityrule).contains(Rules);
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO, "Verifying catalog entity rule");

		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton()
				.clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
		List<String> masterRuledriven = catalogTypePage.getRuleDrivenCatalogs();
		Assertions.assertThat(rule_driven).containsAll(masterRuledriven);

		
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(detailViewPage.getDisplayPriceTxtfldValue());

		log(LogType.INFO,"Verifying Display Price Status");
		pimHomepage.clickLogoutButton();
	}
	
	
	// WRFL_103   --> Try to use the same data in testdata which has been used in "verifyItemErrorMessageForDentalDivision"
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
	@Test(description ="WRFL_103 | Verify if Display Price Status remains unchanged,  when workflow is triggered by Associating Message to an Item", groups = {"US","pim","WorkFlow"}, 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void WRFL_103_Verify_if_Display_Price_Status_remains_unchanged_when_workflow_is_triggered_by_Associating_Message_to_an_Item(Map<String, String> map){
		
		//Login to PIM
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		
		//Searching Item from Query

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType("Items in US Catalog").selectCatalogType(map.get("CatalogType")).removeAndEnterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		//editing Display Price
		String updatedDisplayPrice = detailViewPage.editDisplayPriceTxtfld();
		log(LogType.EXTENTANDCONSOLE, "Updated Display price as --> "+updatedDisplayPrice);

		BasePage.WaitForMiliSec(10000);
		String USCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(USCatalogDisplayPriceValue).contains(updatedDisplayPrice);
		
		BasePage.WaitForMiliSec(300000);
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		String MasterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		
		//Verifying in US Catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton().clickOnFirstResult();
		String masterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(MasterCatalogDisplayPriceValue);


		List<String> messages = javautil.readMultipleValuesFromExcel(map.get("ErrorMessage"));
		List<String> structurePaths = javautil.readMultipleValuesFromExcel(map.get("ClassificationItem"));
		SoftAssert softAssert = new SoftAssert();

		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		classificationpage.clickOnExpandButton();

		//Deleting or Removing two messages and adding one
		int index = 0;
		boolean assigedflag = false;
		for(int i=0;i<messages.size();i++){
			if(classificationpage.isMessageAdded(map.get("TaxnomyType"),structurePaths.get(i))){
				classificationpage.deleteMessageIfExisit(map.get("TaxnomyType"),structurePaths.get(i));
				appUtils.updateMessageInCSV(map.get("DivisionType"),"D",map.get("ErrorAndWarningType"),messages.get(i));
				log(LogType.INFO, messages.get(i) + " is deleted");
				//log(LogType.INFO, "StackTrace Result: " + Thread.currentThread().getStackTrace());
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

		
		  //verify quality status log(LogType.INFO,"Verifying catalog entity rule");
		  pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get(
		  "Qualitystatustab")); List<String> catalogentityrule =
		  qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus"))
		  .sortRulestByLastExecution().
		  getRuleNamesByRuleStatus(map.get("Status")); for (String
		  Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule"))) {
		  Assertions.assertThat(catalogentityrule).containsAnyOf(Rules); }
		  
		  classificationpage.clickOnCloseExpandButton();


		//Verify Error msg in Master catalog
		log(LogType.INFO, "Verify Error Messages in Master");
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
		softAssert.assertTrue(classificationpage.verifyItemErrorMessageforAllDivisions(map.get("TaxnomyDropDownOfClassificationPopUp"), map.get("ErrorAndWarningType"), map.get("DivisionTypeOfClassificationpPop").toUpperCase(), messages.get(index).toUpperCase()));
		log(LogType.INFO, "Verify Error Messages in Master");
		
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(detailViewPage.getDisplayPriceTxtfldValue());

		log(LogType.INFO,"Verifying Display Price Status");
		pimHomepage.clickLogoutButton();
		}
	
	
	// WRFL_104   --> Try to use the same data in testdata which has been used in "Verify_Adding_Mandatory_Field_Local_Attribute_Passed_For_US"
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.Local_Attribute)
	@Test(description ="WRFL_104 | Verify if Display Price Status remains unchanged,  when workflow is triggered by updating Local Attribute for an Item", groups = {"US","pim","WorkFlow"}, 
	dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void WRFL_104_Verify_if_Display_Price_Status_remains_unchanged_when_workflow_is_triggered_by_updating_Local_Attribute_for_an_Item(Map<String, String> map){
		
		//Login to PIM
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType")).removeAndEnterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		//editing Display Price
		String updatedDisplayPrice = detailViewPage.editDisplayPriceTxtfld();
		log(LogType.EXTENTANDCONSOLE, "Updated Display price as --> "+updatedDisplayPrice);

		BasePage.WaitForMiliSec(10000);
		String USCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(USCatalogDisplayPriceValue).contains(updatedDisplayPrice);
		
		BasePage.WaitForMiliSec(300000);
		
		//Searching Item from Query
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("MasterCatalog")).clickSeachButton().clickOnFirstResult();
		BasePage.WaitForMiliSec(3000);

		String MasterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		
		//Verifying in US Catalog
		pimHomepage.mainMenu().clickQueriesMenu().selectCatalogType(map.get("CatalogType")).clickSeachButton().clickOnFirstResult();
		String masterCatalogDisplayPriceValue = detailViewPage.getDisplayPriceTxtfldValue();
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(MasterCatalogDisplayPriceValue);


		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));

		// Adding Value
		localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
				.MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
				.itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
				.addingMandatoryQuantityFieldAttribute(map.get("LocalAttributeQuantityFieldValue"))
				.selectValuefromSaveDropdown(map.get("saveButton"));

		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.selectTabfromDropdown(map.get("ContinueTabName"));
		productDetailSearchPage.clickRefreshIcon();

		List<String> localmandatoryrule = qualityStatusPage.maximizeQualityStatusTab().sortRulestByLastExecution()
				.getRuleNamesByRuleStatus(map.get("QualityTab_StatusColoum"));
		qualityStatusPage.minimizeQualityStatusTab();

		System.out.println(localmandatoryrule);
		
		List<String> myList = new Javautils().readMultipleValuesFromExcel(map.get("LocalAttributeRulePass"));
		Assertions.assertThat(Javautils.compareList(myList, localmandatoryrule)).isEqualTo(true);

		// Navigating to Master
		queriesSubMenu.selectCatalogType(map.get("ContinueCatalogType"))
				.removeAndEnterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult()
				.selectTabfromDropdown(map.get("TabName"));
		
		productDetailSearchPage.clickRefreshIcon();
		productDetailSearchPage.clickRefreshIcon();

		// Validating Item Number should present in master With new change
		
		String MasterBrandValue = localAttributePage.getBrandValue().trim();
		String MasterItemValue = localAttributePage.getItemValue().trim();
		String MasterItemTypeValue = localAttributePage.getItemTypeValue().trim();
		
		Assertions.assertThat(MasterBrandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));
		Assertions.assertThat(MasterItemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
		Assertions.assertThat(MasterItemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));


		
		Assertions.assertThat(masterCatalogDisplayPriceValue).contains(detailViewPage.getDisplayPriceTxtfldValue());

		log(LogType.INFO,"Verifying Display Price Status");
		pimHomepage.clickLogoutButton();
		}
}