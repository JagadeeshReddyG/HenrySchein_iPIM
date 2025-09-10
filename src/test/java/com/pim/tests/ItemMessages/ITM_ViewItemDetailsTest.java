package com.pim.tests.ItemMessages;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.ActionsPage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.pages.QueriesSubMenu;
import com.pim.pages.StructureSubMenu;
import com.pim.pages.TextPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ITM_ViewItemDetailsTest extends BaseTest {
    QueriesSubMenu queriesSubMenu=new QueriesSubMenu();
    StructureSubMenu structuresubmenu=new StructureSubMenu();
    ActionsPage action=new ActionsPage();
    ProductDetailSearchPage productDetailSearchPage = new ProductDetailSearchPage();
    private ITM_ViewItemDetailsTest(){
    }

    @PimFrameworkAnnotation(module = Modules.ITEM_MESSAGES, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.ITM_Attributes)
    @Test(description = "TCID | verify Item Details displayed when searched from the queries", groups = {"CA","pim","ITEM_MESSAGES"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void verifyViewItemDetailsForCA(Map<String, String> map)
    {
        TextPage textPage = new TextPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA English User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA English User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickStructuresMenu().selectCatalogType(map.get("CatalogType"))
                .selectClassificationType(map.get("ClassificationType")).selectItemType(map.get("ItemType"));
        textPage.selectMainCategory(map.get("Main")).selectSubCategory(map.get("Sub"));
        pimHomepage.mainMenu().clickStructuresMenu().clickOnShowAll();
        pimHomepage.productDetailSearchPage().applyFilterInSearchPage(map.get("ItemNumber")).clickOnFirstResult();
        List<String> viewItemDetailsFromStructureGroup = productDetailSearchPage.viewAllItemDetails();

       //Verify All item details are displayed from the query window
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemTypeInQueriesMenu"))
                .selectCatalogType(map.get("CatalogType"))
                .enterHsiItemNumber(map.get("ItemNumber"));
        queriesSubMenu.clickSeachButton();
        structuresubmenu.clickOnFirstItemCode();
        List<String> viewItemDetailsFromQueryWindow = productDetailSearchPage.viewAllItemDetails();

        //Verify item details in structure group and query window are displayed the same
        Assertions.assertThat(viewItemDetailsFromStructureGroup).isEqualTo(viewItemDetailsFromQueryWindow);
        pimHomepage.clickLogoutButton();
    }
}


