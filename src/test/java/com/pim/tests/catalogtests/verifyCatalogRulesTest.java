package com.pim.tests.catalogtests;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogAssignmentPage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QueriesSubMenu;
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


public class verifyCatalogRulesTest extends BaseTest {
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    ClassificationsPage classificationPage = new ClassificationsPage();
    MainMenu mainmenu = new MainMenu();
    CatalogAssignmentPage CatalogAssignmentPage = new CatalogAssignmentPage();
    QueriesSubMenu queriesSubMenu = new QueriesSubMenu();
    BasePage basePage = new BasePage();
    Javautils javaUtil = new Javautils();

    private verifyCatalogRulesTest() {
    }

    //CATR_222, CATR_223, CATR_224
    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
    @Test(description = "CATR_222, CATR_223, CATR_224 | To add and verify dental user driven catalog rules for US", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","AddCatalogs"})
    public void selectDentalUserDrivenCatalogAndExceptionListForUS(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

        //Search for Multiple Items from Queries
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));
        List<String> itemCodeList = javaUtil.readMultipleValuesFromExcel(map.get("ItemNumber"));
        for (int i = 0; i <= itemCodeList.size() - 1; i++) {
            queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
        }
        basePage.WaitForMiliSec(3000);
        basePage.clickAndHoldCtrlKey();
        CatalogAssignmentPage.selectMultipleItemCodes();
        basePage.releaseCtrlKey();
        classificationPage.clickOnExpandButton();
        CatalogAssignmentPage.SelectDentalUserDrivenCatalog(map.get("UserDriven"));
        CatalogAssignmentPage.SelectDentalExceptionList(map.get("ExceptionList"));
        productDetailSearchPage.clickMenuRefreshIcon();
        classificationPage.clickOnCloseExpandButton();

        //Verify catalog user driven and exception list assignment in master
        mainmenu.clickRefreshMenuIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber1")).clickSeachButton().clickOnFirstResult();
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Dental Catalog"));
        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
            Assertions.assertThat(userDrivenForDental).containsAnyOf(map.get("UserDriven"));
            List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
            Assertions.assertThat(ExceptionListForDental).containsAnyOf(map.get("ExceptionList"));
            productDetailSearchPage.selectTabfromDropdown(map.get("All Catalogs"));
            productDetailSearchPage.clickMenuRefreshIcon();
            List<String> allCatalog = CatalogAssignmentPage.getConsolidatedDentalCatalogList();
            List<String> myList = new ArrayList<String>(Arrays.asList(map.get("ConsolidatedCatalogs").split(",")));
            //Assertions.assertThat(allCatalog).isEqualTo(myList);
            Assertions.assertThat(myList).allMatch(e -> allCatalog.contains(e));

