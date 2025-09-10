package com.pim.tests.ItemMessages;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.driver.DriverManager;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TextPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.DateandTimeUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

public class ITM_AttributesTest extends BaseTest {
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    ClassificationsPage classificationPage = new ClassificationsPage();
    MainMenu mainmenu = new MainMenu();
    TextPage textPage = new TextPage();
    StructureSubMenu structureSubMenu = new StructureSubMenu();
    private ITM_AttributesTest() {
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "Verification of Description and MessageType Attribute for Z new GlobalMessage in USCatalog compared to MasterCatalog", groups = {"US","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyDescriptionAndMessageTypeFeildEdits(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
		textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
//        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

		structureSubMenu.enterFilterBySerachGroupName(map.get("SearchValue")).selectSubCategory(map.get("SearchValue"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));
		
        //To Edit Description Feild
        classificationPage.clickOnExpandButton();
        productDetailSearchPage.clickRefreshIcon();
        String usCatalogDescription = textPage.verifyDescriptionAttributeMessage();
        classificationPage.clickOnCloseExpandButton();

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
//      textPage.SelectFilterButton();
//      productDetailSearchPage.clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));


        //Verify Entered Description in Master same as US
        String masterCatalogDescription = textPage.verifyMasterDescription();
        Assertions.assertThat(usCatalogDescription).isEqualTo(masterCatalogDescription);
        pimHomepage.clickLogoutButton();
    }


    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of Description and MessageType Attributes for Z Canada GlobalMessage in USCatalog compared to MasterCatalog", groups = {"CA","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyDescriptionAndMessageTypeFeildEditsForZCanada(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
//        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		structureSubMenu.enterFilterBySerachGroupName(map.get("SearchValue")).selectSubCategory(map.get("SearchValue"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));

        //To Edit Description Feild
        classificationPage.clickOnExpandButton();
        productDetailSearchPage.clickRefreshIcon();
        String usCatalogDescription = textPage.verifyDescriptionAttributeMessage();
        classificationPage.clickOnCloseExpandButton();

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
//        textPage.SelectFilterButton();
//        productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));

        //Verify Entered Description in Master same as CA
        String masterCatalogDescription = textPage.verifyMasterDescription();
        Assertions.assertThat(usCatalogDescription).isEqualTo(masterCatalogDescription);

        pimHomepage.clickLogoutButton();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of presence of message code attributes for Z new GlobalMessage in USCatalog compared to MasterCatalog", groups = {"US","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyAttributesPresence(Map<String, String> map) {
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        verifyAllITM_AttributesForUS();
        pimHomepage.clickLogoutButton();
    }
    public void verifyAllITM_AttributesForUS() {
        Assertions.assertThat(textPage.isAllAttributesVisible("codeNameAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("descriptionAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("keywordsAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("effectiveDateAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("expirationDateAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("optionAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("webCatalogAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("messageTypeAttribute")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("hazardousTypeAttribute")).isTrue();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of presence of message code attributes for Z Canada GlobalMessage in USCatalog compared to MasterCatalog", groups = {"CA","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyAttributesPresenceForZCanada(Map<String, String> map) {
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        verifyAllITM_AttributesForCA();
        pimHomepage.clickLogoutButton();

    }
    public void verifyAllITM_AttributesForCA(){
    Assertions.assertThat(textPage.isAllAttributesVisible("codeNameAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("descriptionAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("keywordsAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("effectiveDateAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("expirationDateAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("optionAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("webCatalogAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("messageTypeAttribute")).isTrue();
    Assertions.assertThat(textPage.isAllAttributesVisible("hazardousTypeAttribute")).isTrue();
}

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of Effective and Expiration Date for Z new GlobalMessage in USCatalog compared to MasterCatalog", groups = {"US","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEffectiveAndExpirationDate(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        //Verify Effective and Expiration date is displayed same as today's date in US and Master Catalog
        classificationPage.clickOnExpandButton();
        textPage.verifyEffectiveDateFeild();
        textPage.verifyExpirationDateFeild();
        classificationPage.clickOnCloseExpandButton();

        //compare effective and expiration date is same as modified date
        classificationPage.clickOnExpandButton();
        String EffectiveDateDisplayedInUS = DateandTimeUtils.getTodaysDateForQualityStatus();
        String modifiedEffectiveDateInUS =  textPage.getEffectiveDate();
        Assertions.assertThat(modifiedEffectiveDateInUS).contains(EffectiveDateDisplayedInUS);

        String ExpirationDateDisplayedInUS = DateandTimeUtils.getFutureDateFiveDaysAdded();
        String modifiedExpiryDateInUS = textPage.getExpirationDate();
        Assertions.assertThat(modifiedExpiryDateInUS).contains(ExpirationDateDisplayedInUS);
        classificationPage.clickOnCloseExpandButton();

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
        textPage.SelectFilterButton();
        productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        //compare effective and expiration modified date in master
        classificationPage.clickOnExpandButton();
        String modifiedEffectiveDateInMaster=  textPage.getEffectiveDate();
        Assertions.assertThat(modifiedEffectiveDateInMaster).contains(EffectiveDateDisplayedInUS);

        String modifiedExpiryDateInMASTER = textPage.getExpirationDate();
        Assertions.assertThat(modifiedExpiryDateInUS).contains(modifiedExpiryDateInMASTER);
        classificationPage.clickOnCloseExpandButton();

        pimHomepage.clickLogoutButton();

    }


    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of Effective and Expiration Date for Z Canada GlobalMessage in USCatalog compared to MasterCatalog", groups = {"CA","pim","ITEM_MESSAGE"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyEffectiveAndExpirationDateForZCanada(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        //Verify Effective and Expiration date is displayed same as today's date in US and Master Catalog
        textPage.verifyEffectiveDateFeild();
        classificationPage.clickOnExpandButton();
        textPage.verifyExpirationDateFeild();
        classificationPage.clickOnCloseExpandButton();

        //compare effective and expiration date is same as modified date
        classificationPage.clickOnExpandButton();
        String EffectiveDateDisplayedInUS = DateandTimeUtils.getTodaysDateForQualityStatus();
        String modifiedEffectiveDateInUS =  textPage.getEffectiveDate();
        Assertions.assertThat(modifiedEffectiveDateInUS).contains(EffectiveDateDisplayedInUS);

        String ExpirationDateDisplayedInUS = DateandTimeUtils.getFutureDateFiveDaysAdded();
        String modifiedExpiryDateInUS = textPage.getExpirationDate();
        Assertions.assertThat(modifiedExpiryDateInUS).contains(ExpirationDateDisplayedInUS);
        classificationPage.clickOnCloseExpandButton();

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
        textPage.SelectFilterButton();
        productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        //compare effective and expiration modified date in master
        classificationPage.clickOnExpandButton();
        String modifiedEffectiveDateInMaster=  textPage.getEffectiveDate();
        Assertions.assertThat(modifiedEffectiveDateInMaster).contains(EffectiveDateDisplayedInUS);

        String modifiedExpiryDateInMASTER = textPage.getExpirationDate();
        Assertions.assertThat(modifiedExpiryDateInUS).contains(modifiedExpiryDateInMASTER);
        classificationPage.clickOnCloseExpandButton();

        pimHomepage.clickLogoutButton();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of Options and WebCatalog dropdown values for Z new GlobalMessage in USCatalog compared to MasterCatalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class
    ,groups = {"CA","pim","ITEM_MESSAGE"})
    public void VerifyDropdownTypeValuesSelectionFeilds(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
//        pimHomepage.productDetailSearchPage().6(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        structureSubMenu.enterFilterBySerachGroupName(map.get("SearchValue")).selectSubCategory(map.get("SearchValue"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));

        classificationPage.clickOnExpandButton();
        productDetailSearchPage.clickRefreshIcon();
        textPage.verifyOptionsTypeDropDownValues(map.get("Option"));
        classificationPage.clickOnCloseExpandButton();
        productDetailSearchPage.clickRefreshIcon();
        textPage.verifyWebCatalogTypeDropDownValues(map.get("Web Catalog"));
        classificationPage.clickOnExpandButton();

        String usCatalogModifiedOptionsValue = textPage.getOptionsAttribute();
        Assertions.assertThat(usCatalogModifiedOptionsValue).isEqualTo(map.get("Option"));
        String usCatalogModifiedWebCatalogValue = textPage.getWebCatalogAttribute();
        Assertions.assertThat(usCatalogModifiedWebCatalogValue).isEqualTo(map.get("Web Catalog"));
        classificationPage.clickOnCloseExpandButton();

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
//      textPage.SelectFilterButton();
//      productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
		pimHomepage.productDetailSearchPage().clickOnSearchResult(map.get("MessageCode")).selectTabfromDropdown(map.get("TabName"));

        String masterCatalogModifiedOptionsValue = textPage.getOptionsAttribute();
        String masterCatalogModifiedWebCatalogValue = textPage.getWebCatalogAttribute();
        Assertions.assertThat(masterCatalogModifiedOptionsValue).isEqualTo(map.get("Option"));
        Assertions.assertThat(masterCatalogModifiedWebCatalogValue).isEqualTo(map.get("Web Catalog"));
        pimHomepage.clickLogoutButton();
    }


    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of Options and WebCatalog dropdown values for Z Canada GlobalMessage in USCatalog compared to MasterCatalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class
    , groups = {"CA","pim","ITEM_MESSAGE"})
    public void VerifyDropdownTypeValuesSelectionForZCanada(Map<String, String> map) {
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        textPage.verifyOptionsTypeDropDownValues(map.get("Option"));
        classificationPage.clickOnExpandButton();
        textPage.verifyCAWebCatalogTypeDropDownValues(map.get("Web Catalog"));
        classificationPage.clickOnCloseExpandButton();

        String usCatalogModifiedOptionsValue = textPage.getOptionsAttribute();
        Assertions.assertThat(usCatalogModifiedOptionsValue).isEqualTo(map.get("Option"));
        String usCatalogModifiedWebCatalogValue = textPage.getCAWebCatalogAttribute();
        Assertions.assertThat(usCatalogModifiedWebCatalogValue).isEqualTo(map.get("Web Catalog"));

        //Switch to Master Type
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
        textPage.SelectFilterButton();
        productDetailSearchPage.clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        String masterCatalogModifiedOptionsValue = textPage.getOptionsAttribute();
        String masterCatalogModifiedWebCatalogValue = textPage.getCAWebCatalogAttribute();
        Assertions.assertThat(masterCatalogModifiedOptionsValue).isEqualTo(map.get("Option"));
        Assertions.assertThat(masterCatalogModifiedWebCatalogValue).isEqualTo(map.get("Web Catalog"));
        pimHomepage.clickLogoutButton();

    }

    //To Verify Hazardous Value is displayed as 'NO'
    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | Verification of hazardous value is displayed as 'NO'", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class, groups = {"CA","pim","ITEM_MESSAGE"})
    public void verifyHazardousValueAsNO(Map<String, String> map) {
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        String hazardousValueDisplayed = textPage.getHazardousAttribute();
        Assertions.assertThat(hazardousValueDisplayed).isEqualTo(map.get("Hazardous"));
        pimHomepage.clickLogoutButton();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "Verification of description(French) and keyword(French) isVisible and isEditable for Z Canada GlobalMessage in USCatalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class
    		, groups = {"CA","pim","ITEM_MESSAGE"})
    public void verifyFrenchFormatAttributesIsDisplayedAndIsEditableZCanadaUS(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        textPage.verifyLanguageTypeDropDownValues(map.get("Language"));
        String usCatalogModifiedLanguageType = textPage.getLanguageAttribute();
        Assertions.assertThat(usCatalogModifiedLanguageType).isEqualTo(map.get("Language"));

        Assertions.assertThat(textPage.isAllAttributesVisible("description_French")).isTrue();
        Assertions.assertThat(textPage.isFrenchAttributeFeildsEditable("descriptionTextField")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("keywords_French")).isTrue();
        Assertions.assertThat(textPage.isFrenchAttributeFeildsEditable("TextFeild")).isTrue();

        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("MASTER Catalog"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        String masterCatalogModifiedLanguageType = textPage.getLanguageAttribute();
        Assertions.assertThat(usCatalogModifiedLanguageType).isEqualTo(masterCatalogModifiedLanguageType);
        pimHomepage.clickLogoutButton();
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGE, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "Verification of description(French) and keyword(French) is visible for Z Canada GlobalMessage in CACatalog", dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class
    		, groups = {"CA","pim","ITEM_MESSAGE"})
    public void verifyFrenchFormatAttributesIsDisplayedForZCanadaCA(Map<String, String> map){
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("MessageCode")).clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        Assertions.assertThat(textPage.isAllAttributesVisible("description_French")).isTrue();
        Assertions.assertThat(textPage.isFrenchAttributeFeildsEditable("descriptionTextField")).isTrue();
        Assertions.assertThat(textPage.isAllAttributesVisible("keywords_French")).isTrue();
        Assertions.assertThat(textPage.isFrenchAttributeFeildsEditable("TextFeild")).isTrue();
        pimHomepage.clickLogoutButton();
    }

}



