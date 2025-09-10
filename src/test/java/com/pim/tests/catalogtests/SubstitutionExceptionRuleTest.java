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
import com.pim.pages.GlobalAttributePage;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.pim.reports.FrameworkLogger.log;

public class SubstitutionExceptionRuleTest extends BaseTest {
    ReferencesPage referencesPage = new ReferencesPage();
    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
    GlobalAttributePage globalattribute = new GlobalAttributePage();
    CatalogTypePage catalogTypePage = new CatalogTypePage();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    AllCatalogsPage allCatalogsPage=new AllCatalogsPage();
    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
    List<String> medical_user_driven;
    List<String> dental_user_driven;
    List<String> special_market_user_driven;
    List<String> special_market_catalogs;
    Javautils javautils = new Javautils();

    //CATR_187
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_187 | Verification Of Substitution Exception Rule In Medical Divisions where parent and child manu code is not same", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithChildManuCodeIsNotSameAsParent(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item in US catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        //assigning medical catalog
        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_188
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_188 | Verification Of Substitution Exception Rule In Dental Divisions when parent item Manu code is same as child", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentManuCodeIsSameAsChild(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.clickReferenceTypeHeader().getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        //assigning dental catalog
        productdetail.selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("userDriven"));
		BasePage.WaitForMiliSec(3000);
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDriven"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clearUserDriven(map.get("SMUserDriven"));
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        BasePage.WaitForMiliSec(180000);
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

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
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown(map.get("AllCatalogtab"));
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        pimHomepage.clickLogoutButton();
    }

    //CATR_189
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_189 | Verification Of Substitution Exception Rule In Dental Divisions when child item Manu code is not same as Parent", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithChildManuCodeIsNotSameAsParent(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        //assigning dental catalog
        productdetail.selectTabfromDropdown(map.get("DentalTab"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_190
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_190 | Verification Of Substitution Exception Rule In Medical Divisions where parent and child manu code is not same", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithBothParentChildManuCodeIsNotSame(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        //assigning medical catalog
        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).containsAnyOf(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_191
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_191 | Verification Of Substitution Exception Rule In Dental Divisions where parent and child manu code is not ORAPHA", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentAndChildManuCodeIsNotORAPHA(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        //assigning dental catalog
        productdetail.selectTabfromDropdown(map.get("DentalTab"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_192
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_192 | Verification Of Substitution Exception Rule In Medical Divisions where parent manu code is not same as child", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithParentManuCodeIsNotSameAsChild(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        //assigning medical catalog
        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_193
    @PimFrameworkAnnotation(module = Modules.DENTAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_193 | Verification Of Substitution Exception Rule In Dental Divisions where parent and child manu code is ORAPHA", groups = {"US","pim","DENTAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInDentalCatalogWithParentAndChildManuCodeIsORAPHA(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        //assigning dental catalog
        productdetail.selectTabfromDropdown(map.get("DentalTab"));
        log(LogType.INFO, "Navigate to Dental Catalog Tab to assign Dental catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        dental_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(dental_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).doesNotContain(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("DentalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_194
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_194 | Verification Of Substitution Exception Rule In Medical Divisions", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithParentManuCodeIsNotORAPHAAndChildIsORAPHA(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        productdetail.selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Parent");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isNotEqualTo(map.get("ManufactureCode"));

        //assigning medical catalog
        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //assigning feeddealer in special markets catalog
        productdetail.selectTabfromDropdown("Special Markets Catalog");
        log(LogType.INFO, "Navigate to Special Markets Catalog Tab to assign FEDDEALER catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("SMUserDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        special_market_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(special_market_user_driven).containsAnyOf(map.get("SMUserDriven"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global attribute tab to verify the Manufacture code in Master Catalog");
        productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("AllCatalogsTab");
        special_market_catalogs = allCatalogsPage.getAllSpecialMarketsCatalogList();
        Assertions.assertThat(special_market_catalogs).doesNotContain(map.get("SMUserDriven"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_209
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.SubstituteExceptionRule)
    @Test(description = "CATR_209 | Verification Of Substitution Exception Rule In Medical Divisions for MPC143", groups = {"US","pim","MEDICAL_CATALOG"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verificationOfSubstitutionExceptionRuleInMedicalCatalogWithChildManuAsMPC143(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();

        //verifying in parent item in US catalog
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("ReferenceTab"));
        String referenceType = referencesPage.getReferenceType();
        Assertions.assertThat(referenceType).isEqualTo(map.get("ReferenceType"));
        String referenceObjectNumber = referencesPage.getReferenceObjectNumber();

        //assigning medical catalog
        productdetail.selectTabfromDropdown(map.get("MedicalTab"));
        log(LogType.INFO, "Navigate to Medical Catalog Tab to assign Medical catalog in Parent");
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDrivenAssert"));

        //verify quality status
        productdetail.selectTabfromDropdown(map.get("Qualitystatustab"));
        List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).getRuleNamesByRuleStatus(map.get("Status"));
        List<String> myList = new ArrayList<String>(Arrays.asList(map.get("CatalogEntityRule").split(",")));
        Assertions.assertThat(catalogentityrule).isEqualTo(myList);
        log(LogType.INFO,"Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to Medical catalog of Master Catalog");
        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));

        //verifying in child item
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(referenceObjectNumber).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
        log(LogType.INFO, "Navigate to Global Attribute Tab to verify Manufacture Code in Child");

        //verifying manufacture code
        String manufacturecode = globalattribute.getManufacturercode();
        Assertions.assertThat(manufacturecode).isEqualTo(map.get("ManufactureCode"));

        productdetail.selectTabfromDropdown("TabName");
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).isEqualTo(map.get("userDrivenAssert"));

        //Deleting userdriven in parent
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ParentItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("MedicalTab"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO,"Deleting the Userdriven");
        pimHomepage.clickLogoutButton();

    }

}