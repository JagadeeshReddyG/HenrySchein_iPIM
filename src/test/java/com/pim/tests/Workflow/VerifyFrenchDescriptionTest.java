package com.pim.tests.Workflow;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.TasksSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.pim.reports.FrameworkLogger.log;

public class VerifyFrenchDescriptionTest extends BaseTest {
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    QualityStatusPage qualityStatus = new QualityStatusPage();
    LocalAttributePage localAttributePage = new LocalAttributePage();
    CanadaTranslationPage canadaTranslationPage = new CanadaTranslationPage();
    TasksSubMenu tasksSubMenu = new TasksSubMenu();

    private VerifyFrenchDescriptionTest(){
    }

    //WRFL_082
    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
    @Test(description ="WRFL_082 | Verify if new task is created with title New French Description,if there is french description does not exists in master catalog", groups = {"US","CA","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyTasksAreGeneratedForFrenchDescriptionDoesNotExist(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
                .clickOnFirstResult();

//        //To Check if any Local Attribute rules get failed
//        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("QualityStatusTab"));
//        productDetailSearchPage.clickRefreshIcon();
//        List<String> localMandatoryRule = qualityStatus.maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
//        Assertions.assertThat(localMandatoryRule).isNullOrEmpty();
//        qualityStatus.minimizeQualityStatusTab();
        
//        //To Edit Mandatory Fields Of Local Attribute
//        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));
//        productDetailSearchPage.clickRefreshIcon();
//        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValue")).selectValuefromSaveDropdown(map.get("saveButton"));
//        productDetailSearchPage.clickRefreshIcon();
//        pimHomepage.clickLogoutButton();
        
//        //To check Local Attribute Rules get Executed
//        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("QualityStatusTab"));
//        qualityStatus.maximizeQualityStatusTab();
//        pimHomepage.productDetailSearchPage().clickRefreshIcon();
//        List<String> localAttributeRules = qualityStatus.selectRuleTypeFromDropDown(map.get("RuleType")).getRuleNamesByRuleStatus(map.get("Status"));
//        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("LocalAttributeRules").split(",")));
//        Assertions.assertThat(localAttributeRules).isEqualTo(myList);
//        log(LogType.INFO,"Verifying Local Attribute rules");

        //Verify English Description shown is Updated in Canada_Translation tab
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("CanadaTranslationTab"));
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.clickRefreshIcon();
        String actualDescription = canadaTranslationPage.getFullDisplayDescription1();
        String abbreviatedDescription = canadaTranslationPage.getAbbreviatedDisplayDescription1();
        // Check that the descriptions are empty
        Assertions.assertThat(abbreviatedDescription).contains("No content");
        Assertions.assertThat(actualDescription).contains("No content");

        //To Verify Tasks are Generated to CreateFrenchDescription
//        PimHomepage pimHomepage1 = new LoginPage()
//                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
//                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		
        pimHomepage.mainMenu().clickTasksMenu().SelectSpecificUserGroup(map.get("CanadaTranslator"))/* .clickFilterButton().SelectFirstUserGroup() */;
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
        pimHomepage.clickLogoutButton();
    }

    //WRFL_083
    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
    @Test(description ="WRFL_083 | Verify EnglishDescription gets updated in CanadaTranslationTab and task is generated to update FrenchDescription", groups = {"US","CA","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyTasksAreGeneratedToUpdateFrenchDescription(Map<String, String> map) {
    	
    	PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
                .clickOnFirstResult();
    	
//        //Verify English Description shown is Updated in Canada_Translation tab
//        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("CanadaTranslationTab"));
//        qualityStatus.maximizeQualityStatusTab();
//        String ActualDescription = canadaTranslationPage.getFullDisplayDescription();
//        String AbbreviatedDescription = canadaTranslationPage.getAbbreviatedDisplayDescription();
//        Assertions.assertThat(AbbreviatedDescription).contains(map.get("AbbreviatedDescription"));
//        Assertions.assertThat(ActualDescription).contains(map.get("FrenchDescription"));
        
        //To Edit Mandatory Fields Of Local Attribute
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));
        productDetailSearchPage.clickRefreshIcon();
        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValue")).selectValuefromSaveDropdown(map.get("saveButton"));
        productDetailSearchPage.clickRefreshIcon();
        pimHomepage.clickLogoutButton();

