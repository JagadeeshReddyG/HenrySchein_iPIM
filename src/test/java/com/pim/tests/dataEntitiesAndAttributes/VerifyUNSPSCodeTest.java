package com.pim.tests.dataEntitiesAndAttributes;
import java.util.Map;
import com.pim.utils.Javautils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.BasePage;
import com.pim.pages.ClassificationsPage;
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

public class VerifyUNSPSCodeTest extends BaseTest {
    LocalAttributePage localAttributePage = new LocalAttributePage();
    StructureSubMenu structuresubmenu=new StructureSubMenu();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    MainMenu mainmenu=new MainMenu();
    ClassificationsPage classificationsPage=new ClassificationsPage();
    QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
    BasePage basePage = new BasePage();
    WebDescription webDescription =new WebDescription();
    Javautils javaUtils = new Javautils();


    private VerifyUNSPSCodeTest(){
    }

    //DataAttributes_016
    @PimFrameworkAnnotation(module = Modules.DataEntities_Attributes, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.DataEntities_AttributesTestData)
    @Test(description = "DataAttributes_016 | Verify UNSPSC code feild is visible DataAttributes_016", groups = {"CA","pim","DataEntities_Attributes"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void VerifyClassificationCodeAndDivisionForDentalItemUNSPSC(Map<String, String> map) {
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();

        //verify UNSPSC dode in Classification tab
        productDetailSearchPage.selectTabfromDropdown(map.get("ClassificationTab"));
        classificationsPage.clickOnExpandButton();
        verifyUNSPSCCodeIsVisible();
        classificationsPage.clickOnCloseExpandButton();
    }
    public void verifyUNSPSCCodeIsVisible() {
        Assertions.assertThat(classificationsPage.isAllAttributesVisible("UNSPSC_Code")).isTrue();
    }
}
