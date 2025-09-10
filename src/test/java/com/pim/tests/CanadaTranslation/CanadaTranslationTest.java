package com.pim.tests.CanadaTranslation;

import com.pim.annotations.PimFrameworkAnnotation;
import com.pim.annotations.TestDataSheet;
import com.pim.enums.CategoryType;
import com.pim.enums.Modules;
import com.pim.enums.TestCaseSheet;
import com.pim.enums.WaitLogic;
import com.pim.pages.CanadaTranslationPage;
import com.pim.pages.CatalogTypePage;
import com.pim.pages.LoginPage;
import com.pim.pages.PimHomepage;
import com.pim.pages.WebDescription;
import com.pim.tests.BaseTest;
import com.pim.utils.DataProviderUtils;
import com.pim.utils.ExcelUtils;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;


public class CanadaTranslationTest extends BaseTest {


    CanadaTranslationPage CanadaTranslationObj = new CanadaTranslationPage();
    @PimFrameworkAnnotation(module = Modules.CANADA_TRANSLATION, category = CategoryType.REGRESSION)
    @TestDataSheet(sheetname = TestCaseSheet.WebDescription)
    @Test(description = "TCID | Verify all the fields states in Canada Translation Tab", groups = {"CA","pim","CANADA_TRANSLATION"},dataProvider = "getCatalogData", dataProviderClass = DataProviderUtils.class)
    public void CanadaTranslationtest(Map<String, String> map) throws InterruptedException, IOException {
        CatalogTypePage catalogtypepage = new CatalogTypePage();
        PimHomepage pimHomepage = new LoginPage()
                .enterUserName(ExcelUtils.getLoginData().get("CA French User").get("UserName"))
                .enterPassword(ExcelUtils.getLoginData().get("CA French User").get("Password")).clickLoginButton();
        pimHomepage.mainMenu().clickQueriesMenu().selectItemType(map.get("ItemType")).selectCatalogType(map.get("CatalogType"))
		.enterHsiItemNumber(map.get("itemNumber")).clickSeachButton().clickOnFirstResult().selectTabfromDropdown(map.get("TabName"));

        Thread.sleep(2000);
        VerifyCanadaTranslationFields();
        verifyCanadatranslationFieldsAreEditable();
       // VerifyDefaultLanguageValues(map);
    }

    public void VerifyCanadaTranslationFields()
    {
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("JDE_Description")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("classification_code")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Translation_From")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Translation_To")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("LeftSide_Division")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("RightSide_Division")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Left_Langaugae")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Right_Langaugae")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Full_Display_Description_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Full_Display_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Abbreviated_Display_Description_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Abbreviated_Display_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Look_Ahead_SearchDescription_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Look_Ahead_SearchDescription_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Detail_Extended_Description_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Detail_Extended_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Search_Description_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Search_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("ProductOvreview_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("ProductOvreview_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Extended_WebDescription_LeftSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("Extended_WebDescription_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldVisible("JDE_Frech_Description")).isTrue();
    }

    public void verifyCanadatranslationFieldsAreEditable()
    {
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Full_Display_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Abbreviated_Display_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Look_Ahead_SearchDescription_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Detail_Extended_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Search_Description_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("ProductOvreview_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Extended_WebDescription_RightSide")).isTrue();
        Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Left_Langaugae","Left_Langaugae_dropdown")).isFalse();
      	Assertions.assertThat(CanadaTranslationObj.isCanadaTranslationFieldEditable("Right_Langaugae","Right_Langaugae_dropdown")).isFalse();
    }

    public void VerifyDefaultLanguageValues(Map<String, String> map)
    {
        Assertions.assertThat(CanadaTranslationObj.ElementValues()).isEqualTo(map.get("Division"));
        Assertions.assertThat(CanadaTranslationObj.compareElementValues("Right_Langaugae_text")).isEqualTo(map.get("DafaultLanguage"));
        Assertions.assertThat(CanadaTranslationObj.compareElementValues("Left_Langaugae_text")).isEqualTo(map.get("DafaultLanguage1"));
    }


}