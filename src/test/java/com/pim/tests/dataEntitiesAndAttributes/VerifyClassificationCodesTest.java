package com.pim.tests.dataEntitiesAndAttributes;
import static com.pim.reports.FrameworkLogger.log;
import java.util.Map;

import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.LogType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.ClassificationsPage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LocalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.MainMenu;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;

public class VerifyClassificationCodesTest extends BaseTest {
    LocalAttributePage localAttributePage = new LocalAttributePage();
    StructureSubMenu structuresubmenu=new StructureSubMenu();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    MainMenu mainmenu=new MainMenu();
    ClassificationsPage classificationsPage=new ClassificationsPage();
    QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
    BasePage basePage = new BasePage();
    WebDescription webDescription =new WebDescription();
    CanadaTranslationPage canadaTranslationPage =new CanadaTranslationPage();

    private VerifyClassificationCodesTest(){
    }

    //DataAttributes_001
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
    @Test(description = "DataAttributes_001 | Verify Classification code and division for a dental item in canada translation and web description DataAttributes_001", groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void VerifyClassificationCodeAndDivisionForDentalItem(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();

        //verify classification code in canada translation
        productDetailSearchPage.selectTabfromDropdown(map.get("CanadaTranslation"));
        classificationsPage.clickOnExpandButton();
        String FirstLetterOfClassificationCode =  Javautils.getFirstLetter(canadaTranslationPage.getClassificationCode());
        Assertions.assertThat(FirstLetterOfClassificationCode).isEqualTo(map.get("ClassificationCode"));
        classificationsPage.clickOnCloseExpandButton();

        //verify division in web description
        productDetailSearchPage.selectTabfromDropdown(map.get("WebDescription"));
        classificationsPage.clickOnExpandButton();
        String SelectedDivision = webDescription.getDentalDivision();
        Assertions.assertThat(SelectedDivision).isEqualTo(map.get("Division"));
        classificationsPage.clickOnCloseExpandButton();
    }

    //DataAttributes_002
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
    @Test(description = "DataAttributes_002 | Verify Classification code and division for a medical item in canada translation and web description DataAttributes_002", groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void VerifyClassificationCodeAndDivisionForMedicalItem(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();

        //verify classification code in canada translation
        productDetailSearchPage.selectTabfromDropdown(map.get("CanadaTranslation"));
        classificationsPage.clickOnExpandButton();
        String FirstLetterOfClassificationCode =  Javautils.getFirstLetter(canadaTranslationPage.getClassificationCode());
        Assertions.assertThat(FirstLetterOfClassificationCode).isEqualTo(map.get("ClassificationCode"));
        classificationsPage.clickOnCloseExpandButton();

        //verify division in web description
        productDetailSearchPage.selectTabfromDropdown(map.get("WebDescription"));
        classificationsPage.clickOnExpandButton();
        String SelectedDivision = webDescription.getMedicalDivision();
        Assertions.assertThat(SelectedDivision).isEqualTo(map.get("Division"));
        classificationsPage.clickOnCloseExpandButton();
    }

    //DataAttributes_003
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
    @Test(description = "DataAttributes_003 | Verify Classification code and division for a Zahn item in canada translation and web description DataAttributes_003", groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void VerifyClassificationCodeAndDivisionForZahnItem(Map<String, String> map)
    {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();

        //verify classification code in canada translation
        productDetailSearchPage.selectTabfromDropdown(map.get("CanadaTranslation"));
        classificationsPage.clickOnExpandButton();
        String FirstLetterOfClassificationCode =  Javautils.getFirstLetter(canadaTranslationPage.getClassificationCode());
        Assertions.assertThat(FirstLetterOfClassificationCode).isEqualTo(map.get("ClassificationCode"));
        classificationsPage.clickOnCloseExpandButton();

        //verify division in web description
        productDetailSearchPage.selectTabfromDropdown(map.get("WebDescription"));
        classificationsPage.clickOnExpandButton();
        String SelectedDivision = webDescription.getZahnDivision();
        Assertions.assertThat(SelectedDivision).isEqualTo(map.get("Division"));
        classificationsPage.clickOnCloseExpandButton();
    }
}