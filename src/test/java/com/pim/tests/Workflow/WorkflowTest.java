package com.pim.tests.Workflow;


import static com.pim.reports.FrameworkLogger.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

public class WorkflowTest extends BaseTest {
	MainMenu mainmenu=new MainMenu();
	BasePage basePage = new BasePage();
	StructureSubMenu structuresubmenu=new StructureSubMenu();
	ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QualityStatusPage qualitystatus = new QualityStatusPage();
	Javautils javautils=new Javautils();
	
	
	    private WorkflowTest(){
	    }
	    
	    	//WRFL_072,WRFL_073,WRFL_074,WRFL_075,WRFL_076,WRFL_077
	//Verify Local Attribute Quality Rules get run on creation of item in JDE and TO DO update the data for this test case to use new JDE created item
	    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Exceptionally_Passed)
	    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
	    @Test(description ="WRFL_072,WRFL_073,WRFL_074,WRFL_075,WRFL_076,WRFL_077 | Verify if local attribute, local mandatory, description, description text, ecomm rules are ran on importing an item from JDE",groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	    public void VerifyIfAttributesRulesAreRanOnImportingAnItemFromJDE(Map<String, String> map)
	    {
	        PimHomepage pimHomepage = new LoginPage()
	                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
	                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
	        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
			structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
			productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
			qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution();
			List<String> qualityStatusRules = qualitystatus.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
			for (String Rules:javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules")))
			{
				Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);	
			}
			qualitystatus.minimizeQualityStatusTab();
	        log(LogType.INFO,"Verifying Attribute Rule");
	    }

	//WRFL_052, WRFL_053, WRFL_054 AND WRFL_055
	//Verify Global Attribute Quality Rules get run on creation of item in JDE and TODO update the data for this test case to use new JDE created item
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Exceptionally_Passed)
	@TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
	@Test(description ="WRFL_052, WRFL_053, WRFL_054 AND WRFL_055 | Verify if global attribute, global mandatory, catalog entity rules are ran on importing an item from JDE",groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyIfAttributesRulesAreRanGlobalAttributes(Map<String, String> map)
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).clickOnFirstResult().applyDateFilterInSearchPageByAnyValue(map.get("ItemCreationDate"),map.get("Action"));
		structuresubmenu.selectSpecificItem(map.get("specificItemCode"));
		productDetailSearchPage.selectTabfromDropdown(map.get("Qualitystatustab"));
		qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution();
		List<String> qualityStatusRules = qualitystatus.getRuleNamesByRuleStatusInQualityStatusForItemCreation(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("QualityStatusRules")))
		{
			Assertions.assertThat(qualityStatusRules).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying Attribute Rule");
	}
	//Verify login functionality
	@PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Exceptionally_Passed)
	@TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
	@Test(description ="Verify Login functionality",groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
	public void VerifyLoginToApplication(Map<String, String> map)
	{
		PimHomepage pimHomepage = new LoginPage()
				.enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
				.enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
		pimHomepage.clickLogoutButton();
		//.getTitle();
		//Assertions.assertThat(title).isEqualTo("Informatica MDM – Product 360");
	}
}