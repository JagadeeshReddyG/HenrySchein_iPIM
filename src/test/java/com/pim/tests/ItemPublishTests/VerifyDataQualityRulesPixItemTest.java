package com.pim.tests.ItemPublishTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.*;
import com.pim.pages.us.CreateItemValidationPage;
import com.pim.pages.us.ItemPublishDateAndFlagPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class VerifyDataQualityRulesPixItemTest extends BaseTest {

    CreateItemValidationPage itemValidationPage = new CreateItemValidationPage();
    ItemPublishDateAndFlagPage itemPublish = new ItemPublishDateAndFlagPage();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    CatalogTypePage catalogTypePage = new CatalogTypePage();
    Javautils javautils=new Javautils();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
	QueriesSubMenu queriesSubMenu = new QueriesSubMenu();

    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.Exceptionally_Passed)
    @TestDataSheet(sheetname = TestCaseSheet.ItemPublishTestData)
    @Test(description = "Verify the Import Workflow for an item created in PIX",groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_Data_Quality_Rules_Execution_data_loaded_from_Pix_In_US_Catalog(Map<String, String> map) {

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

        itemValidationPage.clickOnCatalogsMenu().clickOnUsCatalogSubMenu().clickOnUncheckedIcon().selectFilterByOption().enterFilterSearchField(map.get("filterSearch")+" "+DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1)).clickOnFilterButton();
        BasePage.WaitForMiliSec(30000);
        Assertions.assertThat(itemValidationPage.captureJDEdescriptionTextValue()).isEqualTo(map.get("filterSearch")+" "+DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
        log(LogType.INFO,"JDE Description Title Validated Successfully.");
        String itemNumber = productDetailSearchPage.getHSIItemNumberByJDEDescription(map.get("filterSearch")+" "+DateandTimeUtils.getSpecifiedDayFromCurrentDay(-1));
        
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(itemNumber);
        queriesSubMenu.clickSeachButton();
        productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        BasePage.WaitForMiliSec(1000);
        catalogTypePage.selectPublishDate(DateandTimeUtils.getSpecifiedDayFromCurrentDay(2));
        log(LogType.INFO,"Change publish date to current date");

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab();
		List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
		for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
		{
			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
		}
		qualitystatus.minimizeQualityStatusTab();
		log(LogType.INFO,"Verifying workflow rule");

        pimHomepage.clickLogoutButton();

    }
}
