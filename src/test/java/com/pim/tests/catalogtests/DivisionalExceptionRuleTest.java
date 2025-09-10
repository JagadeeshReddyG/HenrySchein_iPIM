package com.pim.tests.catalogtests;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QualityStatusPage;
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

public class DivisionalExceptionRuleTest extends BaseTest {
    CatalogTypePage catalogTypePage = new CatalogTypePage();
    ProductDetailSearchPage productdetail = new ProductDetailSearchPage();
    QueriesSubMenu queriessubmenu = new QueriesSubMenu();
    QualityStatusPage qualitystatus = new QualityStatusPage();
    GlobalAttributePage globalattribute = new GlobalAttributePage();
    Javautils javautil = new Javautils();
    List<String> medical_user_driven;

    //CATR_127
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
    @Test(description = "CATR_127 | Verify whether the rule driven catalogs were removed for MPC 143 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
    public void verify_rule_driven_catalog_for_medical_division_not_showing_up_for_MPC_143(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        log(LogType.INFO, "Navigate to medical catalog tab to verify rule driven");

        //verifying MPC code
        String mpccode = productdetail.getItemField(map.get("fieldIndex"));
        Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
        log(LogType.INFO, "Verifying MPC code");

        //assigning medical catalog
        catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
        pimHomepage.productDetailSearchPage().clickRefreshIcon();
        medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
        Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

        //verify rule driven
        String empty_rule_driven = catalogTypePage.getEmptyRuleDriven();
        Assertions.assertThat(empty_rule_driven).isEqualTo(map.get("RuleDriven"));

        //verify quality status
        pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
        qualitystatus.sortRulestByLastExecution();
        List<String> catalogentityrule = qualitystatus.getRuleNamesByRuleStatus(map.get("Status"));
        Assertions.assertThat(catalogentityrule).containsAnyOf(map.get("CatalogEntityRule"));
        log(LogType.INFO, "Verifying catalog entity rule");

        //verify in master
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
        log(LogType.INFO, "Navigate to medical catalog tab to verify the userdriven and rule driven in Master Catalog");

        //verifying MPC code
        mpccode = productdetail.getItemField(map.get("fieldIndex"));
        Assertions.assertThat(mpccode).isEqualTo(map.get("MPC"));
        log(LogType.INFO, "Verifying MPC code");

        List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
        String masterRuledriven = catalogTypePage.getEmptyRuleDriven();
        Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));
        Assertions.assertThat(masterRuledriven).isEqualTo(map.get("RuleDriven"));

