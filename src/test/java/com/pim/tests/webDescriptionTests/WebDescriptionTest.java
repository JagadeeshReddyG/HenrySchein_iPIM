package com.pim.tests.webDescriptionTests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static com.pim.pages.WebDescription.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class WebDescriptionTest extends BaseTest {

    WebDescription WebObj = new WebDescription();
    
    //FDM_03
    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(priority = 0,description = "FDM_03 | Verify Web Description Tab fields on the basis of Dental Division and French Language", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void DentalDivisiontest(Map<String, String> map) throws InterruptedException, IOException {
        CatalogTypePage catalogtypepage = new CatalogTypePage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).applyFilterInSearchPage(map.get("itemNumber"))
                .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        Thread.sleep(2000);
        WebObj.SelectLaungugeAndDivisionTypeDropDown(map.get("Division"),map.get("Language"));
        VerifyWebDescriptionFields();
    }

    public void VerifyWebDescriptionFields()
    {
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("lsdTextField")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("addTextfield")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("fddTextfield")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("sdTextField")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("poTextField")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("ewdTextField")).isTrue();
        Assertions.assertThat(WebObj.isWebDescriptionFieldVisible("pcdTextField")).isTrue();
    }

  //FDM_04
    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(priority = 1,description = "FDM_04 | Verify Web Description Tab fields on the basis of Medical Division and French Language", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void MedicalDivisiontest(Map<String, String> map) throws InterruptedException, IOException {
    	 PimHomepage pimHomepage = new LoginPage()
                 .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                 .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();
         pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).applyFilterInSearchPage(map.get("itemNumber"))
                 .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
         Thread.sleep(2000);
        WebObj.SelectLaungugeAndDivisionTypeDropDown(map.get("Division"),map.get("Language"));
        VerifyWebDescriptionFields();
    }
  //FDM_05
    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(priority = 2,description = "Verify Web Description Tab fields on the basis of Zahn Division and French Language", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void ZahnDivisiontest(Map<String, String> map) throws InterruptedException, IOException {
    	PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).applyFilterInSearchPage(map.get("itemNumber"))
                .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        Thread.sleep(2000);
        WebObj.SelectLaungugeAndDivisionTypeDropDown(map.get("Division"),map.get("Language"));
        VerifyWebDescriptionFields();
    }
    
  //FDM_06 and //FDM_07
    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(priority = 3,description = "FDM_06 and FDM_07 | Verify Web Description Tab fields on the basis of Special Market Division and French Language", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class,groups = {"CA","WEB_DESCRIPTION"})
    public void SpecialMarketDivisiontest(Map<String, String> map) throws InterruptedException, IOException {
    	PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).applyFilterInSearchPage(map.get("itemNumber"))
                .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        Thread.sleep(2000);
        WebObj.SelectLaungugeAndDivisionTypeDropDown(map.get("Division"),map.get("Language"));
        VerifyWebDescriptionFields();
    }

}