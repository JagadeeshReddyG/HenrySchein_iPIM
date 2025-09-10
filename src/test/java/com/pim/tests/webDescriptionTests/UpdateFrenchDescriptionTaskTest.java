package com.pim.tests.webDescriptionTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.DataBaseColumnName;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.TasksSubMenu;
import com.pim.pages.WebDescription;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.queries.Catalogqueries;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DatabaseUtilitiies;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class UpdateFrenchDescriptionTaskTest extends BaseTest  {

	TasksSubMenu tasksSubMenu =new TasksSubMenu();

	@PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.WebDescription_Workflow)
	@Test(description = "TCID | Verify Update French Description", dataProvider = "getCatalogData", 
	dataProviderClass = DataProviderUtils.class,groups = {"CA","pim","WEB_DESCRIPTION"})
	public void validateFrenchDescriptionUnderTasks(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		LocalAttributePage localAttributePage = new LocalAttributePage();
		ProductDetailSearchPage productDetailPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		productDetailPage.expandTab();
		localAttributePage.itemTypeFieldAttribute(map.get("itemTypeValue"));
		localAttributePage.selectValuefromSaveDropdown(map.get("saveValue"));
		productDetailPage.clickRefreshIcon();
		pimHomepage.clickLogoutButton();

		PimHomepage pimHomepage1 = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage1.mainMenu().clickTasksMenu()/*.enterSearchTextToFilter(map.get("SearchText")).clickFilterButton()*/.SelectSpecificUserGroup(map.get("SpecificUser"));
		productDetailPage.applyFilterToSearchAnyValueinTaskList(map.get("TaskName"), map.get("itemNumber")).clickOnFirstResult();
		Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
		pimHomepage.clickLogoutButton();
	}

	@PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.WebDescription_Workflow)
	@Test(description = "TCID | Verify if update task is created if there is no change in english description for french description", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","pim","WEB_DESCRIPTION"})
	public void validateFrenchDescriptionTaskNotCreated(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		LocalAttributePage localAttributePage = new LocalAttributePage();
		ProductDetailSearchPage productDetailPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickTasksMenu()
				/* .enterSearchTextToFilter(map.get("SearchText")).clickFilterButton() */.SelectSpecificUserGroup(map.get("SpecificUser"));
		productDetailPage.applyFilterToSearchAnyValueinTaskList(map.get("TaskName"), map.get("itemNumber"));
		Assertions.assertThat(webDescription.getTaskFilterRecord()).doesNotContain(map.get("itemNumber"));
		pimHomepage.clickLogoutButton();
	}


	//FDM_22-Verify manually entered French descriptions (for 'Dental' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_22 | Verify manually entered French descriptions (for 'Dental' division) are published to the web applications through the middleware layer for pre requisite", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","French_DESCRIPTION"})
	public void VerifyManuallyEnteredFrenchDescriptionsforDentalDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		GlobalAttributePage globalAttributePage = new GlobalAttributePage();
		ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		globalAttributePage.EnterFrenchDescriptionManually(map.get("FrenchDecription"));



	}
	//FDM_23-Verify manually entered French descriptions (for 'Medical' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_23 | Verify manually entered French descriptions (for 'Medical' division) are published to the web applications through the middleware layer for pre requisite", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","French_DESCRIPTION"})
	public void VerifyManuallyEnteredFrenchDescriptionsforMedicalDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		GlobalAttributePage globalAttributePage = new GlobalAttributePage();
		ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		globalAttributePage.EnterFrenchDescriptionManually(map.get("FrenchDecription"));



	}

	//FDM_24-Verify manually entered French descriptions (for 'Zahn' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_24 | Verify manually entered French descriptions (for 'Zahn' division) are published to the web applications through the middleware layer for pre requisite", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","French_DESCRIPTION"})
	public void VerifyManuallyEnteredFrenchDescriptionsforZahnDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		GlobalAttributePage globalAttributePage = new GlobalAttributePage();
		ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		globalAttributePage.EnterFrenchDescriptionManually(map.get("FrenchDecription"));
	}

	//FDM_25-Verify manually entered French descriptions (for 'SpecialMarket' division) are published to the web applications through the middleware layer.
	@PimFrameworkAnnotation(module = Modules.French_DESCRIPTION, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.French_DESCRIPTION_Middleware)
	@Test(description = "FDM_25  | Verify manually entered French descriptions (for 'SpecialMarket' division) are published to the web applications through the middleware layer for pre requisite", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","French_DESCRIPTION"})
	public void VerifyManuallyEnteredFrenchDescriptionsforSpecialMarketDivisionArePublishedToTheWebApplicationsThroughTheMiddlewarelayer(Map<String, String> map){
		CatalogTypePage catalogtypepage = new CatalogTypePage();
		GlobalAttributePage globalAttributePage = new GlobalAttributePage();
		ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
		WebDescription webDescription = new WebDescription();

		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		globalAttributePage.EnterFrenchDescriptionManually(map.get("FrenchDecription"));
	}



}