        //To Verify Tasks are Generated to UpdateFrenchDescription
        PimHomepage pimHomepage1 = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		pimHomepage1.mainMenu().clickTasksMenu().SelectSpecificUserGroup(map.get("CanadaTranslator"))/* .clickFilterButton().SelectFirstUserGroup() */;
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
        pimHomepage1.clickLogoutButton();
    }

    //WRFL_085 AND WRFL_086
    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
    @Test(description ="WRFL_085 AND WRFL_086 | Verify if there is any change in english description,new task gets created for french description if empty description exists in master catalog", groups = {"US","CA","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyTasksAreGeneratedToEmptyFrenchDescription(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
                .clickOnFirstResult();
        
        //Verify English Description shown is Updated in Canada_Translation tab
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("CanadaTranslationTab"));
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.clickRefreshIcon();
        String actualDescription = canadaTranslationPage.getFullDisplayDescription1();
        String abbreviatedDescription = canadaTranslationPage.getAbbreviatedDisplayDescription1();
        // Check that the descriptions are empty
        Assertions.assertThat(abbreviatedDescription).contains("No content");
        Assertions.assertThat(actualDescription).contains("No content");

        //To Verify Tasks are Generated to CreateFrenchDescription
//        PimHomepage pimHomepage1 = new LoginPage()
//                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
//                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
		
        pimHomepage.mainMenu().clickTasksMenu().SelectSpecificUserGroup(map.get("CanadaTranslator"))/* .clickFilterButton().SelectFirstUserGroup() */;
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
        pimHomepage.clickLogoutButton();
    }

//    //WRFL_083
//    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
//    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
//    @Test(description ="WRFL_083 | Verify EnglishDescription gets updated in CanadaTranslationTab and task is generated to update FrenchDescription", groups = {"US","CA","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
//    public void verifyTasksAreGeneratedToUpdateFrenchDescription(Map<String, String> map) {
//    	
//    	PimHomepage pimHomepage = new LoginPage()
//                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
//                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
//        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
//                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
//                .clickOnFirstResult();
//    	
////        //Verify English Description shown is Updated in Canada_Translation tab
////        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("CanadaTranslationTab"));
////        qualityStatus.maximizeQualityStatusTab();
////        String ActualDescription = canadaTranslationPage.getFullDisplayDescription();
////        String AbbreviatedDescription = canadaTranslationPage.getAbbreviatedDisplayDescription();
////        Assertions.assertThat(AbbreviatedDescription).contains(map.get("AbbreviatedDescription"));
////        Assertions.assertThat(ActualDescription).contains(map.get("FrenchDescription"));
//        
//        //To Edit Mandatory Fields Of Local Attribute      
//        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));
//        productDetailSearchPage.clickRefreshIcon();
//        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValue")).selectValuefromSaveDropdown(map.get("saveButton"));
//        productDetailSearchPage.clickRefreshIcon();
//        pimHomepage.clickLogoutButton();
//        
//      //To Verify New task should get created for french description
//        PimHomepage pimHomepage1 = new LoginPage()
//                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
//                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
//		pimHomepage1.mainMenu().clickTasksMenu().SelectSpecificUserGroup(map.get("CanadaTranslator"))/* .clickFilterButton().SelectFirstUserGroup() */;
//        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
//        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
//        pimHomepage1.clickLogoutButton();
//    }

    //WRFL_064 AND WRFL_068
    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
    @Test(description ="WRFL_068,WRFL_064 | Verify task name and check generated task gets resolved on double click", groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyGeneratedTasksCanBeResolved(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickTasksMenu();
		pimHomepage
				.mainMenu().clickTasksMenu()/*
											 * .enterSearchTextToFilter(map.get("CanadaTranslator")).clickFilterButton()
											 */.SelectSpecificUserGroup(map.get("CanadaTranslator"));
        productDetailSearchPage.clickOnFirstResult();
       // productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
        String ActualTaskName = tasksSubMenu.getTaskName();
        String itemnumberFromTaskDescription = Javautils.getIntegerFromString(ActualTaskName);
        //Assertions.assertThat(ActualTaskName).isEqualTo(map.get("ExpectedTaskName"));
        Assertions.assertThat(ActualTaskName).contains(map.get("ExpectedTaskName"));
        productDetailSearchPage.clickOnFirstResult();
        tasksSubMenu.AcceptTask();
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.applyFilterInSearchPageByAnyValueinTask(map.get("TypeName"), itemnumberFromTaskDescription);
        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isFalse();
    }
}