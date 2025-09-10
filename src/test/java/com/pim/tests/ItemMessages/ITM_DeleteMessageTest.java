package com.pim.tests.ItemMessages;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.ClassificationsPage;
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
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.pim.reports.FrameworkLogger.log;

import java.util.Map;

public class ITM_DeleteMessageTest extends BaseTest {
    StructureSubMenu structuresubmenu=new StructureSubMenu();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
    ClassificationsPage classificationPage=new ClassificationsPage();
	ActionsPage action = new ActionsPage();


    private ITM_DeleteMessageTest(){
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(description = "ITM_154_US | Verify deletion of Primary Taxonomy assigned message for an item in classifications tab for US",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyPrimaryTaxonomyMessageDeletionInUS(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //Verify deletion of primary taxonomy item message in US Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        String actualPrimaryTaxonomyMessageInUS = classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        classificationPage.removeButton();
        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyPrimaryTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of primary taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyPrimaryTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyPrimaryTaxonomyIsVisibleForUS() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("PrimaryTaxonomy")).isTrue();
    }

        @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(description = "ITM_154_CA | Verify deletion of Primary Taxonomy assigned message for an item in classifications tab for CA",groups = {"CA","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyPrimaryTaxonomyMessageDeletionInCA(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

        //Verify deletion of primary taxonomy item message in CA Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        classificationPage.removeButton();
        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyPrimaryTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of primary taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyPrimaryTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyPrimaryTaxonomyIsVisibleForCA() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("PrimaryTaxonomy")).isTrue();
    }


    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(description = "ITM_155_US | Verify deletion of ecommerce Taxonomy assigned message for an item in classifications tab for US",groups = {"US","pim","ITEM_MESSAGES"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEcommerceTaxonomyMessageDeletionInUS(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

        //Verify deletion of Ecommerce taxonomy item message in US Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        classificationPage.removeButton();
        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyEcommerceTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of Ecommerce taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyEcommerceTaxonomyIsVisibleForUS();
        classificationPage.clickOnCloseExpandButton();
    }
    public void verifyEcommerceTaxonomyIsVisibleForUS() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("EcommerceTaxonomy")).isTrue();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ItemErrorMessageTestData)
    @Test(description = "ITM_155_CA | Verify deletion of ecommerce Taxonomy assigned message for an item in classifications tab for CA", groups = {"US","pim","ITEM_MESSAGES"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEcommerceTaxonomyMessageDeletionInCA(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

        //Verify deletion of Ecommerce taxonomy item message in CA Catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        classificationPage.removeButton();
        classificationPage.clickYes();
        Assertions.assertThat(classificationPage.isPermissionErrorMessageVisible(map.get("permissionErrorMessage"))).isTrue();
		log(LogType.EXTENTANDCONSOLE, map.get("permissionErrorMessage") + " error message appears");
        classificationPage.closePermissionRequiredErrorMessage();
        verifyEcommerceTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();

        //Verify deletion of Ecommerce taxonomy item message in MASTER Catalog
        pimHomepage.mainMenu().clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationPage.clickOnExpandButton();
        classificationPage.clickClassificationItem(map.get("TaxnomyType"),map.get("ClassificationItem"));
        verifyEcommerceTaxonomyIsVisibleForCA();
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }
    public void verifyEcommerceTaxonomyIsVisibleForCA() {
        Assertions.assertThat(classificationPage.isAllAttributesVisible("EcommerceTaxonomy")).isTrue();
    }
}