        //Deleting userdriven
        productdetail.clickMenuRefreshIcon();
        queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        catalogTypePage.clearSelectedFields(map.get("userDriven"));
        log(LogType.INFO, "Deleting the Userdriven");
        pimHomepage.clickLogoutButton();
    }

    //CATR_128
    @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
    @Test(description = " CATR_128 | Verify whether the user driven catalogs showing up INSOURCE & C340B catalog for M61/G61 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
    public void verify_user_driven_catalog_for_medical_division_showing_up_only_for_INSOURCE_C340B_for_M61_G61_Class(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

        List<String> itemcodelist = javautil.readMultipleValuesFromExcel(map.get("ItemNumber"));
        List<String> classificationcodelist = javautil.readMultipleValuesFromExcel(map.get("Classification"));
        List<String> alluserdrivenlist = javautil.readMultipleValuesFromExcel(map.get("allUserDriven"));
        List<String> userdrivenlist = javautil.readMultipleValuesFromExcel(map.get("userDriven"));
        for (int i = 0; i <= itemcodelist.size() - 1; i++) {
        	productdetail.clickMenuRefreshIcon();
            pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"));
            queriessubmenu.enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
            log(LogType.INFO, "Navigate to global attribute tab to verify classification code");

            //verifying classification code
            String classificationcode = globalattribute.getClassificationCode();
            Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
            log(LogType.INFO, "Verifying classification code");

            productdetail.selectTabfromDropdown(map.get("TabName"));
            log(LogType.INFO, "Navigate to Medical catalog to assign catalogs in userdriven");

            //assigning catalogs
            for(int j = 0; j <= alluserdrivenlist.size() - 1; j++) {
            	catalogTypePage.clearUserDriven(alluserdrivenlist.get(j));
        		BasePage.WaitForMiliSec(3000);
                catalogTypePage.clickUserDriven().selectUserDriven(alluserdrivenlist.get(j));
                pimHomepage.productDetailSearchPage().clickRefreshIcon();
            }
            medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
            Assertions.assertThat(medical_user_driven).containsAnyElementsOf(userdrivenlist);

          //verify quality status
			pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
			List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
			for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
			{
				Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
			}
			qualitystatus.minimizeQualityStatusTab();
			log(LogType.INFO,"Verifying catalog entity rule");

            //verify in master
            productdetail.clickMenuRefreshIcon();
            queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(itemcodelist.get(i)).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
            log(LogType.INFO, "Navigate to global attribute tab to verify classification code in Master Catalog");

            //verifying classification code
            classificationcode = globalattribute.getClassificationCode();
            Assertions.assertThat(classificationcode).isEqualTo(classificationcodelist.get(i));
            log(LogType.INFO, "Verifying classification code");

            productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
            log(LogType.INFO, "Navigate to Medical catalog to verify catalogs in userdriven for Master Catalog");

            List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
            Assertions.assertThat(masterUserdriven).containsAll(userdrivenlist);
			
        }
        pimHomepage.clickLogoutButton();
    }

        //CATR_129,CATR_130
        @PimFrameworkAnnotation(module = Modules.MEDICAL_CATALOG, category = CategoryType.REGRESSION)
        @TestDataSheet(sheetname = TestCaseSheet.DivisionalExceptionRule)
        @Test(description = "CATR_129,CATR_130 | Verify whether the rule driven catalogs were removed for S10 and S30 ClassItems based on the Divsional Exceptional Rule.", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"US","pim","MEDICAL_CATALOG"})
        public void verify_rule_driven_catalog_for_medical_division_not_showing_up_for_S10_S30_Class(Map < String, String > map){
            PimHomepage pimHomepage = new LoginPage()
                    .enterUserName(ExcelUtils.getLoginData().get("Medical User").get("UserName"))
                    .enterPassword(ExcelUtils.getLoginData().get("Medical User").get("Password")).clickLoginButton();

            pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
                    .enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
            log(LogType.INFO, "Navigate to global attribute tab to verify classification code");

            //verifying classification code
            String classificationcode = globalattribute.getClassificationCode();
            Assertions.assertThat(classificationcode).isEqualTo(map.get("Classification"));
            log(LogType.INFO, "Verifying classification code");

            productdetail.selectTabfromDropdown(map.get("TabName"));
            log(LogType.INFO, "Navigate to Medical catalog to assign Medical in userdriven");

            //assigning medical catalog
            catalogTypePage.clearUserDriven(map.get("userDriven"));
    		BasePage.WaitForMiliSec(3000);
            catalogTypePage.clickUserDriven().selectUserDriven(map.get("userDriven"));
            pimHomepage.productDetailSearchPage().clickRefreshIcon();
            medical_user_driven = catalogTypePage.getUserDrivenCatalogs();
            Assertions.assertThat(medical_user_driven).containsAnyOf(map.get("userDriven"));

            //verify rule driven
            String empty_rule_driven = catalogTypePage.getEmptyRuleDriven();
            Assertions.assertThat(empty_rule_driven).isEqualTo(map.get("RuleDriven"));

          //verify quality status
    		pimHomepage.productDetailSearchPage().selectTabfromDropdown(map.get("Qualitystatustab"));
    		List<String> catalogentityrule = qualitystatus.selectRuleTypeFromDropDown(map.get("Qualitystatus")).maximizeQualityStatusTab().sortRulestByLastExecution().getRuleNamesByRuleStatus(map.get("Status"));
    		for (String Rules:javautil.readMultipleValuesFromExcel(map.get("CatalogEntityRule")))
    		{
    			Assertions.assertThat(catalogentityrule).containsAnyOf(Rules);	
    		}
    		qualitystatus.minimizeQualityStatusTab();

            //verify in master
            productdetail.clickMenuRefreshIcon();
            queriessubmenu.selectItemType(map.get("ItemType")).selectCatalogType(map.get("MasterCatalog")).enterHsiItemNumber(map.get("ItemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("GlobalAttributeTab"));
            log(LogType.INFO, "Navigate to global attribute tab to verify classification code in Master Catalog");

            //verifying classification code
            classificationcode = globalattribute.getClassificationCode();
            Assertions.assertThat(classificationcode).isEqualTo(map.get("Classification"));
            log(LogType.INFO, "Verifying classification code");

            productdetail.selectTabfromDropdown(map.get("TabName")).clickRefreshIcon();
            log(LogType.INFO, "Navigate to Medical catalog to verify Medical in userdriven and empty ruledriven");

            List<String> masterUserdriven = catalogTypePage.getUserDrivenCatalogs();
            String masterRuledriven = catalogTypePage.getEmptyRuleDriven();
            Assertions.assertThat(masterUserdriven).containsAnyOf(map.get("userDriven"));
            Assertions.assertThat(masterRuledriven).isEqualTo(map.get("RuleDriven"));

            pimHomepage.clickLogoutButton();
        }


    }
