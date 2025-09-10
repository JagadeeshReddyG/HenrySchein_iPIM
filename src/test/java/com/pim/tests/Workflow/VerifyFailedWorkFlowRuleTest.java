package com.pim.tests.Workflow;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.TasksSubMenu;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.pim.reports.FrameworkLogger.log;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VerifyFailedWorkFlowRuleTest extends BaseTest {
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    QualityStatusPage qualityStatus = new QualityStatusPage();
    LocalAttributePage localAttributePage = new LocalAttributePage();
    ActionsPage actionsPage = new ActionsPage();
    TasksSubMenu tasksSubMenu = new TasksSubMenu();

    private VerifyFailedWorkFlowRuleTest(){
    }

    @PimFrameworkAnnotation(module = Modules.WorkFlow, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.WorkFlow_TestData)
    @Test(description ="Verify failed workflow rules is generated for empty mandatory local attribute feilds", groups = {"US","pim","WorkFlow"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyFailedWorkFlowRuleIsGeneratedForEmptyMandatoryLocalAttributes(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
                .clickOnFirstResult();

        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));

        qualityStatus.maximizeQualityStatusTab();

        //Update Local Attributes to No Content
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("LocalAttribute"));
        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValueEnter2")).selectValuefromSaveDropdown(map.get("saveButton"));
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.clickRefreshIcon();
        localAttributePage.MandatoryItemFieldAttribute(map.get("ItemValueEnter"))
                .selectValuefromSaveDropdown(map.get("saveButton"));
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.clickRefreshIcon();


        //Verify Failed WorkFlow Rule in Quality Status Tab
        productDetailSearchPage.selectTabfromDropdown(map.get("QualityStatusTab"));
        productDetailSearchPage.clickRefreshIcon();
        productDetailSearchPage.clickRefreshIcon();
        List<String> LocalMandatoryRule2 = qualityStatus.sortRulestByStatus()
				.getRuleNamesByRuleStatus(map.get("Status"));
        Assertions.assertThat(LocalMandatoryRule2).containsAnyOf(map.get("FailedWorkFlowRule"));
        qualityStatus.minimizeQualityStatusTab();

        //Trigger WorkFlowBusinessRule
        actionsPage.clickOnActionsDropdown();
        actionsPage.SelectBusinessRule(map.get("BusinessRunRules"));

        //Verify Task is Created to Update Missing Mandatory Local Attributes
        pimHomepage.clickLogoutButton();
        PimHomepage pimHomepage2 = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickTasksMenu().enterSearchTextToFilter(map.get("CanadaTranslator")).clickFilterButton().SelectSpecificUserGroup(map.get("CanadaTranslator"));
        productDetailSearchPage.applyFilterInSearchPageByAnyValue(map.get("TypeName"), map.get("ItemNumber")).clickOnFirstResult();
        Assertions.assertThat(tasksSubMenu.isTaskPresent("TasksList")).isTrue();
        String ActualTaskName = tasksSubMenu.getTaskName();
        Assertions.assertThat(ActualTaskName).isEqualTo("["+map.get("ItemNumber")+"]"+" "+map.get("ExpectedTaskName"));
        pimHomepage.clickLogoutButton();
    }
    }
