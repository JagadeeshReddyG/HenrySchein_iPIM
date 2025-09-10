package com.pim.tests.dataEntitiesAndAttributes;

import static com.pim.reports.FrameworkLogger.log;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;

public class DataEntitiesAndAttributesTest extends BaseTest {
	LocalAttributePage localAttributePage = new LocalAttributePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	ClassificationsPage classificationpage=new ClassificationsPage();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();
	WebDescription webDescription =new WebDescription();
	
	private DataEntitiesAndAttributesTest(){
	}
	//DataAttributes_008--Done
	@PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
	@Test(description = "DataAttributes_008 | Verify that Print Catalog information for a Product is not displayed for Canadian data stewards users",groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatPrintCatalogInformationForAProductIsNotDisplayedForCanadianDataStewardsUsers(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
		.selectClassificationType(map.get("CategoryType")).selectItemType(map.get("ItemType"))
		.clickOnSelectedTaxnomyType(map.get("CategoryType")).clickOnSelectedTaxnomyType(map.get("CategoryType"))
		.clickOnfirstSubsetDropdown(map.get("SubsetDropDown"))
		.clickOnShowAll().clickOnFirstItemCode();
		String printCatalogName = productDetailSearchPage.getItemField(map.get("fieldIndex"));
		Assertions.assertThat(printCatalogName).isEqualTo("");
        log(LogType.INFO, "Verifying Print Catalog Name");
		
		
}
	//DataAttributes_013--Done
	@PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
	@Test(description = "DataAttributes_013 | Verify the product description in header tab is displayed in English by default DataAttributes_013",groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheProductDescriptionInHeaderTabIsDisplayedInEnglishByDefault(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));
		String language=webDescription.getLanguage();
		System.out.println("Launguage----"+ language );
		Assertions.assertThat(language).isEqualTo(map.get("Language"));
}
    //DataAttributes_014--Done
	@PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
	@Test(description = "DataAttributes_014 | Verify that from the Language field drop down user is able to select other Languages and add the production description manually in other language",groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatWeatherUserIsAbleToSelectOtherLanguagesFromLanguageDropDownField(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("TabName"));
		webDescription.selectLanguageFieldDropdown(map.get("Language"));
}
	//DataAttributes_015--Done
	@PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
	@Test(description = "DataAttributes_015 | Verify the system is able to capture the country of Item creation",groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyTheSystemIsAbleToCaptureTheCountryOfItemCreation(Map<String, String> map)   
	{ 
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		
		//verifying Country Of Origin Value
        String countryOfOrigin = productDetailSearchPage.getItemField(map.get("fieldIndex"));
        Assertions.assertThat(countryOfOrigin).isEqualTo(map.get("CountryOfOrigin"));
        log(LogType.INFO, "Verifying Country Of Origin");
		
}}