        pimHomepage.clickLogoutButton();
    }

    //CATR_222, CATR_223, CATR_224 For CA
    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
    @Test(description = "CATR_222, CATR_223, CATR_224 For CA| To add and verify dental user driven catalog rules for CA", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
    public void selectDentalUserDrivenCatalogAndExceptionListForCA(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();

        //Search for Multiple Items from Queries
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"));
        List<String> itemCodeList = javaUtil.readMultipleValuesFromExcel(map.get("ItemNumber"));
        for (int i = 0; i <= itemCodeList.size() - 1; i++) {
            queriesSubMenu.enterHsiItemNumber(itemCodeList.get(i)).clickSeachButton();
        }
        basePage.WaitForMiliSec(3000);
        basePage.clickAndHoldCtrlKey();
        CatalogAssignmentPage.selectMultipleItemCodes();
        basePage.releaseCtrlKey();
        classificationPage.clickOnExpandButton();
        CatalogAssignmentPage.SelectDentalUserDrivenCatalog(map.get("UserDriven"));
        CatalogAssignmentPage.SelectDentalExceptionList(map.get("ExceptionList"));
        productDetailSearchPage.clickMenuRefreshIcon();
        classificationPage.clickOnCloseExpandButton();

        //Verify catalog user driven and exception list assignment in master
        productDetailSearchPage.clickMenuRefreshIcon();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber1")).clickSeachButton().clickOnFirstResult();
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Dental Catalog"));
        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
        Assertions.assertThat(userDrivenForDental).containsAnyOf(map.get("UserDriven"));
        List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
        Assertions.assertThat(ExceptionListForDental).containsAnyOf(map.get("ExceptionList"));
        productDetailSearchPage.selectTabfromDropdown(map.get("All Catalogs"));
        productDetailSearchPage.clickMenuRefreshIcon();
        List<String> allCatalog = CatalogAssignmentPage.getConsolidatedDentalCatalogList();
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("ConsolidatedCatalogs").split(",")));
        Assertions.assertThat(allCatalog).isEqualTo(myList);

        pimHomepage.clickLogoutButton();
    }


    //CATR_226
    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
    @Test(description = "CATR_226 | verify tabs namely CanadianZahnCatalogs,CanadianDentalCatalogs and CanadianMedicalCatalogTab for CA english user", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
    public void verifyAllDivisionTabsForCA_EnglishUser(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Canadian Zahn Catalogs"));
        verifyAllDivisionsTabForCA_EnglishUser();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllDivisionsTabForCA_EnglishUser() {
        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("ZahnCatalogCanadianTab")).isTrue();
        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("MedicalCatalogCanadianTab")).isTrue();
        Assertions.assertThat(CatalogAssignmentPage.isAllAttributesVisible("DentalCatalogCanadianTab")).isTrue();
    }

    //CATR_225, CATR_227, CATR_228, CATR_229
    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
    @Test(description = "CATR_225, CATR_227, CATR_228, CATR_229 | Verify user driven and exception catalog list for Dental, Medical and Zahn canadian catalogs", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
    public void VerifyUserDrivenCatalogAndExceptionalCatalogList(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();

        //Verify User Driven and Exception List for Canadian Dental Catalog
        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Dental Catalogs"));
        List<String> allCatalogForUserDrivenDental =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
        List<String> myListUDDental = new ArrayList<String>(Arrays.asList(map.get("UserDrivenDental").split(",")));
        Assertions.assertThat(allCatalogForUserDrivenDental).isEqualTo(myListUDDental);
        classificationPage.clickOnExpandButton();
        List<String> allCatalogForExceptionListDental =CatalogAssignmentPage.clearAndVerifyExceptionList();
        List<String> myListELDental = new ArrayList<String>(Arrays.asList(map.get("ExceptionListDental").split(",")));
        Assertions.assertThat(allCatalogForExceptionListDental).isEqualTo(myListELDental);
        classificationPage.clickOnCloseExpandButton();

        //Verify User Driven and Exception List for Canadian Medical Catalog
        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Medical Catalogs"));
        List<String> allCatalogForUserDrivenMedical =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
        List<String> myListUDMedical = new ArrayList<String>(Arrays.asList(map.get("UserDrivenMedical").split(",")));
        Assertions.assertThat(allCatalogForUserDrivenMedical).isEqualTo(myListUDMedical);
        classificationPage.clickOnExpandButton();
        List<String> allCatalogForExceptionListMedical =CatalogAssignmentPage.clearAndVerifyExceptionList();
        List<String> myListELMedical = new ArrayList<String>(Arrays.asList(map.get("ExceptionListMedical").split(",")));
        Assertions.assertThat(allCatalogForExceptionListMedical).isEqualTo(myListELMedical);
        classificationPage.clickOnCloseExpandButton();

        //Verify User Driven and Exception List for Canadian Zahn Catalog
        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Zahn Catalogs"));
        List<String> allCatalogForUserDrivenZahn =  CatalogAssignmentPage.clearAndVerifyUserDrivenList();
        List<String> myListUDZahn = new ArrayList<String>(Arrays.asList(map.get("UserDrivenZahn").split(",")));
        Assertions.assertThat(allCatalogForUserDrivenZahn).isEqualTo(myListUDZahn);
        classificationPage.clickOnExpandButton();
        List<String> allCatalogForExceptionListZahn =CatalogAssignmentPage.clearAndVerifyExceptionList();
        List<String> myListELZahn = new ArrayList<String>(Arrays.asList(map.get("ExceptionListZahn").split(",")));
        Assertions.assertThat(allCatalogForExceptionListZahn).isEqualTo(myListELZahn);
        classificationPage.clickOnCloseExpandButton();
        pimHomepage.clickLogoutButton();
    }

    //CATR_230
    @PimFrameworkAnnotation(module = Modules.AddCatalogs, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.AddCatalogs)
    @Test(description = "CATR_230 | Verify Catalog not part of medical division should not be present as part of user driven/exceptional catalog list", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","AddCatalogs"})
    public void VerifyCatalogNotPartOfMedicalShouldNotBePresent(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult();
        productDetailSearchPage.selectTabfromDropdown(map.get("Canadian Medical Catalogs"));
        List<String> userDrivenForDental = CatalogAssignmentPage.getUserDrivenDentalCatalogs();
        Assertions.assertThat(userDrivenForDental).doesNotContain(map.get("UserDriven"));
        log(LogType.INFO,"Medical User Driven values does not contain Dental Values");
        List<String> ExceptionListForDental = CatalogAssignmentPage.getExceptionListForDentalCatalog();
        Assertions.assertThat(ExceptionListForDental).doesNotContain(map.get("ExceptionList"));
        log(LogType.INFO,"Medical Exception List values does not contain Dental Values");
        pimHomepage.clickLogoutButton();
    }
}