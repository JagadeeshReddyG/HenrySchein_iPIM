package com.pim.tests.userRoleTests;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.LocalAttributePage;
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

public class UserRoleTests extends BaseTest {
	LocalAttributePage localAttributePage = new LocalAttributePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();                     
	MainMenu mainmenu=new MainMenu();
	ClassificationsPage classificationpage=new ClassificationsPage();
	QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
	BasePage basePage = new BasePage();
	Javautils javautil = new Javautils();
	CanadaTranslationPage canadaTranslationPage=new  CanadaTranslationPage();
	QualityStatusPage qualityStatusPage = new QualityStatusPage();
	
	private UserRoleTests(){
	}
	//UserRole_001
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_001 | Verify that user with  Read only role will have read only access to PIM", groups = {"US","pim","User_Role"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatUserWithReadOnlyAccess(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.maximizeProductDetailTab();
		productDetailSearchPage.clickOnTabDropDown();
		List<String> tabListFromExcel  = javautil.readMultipleValuesFromExcel(map.get("TabName"));
		List<String> tablist = productDetailSearchPage.getTabList();
		productDetailSearchPage.minimizeProductDetailTab();

		//removed Logistic tab as per ron Suggestion they have removed

		for (String tabname : tabListFromExcel) {
			Assertions.assertThat(tablist).contains(tabname);
		}
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
        Assertions.assertThat(localAttributePage.isLocalAttributeFieldEditable("colorFieldValue", "colorValueDropdown")).isFalse();
        }
	//UserRole_002
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_002 | Verify that English data steward user has view access to PIM UI - UserRole_002",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatEnglishDataStewardUserHasViewAccessToPIMUI(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.maximizeProductDetailTab();
		productDetailSearchPage.clickOnTabDropDown();
		List<String> tabListFromExcel  = javautil.readMultipleValuesFromExcel(map.get("TabName"));
		List<String> tablist = productDetailSearchPage.getTabList();
		productDetailSearchPage.minimizeProductDetailTab();
		//removed Logistic tab as per ron Suggetion they have removed
        for (String tabname : tabListFromExcel) {
			Assertions.assertThat(tablist).contains(tabname);
			}
	}
	//UserRole_003
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_003 | Verify that English data steward user has update access to modify attributes maintained through PIM (non JDE attributes)",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatEnglishDataStewardUserHasUpdateAccessToModifyAttributesMaintainedThroughPIMUI(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
		localAttributePage.selectLanguageFieldDropdown(map.get("LocalAttributeLaunguageField")).itemTypeFieldAttribute(map.get("LocalAttributeItemTypeFieldValue"));
		String itemtypeValue=localAttributePage.getItemTypeValueWithOutSpace();
				
		Assertions.assertThat(itemtypeValue).isEqualTo(map.get("LocalAttributeItemTypeFieldValue"));
	
	}
	//UserRole_005
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_005 | Verify that French data steward user has view access to PIM UI",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatFrenchDataStewardUserHasViewAccessToPIMUI(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();

		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.maximizeProductDetailTab();
		productDetailSearchPage.clickOnTabDropDown();
		List<String> tabListFromExcel  = javautil.readMultipleValuesFromExcel(map.get("TabName"));
		List<String> tablist = productDetailSearchPage.getTabList();
		productDetailSearchPage.minimizeProductDetailTab();
		//removed Logistic tab as per ron Suggetion they have removed
        for (String tabname : tabListFromExcel) {
			Assertions.assertThat(tablist).contains(tabname);
			}
	}
	
	//UserRole_006
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_006 | Verify that French data steward user has update access to modify attributes maintained through PIM (non JDE attributes)",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatFrenchDataStewardUserHasUpdateAccessToModifyAttributesMaintainedThroughPIMUI(Map<String, String> map)
	{
		PimHomepage pimHomepage = new LoginPage()
	.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
	.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
	pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
	.selectCatalogType(map.get("CatalogType"))
	.enterHsiItemNumber(map.get("ItemNumber"));
	queriesSubMenu.clickSeachButton();
	structuresubmenu.clickOnFirstItemCode();
	productDetailSearchPage.selectTabfromDropdown(map.get("LocalAttributeTab"));
	localAttributePage.selectLanguageFieldDropdown(map.get("LocalAttributeLaunguageField")).addingMandatoryQuantityFieldAttributeForFrench(map.get("QuantityInputFieldValue")).selectValuefromSaveDropdown(map.get("saveButton"));
	String quantityValue=localAttributePage.getQuantityValuewithoutTrimforFrench();
	Assertions.assertThat(quantityValue).isEqualTo(map.get("QuantityInputFieldValue"));
	}
	
	//UserRole_004
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_004 | Verify That English Data Steward User Is Not Allowed To Update French Descriptions",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatEnglishDataStewardUserIsNotAllowedToUpdateFrenchDescriptions(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("CanadaTranslationTab"));
		VerifyThatEnglishDataStewardUserIsNotAllowedToUpdateFrenchDescriptions();
	}
		public void VerifyThatEnglishDataStewardUserIsNotAllowedToUpdateFrenchDescriptions() {
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Full_Display_Description_RightSide","FieldDropDown")).isFalse();
	    Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Abbreviated_Display_Description_RightSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Look_Ahead_SearchDescription_RightSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Detail_Extended_Description_RightSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Search_Description_RightSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("ProductOvreview_RightSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Extended_WebDescription_RightSide","FieldDropDown")).isFalse();
}
    //UserRole_007
	@PimFrameworkAnnotation(module = Modules.User_Role, category = CategoryType.REGRESSION)
	@TestDataSheet(sheetname = TestCaseSheet.UserRoleTestData)
	@Test(description = "UserRole_007 | Verify that French data steward user is not allowed to update English Descriptions",groups = {"CA","pim","User_Role"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyThatFrenchDataStewardUserIsNotAllowedToUpdateEnglishDescriptions(Map<String, String> map)   
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
		.selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("ItemNumber"));
		queriesSubMenu.clickSeachButton();
		structuresubmenu.clickOnFirstItemCode();
		productDetailSearchPage.selectTabfromDropdown(map.get("CanadaTranslationTab"));
	    VerifyThatFrenchDataStewardUserIsNotAllowedToUpdateEnglishDescriptions();
	}
	    public void VerifyThatFrenchDataStewardUserIsNotAllowedToUpdateEnglishDescriptions() {
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Full_Display_Description_LeftSide","FieldDropDown")).isFalse();
	    Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Abbreviated_Display_Description_LeftSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Look_Ahead_SearchDescription_LeftSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Detail_Extended_Description_LeftSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Search_Description_LeftSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("ProductOvreview_LeftSide","FieldDropDown")).isFalse();
		Assertions.assertThat(canadaTranslationPage.isCanadaTranslationFieldEditable("Extended_WebDescription_LeftSide","FieldDropDown")).isFalse();
}}
		
		
	