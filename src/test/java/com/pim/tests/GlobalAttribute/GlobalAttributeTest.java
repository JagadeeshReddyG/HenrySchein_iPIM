package com.pim.tests.GlobalAttribute;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.GlobalAttributePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.ProductDetailSearchPage;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;


public class GlobalAttributeTest extends BaseTest {
//FDM_02-Verify if 30 character french short description are generated in PIM  for all the  Canadian item on initial load and check if it is readonly

	
	GlobalAttributePage globalAttribute = new GlobalAttributePage();
    @PimFrameworkAnnotation(module = Modules.WEB_DESCRIPTION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(priority = 0,description = "FDM_02 | Verify French Description is readonly and has 30 character values",groups = {"US","pim","WEB_DESCRIPTION"}, dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void VerifyFrenchDescriptionReadonlyAndCheckLength(Map<String, String> map) throws InterruptedException, IOException {
        CatalogTypePage catalogtypepage = new CatalogTypePage();
        ProductDetailSearchPage productDetailPage = new ProductDetailSearchPage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("US User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("US User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickCatalogsMenu().selectCatalogType(map.get("CatalogType")).applyFilterInSearchPage(map.get("itemNumber"))
                .clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));
        productDetailPage.expandTab();
        Assertions.assertThat(globalAttribute.isFrenchDescriptionReadOnly("JDE_French_Description","FieldDropDown")).isFalse();
        Assert.assertEquals(true, globalAttribute.checkFrenchDescriptionFieldlength()<=30);
    }

}