package com.pim.tests.Prices;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.*;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Map;

public class Price extends BaseTest {

    LocalAttributePage localAttributePage = new LocalAttributePage();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    QualityStatusPage qualityStatusPage = new QualityStatusPage();
    SeoTabPage seoTabPage = new SeoTabPage();
    GlobalAttributePage globalAttributePage = new GlobalAttributePage();
    WebDescription webDescription = new WebDescription();
    QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
    PimHomepage pimHomepage = new PimHomepage();
    LoginPage loginPage = new LoginPage();
    MainMenu mainMenu = new MainMenu();
    PricePage pricePage = new PricePage();

    @PimFrameworkAnnotation(module = Modules.Prices, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.Prices)
    @Test(description = "ListPrice_001 | Verify local attribute changes for a SpecialMarket item updated in respective table when the items local attribute are modified in PIM  pre-requisite", groups = {"US","pim","LOCAL_ATTRIBUTE"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verify_List_Price_JSON_Verification_For_An_Item(
            Map<String, String> map) {

        FileUtils.storeCurrentTimeInJsonTimesPropertiesFile(map.get("TestCaseName")+map.get("itemNumber"));

        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType"))
                .selectCatalogType(map.get("CatalogType")).enterHsiItemNumber(map.get("itemNumber")).clickSeachButton()
                .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        productDetailSearchPage.maximizeProductDetailTab();

        localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValueWithNoContent"))
                .MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValueWithNoContent"))
                .itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValueWithNoContent"))
                .selectValuefromSaveDropdown(map.get("saveButton"));
        productDetailSearchPage.clickRefreshIcon();

        BasePage.WaitForMiliSec(5000);
        // Adding The valid Value in Local Attribute Filed
        localAttributePage.brandFieldAttribute(map.get("LocalAttributeBrandFieldValue"))
                .MandatoryItemFieldAttribute(map.get("LocalAttributeItemFieldValue"))
                .itemTypeFieldAttribute(map.get("LocalAttributeitemTypeFieldValue"))
                .selectValuefromSaveDropdown(map.get("saveButton"));
        productDetailSearchPage.clickRefreshIcon();

        String itemValue = localAttributePage.getItemValue().trim();
        Assertions.assertThat(itemValue).isEqualTo(map.get("LocalAttributeItemFieldValue"));
        String itemTypeValue = localAttributePage.getItemTypeValue().trim();
        Assertions.assertThat(itemTypeValue).isEqualTo(map.get("LocalAttributeitemTypeFieldValue"));
        String brandValue = localAttributePage.getBrandValue().trim();
        Assertions.assertThat(brandValue).isEqualTo(map.get("LocalAttributeBrandFieldValue"));



    }


}
