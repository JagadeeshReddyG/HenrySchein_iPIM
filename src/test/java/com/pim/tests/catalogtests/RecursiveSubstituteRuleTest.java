package com.pim.tests.catalogtests;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.AllCatalogsPage;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.ReferencesPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class RecursiveSubstituteRuleTest extends BaseTest {
    ReferencesPage referencesPage = new ReferencesPage();
    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
    CatalogTypePage catalogTypePage = new CatalogTypePage();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
    String referenceObjectNumber;
    //List<String> medical_user_driven;
    //List<String> dental_user_driven;
    //List<String> zahn_user_driven;
    //List<String> special_market_user_driven;
    //List<String> special_market_catalogs;
    Javautils javautils = new Javautils();

    //CATR_195
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
    @Test(description = "CATR_195 | Verification Of Recursive Substitution Rule In Dental Divisions CATR_195", groups = {"US", "CA","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfRecursiveSubstitutionRuleInDentalCatalog(Map<String, String> map) { PimHomepage pimHomepage = new LoginPage()
            .enterUserName(ExcelUtils.getLoginData().get("Dental User").get("UserName"))
            .enterPassword(ExcelUtils.getLoginData().get("Dental User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

        //assigning dental catalog in parent
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("userDriven"));
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        List<String> dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
        for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
        {
            Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
        }
        qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to Dental Catalog tab to verify the Dental userDriven field in Master Catalog");
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item 1
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

        log(LogType.INFO, "Navigate to Dental catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        productdetail.selectTabfromDropdown(map.get("TabName"));
        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Dental userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        dental_user_driven = allCatalogsPage.getAllDentalCatalogList();
        Assertions.assertThat(dental_user_driven).contains(map.get("userDrivenAssert"));

        //verifying in child item 2
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Dental catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        dental_user_driven = allCatalogsPage.getAllDentalCatalogList();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //Deleting userDriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the UserDriven");
        pimHomepage.clickLogoutButton();

    }

    //CATR_196
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
    @Test(description = "CATR_196 | Verification Of Recursive Substitution Rule In Medical Divisions CATR_196", groups = {"US", "CA","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfRecursiveSubstitutionRuleInMedicalCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

        //assigning medical catalog in parent
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("userDriven"));
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        List<String>  MedicalUserDriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(MedicalUserDriven).containsAnyOf(map.get("userDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
        for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
        {
            Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
        }
        qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to Medical Catalog tab to verify the medical user driven field in Master Catalog");
        List<String> MasterUserDriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(MasterUserDriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item 1
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

        log(LogType.INFO, "Navigate to Medical catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        productdetail.selectTabfromDropdown(map.get("TabName"));
        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Medical userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        List<String> medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
        Assertions.assertThat(medical_user_driven).contains(map.get("userDrivenAssert"));

        //verifying in child item 2
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Medical catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Medical userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //Deleting userDriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the UserDriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_197 - Child items will not inherit user driven catalogs for when verifying recursive substitution rule in zahn division
    @PimFrameworkAnnotation(module = Modules.ZAHN_CATALOG, category = CategoryType.Valid_Failure)
    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
    @Test(description = "CATR_197 | Verification Of Recursive Substitution Rule In Zahn Division", groups = {"US", "CA","pim","ZAHN_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfRecursiveSubstitutionRuleInZahnCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Zahn User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Zahn User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

        //assigning Zahn catalog in parent
        productdetail.selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearUserDriven(map.get("userDriven"));
        BasePage.WaitForMiliSec(5000);
        log(LogType.INFO, "Navigate to Zahn Catalog Tab to assign Zahn catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        List<String> zahn_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(zahn_user_driven).containsAnyOf(map.get("userDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
        for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
        {
            Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
        }
        qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to Zahn Catalog tab to verify the zahn user driven field in Master Catalog");
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item 1
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

        log(LogType.INFO, "Navigate to Zahn catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        productdetail.selectTabfromDropdown(map.get("TabName"));
        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild1).doesNotContain(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Zahn userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        zahn_user_driven = allCatalogsPage.getAllZahnCatalogList();
        Assertions.assertThat(zahn_user_driven).doesNotContain(map.get("userDriven"));

        //verifying in child item 2
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Zahn catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild2).doesNotContain(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Zahn userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        zahn_user_driven = allCatalogsPage.getAllZahnCatalogList();
        Assertions.assertThat(zahn_user_driven).doesNotContain(map.get("userDriven"));

        //Deleting userDriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the UserDriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_198
    @PimFrameworkAnnotation(module = Modules.SPECIAL_MARKET_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
    @Test(description = "CATR_198 | Verification Of Recursive Substitution Rule In SM Divisions", groups = {"US", "CA","pim","SPECIAL_MARKET_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfRecursiveSubstitutionRuleInSMCatalog(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Special Markets User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Special Markets User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 1" +" " +referenceObjectNumber);

        //assigning special market catalog in parent
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to SM Catalog Tab to assign SM catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("userDriven"));
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        BasePage.WaitForMiliSec(5000);
        List<String> special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        BasePage.WaitForMiliSec(5000);
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("userDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
        for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
        {
            Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
        }
        qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to sm catalog tab to verify the sm user driven field in Master Catalog");
        BasePage.WaitForMiliSec(150000);
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        BasePage.WaitForMiliSec(2000);
        System.out.println("masterUserdriven" + masterUserdriven);
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item 1
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item 2" +" " +referenceObjectNumber);

        log(LogType.INFO, "Navigate to special market catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        productdetail.selectTabfromDropdown(map.get("TabName"));
        List<String> RuleDrivenForChild1 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild1).contains(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Special market userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        special_market_user_driven = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_user_driven).contains(map.get("userDrivenAssert"));

        //verifying in child item 2
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to special market catalog Tab to verify assigned user driven for parent is inherited to rules in child");
        List<String> RuleDrivenForChild2 = catalogTypePage.getRuleDrivenCatalogs();
        Assertions.assertThat(RuleDrivenForChild2).contains(map.get("userDrivenAssert"));

        log(LogType.INFO, "Navigate to All Catalogs Tab to verify Special market userDriven field");
        productdetail.selectTabfromDropdown(map.get("AllCatalogsTab"));
        special_market_user_driven = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_user_driven).contains(map.get("userDrivenAssert"));

        //Deleting userDriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the UserDriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_204
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.RecursiveSubstituteRule)
    @Test(description = "CATR_204 | Verification Of Recursive Substitution Rule In Medical Divisions when child catalog exists ehrn parent catalog delete", groups = {"US", "CA","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfRecursiveSubstitutionRuleWhetherChildCatalogExistAfterParentCatalogDeletion(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        log(LogType.INFO, "Navigate to Reference Tab to fetch child item");
        referenceObjectNumber = referencesPage.getReferenceObjectNumberByEndDate();

        //assigning medical catalog in parent
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign medical catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("userDriven"));
        BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        List<String> medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
        for (String Rules:javautils.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
        {
            Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);
        }
        qualitystatus.minimizeQualityStatusTab();
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to medical catalog tab to verify the medical userdriven field in Master Catalog");
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogsTab"));
        log(LogType.INFO, "Navigate to All catalogs tab");

        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

        //Deletion of user driven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).doesNotContain(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");

        //verifying in child item whether it still exists
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("AllCatalogsTab"));
        log(LogType.INFO, "Navigate to All catalogs tab to see whether deleted catalog still exist after deletion in parent.");

        medical_user_driven = allCatalogsPage.getAllMedicalCatalogList();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));
        pimHomepage.clickLogoutButton();
    }
}
