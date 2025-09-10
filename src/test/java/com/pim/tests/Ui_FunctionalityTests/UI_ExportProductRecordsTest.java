package com.pim.tests.Ui_FunctionalityTests;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TextPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

public class UI_ExportProductRecordsTest extends BaseTest {
    ActionsPage action=new ActionsPage();
    StructureSubMenu structuresubmenu=new StructureSubMenu();

    private UI_ExportProductRecordsTest(){
    }

    //UI_035
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description = "UI_035 | Verify user is able to export product record into spreadsheet with the attribute header UI_035",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyExportSelectedRows(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        structuresubmenu.selectErrorOrWarning(map.get("MessageType")).selectDivisionErrorOrWarningMessage(map.get("DivisionType"))
                .clickOnShowAll().clickOnFirstItemCode();
        //clicks on selected rows csv file
        action.clickOnActionsDropdown();
        action.clickOnExportAsCSVAndDownloadSheet();
        pimHomepage.clickLogoutButton();
    }

    //UI_036
    @PimFrameworkAnnotation(module = Modules.Entities_UI_Func, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Entities_UI_Func_TestData)
    @Test(description = "UI_036 | Verify user is able to export product record into spreadsheet with the attribute header UI_036",groups = {"US","pim","Entities_UI_Func"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyExportProductDetailsInCSV(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        structuresubmenu.selectErrorOrWarning(map.get("MessageType")).selectDivisionErrorOrWarningMessage(map.get("DivisionType"))
                .clickOnShowAll().clickOnFirstItemCode();
        //clicks on exportDescription file
        action.clickOnActionsDropdown();
        action.clickOnExportDirectlyAnyOptionsAndDownloadSheet();
        pimHomepage.clickLogoutButton();
 }

}
